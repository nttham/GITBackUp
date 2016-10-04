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

/**
 * File : Microservices.java Description : This class will have the
 * creation and bind service implementations.
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

	
	/**
	 * This method will create a service.
	 * Instance creation and binding with logger
	 * @throws MyException 
	 *  
	 **/
	public String createservicemongo(AccessData accessdata) throws MyException{
	BindService bservice = new BindService();
	MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
		String serviceinstanceguid = null;
		// accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders(),accessdata.getBlueprint().getLogger()));
		accessdata.setEnv_json(mip.envJson(accessdata.getuIModelJson().getEnvironment_json()));
		String token = accessdata.getuIModelJson().getAccesstoken();
		String[] a = accessdata.getuIModelJson().getAppurl().split("/");
		String auth = a[1];
		// String auth= accessdata.getuIModelJson().getApp_name();
		JSONObject createservice = serviceobj(accessdata);
		if (!(platform.isEmpty())) {
			if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXTERNAL)) {
				forwhat = "oauth";
				serviceinstanceguid = bservice.createServiceInstance(token, accessdata.getuIModelJson().getSpaceguid(),
						accessdata.getuIModelJson().getServiceplanguid(), platform, createservice,
						accessdata.getAppguid(), auth, accessdata.getuIModelJson().getServicename(), forwhat);
			}

			else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.NEWLOGGER)) {
				forwhat = "logger";
				serviceinstanceguid = bservice.createServiceInstance(token, accessdata.getuIModelJson().getSpaceguid(),
						accessdata.getuIModelJson().getEnvironment_json().getLogger().getService_plan_guid(), platform,
						createservice, accessdata.getAppguid(), auth, accessdata.getuIModelJson().getServicename(),
						forwhat);
			} else if (accessdata.getLogstatus().equalsIgnoreCase(ApplicationConstants.EXISTING)) {
				forwhat = "logger";
				JSONObject bindobj = bindserviceobj(accessdata);
				bservice.bindServiceInstanceToApp(accessdata.getuIModelJson().getEnvironment_json().getLogger().getService_instance_id(), token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						"");
				bservice.restageApp(accessdata.getAppguid(), accessdata.getuIModelJson().getAccesstoken(), platform);

				LOG.info("createservicemongo method is success");
				return SUCCESS;
			}

			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			String serviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj(accessdata);
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						serviceaappguid);
				bservice.restageApp(accessdata.getAppguid(), accessdata.getuIModelJson().getAccesstoken(), platform);

				LOG.info("createservicemongo method is success");
			} else {
				return FAILURE;
			}
		} else {
			return FAILURE;
		}
		return SUCCESS;
	}

		
	/**
	 * This method will start the dynamic app
	 * @throws MyException 
	 *  
	 **/
	public void startapp(AccessData accessdata) throws MyException{
		BindService bservice = new BindService();
		String platform = accessdata.getEndpoint();
		bservice.getAppEnv(accessdata.getuIModelJson().getAccesstoken(), accessdata.getAppguid(), platform);
		pushtheapp.startApplication(accessdata.getuIModelJson().getAccesstoken(), accessdata.getAppguid(), platform,
				accessdata.getDomainguid());
	}

	/**
	 * This method will create a service instance if log status is external
	 * @throws MyException 
	 *  
	 **/
	public String createservice(AccessData accessdata) throws MyException{
		BindService bservice = new BindService();
		MultiIOprocessing mip = new MultiIOprocessing();
		String platform = accessdata.getEndpoint();
		String forwhat = null;
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
			// String auth= accessdata.getuIModelJson().getApp_name();

			String serviceinstanceguid = bservice.createServiceInstance(token,
					accessdata.getuIModelJson().getSpaceguid(), accessdata.getuIModelJson().getServiceplanguid(),
					platform, createservice, accessdata.getAppguid(), auth,
					accessdata.getuIModelJson().getServicename(), forwhat);
			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			String serviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj(accessdata);
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getuIModelJson().getServicename(), accessdata.getAppguid(), bindobj,
						serviceaappguid);
				LOG.info("createservice method is success");
			}
		} else {
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @param jobj
	 * @return
	 */
	public JSONObject serviceobj(AccessData accessdata) {
		// String serviceplan = (String) jobj.get("service_plan_guid");
		// String serviceplan =
		// accessdata.getuIModelJson().getServiceplanguid();
		// JSONObject token = new JSONObject();
		// // token.put("service_plan_guid",
		// // accessdata.getBlueprint().getServicePlanguid());
		// token.put("access_token",
		// accessdata.getuIModelJson().getAccesstoken());
		// token.put("token_type", "bearer");
		// token.put("refresh_token", "");
		// token.put("expires_in", "");
		// token.put("scope", "");
		// token.put("jti", "");

		JSONObject createservice = new JSONObject();
		// createservice.put("endpoint", accessdata.getEndpoint());
		// createservice.put("organization_guid",
		// accessdata.getuIModelJson().getOrgguid());
		// createservice.put("plan_id",
		// accessdata.getuIModelJson().getServiceplanguid());
		// createservice.put("space_guid",
		// accessdata.getuIModelJson().getSpaceguid());
		createservice.put("appname", accessdata.getuIModelJson().getServicename() + accessdata.getMessageid());
		// createservice.put("domain_guid", accessdata.getDomainguid());
		// createservice.put("host", accessdata.getHost());
		createservice.put("environment_json", accessdata.getEnv_json());
		// createservice.put("token", token);
		return createservice;
	}

	private JSONObject bindserviceobj(AccessData accessdata) {
		JSONObject jobj = new JSONObject();
		// if (!(accessdata.getBlueprint().getPrimaryservice().getProviders() ==
		// null)) {
		// if
		// (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getFacebook()
		// == null)) {
		// jobj.put(Messages.getString("AppManager.facebook"), true);
		//
		// }
		// if
		// (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getTwitter()
		// == null)) {
		// jobj.put(Messages.getString("AppManager.twitter"), true);
		// }
		// if
		// (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getGoogle()
		// == null)) {
		// jobj.put(Messages.getString("AppManager.google"), true);
		// }
		// if
		// (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getLinkedin()
		// == null)) {
		// jobj.put(Messages.getString("AppManager.linkedin"), true);
		// }
		// jobj.put("appname", accessdata.getuIModelJson().getServicename() +
		// accessdata.getMessageid());
		// jobj.put("host", accessdata.getHost());
		// } else {
		return jobj;
		// }
		// return jobj;
	}
}
