
package com.appfactory.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;

/**
 * File : Application.java Description : This class is desgined to set all the
 * enviromental variables Revision History : Version Date Author Reason 0.1
 * 16-Aug-2016 559296 Initial version
 */

@Component
public class EnvSetup {
	@Autowired
	private AccessData accessdata;

	public void envSetup(final String ServerName) {
		switch (ServerName) {
		case "cloud":
			accessdata.setBase_dir(Messages.getEnv("PCF_Base_path"));
			accessdata.setStatus_url("https://appmanagerserver." + accessdata.getuIModelJson().getHost());

			break;
		case "local":
			accessdata.setBase_dir(Messages.getEnv("Local_Base_path"));
			accessdata.setStatus_url(Messages.getEnv("local_api.appmanager.baseurl"));
			break;
		case "dev":
			accessdata.setBase_dir(Messages.getEnv("DEV_Base_path"));
			accessdata.setStatus_url(Messages.getEnv("dev_api.appmanager.baseurl"));
		break;
		case "qa":
			accessdata.setBase_dir(Messages.getEnv("QA_Base_path"));
			accessdata.setStatus_url(Messages.getEnv("qa_api.appmanager.baseurl"));

			break;
		default:
			accessdata.setBase_dir(Messages.getEnv("Local_Base_path"));
			accessdata.setStatus_url("https://" + accessdata.getuIModelJson().getHost() + ":8080/appManagerServer");

		}

	}
}
