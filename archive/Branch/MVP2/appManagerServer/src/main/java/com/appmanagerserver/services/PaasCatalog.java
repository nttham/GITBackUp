package com.appmanagerserver.services;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.utils.ApiUtils;

@Component
public class PaasCatalog {

	@Autowired
	private ApiUtils apiutils;
	@Autowired
	private PlatformAuthenticate platformauth;

	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws MyException 
	 */
	public String getCatalog(String type) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.catalogurl + "?type=" + type;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	
	/**
	 * 
	 * @param id
	 * @param platForm
	 * @param regionid
	 * @return
	 * @throws MyException 
	 */
	public String getCatalogForPlatform(String id, String platForm,String regionid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.speccaturl + "?id=" + id 
				+"&platform="+platForm+"&regionID="+regionid;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @param searchString
	 * @param type
	 * @return
	 * @throws MyException 
	 */
	public String searchCatalog(String searchString, String type) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.searchcatalogurl + "?searchString=" + searchString 
				+"&type="+type;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	
	/**
	 * 
	 * @param type
	 * @param category
	 * @return
	 * @throws MyException
	 */
	public String autheticationCatalog(String type, String category) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.catalogurl + "?type=" + type 
				+"&category="+category;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @param layoutHeaderID
	 * @return
	 * @throws MyException 
	 */
	public String getLayouts(String layoutHeaderID) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.layouturl+layoutHeaderID;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @return
	 * @throws MyException 
	 */
	public String  getPlatforms() throws MyException{
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.platformurl;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @param serviceguid
	 * @return
	 * @throws MyException 
	 */
	public String getServicePlanGUID(String reftoken,String apiurl,String serviceguid,String login_url) throws MyException {
		String token= platformauth.changeToken(reftoken, apiurl,login_url);
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER + token);
		requestHeaders.put(ApplicationConstants.API_URL,apiurl);
		requestHeaders.put(ApplicationConstants.SERVICEGUID, serviceguid);
		String urlLink=ApplicationConstants.serviceplanguid;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @param serviceguid
	 * @return
	 * @throws MyException 
	 */
	public String getExistingInstances(final JSONObject params) throws MyException {
		System.out.println("im here");
		String token= platformauth.changeToken(params.get(ApplicationConstants.TOKEN_STRING).toString(), params.get(ApplicationConstants.API_URL).toString(),params.get(ApplicationConstants.LOGIN_URL).toString());
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER +token);
		requestHeaders.put(ApplicationConstants.API_URL,params.get(ApplicationConstants.API_URL).toString());
		requestHeaders.put(ApplicationConstants.SPACEGUID, params.get(ApplicationConstants.SPACEGUID).toString());
		requestHeaders.put(ApplicationConstants.PLATFORMID, params.get(ApplicationConstants.PLATFORMID).toString());
		requestHeaders.put(ApplicationConstants.REGIONID, params.get(ApplicationConstants.REGIONID).toString());
		requestHeaders.put(ApplicationConstants.CATEGORY_NAME, params.get(ApplicationConstants.CATEGORY_NAME).toString());
		requestHeaders.put(ApplicationConstants.SPACE_NAME,params.get(ApplicationConstants.SPACE_NAME).toString());
		String urlLink=ApplicationConstants.existinginstances;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	
	
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @param serviceguid
	 * @return
	 * @throws MyException 
	 */
	public String getDomainServiceplan(final JSONObject params) throws MyException {
		String token= platformauth.changeToken(params.get(ApplicationConstants.TOKEN_STRING).toString(), params.get(ApplicationConstants.API_URL).toString(),params.get(ApplicationConstants.LOGIN_URL).toString());
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER + token);
		requestHeaders.put(ApplicationConstants.API_URL,params.get(ApplicationConstants.API_URL).toString());
		requestHeaders.put(ApplicationConstants.SPACEGUID, params.get(ApplicationConstants.SPACEGUID).toString());
		requestHeaders.put(ApplicationConstants.PLATFORMID, params.get(ApplicationConstants.PLATFORMID).toString());
		requestHeaders.put(ApplicationConstants.REGIONID, params.get(ApplicationConstants.REGIONID).toString());
		requestHeaders.put(ApplicationConstants.CATEGORY_NAME, params.get(ApplicationConstants.CATEGORY_NAME).toString());
		requestHeaders.put(ApplicationConstants.SPACE_NAME,params.get(ApplicationConstants.SPACE_NAME).toString());
		String urlLink=ApplicationConstants.domainserviceplanguid;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
	return response;
	}
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @return
	 * @throws MyException 
	 */
	public String getOrganisations(String reftoken,String apiurl,String login_url) throws MyException {
		String token= platformauth.changeToken(reftoken, apiurl,login_url);
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER + token);
		requestHeaders.put(ApplicationConstants.API_URL,apiurl);
		String urlLink=ApplicationConstants.orgurl;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
		return response;
	}
	
	public String getRegions(final String regions) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		String urlLink=ApplicationConstants.regionurl+regions;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
		return response;
	}
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @param orguid
	 * @return
	 * @throws MyException 
	 */
	public String getSpace(final String reftoken,final String apiurl,final String orguid,String login_url) throws MyException {
		String token= platformauth.changeToken(reftoken, apiurl,login_url);
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER + token);
		requestHeaders.put(ApplicationConstants.API_URL,apiurl);
		requestHeaders.put("orgguid",orguid);
		String urlLink=ApplicationConstants.spaceurl;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
		return response;
	}
	/**
	 * 
	 * @param token
	 * @param apiurl
	 * @param spaceguid
	 * @param appname
	 * @return
	 * @throws MyException 
	 */
	public String checkAppName(final String reftoken,final String apiurl,final String spaceguid,final String appname,String login_url) throws MyException {
		String token= platformauth.changeToken(reftoken, apiurl,login_url);
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENTTYPE,ApplicationConstants.CONTENT);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION, ApplicationConstants.BEARER + token);
		requestHeaders.put(ApplicationConstants.API_URL,apiurl);
		requestHeaders.put("spaceguid", spaceguid);
		requestHeaders.put("appname", appname);
		String urlLink=ApplicationConstants.checkappname;
		String response=apiutils.executeGetCall(urlLink, requestHeaders);
		return response;
	}
}

