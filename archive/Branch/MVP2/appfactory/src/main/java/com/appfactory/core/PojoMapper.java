package com.appfactory.core;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.UIModelJson;
import com.appfactory.route.IappFactory;
import com.appfactory.utils.EnvSetup;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

@Component
public class PojoMapper {
	@Autowired
	private IappFactory apfactory;
	@Autowired
	private StatusUtils su;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private EnvSetup envsetup;
	
	@Value("${war.deployment.environment}")
	private String deployLocation;
	
	public void processQueue(final String messageContent,final String messageID,final String gistid) {
		ObjectMapper mapper = new ObjectMapper();
		UIModelJson modelJson = new UIModelJson();
		try {
			modelJson = mapper.readValue(messageContent, UIModelJson.class);
			envsetup.envSetup(deployLocation,modelJson);
			apfactory.doAction(modelJson, ApplicationConstants.SAVE, messageID);
			apfactory.doAction(modelJson, ApplicationConstants.DOWNLOAD, messageID);
		} catch (MyException | IOException e) {
			try{
			su.updatestatus(messageID, gistid, ApplicationConstants.WRONG_JSON, ApplicationConstants.CFURL_FAILED, 102, ApplicationConstants.LOG_LEVEL_ERROR);
			apfactory.delete();
			eUtils.myException(CustomErrorMessage.JSONException, e.getMessage());
		}catch(Exception e1){
			eUtils.myException(CustomErrorMessage.NULL_POINTER_ERROR, e.getMessage());
		}
		}
	}
}