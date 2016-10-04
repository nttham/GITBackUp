package com.appManager.core.Test;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.appManager.core.StubManager;
import com.appManager.model.PaasAuthentication;

public class StubmanagerTest {
	PaasAuthentication paasAuthentication = new PaasAuthentication();
	@Test
	public void  doPaasAuthTest(){
		StubManager stubManager = new StubManager();
		 paasAuthentication.setUsername("admin");
		 paasAuthentication.setPassword("admin");
		 paasAuthentication.setPlatform("OneC");
		JSONObject json = new JSONObject();
		json=stubManager.doPassAuth(paasAuthentication);
		assertNotNull(json);
	}

	
	@Test
	public void getCatalogTest(){
		StubManager stubManager = new StubManager();
		JSONObject json = new JSONObject();
		json=stubManager.getCatalog("");
		assertNull(json);
	
	}
	
	@Test
	public void getSpecificCatalogTest(){
		StubManager stubManager = new StubManager();
		JSONArray json = new JSONArray();
		json=stubManager.getSpecificCatalog("", null);
		assertNull(json);
	
	}
	
	@Test
	public void searchCatalogTest(){
		StubManager stubManager = new StubManager();
		JSONObject json = new JSONObject();
		json=stubManager.searchCatalog("", null);
		assertNull(json);
	
	}
	
	
	@Test
	public void authenticationCatalogTest(){
		StubManager stubManager = new StubManager();
		JSONObject json = new JSONObject();
		json=stubManager.authenticationCatalog("", null);
		assertNull(json);
	
	}
	
	
	
	
	
	
	
	
	
	
	
}
