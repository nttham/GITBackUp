package com.appfactory.core;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.model.Logger;
import com.appfactory.model.OtherServices;
import com.appfactory.platformpush.BindService;
import com.appfactory.platformpush.PushTheApp;
import com.appfactory.service.AccessData;
import com.appfactory.utils.StatusUtils;
import com.google.gson.Gson;

/**
 * File : Microservices.java Description : This class will have the creation and
 * bind service implementations.
 * 
 * @author 559296
 */
@Service
public class Microservices {
	/**
	 * These two fields will be used for checking methods success or failure
	 * 
	 **/
	@Autowired
	private PushTheApp pushtheapp;
	@Autowired
	private StatusUtils su;
	@Autowired
	private BindService bservice;
	@Autowired
	private MultiIOprocessing mip;
	@Autowired
	private AccessData accessdata;

	/**
	 * This method will create a service for newlogger scenario
	 * @param logger 
	 * 
	 * @throws MyException
	 * @throws InterruptedException
	 */
	public void newservice(final String planguid,final String provname, Logger logger) throws MyException, InterruptedException {

		final JSONObject createMongoserv = serviceobjLogger();

		final String newservicename = newservicenamelogger(provname);
		bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(), planguid, createMongoserv,
				newservicename);
		final JSONObject bindobj = bindserviceobj();

