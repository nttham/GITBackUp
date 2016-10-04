package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.model.OtherServices;
import com.appfactory.model.UIModelJson;
import com.appfactory.resources.Messages;
import com.appfactory.route.IappFactory;
import com.appfactory.service.AccessData;
import com.appfactory.utils.EnvSetup;
import com.appfactory.utils.ExceptionUtils;

/**
 * File : AppFactorySave.java Description : This class is designed to do
 * action(save/download) required according to request from Listener class :
 * 
 * @author 559296
 *
 */
@Service
public class AppFactorySave implements IappFactory {
	@Autowired
	private AccessData accessdata;
	@Autowired
	private GenerateApp generateApp;
	@Autowired
	private EnvSetup envsetup;
	@Autowired
	private ExceptionUtils eUtils;

	static Logger log = Logger.getLogger(AppFactorySave.class.getName());

	@Value("${war.deployment.environment}")
	private String deployLocation;

	@Override
	public List<String> doAction(UIModelJson uIModelJson, String whatAction, String messageid) throws MyException {
		final List<String> returnarr = new ArrayList<String>();
		switch (whatAction) {
		case "save":
			try {
				accessdata.setuIModelJson(uIModelJson);
				accessdata.setMessageid(messageid);
				envsetup.envSetup(deployLocation);
				accessdata.setModifiedprotocol(uIModelJson.getCategory());
				ArrayList<OtherServices> otherserv = accessdata.getuIModelJson().getOtherservices();

				for (OtherServices each : otherserv) {

					if (each.getService_plan_guid()!=null && each.getProvider()!=null) {

						if (each.getProvider().getService_plan_guid()!=null) {
							accessdata.setLogstatus(ApplicationConstants.NEWLOGGER);
						} else if (each.getProvider().getService_instance_id()!=null) {
							accessdata.setLogstatus(ApplicationConstants.EXISTING);
						} else if (each.getProvider().getPortNo()!=null) {	
							accessdata.setLogstatus(ApplicationConstants.EXTERNAL);
						}
					}
					accessdata.setLogstatus(ApplicationConstants.DEFAULT);	
				}

			} catch (Exception e1) {
				throw eUtils.myException(CustomErrorMessage.CREATE_DOMAIN_ERROR, e1.getMessage());
			}
			break;
		case "download":
			String result = null;
			result = generateApp.baseDirectory();
			File parentpath = new File(accessdata.getParentdirectory());
			MultiIOprocessing miop = new MultiIOprocessing();
			miop.removeDirectory(parentpath);

			if (result.equalsIgnoreCase("failed")) {
				log.error("Operation was a failure");
				returnarr.add(Messages.getString("AppFactorySave.36"));
				returnarr.add(Messages.getString("AppFactorySave.37"));
			} else {
				log.info("Operation was a success");
				returnarr.add(accessdata.getRunningapp());
				returnarr.add(accessdata.getuIModelJson().getDevlopergiturl());
			}

			break;
		default:
			log.info(Messages.getString("AppFactorySave.38"));
		}
		return returnarr;

	}
}
