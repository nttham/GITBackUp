package com.appfactory.platformpush;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ApiUtils;
/**
 * This class has been designed to do all rollback operations here.
 * 
 * @author 559296
 *
 */
@Component
public class RollBackApp {

	@Autowired
	private AccessData accessdata;
	@Autowired
	private ApiUtils apiUtils;
	/**
	 * This method helps to delete the templateapp
	 * @throws MyException
	 */
	public void deleteTheApp() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url()+ApplicationConstants.V2_APPS+accessdata.getTemplateappguid();
		apiUtils.executeDeleteCall(urlLink, requestHeaders);
	}
	
	/**
	 * This method helps to delete the service-instances
	 * @throws MyException
	 */
	public void deleteTheService() throws MyException {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.CONTENT_TYPE_APPLICATION_X);
		requestHeaders.put(ApplicationConstants.AUTHORIZATION,ApplicationConstants.BEARER + accessdata.getuIModelJson().getAccesstoken());
		String urlLink = accessdata.getuIModelJson().getApi_url()+"/v2/service_instances/"+accessdata.getTemplateappguid()+"?async=false&purge=false&recursive=true";
		apiUtils.executeDeleteCall(urlLink, requestHeaders);
	}
}
