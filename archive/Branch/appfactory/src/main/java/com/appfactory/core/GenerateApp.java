package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.GitProcessing.GithubOperations;
import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.ioprocessing.MultiZipProcessing;
import com.appfactory.model.OtherServices;
import com.appfactory.platformpush.BindService;
import com.appfactory.platformpush.PushTheApp;
import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;
import com.appfactory.utils.AppUtils;
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
	private static final Logger LOG = Logger.getLogger(GenerateApp.class.getName());

	@Autowired
	private AccessData accessdata;
	@Autowired
	private BindService bservice;
	@Autowired
	private StatusUtils su;
	@Autowired
	private AppUtils apputils;
	@Autowired
	private ExceptionUtils utils;
	@Autowired
	private PushTheApp pushtheapp;
	@Autowired
	private Microservices microservice;

	/**
	 * This method will create a base directory where all the operation will
	 * happen if directory is there it will skip the step.
	 * 
	 * @throws MyException
	 **/
	public String baseDirectory() throws MyException {
		MultiIOprocessing miop = new MultiIOprocessing();
		File maindirectory = miop.createFolder(accessdata.getBase_dir() + "/appManager", "app");

		String parentdirectory = maindirectory + accessdata.getMessageid();
		accessdata.setParentdirectory(parentdirectory);
		File createdir = miop.createFolder(parentdirectory, accessdata.getuIModelJson().getServicename());
		if (!(createdir.exists())) {
			LOG.error("Base directory is not created");

			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.BASE_DIRECTORY_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			return FAILURE;
		}
		accessdata.setServicefolderpath(createdir);

		LOG.info("baseDirectory method is sucess");
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
	private String cloneGit() throws MyException {
		ArrayList<String> returnarr = new ArrayList<String>();
		GithubOperations gito = new GithubOperations();
		returnarr = gito.cloneGit(accessdata.getServicefolderpath(), accessdata);
		if (returnarr.get(0).equalsIgnoreCase("error")) {
			LOG.error("Not able to get folders from git");

			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.CLONE_TEMPLATE_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			return FAILURE;
		}
		accessdata.setDownloadfolders(returnarr);
		LOG.info("cloneGit method is sucess");

		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.CLONE_TEMPLATE, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
		return generateZip();
	}

	/**
	 * It will create config files with the urls
	 * 
	 * @throws MyException
	 **/
//	private String generateURL() throws MyException {
//		PushTheApp pushOperation = new PushTheApp();
//		MultiIOprocessing miop = new MultiIOprocessing();
//
//		ArrayList<String> urls = pushOperation.checkFortheplatform(accessdata.getuIModelJson().getPlatform(),
//				accessdata.getuIModelJson().getServicename());
////		accessdata.setRunningapp(urls.get(0));
////		accessdata.setEndpoint(urls.get(1));
////		accessdata.setHost(urls.get(2));
//		
//		String url=pushOperation.checkFortheplatform
//		if (accessdata.getModifiedprotocol().equalsIgnoreCase(ApplicationConstants.LOGGER)) {
//
//			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
//					ApplicationConstants.URL_GENERATED, ApplicationConstants.CFURL, 101,
//					ApplicationConstants.LOG_LEVEL_INFO);
//			return generateZip();
//		}
//		miop.changeConfig(
//				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
//						+ accessdata.getuIModelJson().getServicename(),
//				accessdata.getuIModelJson().getServicename(), accessdata.getuIModelJson().getEnvironment_json(),
//				accessdata.getHookObj(), accessdata.getRunningapp());
//		if (urls.isEmpty()) {
//			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
//					ApplicationConstants.URL_GENERATED_FAILED, ApplicationConstants.CFURL_FAILED, 102,
//					ApplicationConstants.LOG_LEVEL_ERROR);
//			return FAILURE;
//		}
//		LOG.info("generateURL method is sucess");
//
//		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
//				ApplicationConstants.URL_GENERATED, ApplicationConstants.CFURL, 101,
//				ApplicationConstants.LOG_LEVEL_INFO);
//		return generateZip();
//	}

	/**
	 * It will create zip file of the generated app.
	 * 
	 * @throws MyException
	 **/
	private String generateZip() throws MyException {
		String source = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getuIModelJson().getServicename();
		String destzip = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getuIModelJson().getServicename() + Messages.getString("AppFactorySave.26");
		MultiZipProcessing mZip = new MultiZipProcessing();
		mZip.zipAction(source, destzip, accessdata.getuIModelJson().getServicename(),
				Messages.getString("AppFactorySave.27"));

		LOG.info("generateZip method is sucess");
		if(accessdata.getuIModelJson().getBoundtype().equalsIgnoreCase(ApplicationConstants.YES)){
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.ZIP_READY, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
		}
		
		if (!accessdata.getuIModelJson().getBoundtype().equalsIgnoreCase(ApplicationConstants.YES)) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.UNBOUND, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
			LOG.info("Its a unbound call");
			MultiIOprocessing mip = new MultiIOprocessing();
			String platform = accessdata.getEndpoint();
			String forwhat = null;
			// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
			accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));
			String token = accessdata.getuIModelJson().getAccesstoken();
			String[] a = accessdata.getuIModelJson().getAppurl().split("/");
			String auth = a[1];
			String serviceplanguid = 	accessdata.getuIModelJson().getServiceplanguid();
