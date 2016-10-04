package com.appfactory.platformpush;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultipartUtility;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ApiUtils;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

@Component
public class PushTheApp {

	@Autowired
	private StatusUtils su;
	@Autowired
	private ExceptionUtils utils;
	@Autowired
	private AccessData accessdata;
	@Autowired
	private ApiUtils apiUtils;

	/**
	 * This method will execute the create app in required platform
	 * 
	 * @return
	 * @throws MyException
	 */
	public String createApps() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		JSONObject jobj = new JSONObject();
		jobj.put(ApplicationConstants.NAME, accessdata.getuIModelJson().getServicename());
		jobj.put(ApplicationConstants.SPACEGUID, accessdata.getuIModelJson().getSpaceguid());
		jobj.put(ApplicationConstants.MEMORY, ApplicationConstants.APP_MEMORY);
		jobj.put(ApplicationConstants.DISK_QUOTA, ApplicationConstants.DISK_MEMORY);
		jobj.put(ApplicationConstants.APP_STATE, ApplicationConstants.APP_STOPPED);
		String postbody = jobj.toString();
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.CREATE_APP_API;
		String response = apiUtils.executePostCall(urlLink, postbody, requestHeaders,
				ApplicationConstants.PLATFORM_CREATEAPP, ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		JSONObject temp = (JSONObject) responseObj.get(ApplicationConstants.METADATA);
		accessdata.setTemplateappguid((String) temp.get(ApplicationConstants.GUID));

		return getDomains();
	}

	/**
	 * This method will get the domain details where we have created the route
	 * 
	 * @return
	 * @throws MyException
	 */
	public String getDomains() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.GET_DOMAINS_API;
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.PLATFORM_GETDOMAINS,
				ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		JSONArray resources = (JSONArray) responseObj.get(ApplicationConstants.RESOURCES);
		JSONObject temp = (JSONObject) resources.get(0);
		temp = (JSONObject) temp.get(ApplicationConstants.METADATA);
		String domainguid = (String) temp.get(ApplicationConstants.GUID);
		accessdata.setDomainguid(domainguid);
		return createRoute(domainguid);

	}

	/**
	 * This method create a route url for the created app
	 * 
	 * @param domainguid
	 * @return
	 * @throws MyException
	 */
	public String createRoute(String domainguid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.GET_ROUTES_API;
		String input = "{\"space_guid\":\"" + accessdata.getuIModelJson().getSpaceguid() + "\",\"domain_guid\":\""
				+ domainguid + "\",\"host\":\"" + accessdata.getuIModelJson().getServicename() + "\"}";
		String response = apiUtils.executePostCall(urlLink, input, requestHeaders,
				ApplicationConstants.PLATFORM_CREATEROUTE, ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		JSONObject temp = (JSONObject) responseObj.get(ApplicationConstants.METADATA);
		return associateRoute((String) temp.get(ApplicationConstants.GUID));

	}

	/**
	 * This method will assign route to the created app
	 * 
	 * @param routeguid
	 * @return
	 * @throws MyException
	 */
	public String associateRoute(String routeguid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS
				+ accessdata.getTemplateappguid() + "/routes/" + routeguid;
		String postbody = "";
		apiUtils.executePutCall(urlLink, postbody, requestHeaders, ApplicationConstants.PLATFORM_ASSOCIATEROUTE,
				ApplicationConstants.CFURL);
		return uploadApplication();
	}

	/**
	 * This will upload the application to the platform
	 * 
	 * @throws MyException
	 * 
	 */
	public String uploadApplication() throws MyException {

		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS
				+ accessdata.getTemplateappguid() + "/bits?async=false";
		try {
			MultipartUtility multipart = new MultipartUtility(urlLink, accessdata.getuIModelJson().getAccesstoken());
			File uploadFile = new File(accessdata.getParentdirectory() + ApplicationConstants.FORWARD_SLASH
					+ accessdata.getuIModelJson().getServicename() + ApplicationConstants.ZIP_EXT);
			multipart.addFormField(ApplicationConstants.RESOURCES, "[]");
			multipart.addFilePart("application", uploadFile);
			multipart.finish();
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.PLATFORM_UPLOADPP_INPROGRESS, ApplicationConstants.CFURL, 101,
					ApplicationConstants.LOG_LEVEL_INFO);
			return startApplication();

		} catch (IOException ex) {
			throw utils.myException(CustomErrorMessage.UPLOAD_APP_ERROR, ex.getLocalizedMessage());
		}
	}

	/**
	 * This method is called only once every steps are finished
	 * 
	 * @return
	 * @throws MyException
	 */
	public String startApplication() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS
				+ accessdata.getTemplateappguid();
		JSONObject jobj = new JSONObject();
		jobj.put(ApplicationConstants.APP_STATE, ApplicationConstants.APP_STARTED);
		String postbody = jobj.toString();
		apiUtils.executePutCall(urlLink, postbody, requestHeaders, ApplicationConstants.PLATFORM_STARTAPP,
				ApplicationConstants.CFURL);
		return "";
	}

	/**
	 * This method will help to get a new token
	 * 
	 * @param reftoken
	 * @param token_url
	 * @return
	 * @throws MyException
	 */
	public String changeToken(final String reftoken, final String token_url) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BASIC_AUTHORIZATION);
		String postBody = ApplicationConstants.GET_REFRESH_TOKEN + reftoken
				+ ApplicationConstants.GET_REFRESH_TOKEN_SCOPE;
		String response = apiUtils.executePostCall(token_url, postBody, requestHeaders,
				ApplicationConstants.AUTH_VALIDATED, ApplicationConstants.CFURL);
		JSONObject jobj = new JSONObject(response);
		String newtoken=jobj.getString(ApplicationConstants.ACCESS_TOKEN);
		return newtoken;
	}
}
