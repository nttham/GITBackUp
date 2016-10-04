package com.appManager.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.appManager.model.PaasAuthentication;
import com.appManager.route.IStubManager;
import com.appManager.utils.AppUtils;

@Service
public class StubManager  implements IStubManager{
	private  enum Platform{
		OneC,
		Bluemix,
		Pivotal,
		Azure
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


private String getPlatformOrdinal(String inputPlatform){
	
	for (Platform selected: Platform.values()){
		if(inputPlatform!=null && inputPlatform.equalsIgnoreCase(selected.name())){
			System.out.println("selected.ordinal() "+ selected.ordinal());
			return ""+selected.ordinal();
		}
		
	}
	return "Invalid Platform";

}



@Override
public JSONObject getCatalog(String catalogtype) {

	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getCatalog(catalogtype);
	return  AppUtils.getInstance().toJson(output);
}



@Override
public JSONArray getSpecificCatalog(String id, String platform) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.getCatalogForPlatform(id, platform);
	return  AppUtils.getInstance().toJsonArray(output);
}



@Override
public JSONObject searchCatalog(String searchstring, String type) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.searchCatalog(searchstring, type);
	return  AppUtils.getInstance().toJson(output);	
}


@Override
public JSONObject authenticationCatalog(String type, String category) {
	PaasCatalog paasCatalog= new PaasCatalog();
	String  output= paasCatalog.autheticationCatalog(type, category);
	return  AppUtils.getInstance().toJson(output);	
}
}
