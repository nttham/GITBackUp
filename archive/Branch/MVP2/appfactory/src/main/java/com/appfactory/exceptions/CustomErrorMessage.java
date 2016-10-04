package com.appfactory.exceptions;

import org.springframework.http.HttpStatus;

/**
 * File : CustomErrorMessage.java Description : This class is designed to
 * store all custom error messages and error code :

 *
 */
public enum CustomErrorMessage {
	INVALID_GITHUB_URL(1001, "invalid github url", HttpStatus.BAD_REQUEST.value()),
	FILE_NOTFOUND(1006, "file.not.found", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	INVALID_USER_NAME(1002,"invalid username",HttpStatus.BAD_REQUEST.value()),
	ERROR_PARSING_JSON(1004,"invalid username",HttpStatus.BAD_REQUEST.value()),
	DELETE_API_FAILED(1004,"delete api call failed",HttpStatus.BAD_REQUEST.value()),
	POST_API_FAILED(1004,"post api call failed",HttpStatus.BAD_REQUEST.value()),
	PUT_API_FAILED(1004,"put api call failed",HttpStatus.BAD_REQUEST.value()),
	GET_API_FAILED(1004,"get api call failed",HttpStatus.BAD_REQUEST.value()),
	QUEUE_CONNECTION_FAILED(1003, "invalid message connection", HttpStatus.BAD_REQUEST.value()),
	STATUS_UPDATION_FAILED(1005, "status updation failed", HttpStatus.BAD_REQUEST.value()),
	 INTERNAL_SERVER_ERROR(1000,"The server encountered an internal error. Please retry the request.",HttpStatus.INTERNAL_SERVER_ERROR.value()),
	BASE_DIRECTORY_ERROR(1007,"Invalid path or Username",HttpStatus.BAD_REQUEST.value()),
	CLONE_GIT_ERROR(1008,"Check Service Folder Path",HttpStatus.BAD_REQUEST.value()),
	PUSH_APP_FAILED(1010,"APP PUSHING FAILED",HttpStatus.CONFLICT.value()),
	CREATE_MICRO_FAILED(1011,"Service Instance Not created",HttpStatus.BAD_REQUEST.value()),
	GIT_PUSH_FAILED(1012,"Git push failed check appname and path",HttpStatus.NOT_ACCEPTABLE.value()),
	TRANSPORT_EXCEPTION_CLONE(1013,"send or read from transport failed",HttpStatus.BAD_REQUEST.value()),
	TRANSPORT_EXCEPTION_PUSH(1021,"send or read from transport failed",HttpStatus.BAD_REQUEST.value()),
	INVALID_REMOTE_EXCEPTION_CLONE(1014,"Remote Method Call Failed",HttpStatus.BAD_REQUEST.value()),
	GIT_API_EXCEPTION_CLONE(1015,"GIT API ERROR",HttpStatus.BAD_REQUEST.value()),
	INVALID_REMOTE_EXCEPTION_PUSH(1016,"Remote Method Call Failed",HttpStatus.BAD_REQUEST.value()),
	GIT_API_EXCEPTION_CLONE_PUSH(1017,"GIT API ERROR",HttpStatus.BAD_REQUEST.value()),
	NO_FILE_PATTERN_EXCEPTION(1018,"FILE PATTERN ERROR",HttpStatus.BAD_REQUEST.value()),
	GIT_API_EXCEPTION_CLONE_N(1019,"GIT API ERROR",HttpStatus.BAD_REQUEST.value()),
	GIT_API_EXCEPTION_CLONE_P(1024,"GIT API ERROR",HttpStatus.BAD_REQUEST.value()),
	NO_HEAD_EXCEPTION(1020,"NO HEAD FOUND",HttpStatus.NOT_FOUND.value()),
	WRONG_REPO_STATE(1022,"COMMIT AMMENDON INITIAL NOT POSSIBLE",HttpStatus.NOT_FOUND.value()),
	CONCURRENT_REF_EXCEPTION(1023,"REF UPDATE FAILED,ANOTHER PROCESS IN RUNNING",HttpStatus.TOO_MANY_REQUESTS.value()),
	HOOK_ABORT_EXCEPTION(1025," hook returns a process result with a value different from 0",HttpStatus.CONFLICT.value()),
	UNMERGED_PATH_EXCEPTION(1026,"Branch deletion failed due to unmerged data ",HttpStatus.BAD_REQUEST.value()),
	GIT_API_EXCEPTION_CLONE_1(1027,"GIT API ERROR",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION(1028,"INVALID DIRECTORIES OR APPNAME",HttpStatus.BAD_REQUEST.value()),
	JSONException(1029,"WRONG INPUT FROM UI",HttpStatus.BAD_REQUEST.value()),
	ENVSETUP(1029,"WRONG INPUT FROM UI",HttpStatus.BAD_REQUEST.value()),
	FORCE_DELETE(1064,"UNABLE TO DELETE",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_COPY(1030,"INVALID DIRECTORIES OR APPNAME",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_1(1031,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_2(1032,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_3(1033,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_4(1034,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_5(1035,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_6(1036,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_M(1037,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_Z(1038,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_u(1039,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_B(1040,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_SI(1041,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	MALFORMED_URL_EXCEPTION(1042,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_SE(1043,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	MALFORMED_URL_EXCEPTION_SE(1044,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_SP(1045,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	MALFORMED_URL_EXCEPTION_SP(1046,"url error",HttpStatus.BAD_REQUEST.value()),
	MALFORMED_URL_EXCEPTION_SA(1047,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_SA(1048,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION_HE(1049,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION(1050,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTIONb(1051,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION1(1052,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION2(1053,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION3(1054,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION4(1055,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION5(1056,"PARSE ERROR",HttpStatus.BAD_REQUEST.value()),
	GENERATE_URL_ERROR(1057,"Invalid Url Path",HttpStatus.NOT_ACCEPTABLE.value()),
	STAGING_ERROR(1048,"We have staging issue.Kindly try again later",HttpStatus.BAD_REQUEST.value()),
	OTHER_SERVICE_ERROR(1058,"There is a small problem in configuring other services.",HttpStatus.BAD_REQUEST.value()),
	STATUS_UPDATE_ERROR(1007,"There is a small problem in giving you the status.",HttpStatus.BAD_REQUEST.value()),
	CREATE_APP_ERROR(1059,"Issue is in creating app",HttpStatus.BAD_REQUEST.value()),
	CREATE_SERVICE_ERROR(1090,"Issue is in creating service",HttpStatus.BAD_REQUEST.value()),
	CREATE_DOMAIN_ERROR(1060,"Issue is in creating app",HttpStatus.BAD_REQUEST.value()),
	CREATE_ROUTE_ERROR(1061,"Issue is in creating app",HttpStatus.BAD_REQUEST.value()),
	UPLOAD_APP_ERROR(1062,"Issue is in creating app",HttpStatus.BAD_REQUEST.value()),
	START_APP_ERROR(1063,"Issue is in creating app",HttpStatus.BAD_REQUEST.value()),
	API_ERROR(1089,"Connection or e is null",HttpStatus.BAD_REQUEST.value()),
	CONFIGURE_ERROR(1091,"Error in configure service",HttpStatus.BAD_REQUEST.value()),
	UNBOUND_CONFIGURE_ERROR(1092,"Error in unbound configure service",HttpStatus.BAD_REQUEST.value()),
	NULL_POINTER_ERROR(1092,"Null pointer issue",HttpStatus.BAD_REQUEST.value()),
	GIT_PUSH_ERROR(1092,"Error in gitpush",HttpStatus.BAD_REQUEST.value());
	
	private int errorCode;

	private final String errorMessage;

	private int statusCode;

	private CustomErrorMessage(int errorCode, String errorMessage, int statusCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
