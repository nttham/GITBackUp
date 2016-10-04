/**
 * 
 */
package com.appfactory.platformpush;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ApiUtils;
import com.appfactory.utils.StatusUtils;

/**
 * This will restage the app.
 * 
 * @author 559296
 *
 */
@Component
public class RestageApp {
	@Autowired
	private ApiUtils apiUtils;
	@Autowired
	private AccessData accessdata;
	@Autowired
	private StatusUtils su;

	/**
	 * This we need to call every time once we bind anything to the app
	 * 
	 * @param app_guid
	 * @return
	 * @throws MyException
	 */
	public void restageApp(String app_guid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS + app_guid
				+ "/restage";
		apiUtils.executerestagePostCall(urlLink, requestHeaders, ApplicationConstants.APP_RESTAGE,
				ApplicationConstants.CFURL);
		
	}

	/**
	 * 
	 * @param app_guid
	 * @throws MyException
	 * @throws InterruptedException 
	 */
	public void appstats(String app_guid) throws MyException, InterruptedException {
		Thread.sleep(40000);
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS + app_guid;
		String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.APP_HEALTH,
				ApplicationConstants.CFURL);
		JSONObject responseObj = new JSONObject(response);
		while (responseObj.has(ApplicationConstants.PACKAGE_STATE)) {
			if (responseObj.getString(ApplicationConstants.PACKAGE_STATE)
					.equalsIgnoreCase(ApplicationConstants.PACKAGE_STATE)) {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.APP_HEALTH_GREEN + responseObj.getString(ApplicationConstants.APP_STATE),
						ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
			} else {
				su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
						ApplicationConstants.APP_HEALTH, ApplicationConstants.CFURL, 103,
						ApplicationConstants.LOG_LEVEL_INFO);
				appstats(app_guid);
			}

		}
	}

	/**
	 * 
	 * @param app_guid
	 * @throws MyException
	 */
	public void checkAppStatus(final String appguid, int count) throws MyException {

		if (count < 5) {
			Map<String, String> requestHeaders = new HashMap<String, String>();
			requestHeaders.put(ApplicationConstants.AUTHORIZATION,
					ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
			String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS + appguid
					+ "/instances";
			String response = apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.APP_HEALTH,
					ApplicationConstants.CFURL);
			JSONObject responseObj = new JSONObject(response);
			for (int i = 0; i < responseObj.length(); i++) {
				JSONObject apphealth = responseObj.getJSONObject(String.valueOf(i));
				if (apphealth.has(ApplicationConstants.APP_STATE)) {
					su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
							ApplicationConstants.APP_HEALTH_GREEN + apphealth.getString(ApplicationConstants.APP_STATE),
							ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);
				} else {
					su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
							ApplicationConstants.APP_HEALTH, ApplicationConstants.CFURL, 103,
							ApplicationConstants.LOG_LEVEL_INFO);
					checkAppStatus(appguid, count++);
				}
			}
		} else {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.APP_HEALTH_RED, ApplicationConstants.CFURL, 103,
					ApplicationConstants.LOG_LEVEL_INFO);

		}

	}
	
	/**
	 * This method is desgined to show case all the stats related to the 
	 * template app.
	 * @param app_guid
	 * @throws MyException
	 */
	public void statsofApp(String app_guid) throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,
				ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url() + ApplicationConstants.V2_APPS + app_guid
				+ "/stats";
		apiUtils.executeGetCall(urlLink, requestHeaders, ApplicationConstants.GET_STATS_APP, ApplicationConstants.CFURL);

		
	}
}
