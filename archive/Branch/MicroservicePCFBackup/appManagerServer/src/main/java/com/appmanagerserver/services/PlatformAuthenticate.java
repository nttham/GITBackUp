package com.appmanagerserver.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.appmanagerserver.constants.ApplicationConstants.BASE_URL;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.constants.ApplicationConstants.AUTH_URL;
import com.appmanagerserver.utils.AppUtils;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerserver.utils.SSLUtilities;

public class PlatformAuthenticate {
	@Autowired
	private ExceptionUtils eUtils;

	private String baseURL = null;

	public String login(String user, String password, String platform) {
		SSLUtilities.disableSSLCertificateChecking();
		int platformOrdinal = Integer.parseInt(platform);

		String authURL = AUTH_URL.values()[platformOrdinal].loginURL();
		try {
			URL url = new URL(authURL + "oauth/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("AUTHORIZATION", "Basic Y2Y6");

			String input = "username=" + user + "&password=" + password + "&grant_type=password";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			@SuppressWarnings("unused")
			String line = "";

			JSONObject jsonObject = null;
			while ((output = br.readLine()) != null) {

				JSONParser parser = new JSONParser();
				Object obj = parser.parse(output);

				jsonObject = (JSONObject) obj;
				System.out.println("--------Output:------------ " + output);

			}
			conn.disconnect();

			System.out.println("User is authenticated to cloud foundry");
			return getOrganization(jsonObject, platform);

		} catch (MalformedURLException e) {

			eUtils.myException(CustomErrorMessage.MALFORMED_URL_EXCEPTION, e.getMessage());
			return "Failed";

		} catch (IOException e) {
			e.printStackTrace();
			eUtils.myException(CustomErrorMessage.IOEXCEPTION, e.getMessage());
			return "Failed";

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION4, e.getMessage());
			return "Failed";

		}
	}

	@SuppressWarnings("unchecked")
	private String getOrganization(JSONObject jsonObject, String platForm) {
		SSLUtilities.disableSSLCertificateChecking();
		try {

			int platformOrdinal = Integer.parseInt(platForm);

			this.baseURL = BASE_URL.values()[platformOrdinal].url();
			String token = (String) jsonObject.get("access_token");

			URL url = new URL(baseURL + "/v2/organizations");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer " + token);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			jsonObject.put("org", responseObj);

			// String finalResponse= getSpace(jsonObject);
			// System.out.println("Fetched Organizantion details.");
			// JSONParser mparser = new JSONParser();
			// Object spaceobj = mparser.parse(finalResponse);
			// JSONObject response = (JSONObject) obj;
			//
			return getSpace(jsonObject); // jsonObject.toJSONString();

		} catch (MalformedURLException e) {

			eUtils.myException(CustomErrorMessage.MALFORMED_URL_EXCEPTION1, e.getMessage());

		} catch (IOException e) {

			eUtils.myException(CustomErrorMessage.IOEXCEPTION1, e.getMessage());

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION5, e.getMessage());
		}
		return "Failed";

	}

	@SuppressWarnings("unchecked")
	public String getSpace(JSONObject inputObj) {
		SSLUtilities.disableSSLCertificateChecking();
		try {

			String token = (String) inputObj.get("access_token");

			JSONObject org = (JSONObject) inputObj.get("org");
			JSONArray resources = (JSONArray) org.get("resources");

			JSONObject entity = (JSONObject) resources.get(0);
			JSONObject metadata = (JSONObject) entity.get("metadata");
			String orgguid = (String) metadata.get("guid");

			URL url = new URL(baseURL + "/v2/organizations/" + orgguid + "/spaces");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer " + token);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line;
			// System.out.println("We are also getting the Space details");
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			responseObj.put("org_guid", orgguid);
			responseObj.put("space_guid", AppUtils.getInstance().getGuid(output));
			responseObj.put("token", token);

			return responseObj.toJSONString();

		} catch (MalformedURLException e) {

			eUtils.myException(CustomErrorMessage.MALFORMED_URL_EXCEPTION2, e.getMessage());

		} catch (IOException e) {

			eUtils.myException(CustomErrorMessage.IOEXCEPTION2, e.getMessage());

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION6, e.getMessage());
		}
		return "Failed";

	}

}
