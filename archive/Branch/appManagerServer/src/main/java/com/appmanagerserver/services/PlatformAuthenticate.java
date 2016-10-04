package com.appmanagerserver.services;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.constants.ApplicationConstants.BASE_URL;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.utils.ApiUtils;
import com.appmanagerserver.utils.AppUtils;
@Component
public class PlatformAuthenticate {

	@Autowired
	private ApiUtils apiutils;
	@Autowired
	private AppUtils appUtils;
	private String baseURL = null;

	
	
	/**
	 * This method will help user to authenticate in 
	 * different platforms based on the request
	 * @param user
	 * @param password
	 * @param platform
	 * @return
	 * @throws MyException 
	 */
	public String login(String user, String password, String  login_url) throws MyException{
	//	int platformOrdinal = Integer.parseInt(platform);
	//	String authURL = AUTH_URL.values()[platformOrdinal].loginURL();
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE, ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BASIC_AUTHORIZATION);
		String requestURL= login_url ;
		String postBody="username=" + user + "&password=" + password + "&grant_type=password";
		String response= apiutils.executePostCall(requestURL, postBody, requestHeaders);
		return response;
	}
	
	/**
	 * After authentication get the list of organisation
	 * currently its not been used
	 * @param jsonObject
	 * @param platForm
	 * @return
	 * @throws MyException 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private String getOrganization(JSONObject jsonObject, String platForm) throws MyException{
		String token = (String) jsonObject.get("access_token");
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE, ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER+ token);
		
		int platformOrdinal = Integer.parseInt(platForm);
		this.baseURL = BASE_URL.values()[platformOrdinal].url();
		String requestURL= baseURL + "/v2/organizations";
		String response= apiutils.executeGetCall(requestURL, requestHeaders);
		JSONObject orgObj=appUtils.toJson(response);
		orgObj.put(ApplicationConstants.TOKEN_STRING, token);
		return getSpace(orgObj); 
	}
	
	/**
	 * This method get the details of space from this organisation 
	 * currently its not been used
	 * @param jsonObject
	 * @param platForm
	 * @return
	 * @throws MyException 
	 */
	@SuppressWarnings("unchecked")
	private String getSpace(JSONObject jsonObject) throws MyException{
		String token = (String) jsonObject.get(ApplicationConstants.TOKEN_STRING);

		JSONArray resources = (JSONArray) jsonObject.get("resources");

		JSONObject entity = (JSONObject) resources.get(0);
		JSONObject metadata = (JSONObject) entity.get("metadata");
		String orgguid = (String) metadata.get("guid");
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE, ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER+ token);
		String requestURL= baseURL + "/v2/organizations/" + orgguid + "/spaces";
		String response= apiutils.executeGetCall(requestURL, requestHeaders);
		JSONObject spaceObj=appUtils.toJson(response);
		spaceObj.put("org_guid", orgguid);
		spaceObj.put("space_guid", AppUtils.getInstance().getGuid(response));
		spaceObj.put(ApplicationConstants.TOKEN_STRING, token);
		return spaceObj.toJSONString(); 
	}
}
