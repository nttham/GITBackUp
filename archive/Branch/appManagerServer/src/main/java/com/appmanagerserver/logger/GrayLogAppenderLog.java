/**
 * 
 */
package com.appmanagerserver.logger;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.messages.Messages;
import com.appmanagerserver.utils.ApiUtils;
import com.appmanagerserver.utils.ExceptionUtils;
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
	private ApiUtils apiutils;
	@Autowired
	private ExceptionUtils eUtils;
	
	
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
			inputObject.put(ApplicationConstants.APP_ID, "DeployProcessor");

			postBody = inputObject.toJSONString();

			requestHeaders.put(ApplicationConstants.CONTENTTYPE, ApplicationConstants.JSON_CONTENT_TYPE);

			String response=apiutils.executePostCall(requestURL, postBody, requestHeaders);

		} catch (Exception e) {
			throw eUtils.myException(CustomErrorMessage.INTERNAL_SERVER_ERROR, "null");
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
}
