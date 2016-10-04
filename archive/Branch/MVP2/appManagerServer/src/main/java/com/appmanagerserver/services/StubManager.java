package com.appmanagerserver.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appmanagerserver.blueprints.PaasAuthentication;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.messages.Messages;
import com.appmanagerserver.utils.AppUtils;
import com.appmanagerservice.sendinterface.IStubManager;

@Service
public class StubManager  implements IStubManager{

	@Autowired
	private PlatformAuthenticate platformauth;
	@Autowired
	private PaasCatalog paasCatalog;
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
public JSONObject doPassAuth(PaasAuthentication name) throws MyException {
	
	
	
	String platformOrdinal=getPlatformOrdinal(name.getPlatform());
	String result="";
	if(!platformOrdinal.equalsIgnoreCase("") || !platformOrdinal.equalsIgnoreCase("Invalid Platform"))
	{

		result=	platformauth.login(name.getUsername(),name.getPassword(),name.getLogin_url());
		
	}
	else{
	result= platformOrdinal;
	}
	
	return AppUtils.getInstance().toJson(result);

}


private String getPlatformOrdinal(final String inputPlatform){
	
	for (Platform selected: Platform.values()){
		if(inputPlatform!=null && inputPlatform.equalsIgnoreCase(selected.id)){
			return ""+selected.ordinal();
		}
		
	}
	return "Invalid Platform";

}



@Override
public JSONObject getCatalog(final String catalogtype) throws MyException {

	String  output= paasCatalog.getCatalog(catalogtype);
	return  AppUtils.getInstance().toJson(output);
}



@Override
public JSONArray getSpecificCatalog(final String id,final String platform,String regionid) throws MyException {
	String  output= paasCatalog.getCatalogForPlatform(id, platform,regionid);
	return  AppUtils.getInstance().toJsonArray(output);
}



@Override
public JSONObject searchCatalog(final String searchstring,final String type) throws MyException {
	String  output= paasCatalog.searchCatalog(searchstring, type);
	return  AppUtils.getInstance().toJson(output);	
}


@Override
public JSONObject authenticationCatalog(final String type,final String category) throws MyException {
	String  output= paasCatalog.autheticationCatalog(type, category);
	return  AppUtils.getInstance().toJson(output);	
}


@Override
public JSONObject getLayouts(String layoutHeaderID) throws MyException {
	String  output= paasCatalog.getLayouts(layoutHeaderID);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getPlatforms() throws MyException {
	String  output= paasCatalog.getPlatforms();
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getServicePlanGUID(final String token,final String apiurl,final String serviceguid,final String login_url) throws MyException {
	String access_token=platformauth.changeToken(token, apiurl,login_url);
	String  output= paasCatalog.getServicePlanGUID(access_token,apiurl,serviceguid,login_url);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getExistingInstances(JSONObject params ) throws MyException {
	String  output= paasCatalog.getExistingInstances(params);
	return  AppUtils.getInstance().toJson(output);
}

@Override
public JSONObject getDomainServiceplan(JSONObject params) throws MyException {
	String  output= paasCatalog.getDomainServiceplan(params);
	return  AppUtils.getInstance().toJson(output);
}


@Override
public JSONObject getOrganisations(final String token,final String appurl,final String login_url) throws MyException {
	String  output= paasCatalog.getOrganisations(token,appurl,login_url);
	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject getRegions(final String regions) throws MyException {
	String  output= paasCatalog.getRegions(regions);
	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject getSpace(final String token,final String appurl,final String orgguid,final String login_url) throws MyException {
	String  output= paasCatalog.getSpace(token,appurl,orgguid,login_url);
	

	return  AppUtils.getInstance().toJson(output);
}
@Override
public JSONObject checkAppName(final String token,final String apiurl,final String spaceguid,final String appname,final String login_url) throws MyException {
	String  output= paasCatalog.checkAppName(token,apiurl,spaceguid,appname,login_url);
	return  AppUtils.getInstance().toJson(output);
}
}
