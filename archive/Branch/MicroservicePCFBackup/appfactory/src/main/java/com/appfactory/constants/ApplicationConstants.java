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
	public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
	public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
	public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint");
	public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
	public static final String GET_REQUEST = "GET";
	public static final String POST_REQUEST = "POST";
	public static final String PLATFORM_CREATEAPP = "App is created and ready to push";
	public static final String PLATFORM_CREATEAPP_ERROR = "Failed to create the app";
	public static final String PLATFORM_GETDOMAINS = "To push the app we have got the domain details";
	public static final String PLATFORM_CREATEROUTE = "We have created a route from where application can be accessed";
	public static final String INTERNAL_SERVER_ERROR = "The server encountered an internal error. Please retry the request.";
	public static final String PLATFORM_ASSOCIATEROUTE = "Route is associated with the app";
	public static final String PLATFORM_UPLOADPP_INPROGRESS="Application push to cloud foundry is in progress.";
	public static final String PLATFORM_UPLOADPP = "Application is pushed to the cloud foundry";
	public static final String PLATFORM_UPLOADPP_FAILED = "Application failed while pusing the app";
	public static final String PUSHAPP = "Failed  to push App";
	public static final String STAGING_ERROR = "There is no space available";
	public static final String PLATFORM_STARTAPP = "Your application has been started";
	public static final String BIND_LOGGER = "Instance created and binded for logger";
	public static final String BIND_APPSERVIVCE = "Instance created and binded for microservice";
	public static final String MICRO_APP = "Microservice is up and running";
	public static final String OTHERSERVICE = "Service instance has been created for otherservice";
	public static final String LOGGERSERVICE = "Logger Service instance has been created";
	public static final String OTHERSERVICE_BIND = "Service instance has been binded for otherservice";
	public static final String OTHERSERVICE_BIND_ERROR = "Service instance failed while binding for otherservice";
	public static final String MICRO_GIT = "Microservice is pushed to your github account";
	public static final String BASE_DIRECTORY = "Base directory is created";
	public static final String BASE_DIRECTORY_FAILED = "Base directory is not created";
	public static final String CFURL = "CF url creation is in process";
	public static final String CFURL_FAILED = "CF url creation failed";
	public static final String CLONE_TEMPLATE = "Created a copy of template";
	public static final String CLONE_TEMPLATE_FAILED = "Unable to clone the template app";
	public static final String URL_GENERATED = "Generated a url for the app";
	public static final String URL_GENERATED_FAILED = "There is some problem in generating url";
	public static final String ZIP_READY = "App is ready to push";
	public static final String ZIP_FAILED = "App failed before pushing starts";
	public static final String PUT_METHOD = "PUT";
	public static final String ACCEPT = "Accept";
	public static final String ERROR = "error";
	public static final String YES = "yes";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	public static final String API_SERVICE_URL = "api.appmanager.baseurl";
	public static final String DIRECTORY = "/appManagerServer";
	public static final String CONTENT_TYPE_APPLICATION_X = "application/x-www-form-urlencoded";
	public static final String BASE_URL = "/app";
	public static final String SAVE_STATUS = "/status";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "bearer ";
	public static final String SERVICE_BINDING_URL = "/v2/service_bindings";
	public static final String SERVICE_INSTANCE_URL = "/v2/service_instances?accepts_incomplete=false";
	public static final String LOGGER = "Logger";
	public static final String SSL_INSTANCE_KEY = "SSL";
	public static final String EXTERNAL = "external";
	public static final String EXISTING = "existing";
	public static final String NEWLOGGER = "newlogger";
	public static final String OTHER_SERVICE_ERROR = "Application failed while configuring otherservices";
	public static final String PLATFORM_URL_PATH = "/v2/apps";
	public static final String LOCAL_DOWNLOAD_MSG="Template app is not ready for download";
	public static final String LOG_LEVEL_INFO="INFO";
	public static final String LOG_LEVEL_ERROR="ERROR";
	public static final String CLOUD="PCF";
	/**
	 * Creating constant variable for Get
	 */
	public static final String GET_METHOD = "GET";
	/**
	 * Creating constant variable for Post
	 */
	public static final String POST_METHOD = "POST";
	public static final String CONTENT_TYPE = "Content-Type";
	/**
	 * Creating constant variable for application/json
	 */
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String ACTUAL_RESPONSE_QUEUE = "app.managerserver.response.queue";
	public static final String GIST_STAUTUS_FILE_NAME = "microservice_status.json";

}
