/**
 * 
 */
package com.appmanagerserver.logger;

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

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.ApiExceptions;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.messages.Messages;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerserver.utils.SSLUtilities;
import com.appmanagerservice.sendinterface.ILogger;

/**
* File                          : GrayLogAppenderLog.java
* Description          : This class will support logging in graylog microservice api.
* Revision History :
* Version      Date            Author       Reason
* 0.1          26-Aug-2016     559296  Initial version
*/
@Component
public class GrayLogAppenderLog implements ILogger {
	@Autowired
	private ApiExceptions apiexc;
	@Autowired
	private ExceptionUtils eUtils;
	
	
	/**
	 * 
	 * @param level
	 * @param message
	 * @throws MyException
	 */

	@SuppressWarnings("unchecked")
	public void saveLogToGrayLog(String level, String message) throws MyException {
		try {
			System.out.println("Its inside saveLogToGrayLog");
			Map<String, String> requestHeaders = new HashMap<String, String>();

			String postBody = null;

			JSONObject inputObject = new JSONObject();

			String requestURL = Messages.getString(ApplicationConstants.GRAY_LOG_URL);

			inputObject.put(ApplicationConstants.LEVEL, level);
			inputObject.put(ApplicationConstants.MESSAGE, message);
			inputObject.put(ApplicationConstants.APP_ID, "AppManager-pcf-mvp2");

			postBody = inputObject.toJSONString();

			requestHeaders.put(ApplicationConstants.CONTENTTYPE, ApplicationConstants.JSON_CONTENT_TYPE);

			executePostCall(requestURL, postBody, requestHeaders);

		} catch (Exception e) {
			 eUtils.myException(CustomErrorMessage.GREYLOG_SERVER_ERROR,ApplicationConstants.UNABLE_GREY);
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
	/**
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @return
	 * @throws MyException
	 */
	private String executePostCall(String urlLink, String input, Map<String, String> connectionProperties) throws MyException  {
		HttpURLConnection conn = null;
		try {
			System.out.println("Its inside executePostCall");
			SSLUtilities.disableSSLCertificateChecking();
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
			
			return buffer.toString();
		} catch (IOException e) {
			System.out.println("Its inside executePostCall error");
			 eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, apiexc.handleException(conn, e));
			 return null;
		} finally {
			conn.disconnect();
		}
	}
}
