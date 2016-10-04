package com.appManager.route;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appManager.model.PaasAuthentication;

public interface IStubManager {
	public JSONObject doPassAuth(PaasAuthentication auth);
	//public String searchCatalog(PaasCatalog catalogService);
	public JSONObject getCatalog(String catalogService);
	public JSONArray getSpecificCatalog(String id, String platform);
	public JSONObject searchCatalog(String id, String platform);
	public JSONObject authenticationCatalog(String type,String category);
}
