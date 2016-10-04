package com.micro.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.micro.common.Colors;


public class AppServices {
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
		return err.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String createApp(String appname,JSONObject response,String zip_path) {
		HttpURLConnection conn = null;
		try {
			String token = (String) response.get("token");
			JSONArray resources = (JSONArray) response.get("resources");
			JSONObject entity = (JSONObject) resources.get(0);
			JSONObject metadata = (JSONObject) entity.get("metadata");
			String space_guid =  (String) metadata.get("guid");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/apps");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);
			
			String input = "{\"name\":\"" + appname + "\",\"space_guid\":\"" + space_guid + "\"}";
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
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;

			JSONObject temp = (JSONObject) responseObj.get("metadata");
			String app_guid = (String) temp.get("guid");
			responseObj.put("app_guid", app_guid);
			responseObj.put("access_token", token);
			responseObj.put("app_name", appname);
			responseObj.put("space_guid", space_guid);
			//responseObj.put("bind_services", (String) inputObj.get("bind_services"));
			System.out.println(Colors.getAnsiGreen()+"App is created and ready to push"+Colors.getAnsiReset());

			return getDomains(responseObj,zip_path);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(handleException(conn, e));
			return "error";

		} catch (ParseException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}

	}

	@SuppressWarnings("unchecked")
	private String getDomains(JSONObject jsonObject,String zip_path) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/domains");
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
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			JSONArray resources = (JSONArray) responseObj.get("resources");
			JSONObject temp = (JSONObject) resources.get(0);
			temp = (JSONObject) temp.get("metadata");
			String guid = (String) temp.get("guid");
			jsonObject.put("domain_guid", guid);
			System.out.println(Colors.getAnsiGreen()+"Fetched the domain details to push the app"+Colors.getAnsiReset());
			return createRoute(jsonObject,zip_path);

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
	public String createRoute(JSONObject jsonObject,String zip_path) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");
			String appname = (String) jsonObject.get("app_name");
			String space_guid = (String) jsonObject.get("space_guid");
			String domain_guid = (String) jsonObject.get("domain_guid");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/routes");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("authorization", "bearer " + token);

			String input = "{\"space_guid\":\"" + space_guid + "\",\"domain_guid\":\"" + domain_guid + "\",\"host\":\""
					+ appname + "\"}";
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
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(output);
			JSONObject responseObj = (JSONObject) obj;
			JSONObject temp = (JSONObject) responseObj.get("metadata");
			String guid = (String) temp.get("guid");
			jsonObject.put("route_guid", guid);
			System.out.println(Colors.getAnsiGreen()+"Created a route from where application can be accessed"+Colors.getAnsiReset());
			return associateRoute(jsonObject,zip_path);

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

	private String associateRoute(JSONObject jsonObject,String zip_path) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");
			String app_guid = (String) jsonObject.get("app_guid");
			String route_guid = (String) jsonObject.get("route_guid");

			URL url = new URL("http://api.54.208.194.189.xip.io/v2/apps/" + app_guid + "/routes/" + route_guid);
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
			System.out.println(Colors.getAnsiGreen()+"Route is associated with the app."+Colors.getAnsiReset());
			return uploadApplication(jsonObject,zip_path);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);

		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}

	@SuppressWarnings("unused")
	private String uploadApplication(JSONObject jsonObject,String zip_path) {
		String token = (String) jsonObject.get("access_token");
		String app_guid = (String) jsonObject.get("app_guid");
		File uploadFile = new File(zip_path);
		String requestURL = "http://api.54.208.194.189.xip.io/v2/apps/" + app_guid + "/bits?async=false";
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, token);
			multipart.addFormField("resources", "[]");
			multipart.addFilePart("application", uploadFile);
			String output = multipart.finish();
			System.out.println(Colors.getAnsiBlue()+"Application is pushed to the cloud foundry"+Colors.getAnsiReset());
			//return bindService(jsonObject);
			return startApplication(jsonObject);

		} catch (IOException ex) {
			System.err.println(ex);
		}
		return "{\"result\":\"failed\"m\"message\":\"upload failed\"}";
	}

	@SuppressWarnings("unused")
	private String bindService(JSONObject jsonObject) {
		String bind = (String) jsonObject.get("bind_services");
		String bind_url = (String) jsonObject.get("service_bindings_url");
		if (!bind.equalsIgnoreCase("")) {

			HttpURLConnection conn = null;
			try {
				String app_guid = (String) jsonObject.get("app_guid");
				String token = (String) jsonObject.get("access_token");

				URL url = new URL("http://api.54.208.194.189.xip.io/v2/events?q=type:audit.service_binding.create");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("authorization", "bearer " + token);

				String input = "{\"service_instance_guid\":\"" + bind + "\",\"app_guid\":\"" + app_guid
						+ "\",\"parameters\":{\"the_service_broker\":\"wants this object\"}}";
				//System.out.println("this is post body" + input);
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
				System.out.println(Colors.getAnsiGreen()+"Bind Service Resopnse " + output+Colors.getAnsiReset());
				return startApplication(jsonObject);

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return handleException(conn, e);

			} catch (IOException e) {
				return handleException(conn, e);

			}
		} else {
			return startApplication(jsonObject);
		}
	}

	private String startApplication(JSONObject jsonObject) {
		HttpURLConnection conn = null;
		try {
			String token = (String) jsonObject.get("access_token");
			String app_guid = (String) jsonObject.get("app_guid");
			URL url = new URL("http://api.54.208.194.189.xip.io/v2/apps/" + app_guid);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("authorization", "bearer " + token);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String input = "{\"console\":true,\"state\":\"STARTED\"}";
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
			System.out.println(Colors.getAnsiGreen()+"Application has been started"+Colors.getAnsiReset());
			return output;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return handleException(conn, e);

		} catch (IOException e) {
			e.printStackTrace();
			return handleException(conn, e);
		}
	}
}
