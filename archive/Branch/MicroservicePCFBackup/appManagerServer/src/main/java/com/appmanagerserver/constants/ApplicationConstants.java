
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerserver.constants;

import com.appmanagerserver.messages.Messages;

/**
 * @author 559296
 *
 */
public class ApplicationConstants {
	
	//For any api call
		public static final String BASE_URL ="/app";
		public static final String GIST_BASE_URL ="/app";
		public static final String FORWARD_SLASH ="/";
		public static final String LOCAL_DOWNLOAD ="/";
		public static final String GIST_CLONE_PATH="";
		public static final String GIST_FILE_UPLOAD_COMMENTS="Template app has been uploaded";
		public static final String EMPTY_STRING="";
		//For appfactory
		public static final String DEPLOYEMENT ="/appFactory";
		public static final String CLOUD="qa";
		
		public static final String DEBUG = "debug";
		public static final String CONTENT_STRING = "content";
		public static final String FILES_STRING = "files";
		public static final String TOKEN_STRING = "token ";
		public static final String ETAG_STRING = "ETag";
		public static final String IF_NONE_MATCH_STR = "If-None-Match";
		public static final String GIST_BASE_APIURL = "https://api.github.com/gists/";
		public static final String MODIFIED_STR= "modified";
		public static final String FALSE_STR= "false";
		//authentication
		public static final String PAAS_AUTHENTICATION ="/paasauthenticate";
		
		//get catalog
		public static final String GET_ALL_CATALOG ="/paasCatalog";
		
		//
		public static final String GET_RELATIVE_CATALOG ="/getRelatedCatalog";

		public static final String SEARCH_CATALOG ="/searchCatalog";
		
		public static final String AUTH_CATEORY="/authCategory";
		public static final String GET_LAYOUTS ="/getLayouts";
		public static final String GET_PLATFORMS ="/getPlatforms";
		public static final String GET_REGIONS ="/getRegions";
		public static final String GET_ORGANISATIONS ="/getOrganisations";
		public static final String GET_SPACE ="/getSpace";
		public static final String GET_APPNAME ="/getAppname";
		public static final String GET_SERVICEPLANGUID ="/getServicePlanGUID";
		public static final String GET_EXISTINGINSTANCES ="/getExistingInstances";

		public static final String JSON_CONTENT_TYPE = "application/json";
	    public static final String APPFACTORY_QUEUE_NAME = "app.appfactory.queue";

	    public static final String INTERNAL_SERVER_ERROR = "There is a internal server error.Please try again";
	    
	    public static final String RESPONSE_QUEUE ="/result";
	    public static final String SAVE_STATUS ="/status";
	    public static final String DOWNLOAD ="/download";
		public static final String ERROR = "error";
		public static final String YES = "yes";
		public static final String INVALID_REQUEST = "invalid request";
		public static final String CACHE_KEY = "key";
		public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
		public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
		public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint"); 
		public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
		public static final String PLATFORMID_ONEC = Messages.getString("FoundryPlatformid");
		public static final String PLATFORMID_PIVOTAL = Messages.getString("Pivotalid");
		public static final String PLATFORMID_BLUEMIX = Messages.getString("Bluemixid"); 
		public static final String PLATFORMID_AZURE = Messages.getString("Azureidid");
		public static final String SSL_INSTANCE_KEY = "SSL";
		public static final String GIST_STATUS_FILE_NAME = "microservice_status.json";
		public static final String GIST_CREATE_DESCRIPTION = "gist store";
		public static final String INFO = "info";
		public static final String DEPLOYMENT_STAGED_ID = "gist store";
		public static final String DEPLOYMENT_STAGED = "microservice_status.json";
		public static final String GIST_UPDATE_DESCRIPTION = "Status has been updated";
		

		public static final String GIST_SCOPE = "gist";
		public static final String ORG_URL = "/v2/organizations/";
		public static final String GET_REQUEST = "GET";
		public static final String POST_REQUEST = "POST";
		public static final String SPACE = "/spaces";
		public static final String AUTHORIZATION = "authorization";
		public static final String BASIC_AUTHORIZATION = "Basic Y2Y6";
		public static final String RESOURCES = "resources";

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
		public static  String layouturl = getServerURL()+"PaaSCatalog/layouts?layoutHeaderID=";
		public static  String platformurl = getServerURL()+"PaaSCatalog/platform";
		public static  String serviceplanguid = getServerURL()+"PaaSCatalog/getServicePlanGUID";
		public static  String existinginstances = getServerURL()+"PaaSCatalog/getExistingInstances";
		public static  String orgurl = getServerURL()+"PaaSCatalog/organizations";
		public static  String regionurl = getServerURL()+"PaaSCatalog/regions?platformID=";
		public static  String spaceurl = getServerURL()+"PaaSCatalog/spaces";
		public static  String checkappname = getServerURL()+"PaaSCatalog/appNameCheck";
		public static final String LOCAL_DOWNLOAD_MSG="Template app is not ready for download";
//		public static final String AUTH_CATEGORY_URL="http://localhost:3030/PaaSCatalog/getCatalog";

		
		private static String getServerURL(){
			return Messages.getString("SERVER_URL"); 
		}
		
		public enum Platform {
			//FoundryPlatform, Bluemix, Pivotal, Azure
			PLATFORMID_ONEC(Messages.getString("FoundryPlatformid")), PLATFORMID_BLUEMIX(
					Messages.getString("Bluemixid")), PLATFORMID_PIVOTAL(Messages.getString("Pivotalid")), PLATFORMID_AZURE(Messages.getString("Azureidid"));

			private String id;

			private Platform(String id) {
				this.id = id;
			}

			public String id() {
				return id;
			}
		}
}
