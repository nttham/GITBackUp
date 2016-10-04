package com.appfactory.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.resources.Messages;


@Component
public class AppUtils {
	@Autowired
	private ExceptionUtils eUtils;

	
	static AppUtils appUtils;
	protected JSONParser parser;
	private String guid;
	public static AppUtils getInstance(){
		
		if(appUtils==null)
{
		return	new AppUtils();
	}
		else{
			return appUtils;
		}
}
	
	@Autowired
	private ExceptionUtils utils;
	/**
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @return
	 * @throws MyException
	 */
	public String executePostCall(String urlLink, String input, Map<String, String> connectionProperties) throws MyException {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlLink);
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(ApplicationConstants.POST_REQUEST);

			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer buffer = new StringBuffer();
			while (true) {
				final String line = br.readLine();
				if (line == null)
					break;
				buffer.append(line);
			}
			conn.disconnect();
			return buffer.toString();
		} catch (Exception e) {
			conn.disconnect();
			throw utils.myException(CustomErrorMessage.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
		}
	}
	/**
	 * 
	 * @param output
	 * @return
	 */
	public String getGuid(String output){
		 parser = new JSONParser();
		try {
		JSONObject jsonObject = (JSONObject)parser.parse(output);
		

		JSONArray resArray =(JSONArray) jsonObject.get(ApplicationConstants.RESOURCES); 
		JSONObject mDataObj = (JSONObject) resArray.get(0);
		JSONObject metaData =(JSONObject)mDataObj.get(ApplicationConstants.METADATA);
		 guid =(String) metaData.get(ApplicationConstants.GUID); 
		
		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION1, e.getMessage());
		}
		return guid;
	}
/**
 * 
 * @param output
 * @return
 */
	public String getBindableServiceGuid(String output){
		 parser = new JSONParser();
		try {
		JSONObject jsonObject;
	
			jsonObject = (JSONObject)parser.parse(output);
		

		JSONArray resArray =(JSONArray) jsonObject.get(Messages.getString("AppUtils.resources"));  
		JSONObject mDataObj = (JSONObject) resArray.get(1);
		JSONObject metaData =(JSONObject)mDataObj.get(Messages.getString("AppUtils.metadata"));  
		 guid =(String) metaData.get(Messages.getString("AppUtils.guid"));  
		
		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION2, e.getMessage());
		}
		return guid;
	}
	/**
	 * 
	 * @param output
	 * @return
	 */
	public String getServicePlanGuid(String output){
		 parser = new JSONParser();
			String guid=null;
			try {
		JSONObject jsonObject = (JSONObject)parser.parse(output);
		JSONArray resArray =(JSONArray) jsonObject.get(Messages.getString("AppUtils.resources"));  
		JSONObject mDataObj = (JSONObject) resArray.get(0);
		JSONObject metaData =(JSONObject)mDataObj.get(Messages.getString("AppUtils.metadata"));  
		 guid =(String) metaData.get(Messages.getString("AppUtils.guid"));  
			
	} catch (ParseException e) {
		eUtils.myException(CustomErrorMessage.PARSEEXCEPTION3, e.getMessage());
	}
		return guid;
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	public JSONObject toJson(String input){
		 parser = new JSONParser();

	try {
		return	(JSONObject) parser.parse(input);
	} catch (ParseException e) {
		eUtils.myException(CustomErrorMessage.PARSEEXCEPTION4, e.getMessage());
		return null;
	}
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	public JSONArray toJsonArray(String input){
		 parser = new JSONParser();
			try {				
				return	(JSONArray) parser.parse(input);
			} catch (ParseException e) {
				eUtils.myException(CustomErrorMessage.PARSEEXCEPTION5, e.getMessage());
				return null;
			}	
	}	
}