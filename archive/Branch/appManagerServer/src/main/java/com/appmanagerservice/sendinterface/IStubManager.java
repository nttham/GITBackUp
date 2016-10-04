package com.appmanagerservice.sendinterface;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appmanagerserver.blueprints.PaasAuthentication;
import com.appmanagerserver.exception.MyException;

public interface IStubManager {
	public JSONObject doPassAuth(PaasAuthentication auth) throws MyException;

	public JSONObject getCatalog(String catalogService) throws MyException;

	public JSONArray getSpecificCatalog(String id, String platform, String regionid) throws MyException;

	public JSONObject searchCatalog(String id, String platform) throws MyException;

	public JSONObject authenticationCatalog(String type, String category) throws MyException;

	public JSONObject getLayouts(String layoutHeaderID) throws MyException;

	public JSONObject getPlatforms() throws MyException;

	public JSONObject getServicePlanGUID(String token, String apiurl, String serviceguid) throws MyException;

	public JSONObject getExistingInstances(String token, String apiurl, String serviceguid) throws MyException;

	public JSONObject getOrganisations(String token, String appurl) throws MyException;

	public JSONObject getRegions(String regions) throws MyException;

	public JSONObject getSpace(String token, String appurl, String orgguid) throws MyException;

	public JSONObject checkAppName(String token, String apiurl, String spaceguid, String appname) throws MyException;
}