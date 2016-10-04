package com.appManager.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.expression.ParseException;

import com.appManager.constants.ApplicationConstants;

public class PaasCatalog {

	public String getCatalog(String type) {
		try {
			URL url = new URL(ApplicationConstants.catalogurl + "?type=" + type);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "Failed";

		} catch (IOException e) {

			e.printStackTrace();
			return "Failed";

		} catch (ParseException e) {
			e.printStackTrace();
			return "Failed";

		}
	}

	public String getCatalogForPlatform(String id, String platForm) {

		try {
			URL url = new URL(ApplicationConstants.speccaturl + "?id=" + id 
					+"&platform="+platForm);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "Failed";

		} catch (IOException e) {

			e.printStackTrace();
			return "Failed";

		} catch (ParseException e) {
			e.printStackTrace();
			return "Failed";
		}

		}
	
	 public String searchCatalog(String searchString, String type)
	 {
			try {
				URL url = new URL(ApplicationConstants.searchcatalogurl + "?searchString=" + searchString 
						+"&type="+type);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

//				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output = "";
				String line = "";
				while ((line = br.readLine()) != null) {
					output = output + line;
				}

				conn.disconnect();
				return output;

			} catch (MalformedURLException e) {

				e.printStackTrace();
				return "Failed";

			} catch (IOException e) {

				e.printStackTrace();
				return "Failed";

			} catch (ParseException e) {
				e.printStackTrace();
				return "Failed";
			} 
	 }

	public String autheticationCatalog(String type, String category) {
		try {
			URL url = new URL(ApplicationConstants.catalogurl + "?type=" + type 
					+"&category="+category);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "Failed";

		} catch (IOException e) {

			e.printStackTrace();
			return "Failed";

		} catch (ParseException e) {
			e.printStackTrace();
			return "Failed";
		} 
	}
}
