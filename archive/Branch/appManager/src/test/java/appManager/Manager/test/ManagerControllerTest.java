package appManager.Manager.test;

import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ManagerControllerTest {
	@Test	
	public void testPassAuthentication(){
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource(Messages.getString("ManagerControllerTest.0"));


			JSONObject credentials = new JSONObject();

			credentials.put("username", Messages.getString("ManagerControllerTest.2"));    
			credentials.put("password", Messages.getString("ManagerControllerTest.4"));    
			credentials.put("platform", Messages.getString("ManagerControllerTest.5"));  
			ClientResponse response = webResource
					.header("Accepts", "application/json")    
					.header("Authorization", "Basic c3ByaW50MTpzcHJpbnQx")    
					.header("Content-Type", "application/json")    
			   .post(ClientResponse.class, credentials.toString());

			if (response.getStatus() != 201) {

				throw new RuntimeException("Failed : HTTP error code : "  
				     + response.getStatus());
			}

			System.out.println("Output from Server .... \n");  
			String output = response.getEntity(String.class);
			System.out.println(output);
		  } catch (Exception e) {

			e.printStackTrace();

		  }
	}
}
