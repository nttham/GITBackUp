package com.appManager.constants;

import com.appManager.resources.Messages;
/**
 * File : ApplicationConstants.java 
 * Description : This class is designed to 
 * store all the constants
 * Revision History : 
 * @author 559296
 *
 */
public class ApplicationConstants {

	public static enum Platform {
		OneC, Bluemix, Pivotal, Azure
	}

	public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
	public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
	public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint"); 
	public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
	public static final String INTERNAL_SERVER_ERROR = "The server encountered an internal error. Please retry the request.";

	public enum BASE_URL {

		BASE_URL_ONEC(Messages.getString("OneC_API_EndPoint")), BASE_URL_BLUEMIX(
				Messages.getString("Bluemix_API_EndPoint")), BASE_URL_PIVOTAL(Messages.getString("Pivotal_API_EndPoint")), BASE_URL_AZURE(Messages.getString("Azure_API_EndPoint"));

		private String url;

		private BASE_URL(String url) {
			this.url = url;
		}

		public String url() {
			return url;
		}
	}

	public enum AUTH_URL {

		AUTH_URL_ONEC(Messages.getString("OneC_login_url")), AUTH_URL_BLUEMIX( //$NON-NLS-1$
				Messages.getString("Bluemix_login_url")), AUTH_URL_PIVOTAL(Messages.getString("Pivotal_login_url")), AUTH_URL_AZURE(Messages.getString("Azure_login_url"));

		private String loginUrl;

		private AUTH_URL(String loginURL) {
			this.loginUrl = loginURL;

		}

		public String loginURL() {
			return loginUrl;
		}
	}
	
	

	public static  String catalogurl =  getServerURL()+ "PaaSCatalog/catalog";

	public static  String speccaturl = getServerURL()+"PaaSCatalog/platformservices";

	public static  String searchcatalogurl = getServerURL()+"PaaSCatalog/searchCatalog";
	
//	public static final String AUTH_CATEGORY_URL="http://localhost:3030/PaaSCatalog/getCatalog";

	
	private static String getServerURL(){
		return Messages.getString("SERVER_URL"); 
	}
}
