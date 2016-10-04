package com.appfactory.platformpush;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultipartUtility;
import com.appfactory.resources.Messages;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.SSLUtilities;
import com.appfactory.utils.StatusUtils;

@Component
public class PushTheApp {
	
	@Autowired
	private StatusUtils su;
	@Autowired
	private ExceptionUtils utils;
	private static final Logger LOG = Logger.getLogger(PushTheApp.class.getName());
	private String messageid = null;
	private String gistid = null;
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
		LOG.error(err.toString());
		if(errjobj.get("description")=="Staging error: no available stagers"){
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.STAGING_ERROR, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
		}
		LOG.error(err.toString());
		return "failed";
	}

	public ArrayList<String> checkFortheplatform(final String what_platform, final String appname) {
		String url;
		String endpoint;
		String host;
		switch (what_platform) {
		case "2001":
			url = "https://" + appname + Messages.getString("Pivotal_API_EndPoint_Generate");
			endpoint = Messages.getString("Pivotal_API_EndPoint");
			;
			host = "cfapps.io";
			break;
		case "bluemix":
			url = "http://" + appname + ".mybluemix.net/";
			endpoint = Messages.getString("Bluemix_API_EndPoint");
			host = "mybluemix.net";
			break;
		case "azure":
			url = "https://" + appname + ".api.54.208.194.189.xip.io/";
			endpoint = Messages.getString("Azure_API_EndPoint");
			host = "";
			break;
		default:
			url = "http://" + appname + Messages.getString("OneC_API_EndPoint_Generate");
			endpoint = Messages.getString("OneC_API_EndPoint");
			host =  Messages.getString("OneC_API_EndPoint_Host");
		}
		ArrayList<String> arr = new ArrayList<>();
		arr.add(url);
		arr.add(endpoint);
		arr.add(host);
		return arr;
	}

	public String checkFortheplatform(String whatplatform, String appname, JSONObject appresponse, String zippath,
			String messageID,String gistid) throws MyException {
		String url;
		String response;
		this.messageid = messageID;
		this.gistid=gistid;
		switch (whatplatform) {
		case "2001":
			url = Messages.getString("Pivotal_API_EndPoint");
			response = createApp(appname, appresponse, zippath, url);
			break;
		case "bluemix":
			url = Messages.getString("Bluemix_API_EndPoint");
			response = createApp(appname, appresponse, zippath, url);
			break;
		case "azure":
			url = Messages.getString("Azure_API_EndPoint");
			response = createApp(appname, appresponse, zippath, url);
			break;
		default:
			url = Messages.getString("OneC_API_EndPoint");
			response = createApp(appname, appresponse, zippath, url);
		}

		return response;
	}
	String token=null;
	String appguid=null;
	private String createApp(String appname, JSONObject response, String zippath, String platformurl) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {
			 token = response.getString("accesstoken");
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
//			jobj.put("memory",Messages.getString("PushTheApp.memory"));
			jobj.put("memory", 256);
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
			 appguid = (String) temp.get("guid");
			responseObj.put("app_guid", appguid);
			responseObj.put("access_token", token);
			responseObj.put("app_name", appname);
			responseObj.put("space_guid", spaceguid);
			LOG.info(ApplicationConstants.PLATFORM_CREATEAPP);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_CREATEAPP, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);

			return getDomains(responseObj, zippath, platformurl);

		} catch (IOException e) {
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_CREATEAPP_ERROR, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
			throw utils.myException(CustomErrorMessage.CREATE_APP_ERROR, e.getLocalizedMessage());
		}

	}

	private String getDomains(JSONObject jsonObject, String zippath, String platformurl) throws MyException {
		SSLUtilities.disableSSLCertificateChecking();
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
			LOG.info(ApplicationConstants.PLATFORM_GETDOMAINS);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_GETDOMAINS, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);

			return createRoute(jsonObject, zippath, platformurl);

		} catch (IOException e) {
			RollBackApp.deleteTheApp(token, appguid, platformurl);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_GETDOMAINS, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
			throw utils.myException(CustomErrorMessage.CREATE_DOMAIN_ERROR, e.getLocalizedMessage());
		}
	}

	private String createRoute(JSONObject jsonObject, String zippath, String platformurl) throws MyException {
		SSLUtilities.disableSSLCertificateChecking();
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
			LOG.info(ApplicationConstants.PLATFORM_CREATEROUTE);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_CREATEROUTE, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);

			return associateRoute(jsonObject, zippath, platformurl, domainguid);

		} catch (IOException e) {
			RollBackApp.deleteTheApp(token, appguid, platformurl);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_CREATEROUTE, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
			throw utils.myException(CustomErrorMessage.CREATE_ROUTE_ERROR, e.getLocalizedMessage());
		}
	}

	private String associateRoute(JSONObject jsonObject, String zippath, String platformurl, String domainguid) throws MyException {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn = null;
		try {
			 token = (String) jsonObject.get("access_token");
			 appguid = (String) jsonObject.get("app_guid");
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
			LOG.info(ApplicationConstants.PLATFORM_ASSOCIATEROUTE);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_ASSOCIATEROUTE, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);

			return uploadApplication(jsonObject, zippath, platformurl, domainguid);

		}  catch (IOException e) {
			RollBackApp.deleteTheApp(token, appguid, platformurl);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_ASSOCIATEROUTE, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
			throw utils.myException(CustomErrorMessage.CREATE_ROUTE_ERROR, e.getLocalizedMessage());
		}
	}

	private String uploadApplication(JSONObject jsonObject, String zippath, String platformurl, String domainguid) throws MyException {
		SSLUtilities.disableSSLCertificateChecking();
		 token = (String) jsonObject.get("access_token");
		 appguid = (String) jsonObject.get("app_guid");
		File uploadFile = new File(zippath);
		String requestURL = platformurl + "/v2/apps/" + appguid + "/bits?async=false";
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, token);
			multipart.addFormField("resources", "[]");
			multipart.addFilePart("application", uploadFile);
			multipart.finish();
			LOG.info(ApplicationConstants.PLATFORM_UPLOADPP_INPROGRESS);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_UPLOADPP_INPROGRESS, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);

			String returnstr = appguid + "," + domainguid;
			return returnstr;

		} catch (IOException ex) {
			RollBackApp.deleteTheApp(token, appguid, platformurl);
			throw utils.myException(CustomErrorMessage.UPLOAD_APP_ERROR, ex.getLocalizedMessage());
		}
	}

	public String startApplication(String token, String appguid, String platformurl, String domainguid) throws MyException {
		SSLUtilities.disableSSLCertificateChecking();
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
			JSONObject responseObj = new JSONObject(output);
			if(responseObj.has("status")){
				LOG.info(ApplicationConstants.STAGING_ERROR);
				su.updatestatus(this.messageid,this.gistid, ApplicationConstants.STAGING_ERROR, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_ERROR);
			}else{
				LOG.info(ApplicationConstants.PLATFORM_STARTAPP);
				su.updatestatus(this.messageid,this.gistid, ApplicationConstants.PLATFORM_STARTAPP, ApplicationConstants.CFURL, 101,ApplicationConstants.LOG_LEVEL_INFO);
			}
	
			String returnstr = appguid + "," + domainguid;
			return returnstr;

		}catch (IOException e) {
			RollBackApp.deleteTheApp(token, appguid, platformurl);
			su.updatestatus(this.messageid,this.gistid, ApplicationConstants.STAGING_ERROR, ApplicationConstants.CFURL, 102,ApplicationConstants.LOG_LEVEL_ERROR);
			throw utils.myException(CustomErrorMessage.START_APP_ERROR, e.getLocalizedMessage());
		}
	}
}
