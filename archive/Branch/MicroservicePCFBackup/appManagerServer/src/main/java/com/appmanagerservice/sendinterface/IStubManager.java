package com.appmanagerservice.sendinterface;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appmanagerserver.blueprints.PaasAuthentication;

public interface IStubManager {
	public JSONObject doPassAuth(PaasAuthentication auth);
	//public String searchCatalog(PaasCatalog catalogService);
	public JSONObject getCatalog(String catalogService);
	public JSONArray  getSpecificCatalog(String id, String platform,String regionid);
	public JSONObject searchCatalog(String id, String platform);
	public JSONObject authenticationCatalog(String type,String category);
	public JSONObject getLayouts(String layoutHeaderID);
	public JSONObject getPlatforms();
	public JSONObject getServicePlanGUID(String token,String apiurl, String serviceguid);
	public JSONObject getExistingInstances(String token,String apiurl, String serviceguid);
	public JSONObject getOrganisations(String token,String appurl);
	public JSONObject getRegions(String regions);
	public JSONObject getSpace(String token,String appurl,String orgguid);
	public JSONObject checkAppName(String token, String apiurl, String spaceguid,
			String appname);
}