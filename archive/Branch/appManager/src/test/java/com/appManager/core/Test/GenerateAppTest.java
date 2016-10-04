package com.appManager.core.Test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.appManager.core.GenerateApp;
import com.appManager.model.BluePrint;
import com.appManager.model.PrimaryProviders;
import com.appManager.model.PrimaryService;
import com.appManager.service.AccessData;


@Service
@Configurable
public class GenerateAppTest {
	
	@Autowired
	private AccessData accessdata;
	@Autowired
	private GenerateApp generateApp;
	
@Test
public void baseDirectory() throws Exception{
	/*BluePrint 	bluePrint = new BluePrint();
	PrimaryService primaryService=new PrimaryService();
	PrimaryProviders primaryProviders=new PrimaryProviders();
	bluePrint.setServicename("failtest2");
	bluePrint.setAccesstoken("eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJlMDUyNDUwMTY0N2U0ZDEyOTMxYTU4YTU3NmEyZTlmMiIsInN1YiI6IjY5MjA2N2QwLWYwNzktNGFjMi04MDkxLTM3OTA1NmYxMWI5OSIsInNjb3BlIjpbIm9wZW5pZCIsInVhYS51c2VyIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiI2OTIwNjdkMC1mMDc5LTRhYzItODA5MS0zNzkwNTZmMTFiOTkiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiZW1haWwiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiYXV0aF90aW1lIjoxNDY2NDA4NjYxLCJyZXZfc2lnIjoiZDJiMWRiZiIsImlhdCI6MTQ2NjQwODY2MSwiZXhwIjoxNDY2NDA5MjYxLCJpc3MiOiJodHRwczovL3VhYS5ydW4ucGl2b3RhbC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInVhYSIsImNsb3VkX2NvbnRyb2xsZXIiLCJwYXNzd29yZCJdfQ.Ed60aAG2lLmGQ7vOAdeEeHVqtj-ToPNmn1d7fLTs54OmtDmPxt8iVoP29ouTZYSe40IhjtKIHRdKWvhj7Y8jy64ZX1Ncs6zRieGvOtXoUgUq-DDE79M-XyU4pDp-R4jRj2PVF2ZorRGMryLyHx3ujAKkr-XQ5mTb9FF3tRGFKIeW1r5Z3bC2s0YnwDTojM7QThB5Jsf_Qsvc-5ibUgoE85hFjmJkJA0W0D8h3mmqO_wGInoqv4SWFcPKIg5VqZG0sznxoaMZ-SLBOc_EbTvRDugY2jMqSixeMPgx6uQgGbApK5_zQ5yMUMmE7o1M0P0Sc6mK4SjO6lbyvwsprKg5tg");
	bluePrint.setSpaceguid("b169a527-a10a-4a84-a45a-2909fee6b1d9");
	bluePrint.setPlatform( "OneC");
	bluePrint.setOrgguid("0f87ea0f-b95f-475d-b361-96dfd212add6");
	bluePrint.setDevlopergiturl("https://github.com/sibabrata-acharya/test22.git");
	bluePrint.setDevlopergitusername("sibabrata.acharya@cognizant.com");
	bluePrint.setDevlopergitpassword("shiv223acharya");*/
	accessdata.setAppguid("shiv223acharya");
	System.out.println(accessdata.getAppguid()); 
	/*primaryService.setAppurl("NodeJS/Authentication_Template");
	
	JSONObject json=new JSONObject();
	
	accessdata.setParentdirectory("");
	accessdata.setHookObj(json);
	accessdata.setRunningapp("Drishya");
	accessdata.setEndpoint("");
	accessdata.setDomainguid("");
	accessdata.setHost("");
	accessdata.setEnv_json(json);
	
	fb.setClientID("468331803360754");
	fb.setClientSecret("8fc8690146770e0a637adcc7293280cc");
	Google.setClientID("625227390094-m47bnlnuaguvq3phn5t5kmp503fsiagd.apps.googleusercontent.com");
	Google.setClientSecret("k0vpP0Tp5dP2oqXmcF9v10G8");
	primaryProviders.setFacebook(fb);
	primaryProviders.setGoogle(Google);*/
	assertNotNull(generateApp.baseDirectory());
} 
	
	
	
	
	
	
	
	
}