//			String serviceplanguid = 	AppUtils.getInstance().getdomainserviceplanguid(accessdata.getuIModelJson().getCategory(), platform, accessdata.getPasscatalog_url());
			if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
				forwhat = "oauth";

			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
				forwhat = "logger";
			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				forwhat = "logger";
			}
			JSONObject createservice = microservice.serviceobj(accessdata);
			bservice.createServiceInstance(token, accessdata.getuIModelJson().getSpaceguid(), serviceplanguid, platform,
					createservice, "", auth, accessdata.getuIModelJson().getServicename(), forwhat);
			return gitPush();
		}
		frameURLs();
		return pushApp();
	}
	
	private void frameURLs(){
	
		accessdata.setEndpoint(accessdata.getuIModelJson().getApi_url());
		accessdata.setRunningapp("https://"+ accessdata.getuIModelJson().getServicename()+"."+
				accessdata.getuIModelJson().getHost());
		accessdata.setPasscatalog_url("http://paascatalog."+accessdata.getuIModelJson().getHost());
		
	}

	/**
	 * It will create push the app, and will start the app.
	 * 
	 * @throws MyException
	 */
	private String pushApp() throws MyException {
		JSONObject appresponse = new JSONObject();
		appresponse.put(Messages.getString("AppFactorySave.accesstoken"), accessdata.getuIModelJson().getAccesstoken());
		appresponse.put(Messages.getString("AppFactorySave.spaceguid"), accessdata.getuIModelJson().getSpaceguid());
		//Set the end point and runing app URL
		accessdata.setEndpoint(accessdata.getuIModelJson().getApi_url());
		String response = pushtheapp.createApp(
				accessdata.getuIModelJson().getServicename(), appresponse,
				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
						+ accessdata.getuIModelJson().getServicename() + Messages.getString("AppFactorySave.29"),
				accessdata.getMessageid(),accessdata.getuIModelJson().getApi_url(), accessdata.getuIModelJson().getGistid());
		if (response.contains("failed")) {

			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.PLATFORM_UPLOADPP_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			return FAILURE;
		}

		String[] res = response.split(",");

		accessdata.setAppguid(res[0]);
		accessdata.setDomainguid(res[1]);
		LOG.info("pushApp method is sucess");

		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.PLATFORM_UPLOADPP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);

		return createMicro();
	}

	/**
	 * It will create the service instance, will get all the service,service
	 * plans,Bind service, create service instance,Restage,App env
	 * 
	 * @throws MyException
	 * 
	 **/

	private String createMicro() throws MyException {

	//	microservice.createservicemongo(accessdata);
		
		
		if(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER)){
            microservice.createserviceLoggerAsService(accessdata);
            su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
    				ApplicationConstants.BIND_LOGGER, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
            microservice.startapp(accessdata);
    		
     }
		else{
		microservice.createservice(accessdata);
		}
		

		/*
		 * if (!accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.
		 * EXTERNAL)) { microservice.createservice(accessdata);
		 * su.updatestatus(accessdata.getMessageid(),
		 * accessdata.getuIModelJson().getGistid(),
		 * ApplicationConstants.BIND_APPSERVIVCE, ApplicationConstants.CFURL,
		 * 101,ApplicationConstants.LOG_LEVEL_INFO); }
		 */

		
		LOG.info("createMicro method is success");
		
		return configureService();
		

	}

	/**
	 * This method will create a service instance for provided guid and will
	 * bind it to the app we have created.
	 * 
	 * @return String
	 * @throws MyException
	 */
	private String configureService() throws MyException {

		ArrayList<OtherServices> otherserv = accessdata.getuIModelJson().getOtherservices();
		if (otherserv.isEmpty()) {
			return gitPush();
		}
		/*
		for (OtherServices each : otherserv) {
			String platform = accessdata.getEndpoint();
			String response = null;
			JSONObject createinstanceobj = new JSONObject();
			createinstanceobj.put("space_guid", accessdata.getuIModelJson().getSpaceguid());
			createinstanceobj.put("name", accessdata.getuIModelJson().getServicename());
			createinstanceobj.put("service_plan_guid",AppUtils.getInstance().getdomainserviceplanguid(accessdata.getuIModelJson().getCategory(), platform, accessdata.getPasscatalog_url()));

		//	createinstanceobj.put("service_plan_guid", accessdata.getuIModelJson().getServiceplanguid());
			String inputparam = createinstanceobj.toString();
			Map<String, String> requestHeaders = new HashMap<String, String>();
			requestHeaders.put(ApplicationConstants.ACCEPT, ApplicationConstants.CONTENT_TYPE_APPLICATION_JSON);
			requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_JSON);
			requestHeaders.put(ApplicationConstants.AUTHORIZATION,
					ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
			String requestURL = platform + ApplicationConstants.SERVICE_INSTANCE_URL;
			try {
				response = apputils.executePostCall(requestURL, inputparam, requestHeaders);
				LOG.info("Otherservice instance is created");

				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.OTHERSERVICE, ApplicationConstants.CFURL, 101,
						ApplicationConstants.LOG_LEVEL_INFO);

			} catch (MyException e) {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.OTHER_SERVICE_ERROR, ApplicationConstants.CFURL_FAILED, 102,
						ApplicationConstants.LOG_LEVEL_ERROR);
				throw utils.myException(CustomErrorMessage.OTHER_SERVICE_ERROR, e.getLocalizedMessage());
			}

		
			JSONObject inputobj = new JSONObject();
			inputobj.put("service_instance_guid", accessdata.getuIModelJson().getServiceplanguid());
			inputobj.put("app_guid", accessdata.getAppguid());
			inputobj.put("appname", accessdata.getuIModelJson().getServicename());
			String input = inputobj.toString();
			Map<String, String> bindrequestHeaders = new HashMap<String, String>();
			requestHeaders.put(ApplicationConstants.ACCEPT, ApplicationConstants.CONTENT_TYPE_APPLICATION_JSON);
			requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_JSON);
			requestHeaders.put(ApplicationConstants.AUTHORIZATION,
					ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
			String bindrequestURL = platform + ApplicationConstants.SERVICE_BINDING_URL;
			try {
				response = apputils.executePostCall(bindrequestURL, input, bindrequestHeaders);
				LOG.info("Otherservice instance is binded");

				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.OTHERSERVICE_BIND, ApplicationConstants.CFURL, 101,
						ApplicationConstants.LOG_LEVEL_INFO);
			} catch (MyException e) {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.OTHERSERVICE_BIND_ERROR, ApplicationConstants.CFURL_FAILED, 102,
						ApplicationConstants.LOG_LEVEL_ERROR);
				throw utils.myException(CustomErrorMessage.OTHER_SERVICE_ERROR, e.getLocalizedMessage());
			}
		}
		*/
		if(!accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER)){

		microservice.createservicemongo(accessdata);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.BIND_LOGGER, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
       
		microservice.startapp(accessdata);
		 su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.MICRO_APP, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
		}
		return gitPush();
	}

	/**
	 * Defaultly it will upload the zip to gist and will give the link to user.
	 * It will push the app, to the devlopers github account.
	 **/
	private String gitPush() {
		Runtime runtime = Runtime.getRuntime(); 
		LOG.info("max memory: " + runtime.maxMemory() / 1024);

		Runtime totalruntime = Runtime.getRuntime(); 
		LOG.info("allocated memory: " + totalruntime.totalMemory() / 1024);

		Runtime freeruntime = Runtime.getRuntime(); 
		LOG.info("free memory: " + freeruntime.freeMemory() / 1024);

		GithubOperations gito = new GithubOperations();
		MultiIOprocessing miop = new MultiIOprocessing();
		File destination = miop.createFolder(accessdata.getParentdirectory(),
				Messages.getString("AppFactorySave.clonefolder"));
		File destinations = miop.createFolder(accessdata.getParentdirectory(),
				Messages.getString("AppFactorySave.gitfolder"));
		File copydir = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getuIModelJson().getServicename());
		File copydirzip = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getuIModelJson().getServicename() + ".zip");
		String applicationname = accessdata.getuIModelJson().getServicename();


		if ((accessdata.getuIModelJson().getDevlopergiturl()==null)||(accessdata.getuIModelJson().getDevlopergiturl().isEmpty())) {

			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.MICRO_APP, accessdata.getRunningapp(), 100,
					ApplicationConstants.LOG_LEVEL_INFO);
			gito.uploadTempapp(accessdata.getuIModelJson().getGistid(), copydirzip, destination);
			return SUCCESS;
		}

		
		String result = gito.push(applicationname, accessdata.getuIModelJson().getDevlopergitusername(),
				accessdata.getuIModelJson().getDevlopergitpassword(), accessdata.getuIModelJson().getDevlopergiturl(),
				destinations, copydir, Messages.getString("AppFactorySave.commituser"));

		/*
		 * if (!(result.equalsIgnoreCase(SUCCESS))) {
		 * gito.uploadTempapp(accessdata.getBlueprint().getGistid(), copydirzip,
		 * destination); >>>>>>> fc5bc280d365e0fe2e664c190ecfd3847b55b21d return
		 * FAILURE; }
		 */
		LOG.info("gitPush method is sucess");
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.MICRO_APP, accessdata.getRunningapp(), 100, ApplicationConstants.LOG_LEVEL_INFO);
		gito.uploadTempapp(accessdata.getuIModelJson().getGistid(), copydirzip, destination);
		return SUCCESS;
	}

}
