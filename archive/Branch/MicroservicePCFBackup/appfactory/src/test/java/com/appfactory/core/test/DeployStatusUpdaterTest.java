package com.appfactory.core.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.appfactory.core.DeployStatusUpdater;
import com.appfactory.exceptions.MyException;

//import static org.junit.Assert.assertNotNull;

public class DeployStatusUpdaterTest {

	@Autowired
	DeployStatusUpdater deployStatusUpdater;
	
	@Autowired
	com.appfactory.model.DeploymentResponseVO DeploymentResponseVO;
	
	com.appfactory.model.DeploymentResponseVO deploymentResponseVO= new com.appfactory.model.DeploymentResponseVO();

	public void updateDeployStatusTest() throws MyException{
		 
		String messgaeid= "0f87ea0f-b95f-475d-b361-96dfd212add6";
		
		deploymentResponseVO.setCfAppURL("http://54.169.136.251:8080/v2/spaces/b169a527-a10a-4a84-a45a-2909fee6b1d9/apps");
		
		
		//deploymentResponseVO.setLocalDownloadPath("C:/Users/540299/.eclipse");
		

		
	}
	 
	
	}


