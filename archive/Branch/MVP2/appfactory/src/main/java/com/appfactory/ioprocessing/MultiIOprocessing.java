package com.appfactory.ioprocessing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.Environment_Json;
import com.appfactory.utils.ExceptionUtils;
/**
 * File : MultiIOprocessing.java 
 * Description : This class is designed to do all i/o based operation.
 * 				 We can add some common methods in this class also.
 * 
 * @author 559296
 *
 */
@Component
public class MultiIOprocessing {
	@Autowired
	private  ExceptionUtils eUtils;
	static Logger log = Logger.getLogger(MultiIOprocessing.class.getName());
	/**
	 * 
	 * @param whichdirectory
	 * @param whatname
	 * @return
	 */
	public File createFolder(String whichdirectory, String whatname) {
		File file = new File(whichdirectory);
		File application = new File(whichdirectory + ApplicationConstants.FORWARD_SLASH + whatname);  
		if (!file.exists()) { 
			if (file.mkdir()) {
				application.mkdir();
				log.info(ApplicationConstants.ABLE_CREATE_DIRECTORY+whichdirectory+"  "+whatname);				
			} else {
				log.error(ApplicationConstants.UNABLE_CREATE_DIRECTORY+whichdirectory+"  "+whatname);	
			}
		}
		return application;
	}

/**
 * 
 * @param dir
 * @throws MyException
 */
	public void removeDirectory(File dir) throws MyException {
			try {
				FileDeleteStrategy.FORCE.delete(dir);
			} catch (IOException e) {
				throw eUtils.myException(CustomErrorMessage.FORCE_DELETE,e.getMessage());
			}
			
	}
/**
 * 
 * @param source
 * @param dest
 * @return
 * @throws MyException
 */
	public String copyFiles(File source, File dest) throws MyException {
		try {
			if (source.exists()) {
				if (!source.isDirectory()) {
					FileUtils.copyFileToDirectory(source, dest);
				} else {
					FileUtils.copyDirectory(source, dest);
				}
			} else {
				return "There is no such directory";
			}
			return "Files are copied";
		} catch (IOException e) {

		throw eUtils.myException(CustomErrorMessage.IOEXCEPTION_COPY,  e.getMessage());
		}
	}



	/**
	 * 
	 * @param environmentJson
	 * @return
	 */
	public JSONObject envJson(Environment_Json environmentJson){
		
		JSONObject object = new JSONObject(environmentJson);
		return object;
	}
}