		bservice.bindServiceInstance(accessdata.getPrimary_serviceinstanceguid(), accessdata.getTemplateappguid(),
				bindobj);

		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.NEW_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
	}

	/**
	 * 
	 * @param planguid
	 * @throws MyException
	 * @throws InterruptedException
	 */
	public void existing(final String serviceguid) throws MyException, InterruptedException {
		final JSONObject bindobj = bindserviceobj();
		bservice.bindServiceInstance(serviceguid, accessdata.getTemplateappguid(), bindobj);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.EXISTING_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
	}

	/**
	 * 
	 * @param serviceguid
	 * @throws MyException
	 * @throws InterruptedException
	 */
	public void external(final String serviceguid) throws MyException, InterruptedException {
		final JSONObject bindobj = bindserviceobj();
		bservice.bindServiceInstance(serviceguid, accessdata.getTemplateappguid(), bindobj);

	}

	/**
	 * This method will start the dynamic app
	 * 
	 * @throws MyException
	 * 
	 **/
	public void startapp() throws MyException {
		pushtheapp.startApplication();
	}

	/**
	 * This method will create a service instance if log status is external
	 * 
	 * @throws MyException
	 * @throws InterruptedException
	 * 
	 **/
	public void createservice() throws MyException, InterruptedException {

		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));

		JSONObject createservice = serviceobj();
		final String newservicename = newservicename();
		final String serviceplanguid = accessdata.getuIModelJson().getServiceplanguid();
		bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(), serviceplanguid, createservice,
				newservicename);
		JSONObject bindobj = bindserviceobj();
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.CREATED_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
		bservice.bindServiceInstance(accessdata.getPrimary_serviceinstanceguid(), accessdata.getTemplateappguid(),
				bindobj);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.BINDING_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);

	}

	/**
	 * @author Sandhya
	 * 
	 *         This method will be called only on logger as service scenario
	 * @throws MyException
	 * @throws InterruptedException
	 */
	public void createserviceLoggerAsService() throws MyException, InterruptedException {
		JSONObject createservice = serviceobjLogger();
		final String serviceplanguid = accessdata.getuIModelJson().getServiceplanguid();
		final String newservicename = newservicename();
		bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(), serviceplanguid, createservice,
				newservicename);
		JSONObject bindobj = bindserviceobj();
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.CREATED_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
		bservice.bindServiceInstance(accessdata.getPrimary_serviceinstanceguid(), accessdata.getTemplateappguid(),
				bindobj);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.BINDING_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);
	}

	/**
	 * 
	 * @param jobj
	 * @return
	 */
	public JSONObject serviceobj() {
		JSONObject createservice = new JSONObject();
		createservice.put(ApplicationConstants.APP_NAME,
				accessdata.getuIModelJson().getServicename() + accessdata.getMessageid());
		createservice.put(ApplicationConstants.ENV_JSON, accessdata.getEnv_json());
		return createservice;
	}

	/**
	 * 
	 * @param logger 
	 * @param accessdata
	 * @return
	 */
	public JSONObject serviceobjMongo(Logger logger) {
		System.out.println(logger.toString());
		JSONObject createservicemongo = new JSONObject();
		createservicemongo.put(ApplicationConstants.APP_NAME,
				accessdata.getuIModelJson().getServicename() + accessdata.getMessageid());
		createservicemongo.put(ApplicationConstants.INSTANCE_TYPE, ApplicationConstants.SINGLE);
		createservicemongo.put(ApplicationConstants.SMALL_LOGGER, logger.toString());
		return createservicemongo;
	}

	/**
	 * 
	 * @param accessdata
	 * @return
	 */
	private JSONObject bindserviceobj() {
		JSONObject jobj = new JSONObject();
		jobj.put(ApplicationConstants.INSTANCE_TYPE, ApplicationConstants.SINGLE);
		return jobj;

	}

	/**
	 * @author Sandhya
	 * @return This will return the parameter object for only logger as service
	 *         scenario
	 */
	public JSONObject serviceobjLogger() {
		JSONObject createservice = new JSONObject();
			JSONObject createservice1 = new JSONObject();
			ArrayList<OtherServices> otherserv = accessdata.getuIModelJson().getOtherservices();
			int i = 0;
			for (OtherServices each : otherserv) {
				int count = i++;
				Gson gson = new Gson();
				String postBody=gson.toJson(each.getProvider());
				JSONObject jsonObj = new JSONObject(postBody);
				String name = accessdata.getuIModelJson().getServicename() + ApplicationConstants.UNDERSCORE
						+ accessdata.getuIModelJson().getOtherservices().get(count).getProvider().getName();
			
					createservice1.put(ApplicationConstants.SMALL_LOGGER, jsonObj);
					createservice.put(ApplicationConstants.INSTANCE_TYPE, ApplicationConstants.SINGLE);
					createservice.put(ApplicationConstants.APP_NAME, name);
					createservice.put(ApplicationConstants.APP_GUID, accessdata.getTemplateappguid());
					createservice.put(ApplicationConstants.ENV_JSON, createservice1);
			
			}
		return createservice;
	}

	/**
	 * 
	 * @return
	 */
	public String newservicename() {
		final String[] appurl = accessdata.getuIModelJson().getAppurl().split(ApplicationConstants.FORWARD_SLASH);
		final String tempappfolder = appurl[1];
		final String newservicename = accessdata.getuIModelJson().getServicename() + tempappfolder;
		return newservicename;
	}

	/**
	 * 
	 * @return
	 */
	public String newservicenamelogger(final String appName) {
		final String newservicename = accessdata.getuIModelJson().getServicename()
				+ accessdata.getuIModelJson().getCategory() + appName;
		return newservicename;
	}
	/**
	 * @author Sandhya
	 * 
	 *         This method will be called only on pushnotification as service
	 *         scenario
	 * @throws MyException
	 * @throws InterruptedException
	 */
	public void createservicePush()	throws MyException, InterruptedException {

		JSONObject createservice = serviceobjPush();
		final String newservicename = newservicename();
		final String serviceplanguid = accessdata.getuIModelJson().getServiceplanguid();
		bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(), serviceplanguid, createservice,
				newservicename);
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.CREATED_MICRO_SERVICE_APP_PUSH, ApplicationConstants.CFURL, 101,
				ApplicationConstants.LOG_LEVEL_INFO);

	}
	/**
	 * 
	 * @param accessdata
	 * @return
	 */
	
	public JSONObject serviceobjPush() {
		JSONObject createservice = new JSONObject();
		createservice.put(ApplicationConstants.TEMPLATES, accessdata.getuIModelJson().getEnvironment_json().getTemplates());
		return createservice;
	}
}
