
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appfactory.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.DeploymentResponseVO;
import com.appfactory.service.AccessData;
import com.appfactory.utils.AppUtils;
import com.appfactory.utils.ExceptionUtils;
import com.google.gson.Gson;

/**
 * @author 559296
 *
 */
@Service
public class DeployStatusUpdater {
	@Autowired
	private AppUtils apputils;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private AccessData accessdata;
	
	public void updateDeployStatus(String messgaeid,String gistID, DeploymentResponseVO responseObj) throws MyException {

		try {
			Gson gson = new Gson();
			String postBody = gson.toJson(responseObj);
			Map<String, String> requestHeaders = new HashMap<String, String>();
			requestHeaders.put(ApplicationConstants.ACCEPT, ApplicationConstants.JSON_CONTENT_TYPE);
			requestHeaders.put(ApplicationConstants.CONTENT_TYPE, ApplicationConstants.JSON_CONTENT_TYPE);
			String requestURL= accessdata.getStatus_url()+
								ApplicationConstants.BASE_URL+
								ApplicationConstants.SAVE_STATUS+
								ApplicationConstants.FORWARD_SLASH+
								messgaeid+ApplicationConstants.FORWARD_SLASH+gistID;
			 apputils.executePostCall(requestURL, postBody, requestHeaders);
		} catch (Exception e) {
			List<String> values = new ArrayList<String>();
			values.add(e.getMessage());
			throw eUtils.myException(CustomErrorMessage.STATUS_UPDATION_FAILED, values);
		}
				
	}

}
