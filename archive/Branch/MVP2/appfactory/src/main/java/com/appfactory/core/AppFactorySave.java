package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.model.UIModelJson;
import com.appfactory.route.IappFactory;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

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
	private MultiIOprocessing miop;
	@Autowired
	private StatusUtils su;
	@Autowired
	private ExceptionUtils eUtils;

	@Override
	public List<String> doAction(UIModelJson uIModelJson, String whatAction, String messageid) throws MyException {
		final List<String> returnarr = new ArrayList<String>();
		switch (whatAction) {
		case "save":
			try {
				accessdata.setuIModelJson(uIModelJson);
				accessdata.setMessageid(messageid);
				accessdata.setModifiedprotocol(uIModelJson.getCategory());
			} catch (Exception e1) {
				su.updatestatus(messageid, accessdata.getuIModelJson().getGistid(), ApplicationConstants.WRONG_JSON,
						ApplicationConstants.CFURL_FAILED, 102, ApplicationConstants.LOG_LEVEL_ERROR);
				eUtils.myException(CustomErrorMessage.JSONException, ExceptionUtils.getTrackTraceContent(e1));
			}
			break;
		case "download":
			generateApp.baseDirectory();
			File parentpath = new File(accessdata.getParentdirectory());
			miop.removeDirectory(parentpath);
			break;
		default:
			su.updatestatus(messageid, accessdata.getuIModelJson().getGistid(), ApplicationConstants.WRONG_JSON,
					ApplicationConstants.CFURL_FAILED, 102, ApplicationConstants.LOG_LEVEL_ERROR);
			return returnarr;

		}
		return returnarr;
	}

	@Override
	public void delete() {
		try {
			File parentpath = new File(accessdata.getParentdirectory());
			miop.removeDirectory(parentpath);
		} catch (Exception  e) {
			eUtils.myException(CustomErrorMessage.FORCE_DELETE, ExceptionUtils.getTrackTraceContent(e));
		}
	}
}
