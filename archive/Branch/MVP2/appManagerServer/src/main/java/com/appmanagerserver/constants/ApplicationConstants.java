
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
	public static final String API = "api";
	public static final String LOGIN = "login";
	public static final String CATALOG = "Catalog";
	public static final String SERVICEGUID = "serviceguid";
	public static final String PLATFORMID = "platformid";
	public static final String LOGIN_URL ="login_url";
	public static final String SPACE_NAME="spacename";
	public static final String REGIONID = "regionid";
	public static final String CATEGORY_NAME = "categoryname";
	public static final String SPACEGUID = "spaceguid";
	public static final String API_URL = "api_url";
	public static final String BASE_URL = "/app";
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final String FORWARD_SLASH = "/";
	public static final String GIST_CLONE_PATH = "";
	public static final String EMPTY_STRING = "";
	public static final String DEPLOYEMENT = "/appFactory";
	public static final String CLOUD = "qa";
	public static final String DEBUG = "debug";
	public static final String CONTENT_STRING = "content";
	public static final String FILES_STRING = "files";
	public static final String TOKEN_STRING = "token";
	public static final String ETAG_STRING = "ETag";
	public static final String IF_NONE_MATCH_STR = "If-None-Match";
	public static final String GIST_BASE_APIURL = "https://api.github.com/gists/";
	public static final String MODIFIED_STR = "modified";
	public static final String FALSE_STR = "false";
	public static final String PAAS_AUTHENTICATION = "/paasauthenticate";
	public static final String GET_ALL_CATALOG = "/paasCatalog";
	public static final String GET_RELATIVE_CATALOG = "/getRelatedCatalog";
	public static final String GET_DOMAINSERVICEPLANGUID = "/getdomainserviceplanguid";
	public static final String SEARCH_CATALOG = "/searchCatalog";
	public static final String AUTH_CATEORY = "/authCategory";
	public static final String GET_LAYOUTS = "/getLayouts";
	public static final String GET_PLATFORMS = "/getPlatforms";
	public static final String GET_REGIONS = "/getRegions";
	public static final String GET_ORGANISATIONS = "/getOrganisations";
	public static final String GET_SPACE = "/getSpace";
	public static final String GET_APPNAME = "/getAppname";
	public static final String GET_SERVICEPLANGUID = "/getServicePlanGUID";
	public static final String GET_EXISTINGINSTANCES = "/getExistingInstances";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String RESPONSE_QUEUE = "/result";
	public static final String SAVE_STATUS = "/status";
	public static final String DOWNLOAD = "/download";
	public static final String ERROR = "error";
	public static final String YES = "yes";
	public static final String INVALID_REQUEST = "invalid request";
	public static final String CACHE_KEY = "key";
	public static final String SSL_INSTANCE_KEY = "SSL";
	public static final String GIST_STATUS_FILE_NAME = "microservice_status.json";
	public static final String GIST_CREATE_DESCRIPTION = "gist store";
	public static final String INFO = "INFO";
	public static final String DEPLOYMENT_STAGED_ID = "gist store";
	public static final String DEPLOYMENT_STAGED = "microservice_status.json";
	public static final String GIST_SCOPE = "gist";
	public static final String GIST_ID = "gistid";
	public static final String ORG_URL = "/v2/organizations/";
	public static final String GET_REQUEST = "GET";
	public static final String POST_REQUEST = "POST";
	public static final String SPACE = "/spaces";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BASIC_AUTHORIZATION = "Basic Y2Y6";
	public static final String GET_REFRESH_TOKEN = "grant_type=refresh_token&refresh_token=";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String GET_REFRESH_TOKEN_SCOPE = "&scope=";
	public static final String RESOURCES = "resources";
	public static final String METADATA = "metadata";
	public static final String GUID = "guid";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "";
	public static final String ENVIRONMENT = "cloud";
	public static final String STATUS = "status";
	public static final String FAILED = "failed";
	public static final String MESSAGE = "message";
	public static final String RESPONSE = "response";
	public static final String REASON = "Reason";
	public static final String CONTENTTYPE = "Content-Type";
	public static final String CONTENT = "application/x-www-form-urlencoded;charset=utf-8";
	public static final String ERROR_DESCRIPTION = "error_description";
	public static final String FILE = "file";
	public static final String GRAYLOG = "GrayLog";
	public static final String LEVEL = "level";
	public static final String APP_ID = "appid";
	public static final String REQUEST_HEADERS = "Accept=application/json";
	public static final String RESPONSE_HEADERS = "application/json";
	public static final String BEARER = "bearer ";
	public static final String DEPLOY_LOCATION = "deploy_location";
	public static final String OAUTH_TOKEN_URL = "/oauth/token";
	public static final String PUSH_TO_QUEUE = "/pushMsg";
	public static final String RETRIVE_FROM_QUEUE = "/getMsg";
	public static final String DELETE_QUEUE = "/delQueue";
	public static final String RABBIT_MQ_SERVICE = "rabbitmq_service_name";
	/***************************************************************************************************************/
	/**********************
	 * Status messages for UI
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String UNABLE_GREY = "Unable to update greylog";
	public static final String LOCAL_DOWNLOAD_MSG = "Template app is not ready for download";
	public static final String GIST_UPDATE_DESCRIPTION = "Status has been updated";
	public static final String INTERNAL_SERVER_ERROR = "It seems we had a problem.Please try again";
	public static final String GIST_FILE_UPLOAD_COMMENTS = "Template app has been uploaded";
	public static final String STAGED_FOR_DEPLOYMENT = "Ready to start the factory processing";
	public static final String FAILED_PUSHING_MESSAGE = "Unable to push your message";
	public static final String SUCCESSFULLY_PUSHING_MESSAGE = "Your message is in queue now";
	public static final String SUCCESSFULLY_DELETED_QUEUE = "Your queue is deleted";
	public static final String RETRIVING_MESSAGE = "There are no messages in queue";
	/***************************************************************************************************************/
	/**********************
	 * Take inputs from property file as a constant value
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String STARS = "***********************************************************";
	public static final String EXITING_METHOD = "Exiting Method";
	public static final String ENTERING_METHOD = "Entering Method";
	public static final String METHOD_ERROR = "With error";
	public static final String METHOD_RESULT = "With result";
	public static final String CONFIG_LOG_LEVEL = "config.logLevel";
	public static final String GRAY_LOG_URL = "grayLog.url";
	public static final String LOGGER_SELECT = "logger.select";
	public static final String GIST_USERNAME = "DeploymentStsus.gist.user";
	public static final String GIST_PASSWORD = "DeploymentStsus.gist.pwd";
	public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
	public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
	public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint");
	public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
	public static final String PLATFORMID_ONEC = Messages.getString("FoundryPlatformid");
	public static final String PLATFORMID_PIVOTAL = Messages.getString("Pivotalid");
	public static final String PLATFORMID_BLUEMIX = Messages.getString("Bluemixid");
	public static final String PLATFORMID_AZURE = Messages.getString("Azureidid");
	public static final String APPFACTORY_QUEUE_NAME = "app.appfactory.queue";
	/***************************************************************************************************************/
	/************************
	 * Take inputs from the below method as a field value
	 ********************************************************/
	/***************************************************************************************************************/
	public static String catalogurl = getServerURL() + "PaaSCatalog/catalog";
	public static String speccaturl = getServerURL() + "PaaSCatalog/platformservices";
	public static String searchcatalogurl = getServerURL() + "PaaSCatalog/searchCatalog";
	public static String layouturl = getServerURL() + "PaaSCatalog/layouts?layoutHeaderID=";
	public static String platformurl = getServerURL() + "PaaSCatalog/platform";
	public static String serviceplanguid = getServerURL() + "PaaSCatalog/getServicePlanGUID";
	public static String existinginstances = getServerURL() + "PaaSCatalog/getExistingInstances";
	public static String orgurl = getServerURL() + "PaaSCatalog/organizations";
	public static String regionurl = getServerURL() + "PaaSCatalog/regions?platformID=";
	public static String spaceurl = getServerURL() + "PaaSCatalog/spaces";
	public static String checkappname = getServerURL() + "PaaSCatalog/appNameCheck";
	public static String domainserviceplanguid = getServerURL() + "PaaSCatalog/getdomainserviceplanguid";
	/***************************************************************************************************************/
	/************************
	 * This is used for the api documentation
	 ********************************************************/
	/***************************************************************************************************************/
	public static final String MQ_GETMSG_VALUE = Messages.getDocs("getmessages-operation-value");
	
	
	
	
	
	
	
	public enum BASE_URL {

		BASE_URL_ONEC(Messages.getString("OneC_API_EndPoint")), BASE_URL_BLUEMIX(
				Messages.getString("Bluemix_API_EndPoint")), BASE_URL_PIVOTAL(
						Messages.getString("Pivotal_API_EndPoint")), BASE_URL_AZURE(
								Messages.getString("Azure_API_EndPoint"));

		private String url;

		private BASE_URL(String url) {
			this.url = url;
		}

		public String url() {
			return url;
		}
	}

	public enum AUTH_URL {

		AUTH_URL_ONEC(Messages.getString("OneC_login_url")), AUTH_URL_BLUEMIX(
				Messages.getString("Bluemix_login_url")), AUTH_URL_PIVOTAL(
						Messages.getString("Pivotal_login_url")), AUTH_URL_AZURE(Messages.getString("Azure_login_url"));

		private String loginUrl;

		private AUTH_URL(String loginURL) {
			this.loginUrl = loginURL;

		}

		public String loginURL() {
			return loginUrl;
		}
	}

	private static String getServerURL() {
		if(System.getenv(ApplicationConstants.ENVIRONMENT).equalsIgnoreCase(ApplicationConstants.DEPLOY_LOCATION)){
			return System.getenv("passcatalog_server_url");
		}
		return Messages.getString("passcatalog_server_url");
	}

	public enum Platform {
		PLATFORMID_ONEC(Messages.getString("FoundryPlatformid")), PLATFORMID_BLUEMIX(
				Messages.getString("Bluemixid")), PLATFORMID_PIVOTAL(
						Messages.getString("Pivotalid")), PLATFORMID_AZURE(Messages.getString("Azureidid"));

		private String id;

		private Platform(String id) {
			this.id = id;
		}

		public String id() {
			return id;
		}
	}
}
