/**
 * 
 */
package com.appfactory.core;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.model.OtherServices;
import com.appfactory.platformpush.BindService;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

/**
 * @author 559296
 *
 */
@Component
public class UnBoundApps {
	@Autowired
	private AccessData accessdata;
	@Autowired
	private BindService bservice;
	@Autowired
	private StatusUtils su;
	@Autowired
	private Microservices microservice;
	@Autowired
	private MultiIOprocessing miop;
	@Autowired
	private ExceptionUtils eUtils;

	/**
	 * This will create two service instance of user selected service
	 * @throws MyException
	 * @throws InterruptedException 
	 */
	public void createunboundapp() throws MyException, InterruptedException {
		su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
				ApplicationConstants.UNBOUND, ApplicationConstants.CFURL, 101, ApplicationConstants.LOG_LEVEL_INFO);

		accessdata.setEnv_json(miop.envJson(accessdata.getuIModelJson().getEnvironment_json()));

		String[] appurl = accessdata.getuIModelJson().getAppurl().split(ApplicationConstants.FORWARD_SLASH);
		String tempappfolder = appurl[1];
		JSONObject createservice = null;
		if(!(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER))){
			  createservice = microservice.serviceobj();
		}else if(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.PUSHNOTIFICATION)){
			microservice.createservicePush();
		}
		else{
			createservice = microservice.serviceobjLogger();
		}
		
		final String newservicename = accessdata.getuIModelJson().getServicename() + tempappfolder+ApplicationConstants.UNBOUND_NAME;
		bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(),
				accessdata.getuIModelJson().getServiceplanguid(), createservice, newservicename);
		configureService();
	}
	
	/**
	 * This will create only the loggerservice instance
	 * @throws MyException
	 */
	public void configureService() throws MyException {
		if(!(accessdata.getuIModelJson().getCategory().equalsIgnoreCase(ApplicationConstants.LOGGER))){
			try {
				ArrayList<OtherServices> otherserv = accessdata.getuIModelJson().getOtherservices();
				if (otherserv.size() > 0) {
					for (OtherServices each : otherserv) {
						if (each.getProvider() != null) {
							if (each.getService_plan_guid() != null || !each.getService_plan_guid().isEmpty()) {

								final JSONObject createservice = microservice.serviceobjLogger();
								
								final String newservicename =accessdata.getuIModelJson().getServicename()+ accessdata.getuIModelJson().getCategory() + each.getProvider().getName();
								
								bservice.createServiceInstances(accessdata.getuIModelJson().getSpaceguid(),
										each.getService_plan_guid(), createservice, newservicename);
							}
						}
					}
				}
			} catch (Exception e) {
				throw eUtils.myException(CustomErrorMessage.UNBOUND_CONFIGURE_ERROR, ExceptionUtils.getTrackTraceContent(e));
			}
		}
		
	}
}
