package com.appfactory.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.core.DeployStatusUpdater;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.DeploymentResponseVO;
import com.appfactory.model.Logs;
/**
 * File : StatusUtils.java Description : This class is designed to
 * set the update and append status :
 * 
 * @author 559296
 *
 */


@Service
public class StatusUtils {
	
	@Autowired
	private DeployStatusUpdater deployStatusUpdater;
	@Autowired
	private DeploymentResponseVO deploymentResponseVO;
	@Autowired
	private Logs log;

	public  void updatestatus(String messageid,String gistID,String finalstatus,String cfAppURL,int StatusCode,String loglevel){
		deploymentResponseVO.setCfAppURL(cfAppURL);
		log.setLoglevel(loglevel);
		log.setMessage(finalstatus);
		log.setTimeStamp(new Timestamp(new Date().getTime()));
		log.setStatusCode(StatusCode);
		List<Logs> logsList = new ArrayList<Logs>();
		logsList.add(log);
		deploymentResponseVO.setLogsList(logsList);
		try {
			deployStatusUpdater.updateDeployStatus(messageid,gistID, deploymentResponseVO);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
}
