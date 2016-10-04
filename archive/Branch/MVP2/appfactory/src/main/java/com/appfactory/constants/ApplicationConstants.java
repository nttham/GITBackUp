package com.appfactory.constants;

import com.appfactory.resources.Messages;

/**
 * File : ApplicationConstants.java Description : This class is designed to
 * store all the constants Revision History :
 * 
 * @author 559296
 *
 */
public class ApplicationConstants {
	public static final String RABBIT_MQ_SERVICE = "rabbitmq_service_name";
	public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
	public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
	public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint");
	public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
	public static final String PASSCATALOG_URL = "https://paascatalog.";
	public static final String APPMANAGER_URL = Messages.getEnv("pcf_api.appmanager.baseurl");
	public static final String LOCAL_FOLDER_PATH = Messages.getEnv("local_base_path");
	public static final String DEV_FOLDER_PATH = Messages.getEnv("dev_base_path");
	public static final String QA_FOLDER_URL = "qa.base.path";
	public static final String PCF_FOLDER_URL = "pcf_base_path";
	public static final String STATUS_URL = Messages.getString("appmanagerserver_path");
	public static final String PUSHNOTIFICATION = "notification";
	public static final String APP = "app";
	public static final String YES = "yes";
	public static final String ENTITY = "entity";
	public static final String LAST_OPERATION = "last_operation";
	public static final String DIRECTORY = "/appManagerServer";
	public static final String APP_ID = "appid";
	public static final String MESSAGE = "message";
	public static final String DEBUG = "debug";
	public static final String BASE_URL = "/app";
	public static final String SAVE_STATUS = "/status";
	public static final String ENVIRONMENT = "cloud";
	public static final String NAME = "name";
	public static final String SPACEGUID = "space_guid";
	public static final String MEMORY = "memory";
	public static final String DISK_QUOTA = "disk_quota";
	public static final String SAVE = "save";
	public static final String DOWNLOAD = "download";
	public static final String FORWARD_SLASH = "/";
	public static final String ZIP_EXT = ".zip";
	public static final String ZIP_IT = "zipit";
	public static final String PARAMETERS = "parameters";
	public static final String APP_NAME = "appname";
	public static final String INSTANCE_TYPE = "instancetype";
	public static final String SINGLE = "single";
	public static final String ENV_JSON = "environment_json";
	public static final String PROVIDER = "provider";
	public static final String UNDERSCORE = "_";
	public static final String DEPLOY_LOCATION = "deploy_location";
	public static final String UNBOUND_NAME = "unbound";
	public static final String TEMPLATES = "templates";
	/***************************************************************************************************************/
	/**********************
	 * Constants for all the Github activity
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String GITHUB_FOLDER = "GitHubClone";
	public static final String GITHUB_CLONE_PATH = Messages.getEnv("github_clone_path");
	public static final String DEVELOP_FOLDER = Messages.getString("AppFactorySave.clonefolder");
	public static final String GITHUBFOLDER_PATH = Messages.getString("AppFactorySave.gitfolder");
	public static final String TEMPLATE = "/Templates/";
	public static final String GIT_URL = Messages.getString("private.git.url");
	public static final String GIT_PASSWORD = Messages.getString("private.git.pass");
	public static final String GIT_USERNAME = Messages.getString("private.git.user");
	public static final String GIST_USERNAME = Messages.getString("DeploymentStsus.gist.user");
	public static final String GIST_PASSWORD = Messages.getString("DeploymentStsus.gist.pwd");
	public static final String GIST_SCOPE = "gist";
	public static final String GIT_MESSAGE = "We have uploaded your required codebase";
	/***************************************************************************************************************/
	/**********************
	 * Constants for all the Logging activity
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String LOGGER = "Logger";
	public static final String SMALL_LOGGER = "logger";
	public static final String SSL_INSTANCE_KEY = "SSL";
	public static final String EXTERNAL = "external";
	public static final String EXISTING = "existing";
	public static final String DEFAULT = "default";
	public static final String NEWLOGGER = "newlogger";
	public static final String LOG_LEVEL_INFO = "INFO";
	public static final String LOG_LEVEL_ERROR = "ERROR";
	public static final String ACTUAL_RESPONSE_QUEUE = "app.managerserver.response.queue";
	public static final String GIST_STAUTUS_FILE_NAME = "microservice_status.json";
	public static final String CONFIG_LOG_LEVEL = "config.logLevel";
	public static final String GRAY_LOG_URL = "grayLog.url";
	public static final String LOGGER_SELECT = "logger.select";
	public static final String FILE = "file";
	public static final String INFO = "info";
	public static final String GRAYLOG = "GrayLog";
	public static final String LEVEL = "level";
	/***************************************************************************************************************/
	/**********************
	 * Constants for all the api calls
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "bearer ";
	public static final String SERVICE_BINDING_URL = "/v2/service_bindings";
	public static final String SERVICE_INSTANCE_URL = "/v2/service_instances?accepts_incomplete=false";
	public static final String GET_DOMAINSERVICEPLANGUID = "/getdomainserviceplanguid";
	public static final String GET_SERVICEINSTANCES = "/v2/service_instances";
	public static final String GET_SERVICEPLAN_API = "/v2/services/";
	public static final String CREATE_APP_API = "/v2/apps";
	public static final String GET_DOMAINS_API = "/v2/domains";
	public static final String GET_ROUTES_API = "/v2/routes";
	public static final String ERROR_DESCRIPTION = "description";
	public static final String TOKEN_STRING = "token ";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String PLATFORM_URL_PATH = "/v2/apps";
	public static final String CONTENT_TYPE_APPLICATION_X = "application/x-www-form-urlencoded";
	public static final int APP_MEMORY = 256;
	public static final int DISK_MEMORY = 512;
	public static final String GET_REQUEST = "GET";
	public static final String POST_REQUEST = "POST";
	public static final String PUT_METHOD = "PUT";
	public static final String DELETE_METHOD = "DELETE";
	public static final String ACCEPT = "Accept";
	public static final String ERROR = "error";
	public static final String SERVICE_INSTANCE_GUID = "service_instance_guid";
	public static final String SERVICE_PLAN_GUID = "service_plan_guid";
	public static final String METADATA = "metadata";
	public static final String GUID = "guid";
	public static final String RESOURCES = "resources";
	public static final String SERICE_GUID = "service_guid";
	public static final String APP_GUID = "app_guid";
	public static final String V2_APPS = "/v2/apps/";
	public static final String V2_SPACES = "/v2/spaces/";
	public static final String ONLY_APPS = "/apps/";
	public static final String STREAM = "/stream";
	public static final String APP_STARTING = "STARTING";
	public static final String APP_STARTED = "STARTED";
	public static final String APP_STOPPED = "STOPPED";
	public static final String APP_STATE = "state";
	public static final String PACKAGE_STATE = "package_state";
	public static final String PACKAGE_STATE_STAT = "STAGED";
	public static final String BASIC_AUTHORIZATION = "Basic Y2Y6";
	public static final String GET_REFRESH_TOKEN = "grant_type=refresh_token&refresh_token=";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String GET_REFRESH_TOKEN_SCOPE = "&scope=";
	public static final String API = "api";
	public static final String LOGIN = "login";
	public static final String OAUTH_TOKEN_URL = "/oauth/token";
	/***************************************************************************************************************/
	/**********************
	 * Constants for all status messages
	 *********************************************************/
	/***************************************************************************************************************/
	public static final String ENTER_METHOD = "Entering method";
	public static final String EXIT_METHOD = "Exiting method";
	public static final String OTHER_SERVICE_ERROR = "Application failed while configuring otherservices";
	public static final String LOCAL_DOWNLOAD_MSG = "Template microservice is not ready for download";
	public static final String PLATFORM_CREATEAPP = "Factory configured your app for the platform";
	public static final String PLATFORM_CREATEAPP_ERROR = "Failed to create the microservice";
	public static final String PLATFORM_GETDOMAINS = "Factory retrived your domain information";
	public static final String PLATFORM_CREATEROUTE = "Factory processing an address for you microservice";
	public static final String PLATFORM_CREATEROUTE_ERROR = "Error While creating a route";
	public static final String INTERNAL_SERVER_ERROR = "It seems factory is not able to process more request.Try later.";
	public static final String API_SERVER_ERROR = "API is having some problem please try later";
	public static final String PLATFORM_ASSOCIATEROUTE = "Factory is associating the route with the microservice";
	public static final String PLATFORM_UPLOADPP_INPROGRESS = "Factory deployed a Template App";
	public static final String PLATFORM_UPLOADPP = "Application is pushed to the cloud foundry";
	public static final String PLATFORM_UPLOADPP_FAILED = "Application failed while pusing the app";
	public static final String CREATED_MICRO_SERVICE_APP = "Factory created a microservice instance";
	public static final String BINDING_MICRO_SERVICE_APP = "Factory creating a bridge between microservice and template";
	public static final String EXISTING_MICRO_SERVICE_APP = "Factory configuring other existing microservice";
	public static final String NEW_MICRO_SERVICE_APP = "Factory created a new microservice";
	public static final String PUSHAPP = "Failed  to push App";
	public static final String APP_ENV = "App Environement is ready";
	public static final String APP_HEALTH = "Factory started 0 instance in state...";
	public static final String APP_HEALTH_GREEN = "Factory started 1 instance in state...";
	public static final String APP_HEALTH_RED = "Factory is taking time to start your app";
	public static final String STAGING_ERROR = "There is no space available";
	public static final String STARTING_ERROR = "Application is not started";
	public static final String APP_RESTAGE = "Factory is restaging the microservice";
	public static final String PLATFORM_STARTAPP = "Factory processing the start request";
	public static final String AUTH_VALIDATED = "Factory revalidated your authentication";
	public static final String BIND_LOGGER = "Instance created and binded for logger";
	public static final String BIND_APPSERVIVCE = "Instance created and binded for microservice";
	public static final String MICRO_APP = "Microservice is up and running";
	public static final String OTHERSERVICE = "Service instance has been created for otherservice";
	public static final String LOGGERSERVICE = "Logger Service instance has been created";
	public static final String OTHERSERVICE_BIND = "Service instance has been binded for otherservice";
	public static final String OTHERSERVICE_BIND_ERROR = "Service instance failed while binding for otherservice";
	public static final String MICRO_GIT = "Microservice is pushed to your github account";
	public static final String BASE_DIRECTORY = "Factory processing place is ready";
	public static final String GET_SERVICEINSTANCE = "Getting all the service instances";
	public static final String GET_ALL_SERVICEINSTANCE = "Getting all the services";
	public static final String GET_ALL_SERVICEPLANS = "Error in getting the service plans";
	public static final String BINDING_SERVICE_INSTANCE = "Error in binding the service instance";
	public static final String CREATING_SERVICE_INSTANCE = "Error in creating the service instance";
	public static final String BASE_DIRECTORY_FAILED = "Not able to create a place holder";
	public static final String GITHUB_FAILED = "Github operation failed";
	public static final String CFURL = "CF url creation is in process";
	public static final String CFURL_FAILED = "CF url creation failed";
	public static final String CLONE_TEMPLATE = "Factory cloning your request";
	public static final String CLONE_TEMPLATE_FAILED = "Unable to clone the template app";
	public static final String URL_GENERATED = "Generated a url for the app";
	public static final String URL_GENERATED_FAILED = "There is some problem in generating url";
	public static final String ZIP_READY = "Factory created the requested app";
	public static final String UNBOUND = "Unbound service is ready to push";
	public static final String ZIP_FAILED = "App failed before pushing starts";
	public static final String WRONG_JSON = "There is a miscommunication between your input and factory's expectation";
	public static final String CREATE_SERVICE_INSTANCE_STATUS = "Factory successfully created one service instance";
	public static final String BIND_SERVICE_INSTANCE_STATUS = "Factory binded the service instance";
	public static final String UNABLE_GREY = "Unable to save to grey log";
	public static final String UNABLE_CREATE_DIRECTORY = "Failed to create directory= ";
	public static final String ABLE_CREATE_DIRECTORY = "Directory is created= ";
	public static final String GET_STATS_APP = "Factory will show you all stats of the app";
	public static final String CREATED_MICRO_SERVICE_APP_PUSH = "Factory have created a notification instance";
	
}
