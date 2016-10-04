package com.appManager.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appManager.exceptions.MyException;
import com.appManager.ioprocessing.MultiIOprocessing;
import com.appManager.model.BluePrint;
import com.appManager.resources.Messages;
import com.appManager.route.IappFactory;
import com.appManager.service.AccessData;

@Service
public class AppFactorySave implements IappFactory {
	@Autowired
	private AccessData accessdata;
	@Autowired
	private GenerateApp generateApp;
	static Logger log = Logger.getLogger(AppFactorySave.class.getName());

	@Override
	public List<String> doAction(BluePrint blueprint, String whatAction) throws MyException {
		final List<String> returnarr = new ArrayList<String>();
		switch (whatAction) {
		case "save":
			try {
				accessdata.setBlueprint(blueprint);
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
				returnarr.add(accessdata.getBlueprint().getDevlopergiturl());
			}

			break;
		default:
			log.info(Messages.getString("AppFactorySave.38"));
		}
		return returnarr;

	}
}
