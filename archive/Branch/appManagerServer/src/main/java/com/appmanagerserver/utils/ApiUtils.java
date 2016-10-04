/**
 * 
 */
package com.appmanagerserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.ApiExceptions;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;

/**
 * @author 559296
 *This class is designed to have all type of rest api call in it.
 */
@Component
public class ApiUtils {
	@Autowired
	private ApiExceptions apiexc;
	@Autowired
	private ExceptionUtils eUtils;
	public String executeGetCall(String urlLink, Map<String, String> connectionProperties) throws MyException{
		HttpURLConnection conn = null;
	
			try {
				URL url = new URL(urlLink);
				SSLUtilities.disableSSLCertificateChecking();
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod(ApplicationConstants.GET_REQUEST);

				for (String headerKey : connectionProperties.keySet()) {
					conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
				}

				BufferedReader bufferedReader = null;
				
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				

				StringBuffer buffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					buffer.append(line);
				}
				conn.disconnect();
				return buffer.toString();
			} catch (IOException e) {
				throw eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, apiexc.handleException(conn, e));
			} 
		
		
	}
	
	public String executePostCall(String urlLink, String input, Map<String, String> connectionProperties) throws MyException  {
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
			throw eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, apiexc.handleException(conn, e));
		} 
	}
}
