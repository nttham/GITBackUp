/**
 * 
 */
package com.appmanagerserver.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.utils.AppUtils;
import com.appmanagerserver.utils.ExceptionUtils;

/**
 * @author 559296
 *
 */
@Component
public class ApiExceptions {
	@Autowired
	private AppUtils apputils;
	@Autowired
	private ExceptionUtils eUtils;
	private static final Logger LOG = Logger.getLogger(ApiExceptions.class.getName());
	/**
	 * 
	 * @param conn
	 * @param e
	 * @return
	 */
	public String handleException(HttpURLConnection conn, Object e) {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
		} catch (IOException e1) { 
			eUtils.myException(CustomErrorMessage.IOEXCEPTION3, e1.getMessage());
		}
		
		JSONObject error=apputils.toJson(output);
		String description=(String) error.get(ApplicationConstants.ERROR_DESCRIPTION);
		conn.disconnect();
		LOG.error(output);
		return description;
	}
}
