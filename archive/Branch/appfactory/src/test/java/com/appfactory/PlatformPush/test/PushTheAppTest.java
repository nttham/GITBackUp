package com.appfactory.PlatformPush.test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.json.JSONObject;
import org.junit.Test;

import com.appfactory.utils.AppUtils;


import org.junit.Test;

import com.appfactory.exceptions.MyException;
import com.appfactory.platformpush.PushTheApp;
/**
 * 
 * @author 540299
 *
 */
public class PushTheAppTest {
	/**
	 * /**
	 * commented the below cases since it is valid test case
	 * 
	 */
	 
//	@Test
//	public void checkFortheplatformTest() {
//		
//		PushTheApp pushTheApp=new PushTheApp();
//		ArrayList<String> pushapp=new ArrayList<String>();
//		pushapp=pushTheApp.checkFortheplatform("azure", "Drishya");
//		assertNotNull(pushapp);
//	
//		
//		
//	}
//	@Test
//	public void checkFortheplatformTest1() {
//		
//		PushTheApp pushTheApp=new PushTheApp();
//		ArrayList<String> pushapp=new ArrayList<String>();
//		pushapp=pushTheApp.checkFortheplatform("pivotal", "Drishya");
//		assertNotNull(pushapp);
//	
//		
//		
//	}
	
//	@Test
//	public void checkFortheplatformTest2() {
//		
//		PushTheApp pushTheApp=new PushTheApp();
//		ArrayList<String> pushapp=new ArrayList<String>();
//		pushapp=pushTheApp.checkFortheplatform("bluemix", "Drishya");
//		assertNotNull(pushapp);
//	
//		
//		
//	}
//	@Test
//	public void checkFortheplatformTest3() {
//		
//		PushTheApp pushTheApp=new PushTheApp();
//		ArrayList<String> pushapp=new ArrayList<String>();
//		pushapp=pushTheApp.checkFortheplatform(" ghkhjk", "Drishya");
//		assertNotNull(pushapp);
//	
//		
//		
//	}
	@Test
	public void checkFortheplatformtest4() throws MyException{
		
		PushTheApp pushTheApp=new PushTheApp();
		JSONObject appresponse = new JSONObject();
		
		appresponse.put("accesstoken", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJkZGQwYzM2M2VkYjQ0MmJmYTMxMDJjYTgyNzFhODg1NiIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NzA5ODU4NiwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3MDk4NTg2LCJleHAiOjE0NjcwOTkxODYsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.EH1Yp3R_BAxigtefS6qov_IC2mH3MgOoDbHQABp646zzM5lB5rWWsht_EMl-qhy672HZ61g412A8jAbnuhi45SMwYniLV-sueQkViZIgmqNybdPvrXbDiURUvRB0znr-u7SVvZBEOmniYJAw3LHh_PtuR1csoDBafgj0PviYdxk");
		appresponse.put("spaceguid", "b169a527-a10a-4a84-a45a-2909fee6b1d9");
		final String zippath= "D:/Microservices/Templates/NodeJS/Authentication_Template.zip";
		final String messageID= "b169a527-a10a-4a84-a45a-2909fee6b1d9";
		final String response=pushTheApp.createApp("appname", appresponse, "zippath", "messageID", "platformurl", "gistid");   //("onec", "deago","api_url",appresponse,zippath,messageID,"");
		String[] arr = response.split(",");
		assertNotNull(arr.length);
		}
}
