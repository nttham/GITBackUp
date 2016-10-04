package com.appfactory.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.core.DeployStatusUpdater;
import com.appfactory.exceptions.CustomErrorMessage;
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
	private ExceptionUtils eUtils;

	public  void updatestatus(final String messageid,final String gistID,final String finalstatus,final String cfAppURL,final int StatusCode,final String loglevel){
		deploymentResponseVO.setCfAppURL(cfAppURL);
		Logs log = new Logs();
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
			eUtils.myException(CustomErrorMessage.NULL_POINTER_ERROR, e.getMessage());
		}
	}
}
