/**
 * 
 */
package com.appfactory.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.ApiExceptions;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.resources.Messages;
import com.appfactory.route.ILogger;
import com.appfactory.utils.ExceptionUtils;



/**
 * @author 559296
 *
 */
@Component
public class GrayLogAppenderLog implements ILogger{

	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private ApiExceptions apiexc;
	
	/**
	 * 
	 * @param level
	 * @param message
	 * @throws MyException
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void saveLogToGrayLog(String level, String message) throws MyException {
		try {
			Map<String, String> requestHeaders = new HashMap<String, String>();

			String postBody = null;

			JSONObject inputObject = new JSONObject();
			String requestURL = Messages.getString(ApplicationConstants.GRAY_LOG_URL);

			inputObject.put(ApplicationConstants.LEVEL, level);
			inputObject.put(ApplicationConstants.MESSAGE, message);
			inputObject.put(ApplicationConstants.APP_ID, "AppFactory");

			postBody = inputObject.toJSONString();

			requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.JSON_CONTENT_TYPE);

			String response=executePostCall(requestURL, postBody, requestHeaders,"","");

		} catch (Exception e) {
			throw eUtils.myException(CustomErrorMessage.INTERNAL_SERVER_ERROR,ApplicationConstants.UNABLE_GREY);
		}
	}

	
	@Override
	public void debug(String msg) {

		try {
			saveLogToGrayLog(ApplicationConstants.DEBUG, msg);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String msg) {

		try {
			saveLogToGrayLog(ApplicationConstants.INFO, msg);
		} catch (MyException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void error(String msg) {

		try {
			saveLogToGrayLog(ApplicationConstants.ERROR, msg);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
	private String executePostCall(String urlLink, String input, Map<String, String> connectionProperties,String statusmsg,String urlstats) throws MyException  {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlLink);
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
		} catch (IOException e) {
			 eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, apiexc.handleException(conn, e));
			 return null;
		} 
	}
}
