package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.model.BluePrint;
import com.appfactory.model.UIModelJson;
import com.appfactory.resources.Messages;
import com.appfactory.route.IappFactory;
import com.appfactory.service.AccessData;

/**
 * File : AppFactorySave.java Description : This class is designed to
 * do action(save/download) required according to request from Listener class :
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
	static Logger log = Logger.getLogger(AppFactorySave.class.getName());

	@Override
	public List<String> doAction(UIModelJson uIModelJson, String whatAction,String messageid) throws MyException {
		final List<String> returnarr = new ArrayList<String>();
		switch (whatAction) {
		case "save":
			try {
				//accessdata.setBlueprint(blueprint);
				accessdata.setuIModelJson(uIModelJson);
				accessdata.setMessageid(messageid);
//				accessdata.setModifiedprotocol(blueprint.getPrimaryservice().getProtocol());
//				if(!blueprint.getLogger().getPortNo().isEmpty()){
//					accessdata.setLogstatus(ApplicationConstants.EXTERNAL);
//				}else if(!blueprint.getLogger().getService_plan_guid().isEmpty()){
//					accessdata.setLogstatus(ApplicationConstants.NEWLOGGER);
//				}else if(!blueprint.getLogger().getService_instance_id().isEmpty()){
//					accessdata.setLogstatus(ApplicationConstants.EXISTING);
//				}
				accessdata.setModifiedprotocol(uIModelJson.getCategory());
				if(!uIModelJson.getEnvironment_json().getLogger().getPortNo().isEmpty()){
					accessdata.setLogstatus(ApplicationConstants.EXTERNAL);
				}else if(!uIModelJson.getEnvironment_json().getLogger().getService_plan_guid().isEmpty()){
					accessdata.setLogstatus(ApplicationConstants.NEWLOGGER);
				}else if(!uIModelJson.getEnvironment_json().getLogger().getService_instance_id().isEmpty()){
					accessdata.setLogstatus(ApplicationConstants.EXISTING);
				}
			} catch (Exception e1) {
				log.error(Messages.getString("AppFactorySave.1") + e1.getMessage());
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
