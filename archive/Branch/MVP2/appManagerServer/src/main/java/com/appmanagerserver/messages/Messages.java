package com.appmanagerserver.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * File : Message.java 
 * Description : It connect the properties file to 
 * required classes
 * Revision History : 
 * Version 	Date  	Author Reason 
 * 0.1 	27-May-2016 559296 Initial version
 */
public class Messages {
	private static final String BUNDLE_NAME = "messages_en_US";
	private static final String DOCS_FILE_NAME = "apidocs_en_US";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	private static final ResourceBundle DOCS_BUNDLE = ResourceBundle.getBundle(DOCS_FILE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getDocs(String key) {
		try {
			return DOCS_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
