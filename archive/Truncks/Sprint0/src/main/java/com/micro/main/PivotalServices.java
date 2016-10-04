package com.micro.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.micro.utils.AppUtils;

public class PivotalServices {

	@SuppressWarnings("unchecked")
	public String handleException(HttpURLConnection conn, Object e) {
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject err = new JSONObject();
		err.put("status", "failed");
		err.put("message", ((Throwable) e).getMessage());
		JSONParser parser = new JSONParser();
		try {
			err.put("response", (JSONObject) (parser.parse(output)));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.printf("My Error is " + err);
		return err.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getServiceInstances(JSONObject inputObj) {
		HttpURLConnection conn = null;
		try {
			String token = (String) inputObj.get("access_token");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/service_instances");
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
			System.out.println("Get all Service Instance = " + output);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			responseObj.put("service_instance_guid", AppUtils.getInstance().getGuid(output));
			return responseObj.toJSONString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (ParseException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}

	@SuppressWarnings("unchecked")
	public String getAllServices(JSONObject inputObj) {
		HttpURLConnection conn = null;
		try {
			String token = (String) inputObj.get("access_token");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/services");
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
			System.out.println("Fetched the services from cloud foundry");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			responseObj.put("service_guid", AppUtils.getInstance().getBindableServiceGuid(output));
			return responseObj.toJSONString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (ParseException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public String getServicePlans(JSONObject serviceLists, JSONObject inputObj) {
		HttpURLConnection conn = null;
		try {
			String token = (String) inputObj.get("access_token");

			JSONParser parse = new JSONParser();

			String guid = (String) serviceLists.get("service_guid");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/services/" + guid + "/service_plans");
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
			System.out.println("Fetched service plans associated with the services");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			responseObj.put("service_plan_guid", AppUtils.getInstance().getServicePlanGuid(output));
			return responseObj.toJSONString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (ParseException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}

	/**
	 * 
	 * @param servicePlanObj
	 * @param loginObj
	 * @param spaceJSonObject
	 * @param serviceName
	 * @return
	 */
	public String bindServiceInstanceToApp(JSONObject serviceInsatnceObj, JSONObject appObj, JSONObject loginObj,
			String appServiceName) {
		HttpURLConnection conn = null;
		try {
			String service_instance_guid = (String) serviceInsatnceObj.get("service_instance_guid");
			JSONObject metaData = (JSONObject) appObj.get("metadata");
			String app_guid = (String) metaData.get("guid");

			String token = (String) loginObj.get("access_token");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/service_bindings");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			String input = "{\"service_instance_guid\":\"" + service_instance_guid + "\",\"app_guid\":\"" + app_guid
					+ "\",\"parameters\":{\"appname\":\"" + appServiceName
					+ "\"},\"tags\":[\"accounting\",\"mongodb\"]}";
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
			System.out.println("Binding the Instance ");
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}

	@SuppressWarnings("unchecked")
	public String createServiceInstance(JSONObject servicePlanObj, JSONObject loginObj, JSONObject spaceJSonObject,
			String serviceName) {
		HttpURLConnection conn = null;
		try {

			String service_plan_guid = (String) servicePlanObj.get("service_plan_guid");

			String space_guid = AppUtils.getInstance().getGuid(spaceJSonObject.toString());

			String service_name = serviceName;
			String token = (String) loginObj.get("access_token");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/service_instances?accepts_incomplete=false");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			String input = "{\"space_guid\":\"" + space_guid + "\",\"name\":\"" + service_name
					+ "\",\"service_plan_guid\":\"" + service_plan_guid + "\",\"parameters\":{\"appname\":\""
					+ service_name + "\"},\"tags\":[\"accounting\",\"mongodb\"]}";
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
			System.out.println("Service Instance has been created");

			// Rsponse format needs to be modified
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject;
			jsonObject = (JSONObject) jsonParser.parse(output);
			JSONObject metaData = (JSONObject) jsonObject.get("metadata");

			// responseObj.put("service_instance_guid",
			// AppUtils.getInstance().getGuid(output));
			jsonObject.put("service_instance_guid", (String) metaData.get("guid"));
			return jsonObject.toJSONString();

		} catch (MalformedURLException e) {
			// e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			// e.printStackTrace();
			return handleException(conn, e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return handleException(conn, e);
		}
	}

	public String restageApp(JSONObject appObj, JSONObject loginObj) {
		HttpURLConnection conn = null;
		try {

			JSONObject metaData = (JSONObject) appObj.get("metadata");
			String app_guid = (String) metaData.get("guid");

			String token = (String) loginObj.get("access_token");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/apps/" + app_guid + "/restage");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			String input = "";
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
			System.out.println("Restaging the app");

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject;
			jsonObject = (JSONObject) jsonParser.parse(output);

			return jsonObject.toJSONString();

		} catch (MalformedURLException e) {
			// e.printStackTrace();
			return handleException(conn, e);
		} catch (IOException e) {
			// e.printStackTrace();
			return handleException(conn, e);
		} catch (ParseException e) {
			return handleException(conn, e);
		}
	}
	public String getAppEnv(JSONObject appObj, JSONObject loginObj) {
		HttpURLConnection conn = null;
		try {
		 
		 
		JSONObject metaData =(JSONObject)appObj.get("metadata");
		String app_guid =(String) metaData.get("guid");
		String token = (String) loginObj.get("access_token");
		URL url = new URL("http://api.54.208.194.189.xip.io/v2/apps/"+ app_guid + "/env");
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Authorization", "bearer " + token);


		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "";
		String line;
		while ((line = br.readLine()) != null) {
		output = output + line;
		}
		conn.disconnect();
		 
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		jsonObject = (JSONObject) jsonParser.parse(output);
		System.out.println("Fetched environment variables");
		return jsonObject.toJSONString();

		} catch (MalformedURLException e) {
		//e.printStackTrace();
		return handleException(conn, e);
		} catch (IOException e) {
		//e.printStackTrace();
		return handleException(conn, e);
		} catch (ParseException e) {
		return handleException(conn, e);
		}
		}


}
