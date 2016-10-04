package com.appmanagerserver.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.appmanagerserver.blueprints.PaasAuthentication;
import com.appmanagerserver.messages.Messages;
import com.appmanagerservice.sendinterface.IStubManager;
import com.appmanagerserver.utils.AppUtils;

@Service
public class StubManager  implements IStubManager{
	public enum Platform {
		//FoundryPlatform, Bluemix, Pivotal, Azure
		PLATFORMID_ONEC(Messages.getString("FoundryPlatformid")), PLATFORMID_BLUEMIX(
				Messages.getString("Bluemixid")), PLATFORMID_PIVOTAL(Messages.getString("Pivotalid")), PLATFORMID_AZURE(Messages.getString("Azureidid"));

		private String id;

		private Platform(String id) {
			this.id = id;
		}

		public String id() {
			return id;
		}
	}

@SuppressWarnings("unused")
private enum Catalogtype{
	Category,
	Services,
	Hooks,
}

@Override
public JSONObject doPassAuth(PaasAuthentication name) {
	
	PlatformAuthenticate authenticate = new PlatformAuthenticate();
	
	
	String platformOrdinal=getPlatformOrdinal(name.getPlatform());
	String result="";
	if(!platformOrdinal.equalsIgnoreCase("") || !platformOrdinal.equalsIgnoreCase("Invalid Platform"))
	{

		result=	authenticate.login(name.getUsername(),name.getPassword(),platformOrdinal);
		
	}
	else{
	result= platformOrdinal;
	}
	
	return AppUtils.getInstance().toJson(result);

}


private String getPlatformOrdinal(final String inputPlatform){
	
	for (Platform selected: Platform.values()){
		if(inputPlatform!=null && inputPlatform.equalsIgnoreCase(selected.id)){
			System.out.println("selected.ordinal() "+ selected.ordinal());
			
			return ""+selected.ordinal();
		}
		
	}
	return "Invalid Platform";

}



@Override
public JSONObject getCatalog(final String catalogtype) {

	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getCatalog(catalogtype);
	return  AppUtils.getInstance().toJson(output);
}



@Override
public JSONArray getSpecificCatalog(final String id,final String platform,String regionid) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getCatalogForPlatform(id, platform,regionid);
	return  AppUtils.getInstance().toJsonArray(output);
}



@Override
public JSONObject searchCatalog(final String searchstring,final String type) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.searchCatalog(searchstring, type);
	return  AppUtils.getInstance().toJson(output);	
}


@Override
public JSONObject authenticationCatalog(final String type,final String category) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.autheticationCatalog(type, category);
	return  AppUtils.getInstance().toJson(output);	
}


@Override
public JSONObject getLayouts(String layoutHeaderID) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getLayouts(layoutHeaderID);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getPlatforms() {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getPlatforms();
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getServicePlanGUID(final String token,final String apiurl,final String serviceguid) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getServicePlanGUID(token,apiurl,serviceguid);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getExistingInstances(final String token,final String apiurl,final String serviceguid) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getExistingInstances(token,apiurl,serviceguid);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getOrganisations(final String token,final String appurl) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getOrganisations(token,appurl);
	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject getRegions(final String regions) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getRegions(regions);
	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject getSpace(final String token,final String appurl,final String orgguid) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getSpace(token,appurl,orgguid);
	

	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject checkAppName(final String token,final String apiurl,final String spaceguid,final String appname) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.checkAppName(token,apiurl,spaceguid,appname);
	return  AppUtils.getInstance().toJson(output);
}
}
