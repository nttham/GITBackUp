/**
 * 
 */
package com.appfactory.exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.service.AccessData;
import com.appfactory.utils.AppUtils;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

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
	@Autowired
	private StatusUtils su;
	@Autowired
	private AccessData accessdata;
	private static final Logger LOG = Logger.getLogger(ApiExceptions.class.getName());

	/**
	 * 
	 * @param conn
	 * @param e
	 * @return
	 * @throws MyException
	 */
	public String handleException(HttpURLConnection conn, Object e) throws MyException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			String output = "";
			String line;
			try {
				while ((line = br.readLine()) != null) {
					output = output + line;
				}
			} catch (IOException e1) {
				eUtils.myException(CustomErrorMessage.IOEXCEPTION_3, e1.getMessage());
			}

			JSONObject error = apputils.toJson(output);
			String description = (String) error.get(ApplicationConstants.ERROR_DESCRIPTION);
			conn.disconnect();
			LOG.error(output);
			return description;
		} catch (Exception e1) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.API_SERVER_ERROR, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			eUtils.myException(CustomErrorMessage.API_ERROR, ExceptionUtils.getTrackTraceContent(e1));
			return null;
		}
	}
}
