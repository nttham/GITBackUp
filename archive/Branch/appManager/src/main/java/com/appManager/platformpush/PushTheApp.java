package com.appManager.platformpush;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appManager.ioprocessing.MultipartUtility;
import com.appManager.resources.Messages;



public class PushTheApp {

	public String handleException(HttpURLConnection conn, Object e) {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
		JSONObject err = new JSONObject();
		err.put("status", "failed");
		err.put("message", ((Throwable) e).getMessage());
		JSONObject errjobj = new JSONObject();
		err.put("response", errjobj.put("Error", output));
		err.put("Reason", errjobj);
		System.out.println("Error = "+err.toString());
		return err.toString();
	}
public ArrayList<String> checkFortheplatform(final String what_platform,final String appname){
	String url;
	String endpoint;
	String host;
	switch (what_platform) {
	case "pivotal":
		url = "http://"+appname+".cfapps.io/";
		endpoint=Messages.getString("Pivotal_API_EndPoint");;
		host="cfapps.io";
		break;
	case "bluemix":
		url = "http://"+appname+".mybluemix.net/";
		endpoint=Messages.getString("Bluemix_API_EndPoint");
		host="mybluemix.net";
		break;
	case "azure":
		url = "http://"+appname+".api.54.208.194.189.xip.io/";
		endpoint=Messages.getString("Azure_API_EndPoint");
		host="";
		break;
	default:
		url = "http://"+appname+".54.208.194.189.xip.io/";
		endpoint=Messages.getString("OneC_API_EndPoint");
		host="54.208.194.189.xip.io";
	}
	ArrayList<String> arr = new ArrayList<>();
	arr.add(url);
	arr.add(endpoint);
	arr.add(host);
	return arr;
}
	public String checkFortheplatform(String whatplatform,String appname, JSONObject appresponse,
			 String zippath) {
		String url;
		String response;
		switch (whatplatform) {
		case "pivotal":
			url = Messages.getString("Pivotal_API_EndPoint");
			 response=createApp(appname, appresponse, zippath, url);
			break;
		case "bluemix":
			url = Messages.getString("Bluemix_API_EndPoint");
			response=createApp(appname, appresponse, zippath, url);
			break;
		case "azure":
			url = Messages.getString("Azure_API_EndPoint");
			response=createApp(appname, appresponse, zippath, url);
			break;
		default:
			url = Messages.getString("OneC_API_EndPoint");
			response=createApp(appname, appresponse, zippath, url);
		}
return response;
	}
	private String createApp(String appname, JSONObject response, String zippath, String platformurl) {
		HttpURLConnection conn = null;
		try {
			String token = response.getString("accesstoken");
			String spaceguid = response.getString("spaceguid");
			URL url = new URL(platformurl + "/v2/apps");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);
			JSONObject jobj = new JSONObject();
			jobj.put("name", appname);
			jobj.put("space_guid", spaceguid);
			String input = jobj.toString();
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			JSONObject responseObj = new JSONObject(output);

			JSONObject temp = (JSONObject) responseObj.get("metadata");
			String appguid = (String) temp.get("guid");
			responseObj.put("app_guid", appguid);
			responseObj.put("access_token", token);
			responseObj.put("app_name", appname);
			responseObj.put("space_guid", spaceguid);
			// responseObj.put("bind_services", (String)
			// inputObj.get("bind_services"));
			System.out.println("App is created and ready to push");

			return getDomains(responseObj, zippath, platformurl);

		} catch (MalformedURLException e) {
			return handleException(conn, e);

		} catch (IOException e) {
			return handleException(conn, e);
		}

	}

	private String getDomains( JSONObject jsonObject,String zippath,String platformurl) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");

			URL url = new URL(platformurl + "/v2/domains");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer " + token);

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			JSONObject responseObj = new JSONObject(output);
			JSONArray resources = (JSONArray) responseObj.get("resources");
			JSONObject temp = (JSONObject) resources.get(0);
			temp = (JSONObject) temp.get("metadata");
			String guid = (String) temp.get("guid");
			jsonObject.put("domain_guid", guid);
			System.out.println("To push the app we have got the domain details");
			return createRoute(jsonObject, zippath, platformurl);

		} catch (MalformedURLException e) {
			return handleException(conn, e);

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}

	private String createRoute(JSONObject jsonObject,String zippath,String platformurl) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");
			String appname = (String) jsonObject.get("app_name");
			String spaceguid = (String) jsonObject.get("space_guid");
			String domainguid = (String) jsonObject.get("domain_guid");
			URL url = new URL(platformurl + "/v2/routes");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("authorization", "bearer " + token);

			String input = "{\"space_guid\":\"" + spaceguid + "\",\"domain_guid\":\"" + domainguid + "\",\"host\":\""
					+ appname + "\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			JSONObject responseObj = new JSONObject(output);
			JSONObject temp = (JSONObject) responseObj.get("metadata");
			String guid = (String) temp.get("guid");
			jsonObject.put("route_guid", guid);
			System.out.println("We have created a route from where application can be accessed");
			return associateRoute(jsonObject, zippath, platformurl,domainguid);

		} catch (MalformedURLException e) {
			return handleException(conn, e);

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}

	private String associateRoute(JSONObject jsonObject,String zippath,String platformurl,String domainguid) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");
			String appguid = (String) jsonObject.get("app_guid");
			String routeguid = (String) jsonObject.get("route_guid");

			URL url = new URL(platformurl + "/v2/apps/" + appguid + "/routes/" + routeguid);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("authorization", "bearer " + token);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write("");
			out.close();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			System.out.println("Route is associated with the app.");
			return uploadApplication(jsonObject, zippath, platformurl,domainguid);

		} catch (MalformedURLException e) {
			return handleException(conn, e);

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}

	@SuppressWarnings("unused")
	private String uploadApplication(JSONObject jsonObject,String zippath,String platformurl,String domainguid) {
		String token = (String) jsonObject.get("access_token");
		String appguid = (String) jsonObject.get("app_guid");
		File uploadFile = new File(zippath);
		String requestURL = platformurl + "/v2/apps/" + appguid + "/bits?async=false";
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, token);
			multipart.addFormField("resources", "[]");
			multipart.addFilePart("application", uploadFile);
			String output = multipart.finish();
			System.out.println("Application is pushed to the cloud foundry");
			// return bindService(jsonObject);
			String returnstr = appguid+","+domainguid;
			return returnstr;

			
		} catch (IOException ex) {
			System.err.println(ex);
		}
		return "{\"result\":\"failed\"m\"message\":\"upload failed\"}";
	}

	/*@SuppressWarnings("unused")
	private String bindService(JSONObject jsonObject, String platform_url) {
		String bind = (String) jsonObject.get("bind_services");
		String bind_url = (String) jsonObject.get("service_bindings_url");
		if (!bind.equalsIgnoreCase("")) {

			HttpURLConnection conn = null;
			try {
				String app_guid = (String) jsonObject.get("app_guid");
				String token = (String) jsonObject.get("access_token");

				URL url = new URL(platform_url + "/v2/events?q=type:audit.service_binding.create");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("authorization", "bearer " + token);

				String input = "{\"service_instance_guid\":\"" + bind + "\",\"app_guid\":\"" + app_guid
						+ "\",\"parameters\":{\"the_service_broker\":\"wants this object\"}}";
				System.out.println("this is post body" + input);
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String output = "";
				String line;
				while ((line = br.readLine()) != null) {
					output = output + line;
				}
				conn.disconnect();
				System.out.println("Bind Service Resopnse " + output);
				return startApplication(jsonObject, platform_url);

			} catch (MalformedURLException e) {
				return handleException(conn, e);

			} catch (IOException e) {
				return handleException(conn, e);

			}
		} else {
			return startApplication(jsonObject, platform_url);
		}
	}*/

	public String startApplication(String token,String appguid,String platformurl,String domainguid) {
		HttpURLConnection conn = null;
		try {

			URL url = new URL(platformurl + "/v2/apps/" + appguid);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("authorization", "bearer " + token);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String input = "{\"console\":true,\"state\":\"STARTED\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			System.out.println("Your application has been started");
			String returnstr = appguid+","+domainguid;
			return returnstr;

		} catch (MalformedURLException e) {
			return handleException(conn, e);

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}
}
