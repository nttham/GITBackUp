package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.GitProcessing.GithubOperations;
import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.ioprocessing.MultiZipProcessing;
import com.appfactory.model.OtherServices;
import com.appfactory.platformpush.PushTheApp;
import com.appfactory.platformpush.RestageApp;
import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

/**
 * File : GenerateApp.java Description : This class will have small small
 * implimentation of creating the dynamic app. Revision History : Version Date
 * Author Reason 0.1 14-June-2016 559296 Initial version
 */
@Service
public class GenerateApp {
	/**
	 * These two fields will be used for checking methods success or failure
	 * 
	 **/
	private static final String FAILURE = Messages.getString("ManagerController.Failed");
	private static final String SUCCESS = Messages.getString("ManagerController.Success");

	@Autowired
	private AccessData accessdata;
	@Autowired
	private StatusUtils su;
	@Autowired
	private PushTheApp pushtheapp;
	@Autowired
	private Microservices microservice;
	@Autowired
	private GithubOperations gito;
	@Autowired
	private MultiIOprocessing miop;
	@Autowired
	private MultiZipProcessing mZip;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private UnBoundApps unboundapp;
	@Autowired
	private RestageApp restage;

	/**
	 * This method will create a base directory where all the operation will
	 * happen if directory is there it will skip the step.
	 * 
	 * @throws MyException
	 **/
	public String baseDirectory() throws MyException {

		File maindirectory = miop.createFolder(accessdata.getBase_dir() + ApplicationConstants.DIRECTORY,
				ApplicationConstants.APP);

		String parentdirectory = maindirectory + accessdata.getMessageid();
		accessdata.setParentdirectory(parentdirectory);
		File createdir = miop.createFolder(parentdirectory, accessdata.getuIModelJson().getServicename());
		if (!(createdir.exists())) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.BASE_DIRECTORY_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			return FAILURE;
		}
		accessdata.setServicefolderpath(createdir);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.BASE_DIRECTORY, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);

		return cloneGit();

	}

	/**
	 * This method will clone the required codebase from the selected inputs
	 * 
	 * @throws MyException
	 **/
	public String cloneGit() throws MyException {
		ArrayList<String> returnarr = new ArrayList<String>();
		returnarr = gito.cloneGit(accessdata.getServicefolderpath(), accessdata);
		if (returnarr.get(0).equalsIgnoreCase(ApplicationConstants.ERROR)) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.CLONE_TEMPLATE_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			return FAILURE;
		}
		accessdata.setDownloadfolders(returnarr);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.CLONE_TEMPLATE, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
		return generateZip();
	}

	/**
	 * It will create zip file of the generated app.
	 * 
	 * @throws MyException
	 **/
	public String generateZip() throws MyException {

		try {
			mZip.zipAction();
			String token = pushtheapp.changeToken(accessdata.getuIModelJson().getRefreshtoken(),
					accessdata.getuIModelJson().getLogin_url());
			accessdata.getuIModelJson().setAccesstoken(token);
			if (accessdata.getuIModelJson().getBoundtype().equalsIgnoreCase(ApplicationConstants.YES)) {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.ZIP_READY, ApplicationConstants.CFURL, 101,
						ApplicationConstants.LOG_LEVEL_INFO);
			}

			if (!accessdata.getuIModelJson().getBoundtype().equalsIgnoreCase(ApplicationConstants.YES)) {
				unboundapp.createunboundapp();
				return gitPush();
			}

			return pushApp();

		} catch (Exception e) {
			remove();
			eUtils.myException(CustomErrorMessage.IOEXCEPTION_Z, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}
	}

	/**
	 * It will create push the app, and will start the app.
	 * 
	 * @throws MyException
	 */
	public String pushApp() throws MyException {
		try {
			accessdata.setEndpoint(accessdata.getuIModelJson().getApi_url());

			pushtheapp.createApps();

			return createMicro();

		} catch (Exception e) {
			remove();
			eUtils.myException(CustomErrorMessage.CREATE_APP_ERROR, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}

	}

	/**
	 * It will create the service instance, will get all the service,service
	 * plans,Bind service, create service instance,Restage,App env
	 * 
	 * @throws MyException
	 * 
	 **/

	public String createMicro() throws MyException {

		try {
			if (accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER)) {
				microservice.createserviceLoggerAsService();
			}else if(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.PUSHNOTIFICATION)){
				microservice.createservicePush();
			}
			else {
				microservice.createservice();
			}
			return configureService();
		} catch (Exception e) {
			remove();
			eUtils.myException(CustomErrorMessage.CREATE_SERVICE_ERROR, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}

	}

	/**
	 * This method will create a service instance for provided guid and will
	 * bind it to the app we have created.
	 * 
	 * @return String
	 * @throws MyException
	 */
	public String configureService() throws MyException {
		try {
			if (!(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER))) {
				ArrayList<OtherServices> otherserv = accessdata.getuIModelJson().getOtherservices();
				if (otherserv.size() > 0) {
					for (OtherServices each : otherserv) {
						if (each.getProvider() != null) {
							if (each.getService_plan_guid() != null || !each.getService_plan_guid().isEmpty()) {
								accessdata.setLogstatus(ApplicationConstants.NEWLOGGER);
								microservice.newservice(each.getService_plan_guid(), each.getProvider().getName(),each.getProvider());
							} else if (each.getService_instance_id() != null || !each.getService_instance_id().isEmpty()) {
								accessdata.setLogstatus(ApplicationConstants.EXISTING);
								microservice.existing(each.getService_instance_id());
							}
						}
					}
				}
			}
			
			restage.statsofApp(accessdata.getTemplateappguid());
			return gitPush();
		} catch (Exception e) {
			remove();
			eUtils.myException(CustomErrorMessage.CONFIGURE_ERROR, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}
	}

	/**
	 * Defaultly it will upload the zip to gist and will give the link to user.
	 * It will push the app, to the devlopers github account.
	 * 
	 * @throws MyException
	 **/
	public String gitPush() throws MyException {
		try {
			File destination = miop.createFolder(accessdata.getParentdirectory(),
					ApplicationConstants.DEVELOP_FOLDER);

			File destinations = miop.createFolder(accessdata.getParentdirectory(),
					ApplicationConstants.GITHUBFOLDER_PATH);
			File copydir = new File(accessdata.getParentdirectory() + ApplicationConstants.FORWARD_SLASH
					+ accessdata.getuIModelJson().getServicename());
			File copydirzip = new File(accessdata.getParentdirectory() + ApplicationConstants.FORWARD_SLASH
					+ accessdata.getuIModelJson().getServicename() + ApplicationConstants.ZIP_EXT);
			
			
			if (!(accessdata.getuIModelJson().getDevlopergiturl() == null)) {
				gito.push(destinations, copydir);
			}

			
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.MICRO_APP, accessdata.getRunningapp(), 100,
					ApplicationConstants.LOG_LEVEL_INFO);
			gito.uploadTempapp(copydirzip, destination);
			return SUCCESS;
		} catch (Exception e) {
			remove();
			eUtils.myException(CustomErrorMessage.GIT_PUSH_ERROR, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}
	}

	/**
	 * This method will start and restage the app
	 * 
	 * @throws MyException
	 */
	public void startApp() throws MyException {
		microservice.startapp();
	}

	/**
	 * This will do the force delete on any unexpected exception
	 * 
	 * @throws MyException
	 */
	public void remove() throws MyException {
		File parentpath = new File(accessdata.getParentdirectory());
		miop.removeDirectory(parentpath);
	}

}
