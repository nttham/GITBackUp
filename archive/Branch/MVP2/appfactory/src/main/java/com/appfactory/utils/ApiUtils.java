/**
 * 
 */
package com.appfactory.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.ApiExceptions;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.platformpush.RollBackApp;
import com.appfactory.service.AccessData;

/**
 * @author 559296 This class is designed to handle all the rest api exceptions
 */
@Component
public class ApiUtils {
	@Autowired
	private ApiExceptions apiexc;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private AccessData accessdata;
	@Autowired
	private StatusUtils su;
	@Autowired
	private RollBackApp rollBackApp;

	/**
	 * 
	 * @param urlLink
	 * @param connectionProperties
	 * @param statusmsg
	 * @param urlstats
	 * @return
	 * @throws MyException
	 */
	public String executeGetCall(String urlLink, Map<String, String> connectionProperties, String statusmsg,
			String urlstats) throws MyException {
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
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), statusmsg, urlstats,
					101, ApplicationConstants.LOG_LEVEL_INFO);
			return buffer.toString();
		} catch (IOException e) {
			String error = apiexc.handleException(conn, e);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), error, urlstats, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(CustomErrorMessage.GET_API_FAILED, apiexc.handleException(conn, e));
		}
		finally {
			conn.disconnect();
		}

	}

	/**
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @param statusmsg
	 * @param urlstats
	 * @throws MyException
	 */
	public void executerestagePostCall(String urlLink, Map<String, String> connectionProperties,
			String statusmsg, String urlstats) throws MyException {
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
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer buffer = new StringBuffer();
			while (true) {
				final String line = br.readLine();
				if (line == null)
					break;
				buffer.append(line);
			}
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), statusmsg, urlstats,
					101, ApplicationConstants.LOG_LEVEL_INFO);

		} catch (IOException e) {
			String error = apiexc.handleException(conn, e);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), error, urlstats, 101,
					ApplicationConstants.LOG_LEVEL_INFO);
		}
		finally {
			conn.disconnect();
		}
	}

	/**
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @param statusmsg
	 * @param urlstats
	 * @return
	 * @throws MyException
	 */
	public String executePostCall(String urlLink, String input, Map<String, String> connectionProperties,
			String statusmsg, String urlstats) throws MyException {
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
			if (!statusmsg.equalsIgnoreCase("")) {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), statusmsg, urlstats,
						101, ApplicationConstants.LOG_LEVEL_INFO);
			}
			return buffer.toString();
		} catch (IOException e) {
			String error = apiexc.handleException(conn, e);

			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), error, urlstats, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			if (!statusmsg.equalsIgnoreCase(ApplicationConstants.PLATFORM_CREATEAPP)) {
				rollBackApp.deleteTheApp();
				rollBackApp.deleteTheService();
			}
			throw eUtils.myException(CustomErrorMessage.POST_API_FAILED, error);
		}
		finally {
			conn.disconnect();
		}
	}

	/**
	 * 
	 * @param urlLink
	 * @param connectionProperties
	 * @param statusmsg
	 * @param urlstats
	 * @return
	 * @throws MyException
	 */
	public String executePutCall(String urlLink,String input, Map<String, String> connectionProperties, String statusmsg,
			String urlstats) throws MyException {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlLink);
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(ApplicationConstants.PUT_METHOD);

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
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), statusmsg, urlstats,
					101, ApplicationConstants.LOG_LEVEL_INFO);
			return buffer.toString();
		} catch (IOException e) {
			String error = apiexc.handleException(conn, e);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(), error, urlstats, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(CustomErrorMessage.PUT_API_FAILED, apiexc.handleException(conn, e));
		}
		finally {
			conn.disconnect();
		}
	}

	/**
	 * 
	 * @param urlLink
	 * @param connectionProperties
	 * @param statusmsg
	 * @param urlstats
	 * @return
	 * @throws MyException
	 */
	public String executeDeleteCall(String urlLink, Map<String, String> connectionProperties) throws MyException {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlLink);
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(ApplicationConstants.DELETE_METHOD);

			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write("");
			out.close();

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
			String error = apiexc.handleException(conn, e);
			throw eUtils.myException(CustomErrorMessage.DELETE_API_FAILED, error);
		}
		finally {
			conn.disconnect();
		}
	}
}
