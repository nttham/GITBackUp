package com.appfactory.platformpush;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ApiUtils;
import com.appfactory.utils.AppUtils;

@Component
public class BindService {
	@Autowired
	private AccessData accessdata;
	@Autowired
	private ApiUtils apiUtils;
	@Autowired
	private RestageApp restage;

	/**
	 * 
	 * @param space_guid
	 * @param serviceplanguid
	 * @param serviceobj
	 * @param servicename
	 * @return
	 * @throws MyException
	 */
	public void createServiceInstances(final String space_guid, final String serviceplanguid,
			final JSONObject serviceobj, final String servicename) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());

		JSONObject inputobj = new JSONObject();
		inputobj.put(ApplicationConstants.SPACEGUID, space_guid);
		inputobj.put(ApplicationConstants.NAME, servicename);
		inputobj.put(ApplicationConstants.SERVICE_PLAN_GUID, serviceplanguid);
		inputobj.put(ApplicationConstants.PARAMETERS, serviceobj);

		String postbody = inputobj.toString();
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.SERVICE_INSTANCE_URL;
		String output = apiUtils.executePostCall(urlLink, postbody, requestHeaders,
				ApplicationConstants.CREATE_SERVICE_INSTANCE_STATUS, ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(output);
		JSONObject metaData = (JSONObject) responseObj.get(ApplicationConstants.METADATA);
		JSONObject entity = (JSONObject) responseObj.get(ApplicationConstants.ENTITY);
		JSONObject last_operation = (JSONObject) entity.get(ApplicationConstants.LAST_OPERATION);
		accessdata.setPrimary_serviceinstanceguid((String) metaData.get(ApplicationConstants.GUID));
		accessdata.setAppguid((String) last_operation.get(ApplicationConstants.ERROR_DESCRIPTION));
	}

	/**
	 * 
	 * @param service_instance_guid
	 * @param token
	 * @param app_guid
	 * @param bindobj
	 * @param serviceappguid
	 * @return
	 * @throws MyException
	 * @throws InterruptedException 
	 */
	public String bindServiceInstance(final String service_instance_guid, final String app_guid,
			final JSONObject bindobj) throws MyException, InterruptedException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		JSONObject inputobj = new JSONObject();
		inputobj.put(ApplicationConstants.SERVICE_INSTANCE_GUID, service_instance_guid);
		inputobj.put(ApplicationConstants.APP_GUID, app_guid);
		inputobj.put(ApplicationConstants.APP_NAME, accessdata.getuIModelJson().getServicename());
		inputobj.put(ApplicationConstants.PARAMETERS, bindobj);
		String postbody = inputobj.toString();
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.SERVICE_BINDING_URL;
		String response = apiUtils.executePostCall(urlLink, postbody, requestHeaders,
				ApplicationConstants.BIND_SERVICE_INSTANCE_STATUS, ApplicationConstants.CFURL);
		restage.restageApp(accessdata.getTemplateappguid());
		restage.appstats(accessdata.getTemplateappguid());
		restage.checkAppStatus(accessdata.getTemplateappguid(), 0);
		return response;
	}

	/**
	 * 
	 * @param token
	 * @param platform
	 * @return
	 * @throws MyException
	 */
	public String getServiceInstances() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.GET_SERVICEINSTANCES;
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.GET_SERVICEINSTANCE,
				ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		responseObj.put(ApplicationConstants.SERVICE_INSTANCE_GUID, AppUtils.getInstance().getGuid(response));
		return responseObj.toString();
	}

	/**
	 * 
	 * @param serviceLists
	 * @return
	 * @throws MyException
	 */
	public String getServicePlans(final JSONObject serviceLists) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String guid = (String) serviceLists.get(ApplicationConstants.SERICE_GUID);
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.GET_SERVICEPLAN_API + guid
				+ "/service_plans";
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.GET_ALL_SERVICEPLANS,
				ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		responseObj.put(ApplicationConstants.SERVICE_PLAN_GUID, AppUtils.getInstance().getServicePlanGuid(response));
		return responseObj.toString();
	}

	/**
	 * 
	 * @return
	 * @throws MyException
	 */
	public String getAllServices() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + "/v2/services";
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.GET_ALL_SERVICEINSTANCE,
				ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		responseObj.put("service_guid", AppUtils.getInstance().getBindableServiceGuid(response));
		return getServicePlans(responseObj);
	}

	/**
	 * 
	 * @param appguid
	 * @return
	 * @throws MyException
	 */
	public String getAppEnv(String appguid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS + appguid + "/env";
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.APP_ENV,
				ApplicationConstants.CFURL);
		return response;
	}
}
