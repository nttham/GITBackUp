package com.appfactory.platformpush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.utils.AppUtils;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.SSLUtilities;

public class BindService {
	@Autowired
	private ExceptionUtils eUtils;

	public String handleException(HttpURLConnection conn, Object e) {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
		} catch (IOException e1) {
			eUtils.myException(CustomErrorMessage.IOEXCEPTION_B, e1.getMessage());
		}
		JSONObject err = new JSONObject();
		err.put("status", "failed");
		err.put("message", ((Throwable) e).getMessage());
		JSONObject errjobj = new JSONObject();
		err.put("response", errjobj.put("Error", output));
		err.put("Reason", errjobj);
		System.out.println("Error = " + err.toString());
		return err.toString();
	}

	/**
	 * 
	 * @param token
	 * @param platform
	 * @return
	 * @throws MyException 
	 */
	public String getServiceInstances(String token, String platform) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {
			URL url = new URL(platform + "/v2/service_instances");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer " + token);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			System.out.println("Get all Service Instance = " + output);
			JSONObject responseObj = new JSONObject(output);
			responseObj.put("service_instance_guid", AppUtils.getInstance().getGuid(output));
			return responseObj.toString();
		} catch (IOException e) {
			throw eUtils.myException(CustomErrorMessage.IOEXCEPTION_SI, e.getMessage());
		}
	}

	/**
	 * 
	 * @param token
	 * @param platform
	 * @return
	 * @throws MyException 
	 */
	public String getAllServices(String token, String platform) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {
			URL url = new URL(platform + "/v2/services");
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
			System.out.println("We have got all the services from cloud foundry");
			JSONObject responseObj = new JSONObject(output);
			responseObj.put("service_guid", AppUtils.getInstance().getBindableServiceGuid(output));
			return getServicePlans(responseObj, token, platform);
		}catch (IOException e) {
			throw eUtils.myException(CustomErrorMessage.IOEXCEPTION_SE, e.getMessage());
		}
	}

	/**
	 * 
	 * @param serviceLists
	 * @param token
	 * @param platform
	 * @return
	 * @throws MyException 
	 */
	private String getServicePlans(JSONObject serviceLists, String token, String platform) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {
			String guid = (String) serviceLists.get("service_guid");

			URL url = new URL(platform + "/v2/services/" + guid + "/service_plans");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("authorization", "bearer " + token);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
			System.out.println("Got all the service plans associated with the services");
			JSONObject responseObj = new JSONObject(output);
			responseObj.put("service_plan_guid", AppUtils.getInstance().getServicePlanGuid(output));
			return responseObj.toString();
		}  catch (IOException e) {
			throw eUtils.myException(CustomErrorMessage.IOEXCEPTION_SP, e.getMessage());
		}
	}

	/**
	 * 
	 * @param serviceplan
	 *            response
	 * @param bindobj
	 * @param login
	 *            response
	 * @param spaceJSonObject
	 * @param serviceName
	 * @return
	 * @throws MyException 
	 */
	public String bindServiceInstanceToApp(String service_instance_guid, String token, String platform, String appname,
			String app_guid, JSONObject bindobj, String serviceappguid) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {
			URL url = new URL(platform + "/v2/service_bindings");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			/*
			 * String input = "{\"service_instance_guid\":\"" +
			 * service_instance_guid + "\",\"app_guid\":\"" + app_guid +
			 * "\",\"parameters\":{\"appname\":\"" + appname +
			 * "\"},\"tags\":[\"accounting\",\"mongodb\"]}";
			 */
			JSONObject inputobj = new JSONObject();
			inputobj.put("service_instance_guid", service_instance_guid);
			inputobj.put("app_guid", app_guid);
			inputobj.put("appname", appname);
			inputobj.put("parameters", bindobj);
			System.out.println("bindServiceInstanceToApp= " + inputobj.toString());
			String input = inputobj.toString();
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
			System.out.println("binding the Instance ");
			return output;
		}catch (IOException e) {
			throw eUtils.myException(CustomErrorMessage.IOEXCEPTION_SA, e.getMessage());
		}
	}

	/**
	 * 
	 * @param parameters
	 *            parameters object should have
	 *            endpoint,organization_guid,plan_id,service_id,space_guid,
	 *            appname,domain_guid,host, environment_json object,token
	 *            object,serviceplanguid
	 * 
	 * @return
	 * @throws MyException 
	 */
	public String createServiceInstance(String token, String space_guid, String serviceplanguid, String platform,
			JSONObject serviceobj, String app_guid, String auth, String appname, String forwhat) throws MyException {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();

		try {
	
			String servicename =appname+auth;
			URL url = new URL(platform+"/v2/service_instances?accepts_incomplete=false");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);
			JSONObject inputobj = new JSONObject();
			inputobj.put("space_guid", space_guid);
			inputobj.put("name", servicename);
			// hard code service plan guid
			inputobj.put("service_plan_guid", serviceplanguid);
			// if(forwhat.equalsIgnoreCase("oauth")){
//			JSONObject reqObject = new JSONObject();
//
//			reqObject.put("parameters", serviceobj);
			// }
			inputobj.put("parameters",serviceobj);
			System.out.println("createServiceInstance= " + inputobj.toString());
			String input = inputobj.toString();
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
			System.out.println("Create the service Instance");

			JSONObject responseObj = new JSONObject(output);
			if(responseObj.has("status")){
				eUtils.myException(CustomErrorMessage.STAGING_ERROR,  responseObj.getString("message"));
			}
			JSONObject metaData = (JSONObject) responseObj.get("metadata");
			JSONObject entity = (JSONObject) responseObj.get("entity");
			JSONObject last_operation = (JSONObject) entity.get("last_operation");

			// responseObj.put("service_instance_guid",
			// AppUtils.getInstance().getGuid(output));
			System.out.println(responseObj.toString());
			responseObj.put("service_instance_guid", (String) metaData.get("guid"));
			responseObj.put("serviceguid", last_operation.get("description"));
			String response = responseObj.getString("service_instance_guid") + ","
					+ responseObj.getString("serviceguid");
			return response;

		} catch (IOException e) {
			throw eUtils.myException(CustomErrorMessage.STAGING_ERROR, e.getMessage());
		}
	}

	public String restageApp(String app_guid, String token, String platform) {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {

			URL url = new URL(platform + "/v2/apps/" + app_guid + "/restage");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			String input = "";
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
			System.out.println("We are doing the restaging now");

			JSONObject responseObj = new JSONObject(output);

			return responseObj.toString();

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}

	public String getAppEnv(String token, String appguid, String platform) {
		HttpURLConnection conn = null;
		SSLUtilities.disableSSLCertificateChecking();
		try {

			URL url = new URL(platform + "/v2/apps/" + appguid + "/env");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();

			JSONObject responseObj = new JSONObject(output);

			return responseObj.toString();

		} catch (IOException e) {
			return handleException(conn, e);
		}
	}

}
