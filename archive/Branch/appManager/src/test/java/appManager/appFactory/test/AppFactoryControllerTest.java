package appManager.appFactory.test;

import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AppFactoryControllerTest {
@Test
	public void testSample(){
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource("http://localhost:8080/app/test");


			ClientResponse response = webResource.type("application/json")
					.header("Authorization", "Basic c3ByaW50MTpzcHJpbnQx")
			   .get(ClientResponse.class);

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		  } catch (Exception e) {

			e.printStackTrace();

		  } 

	}
	@Test
	public void negativetestAppFactory(){
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource("http://localhost:8080/app/appFactory");

			String input = "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

			ClientResponse response = webResource.type("application/json")
			   .post(ClientResponse.class, input);

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		  } catch (Exception e) {

			e.printStackTrace();

		  }

	}
	@Test
	public void positivetestAppFactory(){
		try {

			Client client = Client.create();
			WebResource webResource = client
			   .resource("http://localhost:8080/app/appFactory");
			JSONObject facebook = new JSONObject();
			facebook.put("clientID", "468331803360754");
			facebook.put("clientSecret", "8fc8690146770e0a637adcc7293280cc");
			JSONObject providers = new JSONObject();
			providers.put("facebook", facebook);
			
			JSONObject prehook = new JSONObject(); 
			prehook.put("channel", "otp");
			prehook.put("channel_Length", 5);
			prehook.put("channel_Type", "numeric");
			prehook.put("channel_ExpiryTime", 4);
			prehook.put("provider", "sendgrid");
			prehook.put("provider_accountSID", "r8skU2912a");
			prehook.put("provider_authToken", "BPRV4rL9N7jM9272");
			prehook.put("provider_toRecipient", "neethu.tthampi@cognizant.com");
			prehook.put("provider_from", "SanthoshReddy.Tirumuru@cognizant.com");
			prehook.put("hook_channel_url", "NodeJS/Otp_Hook");
			prehook.put("provider_url", "NodeJS/SendGrid_Hook");
			
			JSONObject primaryservice = new JSONObject(); 
			primaryservice.put("protocol", "OAuth");
			primaryservice.put("app_url", "NodeJS/OAuth_Authentication");
			primaryservice.put("prehook", prehook);
			primaryservice.put("posthook", "");
			primaryservice.put("onhook", "");
			primaryservice.put("providers", facebook);
			JSONObject otherservices = new JSONObject(); 
			otherservices.put("servicename", "Pushnotification");
			otherservices.put("serviceid", "123456");
			JSONObject blueprint = new JSONObject(); 
			blueprint.put("servicename", "appFactory_unit3");
			blueprint.put("platform", "pivotal");
			blueprint.put("accesstoken", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI0NGIxOTVkNWRiZTQ0OTM1ODQ1MmYwZmU1ODk4NWQ4NyIsInN1YiI6IjY5MjA2N2QwLWYwNzktNGFjMi04MDkxLTM3OTA1NmYxMWI5OSIsInNjb3BlIjpbIm9wZW5pZCIsInVhYS51c2VyIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiI2OTIwNjdkMC1mMDc5LTRhYzItODA5MS0zNzkwNTZmMTFiOTkiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiZW1haWwiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiYXV0aF90aW1lIjoxNDY1NDcwNDQwLCJyZXZfc2lnIjoiZDJiMWRiZiIsImlhdCI6MTQ2NTQ3MDQ0MCwiZXhwIjoxNDY1NDcxMDQwLCJpc3MiOiJodHRwczovL3VhYS5ydW4ucGl2b3RhbC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInVhYSIsImNsb3VkX2NvbnRyb2xsZXIiLCJwYXNzd29yZCJdfQ.qNXmgUQT8YLK_VS7YApGFvgcryjIjX9f-qaX_z4Br9Hxob7srRcjE4Q4pP1FJQfA8gSMxdQ8N8gAxMyCqgmcOL9cPkMM5a4o9SXDngQvWUZw1F6k62WBkbsAOn4mozTNrbj2hPv-aW8oEzRpglmil3nCuyKekLbMFLIqj9_5umV3HgLMMISQRmV5TT_2bIUdH-nXwv1VHcsvJuprGe1v-3vUj7rB_x7HlVGLgoBDUKSgRFLIjZWfBKNjwfNvLhBHR7nDDo3tCfz2RyKEj45MKivxJeBbkhqsMhHd-DFFWUcbnlf67GRwdTcnyvGH2BitB3CJzw6O7d0RM3WZpM6iRw");
			blueprint.put("space_guid", "05c3c599-9326-4998-a683-a782b12ee640");
			blueprint.put("devloper_giturl", "https://github.com/sibabrata-acharya/test22.git");
			blueprint.put("devloper_gitusername", "sibabrata.acharya@cognizant.com");
			blueprint.put("devloper_gitpassword", "shiv223acharya");
			blueprint.put("primaryservice", primaryservice);
			blueprint.put("otherservices", otherservices);
			
			
			//String input = blueprint.toString();

			ClientResponse response = webResource
					.header("Accepts", "application/json")
					.header("Authorization", "Basic c3ByaW50MTpzcHJpbnQx")
					.header("Content-Type", "application/json")
			   .post(ClientResponse.class, blueprint.toString());
 
			
 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);

		  } catch (Exception e) {

			e.printStackTrace();

		  }
	}
}
