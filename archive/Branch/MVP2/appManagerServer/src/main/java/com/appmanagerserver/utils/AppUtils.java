package com.appmanagerserver.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;

@Component
public class AppUtils {

	@Autowired
	private ExceptionUtils eUtils;

	static AppUtils appUtils;
	protected JSONParser parser;
	private String guid;

	public static AppUtils getInstance() {

		if (appUtils == null) {
			return new AppUtils();
		} else {
			return appUtils;
		}
	}

	/**
	 * 
	 * @param output
	 * @return
	 */
	public String getGuid(String output) {
		parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(output);

			JSONArray resArray = (JSONArray) jsonObject.get(ApplicationConstants.RESOURCES);
			JSONObject mDataObj = (JSONObject) resArray.get(0);
			JSONObject metaData = (JSONObject) mDataObj.get(ApplicationConstants.METADATA);
			guid = (String) metaData.get(ApplicationConstants.GUID);

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION7, e.getMessage());
		}
		return guid;
	}

	// GetBindableServiceGuid
	// needs to iterate the response and check for the flag "bindable= true"
	/**
	 * 
	 * @param output
	 * @return
	 */
	public String getBindableServiceGuid(String output) {
		parser = new JSONParser();
		try {
			JSONObject jsonObject;

			jsonObject = (JSONObject) parser.parse(output);

			JSONArray resArray = (JSONArray) jsonObject.get(ApplicationConstants.RESOURCES);
			JSONObject mDataObj = (JSONObject) resArray.get(2);
			JSONObject metaData = (JSONObject) mDataObj.get(ApplicationConstants.METADATA);
			guid = (String) metaData.get(ApplicationConstants.GUID);

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION, e.getMessage());
		}
		return guid;
	}

	/**
	 * 
	 * @param output
	 * @return
	 */
	public String getServicePlanGuid(String output) {
		parser = new JSONParser();
		String guid = null;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(output);
			JSONArray resArray = (JSONArray) jsonObject.get(ApplicationConstants.RESOURCES);
			JSONObject mDataObj = (JSONObject) resArray.get(0);
			JSONObject metaData = (JSONObject) mDataObj.get(ApplicationConstants.METADATA);
			guid = (String) metaData.get(ApplicationConstants.GUID);

		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION1, e.getMessage());
		}
		return guid;
	}

	/**
	 * It converts all the string to a json object.
	 * 
	 * @param input
	 * @return
	 */
	public JSONObject toJson(String input) {
		parser = new JSONParser();

		try {
			return (JSONObject) parser.parse(input);
		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION2, e.getMessage());
			return null;
		}
	}

	public JSONArray toJsonArray(String input) {
		parser = new JSONParser();

		try {

			return (JSONArray) parser.parse(input);
		} catch (ParseException e) {
			eUtils.myException(CustomErrorMessage.PARSEEXCEPTION3, e.getMessage());
			return null;
		}
	}

}
