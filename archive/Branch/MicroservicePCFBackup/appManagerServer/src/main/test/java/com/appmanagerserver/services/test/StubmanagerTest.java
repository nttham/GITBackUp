package com.appmanagerserver.services.test;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.appmanagerserver.blueprints.PaasAuthentication;
import com.appmanagerserver.services.StubManager;

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
		assertNotNull(json);
	
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
		assertNotNull(json);
	
	}
	
	
	@Test
	public void authenticationCatalogTest(){
		StubManager stubManager = new StubManager();
		JSONObject json = new JSONObject();
		json=stubManager.authenticationCatalog("", null);
		assertNotNull(json);
	
	}
	
	@Test
	public void getOrganisationsTest(){
		StubManager stubManager = new StubManager();
	String token="eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiIxYTFiMDU0MWU5YzY0YzNkOWY2M2E0ZDdjNTQzNmFiMyIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NzExNzQ0OSwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3MTE3NDQ5LCJleHAiOjE0NjcxMTgwNDksImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.HqLj7mJpV3lzhJvdx2V3_xEO2PNItUwDRSUAjqBqP42mThs2mWJTMjmlfMOgMEr9shvnEjd7KkojjiaUA5ZGvQGF5cvBnj_5sIVRI0N5cj8KERuvPtIFIA947-CsrYbe6MSzD7kIOKqssqNgZh2_8enY1Ygems4gdXyqKKikV8s";
	String appurl= "/v2/spaces/c994d62f-4a6e-4b80-8c79-212a4bd9492d/apps";
	 
	assertNotNull(stubManager.getOrganisations(token, appurl));
	
}
	
	@Test
	public void getRegionsTest(){
		StubManager stubManager = new StubManager();
	String regions= "Cognizant";
	assertNotNull(stubManager.getRegions(regions));
}
	
	@Test
	public void getSpaceTest(){
		StubManager stubManager = new StubManager();
		String token="eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiIxYTFiMDU0MWU5YzY0YzNkOWY2M2E0ZDdjNTQzNmFiMyIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NzExNzQ0OSwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3MTE3NDQ5LCJleHAiOjE0NjcxMTgwNDksImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.HqLj7mJpV3lzhJvdx2V3_xEO2PNItUwDRSUAjqBqP42mThs2mWJTMjmlfMOgMEr9shvnEjd7KkojjiaUA5ZGvQGF5cvBnj_5sIVRI0N5cj8KERuvPtIFIA947-CsrYbe6MSzD7kIOKqssqNgZh2_8enY1Ygems4gdXyqKKikV8s";
		String appurl= "/v2/spaces/c994d62f-4a6e-4b80-8c79-212a4bd9492d/apps";

assertNotNull(stubManager.getSpace(token, appurl));
	
}
	
	@Test
	public void checkAppNameTest(){
		StubManager stubManager = new StubManager();
	
		String token="eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiIxYTFiMDU0MWU5YzY0YzNkOWY2M2E0ZDdjNTQzNmFiMyIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NzExNzQ0OSwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3MTE3NDQ5LCJleHAiOjE0NjcxMTgwNDksImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.HqLj7mJpV3lzhJvdx2V3_xEO2PNItUwDRSUAjqBqP42mThs2mWJTMjmlfMOgMEr9shvnEjd7KkojjiaUA5ZGvQGF5cvBnj_5sIVRI0N5cj8KERuvPtIFIA947-CsrYbe6MSzD7kIOKqssqNgZh2_8enY1Ygems4gdXyqKKikV8s";
		String apiurl= "/v2/spaces/c994d62f-4a6e-4b80-8c79-212a4bd9492d/apps";
        String spaceguid= "b169a527-a10a-4a84-a45a-2909fee6b1d9";
        String appname= "AppManagerServer";

        
 assertNotNull(stubManager.checkAppName(token, apiurl, spaceguid, appname));
        
        
	}     
	@Test
	public void getLayoutsTest(){
		StubManager stubManager = new StubManager();
	
	JSONObject json= new JSONObject();
	json= stubManager.getLayouts();
	
	assertNotNull(json);
	
	}     
}