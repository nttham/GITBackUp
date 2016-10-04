
package com.appfactory.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.model.UIModelJson;
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
	@Autowired
	private ExceptionUtils eUtils;

	public void envSetup( String ServerName, UIModelJson modelJson) {
		System.out.println(System.getenv("deploy_location"));
		String deploy=System.getenv("deploy_location");
		if(!ApplicationConstants.ENVIRONMENT.equalsIgnoreCase(deploy)){
			deploy=ServerName;
		}
		try {
			switch (deploy) {
			case "cloud":
				accessdata.setBase_dir(System.getenv(ApplicationConstants.PCF_FOLDER_URL));

				accessdata.setStatus_url(System.getenv("pcf_api_appmanager_baseurl"));
				accessdata.setGithub_clonepath(System.getenv("github_clone_path"));
				accessdata.setEndpoint(modelJson.getApi_url());

				accessdata.setRunningapp("https://" + modelJson.getServicename() + "." + modelJson.getHost());

				accessdata.setPasscatalog_url(System.getenv("pcf_server_url"));
				break;
			case "local":
				accessdata.setBase_dir(Messages.getEnv("local_base_path"));
				accessdata.setGithub_clonepath(Messages.getEnv("github_clone_path"));
				accessdata.setStatus_url(Messages.getEnv("local_api_appmanager_baseurl"));

				accessdata.setEndpoint(modelJson.getApi_url());

				accessdata.setRunningapp("https://" + modelJson.getApp_name() + modelJson.getHost());

				accessdata.setPasscatalog_url(ApplicationConstants.PASSCATALOG_URL + modelJson.getHost());
				break;
			default:
				accessdata.setBase_dir(ApplicationConstants.DEV_FOLDER_PATH);

				accessdata.setStatus_url(ApplicationConstants.STATUS_URL);

				accessdata.setEndpoint(modelJson.getApi_url());

				accessdata.setRunningapp("https://" + modelJson.getApp_name() + modelJson.getHost());

				accessdata.setPasscatalog_url(ApplicationConstants.PASSCATALOG_URL + modelJson.getHost());

			}
		} catch (Exception e) {
			eUtils.myException(CustomErrorMessage.ENVSETUP, ExceptionUtils.getTrackTraceContent(e));
		}
	}
}
