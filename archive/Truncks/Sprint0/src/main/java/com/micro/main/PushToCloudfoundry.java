package com.micro.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PushToCloudfoundry {
	public String login(String user,String password){
		try {
			URL url = new URL("http://login.54.208.194.189.xip.io/oauth/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("AUTHORIZATION", "Basic Y2Y6");

			String input = "username="+user+"&password="+password+"&grant_type=password";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(output);
				JSONObject jsonObject = (JSONObject) obj;
				return getOrganization(jsonObject);
			}
			conn.disconnect();
			System.out.println("User is authenticated to cloud foundry");

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}catch (ParseException e) {
			e.printStackTrace();
		}
		return "Failed";
	}
	@SuppressWarnings("unchecked")
	private String getOrganization(JSONObject jsonObject) {
		try {
			String token = (String) jsonObject.get("access_token");
			//System.out.println("I am inside org");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/organizations");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer "+token);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output="";
			String line;
			while ((line = br.readLine()) != null) {
	            output = output + line;
			}	
			conn.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			jsonObject.put("org", responseObj);
			System.out.println("Fetched Organizantion details.");
			return jsonObject.toJSONString();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}catch (ParseException e) {
			e.printStackTrace();
		}
		return "Failed";

	}
	@SuppressWarnings("unchecked")
	public String getSpace(JSONObject inputObj) {
		try {
			
			String token = (String) inputObj.get("access_token");
			
			JSONObject org =  (JSONObject) inputObj.get("org");
			JSONArray resources =  (JSONArray) org.get("resources");
			
			
			JSONObject entity = (JSONObject) resources.get(0);
			JSONObject metadata = (JSONObject) entity.get("metadata");
			String org_guid = (String) metadata.get("guid");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/organizations/"+org_guid+"/spaces");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer "+token);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output="";
			String line;
			//System.out.println("We are also getting the Space details");
			while ((line = br.readLine()) != null) {
	            output = output + line;
			}	
			conn.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			responseObj.put("org_guid", org_guid);
			responseObj.put("token", token);
			
			return responseObj.toJSONString();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return "Failed";

	}
	

}
