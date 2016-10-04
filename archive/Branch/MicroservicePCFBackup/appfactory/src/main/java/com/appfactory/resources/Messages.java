package com.appfactory.resources;

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

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
