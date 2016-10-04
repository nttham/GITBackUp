package com.appfactory.core;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.platformpush.BindService;
import com.appfactory.platformpush.PushTheApp;
import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;
import com.appfactory.utils.AppUtils;
import com.appfactory.utils.StatusUtils;
import com.google.gson.Gson;

/**
 * File : Microservices.java Description : This class will have the creation and
 * bind service implementations.
 * 
 */
@Service
public class Microservices {
	/**
	 * These two fields will be used for checking methods success or failure
	 * 
	 **/
	private static final String FAILURE = Messages.getString("ManagerController.Failed");
	private static final String SUCCESS = Messages.getString("ManagerController.Success");
	private static final Logger LOG = Logger.getLogger(GenerateApp.class.getName());
	@Autowired
	private PushTheApp pushtheapp;
	@Autowired
	private StatusUtils su;
	@Autowired
	private BindService bservice;

	/**
	 * This method will create a service. Instance creation and binding with
	 * logger
	 * 
	 * @throws MyException
	 * 
	 **/
	public String createservicemongo(AccessData accessdata) throws MyException {

		MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
		String serviceinstanceguid = null;
		// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));
		String token = accessdata.getuIModelJson().getAccesstoken();
		String[] a = accessdata.getuIModelJson().getAppurl().split("/");
		String auth = a[1];
		String microserviceaappguid = createserviceLogger(accessdata);
		
		// JSONObject createservice = serviceobj(accessdata);
		if (!(platform.isEmpty())) {

		if (!accessdata.getuIModelJson().getOtherservices().get(0).getService_instance_id().isEmpty()) {
						
			if((!accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.DEFAULT))){
			if ( !(accessdata.getuIModelJson().getOtherservices().get(0).getProvider().getName()
					.equalsIgnoreCase(ApplicationConstants.DBNAME))){
				if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				
					forwhat = "logger";
					JSONObject createMongoserv = serviceobjMongo(accessdata);
					serviceinstanceguid = bservice.createServiceInstance(token, accessdata.getSpace_micro_guid(),
							accessdata.getuIModelJson().getOtherservices().get(0).getProvider().getService_plan_guid(),
							platform, createMongoserv, microserviceaappguid, auth,
							accessdata.getuIModelJson().getServicename(), forwhat);
					JSONObject bindobj = bindserviceobj(accessdata);
					String createresponse[] = serviceinstanceguid.split(",");
					String serviceguid = createresponse[0];
					bservice.bindServiceInstanceToApp(serviceguid, token, platform,
							accessdata.getuIModelJson().getServicename(), microserviceaappguid, bindobj,
							microserviceaappguid);
					su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
							ApplicationConstants.EXISTING_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
							ApplicationConstants.LOG_LEVEL_INFO);
					LOG.info("createservicemongo method is success");
					return SUCCESS;
				} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
					forwhat = "logger";
					JSONObject bindobj = bindserviceobj(accessdata);
					/*
					 * JSONObject createMongoserv=serviceobjMongo(accessdata);
					 */
					bservice.bindServiceInstanceToApp(
							accessdata.getuIModelJson().getOtherservices().get(0).getProvider()
									.getService_instance_id(),
							token, platform, accessdata.getuIModelJson().getServicename(), microserviceaappguid,
							bindobj, "");
					su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
							ApplicationConstants.NEW_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
							ApplicationConstants.LOG_LEVEL_INFO);
					LOG.info("createservicemongo method is success");
					return SUCCESS;
				}
				if (!accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
					String createresponse[] = serviceinstanceguid.split(",");
					String serviceguid = createresponse[0];
					String serviceaappguid = createresponse[1];
					JSONObject bindobj = bindserviceobj(accessdata);
					if (!(serviceinstanceguid.contains("Error"))) {
						bservice.bindServiceInstanceToApp(serviceguid, token, platform,
								accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
								serviceaappguid);

					}

					LOG.info("createservicemongo method is success");
				}
			}
		}
		}
		}
		return SUCCESS;
	}

	/**
	 * This method will start the dynamic app
	 * 
	 * @throws MyException
	 * 
	 **/
	public void startapp(AccessData accessdata) throws MyException {
		String platform = accessdata.getEndpoint();
		bservice.restageApp(accessdata.getAppguid(), accessdata.getuIModelJson().getAccesstoken(), platform);
		bservice.getAppEnv(accessdata.getuIModelJson().getAccesstoken(), accessdata.getAppguid(), platform);
		pushtheapp.startApplication(accessdata.getuIModelJson().getAccesstoken(), accessdata.getAppguid(), platform,
				accessdata.getDomainguid());
	}

	/**
	 * This method will create a service instance if log status is external
	 * 
	 * @throws MyException
	 * 
	 **/
	public String createservice(AccessData accessdata) throws MyException {
		MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
		String microserviceaappguid = null;
		// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));

		if (!(platform.isEmpty())) {
			if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
				forwhat = "oauth";

			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
				forwhat = "logger";
			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				forwhat = "logger";
			}

			String token = accessdata.getuIModelJson().getAccesstoken();
			JSONObject createservice = serviceobj(accessdata);
			String[] a = accessdata.getuIModelJson().getAppurl().split("/");
			String auth = a[1];

			// String serviceinstanceguid =
			// bservice.createServiceInstance(token,
			// accessdata.getuIModelJson().getSpaceguid(),AppUtils.getInstance().getdomainserviceplanguid(accessdata.getuIModelJson().getCategory(),
			// platform, accessdata.getPasscatalog_url()) ,
			// platform, createservice, accessdata.getAppguid(), auth,
			// accessdata.getuIModelJson().getServicename(), forwhat);
			String serviceinstanceguid = bservice.createServiceInstance(token,
					accessdata.getuIModelJson().getSpaceguid(), accessdata.getuIModelJson().getServiceplanguid(),
					platform, createservice, accessdata.getAppguid(), auth,
					accessdata.getuIModelJson().getServicename(), forwhat);
			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			microserviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj(accessdata);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.CREATED_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
					ApplicationConstants.LOG_LEVEL_INFO);
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						microserviceaappguid);
				LOG.info("createservice for microservice app method is success");
			}
		} else {
			return FAILURE;
		}
		return microserviceaappguid;
	}

	// public JSONObject serviceobjLogger(AccessData accessdata) {
	//
	// JSONObject createservice = new JSONObject();
	// createservice.put("instancetype", "single");
	// createservice.put("appname",accessdata.getuIModelJson().getServicename()+"_"+accessdata.getuIModelJson().getOtherservices().get(0).getProvider().getName());
	// createservice.put("app_guid", accessdata.getAppguid());
	//
	// return createservice;
	// }

	public JSONObject serviceobjLogger(AccessData accessdata) {

		JSONObject createservice = new JSONObject();
		JSONObject createservice1 = new JSONObject();
		Gson gson = new Gson();
		String[] a = accessdata.getuIModelJson().getAppurl().split("/");
		String auth = a[1];
		String postBody = gson.toJson(accessdata.getuIModelJson().getOtherservices().get(0).getProvider());
		createservice1.put("logger", AppUtils.getInstance().toJson(postBody));
		JSONObject confg = new JSONObject();
		createservice1.put("configuration", confg);
		createservice.put("instancetype", "single");
		createservice.put("appname", accessdata.getuIModelJson().getServicename() + "_"
				+ accessdata.getuIModelJson().getOtherservices().get(0).getProvider().getName());
		createservice.put("app_guid", accessdata.getAppguid());
		createservice.put("environment_json", createservice1);
		return createservice;
	}

	public String createserviceLogger(AccessData accessdata) throws MyException {
		MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
		String microserviceaappguid = null;
		// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));

		if (!(platform.isEmpty())) {
			if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
				forwhat = "oauth";

			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
				forwhat = "logger";
			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				forwhat = "logger";
			}

			String token = accessdata.getuIModelJson().getAccesstoken();
			JSONObject createservice = serviceobjLogger(accessdata);
			String[] a = accessdata.getuIModelJson().getAppurl().split("/");
			String auth = a[1];
			// service plan of other services
			if (!accessdata.getuIModelJson().getOtherservices().get(0).getService_plan_guid().isEmpty()) {
				String serviceinstanceguid = bservice.createServiceInstanceLogger(token,
						accessdata.getuIModelJson().getSpaceguid(),
						accessdata.getuIModelJson().getOtherservices().get(0).getService_plan_guid(), platform,
						createservice, accessdata.getAppguid(), auth, accessdata.getuIModelJson().getServicename(),
						forwhat);

				String createresponse[] = serviceinstanceguid.split(",");
				String serviceguid = createresponse[0];
				microserviceaappguid = createresponse[1];
				JSONObject bindobj = bindserviceobj(accessdata);
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.CREATED_MICRO_SERVICE_APP_LOGGER, ApplicationConstants.CFURL, 101,
						ApplicationConstants.LOG_LEVEL_INFO);
				if (!(serviceinstanceguid.contains("Error"))) {
					bservice.bindServiceInstanceToApp(serviceguid, token, platform,
							accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
							microserviceaappguid);
				}

			} else if (!accessdata.getuIModelJson().getOtherservices().get(0).getService_instance_id().isEmpty()) {
				JSONObject bindobj = bindserviceobj(accessdata);
				bservice.bindServiceInstanceToApp(
						accessdata.getuIModelJson().getOtherservices().get(0).getService_instance_id(), token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						microserviceaappguid);
			}
			LOG.info("createservice for Logger app method is success");
		} else {
			return FAILURE;
		}
		return microserviceaappguid;
	}

	public String createserviceLoggerAsService(AccessData accessdata) throws MyException {
		MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
		String microserviceaappguid = null;
		// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));

		if (!(platform.isEmpty())) {
			if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
				forwhat = "oauth";

			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
				forwhat = "logger";
			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				forwhat = "logger";
			}

			String token = accessdata.getuIModelJson().getAccesstoken();
			JSONObject createservice = serviceobjLogger(accessdata);
			String[] a = accessdata.getuIModelJson().getAppurl().split("/");
			String auth = a[1];

			String serviceinstanceguid = bservice.createServiceInstance(token,
					accessdata.getuIModelJson().getSpaceguid(), accessdata.getuIModelJson().getServiceplanguid(),
					platform, createservice, accessdata.getAppguid(), auth,
					accessdata.getuIModelJson().getServicename(), forwhat);

			// String serviceinstanceguid =
			// bservice.createServiceInstance(token,
			// accessdata.getuIModelJson().getSpaceguid(),
			// AppUtils.getInstance().getdomainserviceplanguid(accessdata.getuIModelJson().getCategory(),
			// platform, accessdata.getPasscatalog_url())
			// ,platform, createservice, accessdata.getAppguid(), auth,
			// accessdata.getuIModelJson().getServicename(), forwhat);

			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			microserviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj(accessdata);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.CREATED_MICRO_SERVICE_APP, ApplicationConstants.CFURL, 101,
					ApplicationConstants.LOG_LEVEL_INFO);
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						microserviceaappguid);
				LOG.info("createservice for microservice app method is success");
			}
		} else {
			return FAILURE;
		}
		return microserviceaappguid;
	}

	/**
	 * 
	 * @param jobj
	 * @return
	 */
	public JSONObject serviceobj(AccessData accessdata) {

		JSONObject createservice = new JSONObject();
		createservice.put("appname", accessdata.getuIModelJson().getServicename() + accessdata.getMessageid());
		// createservice.put("domain_guid", accessdata.getDomainguid());
		// createservice.put("host", accessdata.getHost());
		createservice.put("environment_json", accessdata.getEnv_json());
		return createservice;
	}

	public JSONObject serviceobjMongo(AccessData accessdata) {

		JSONObject createservicemongo = new JSONObject();
		createservicemongo.put("appname", accessdata.getuIModelJson().getServicename() + accessdata.getMessageid());
		createservicemongo.put("instancetype", "single");
		createservicemongo.put("environment_json", accessdata.getEnv_json());
		return createservicemongo;
	}

	private JSONObject bindserviceobj(AccessData accessdata) {
		JSONObject jobj = new JSONObject();
		jobj.put("instancetype", "single");
		return jobj;

	}
}
