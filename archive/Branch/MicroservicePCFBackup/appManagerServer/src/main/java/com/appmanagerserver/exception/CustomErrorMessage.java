package com.appmanagerserver.exception;

import org.springframework.http.HttpStatus;

public enum CustomErrorMessage {
	QUEUE_CONNECTION_FAILED(1001, "invalid queue connection", HttpStatus.BAD_REQUEST.value()),
	FILE_NOTFOUND(1015, "file.not.found", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	INVALID_USER_NAME(1002,"invalid username",HttpStatus.BAD_REQUEST.value()),
	ERROR_PARSING_JSON(1003,"invalid parsing json",HttpStatus.BAD_REQUEST.value()),
	INTERNAL_SERVER_ERROR(1006, "internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	PARSEEXCEPTION1(1005, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	PARSEEXCEPTION2(1007, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	PARSEEXCEPTION3(1008, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	MALFORMED_URL_EXCEPTION(1009,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION(1010,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION4(1011, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	PARSEEXCEPTION5(1012, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	MALFORMED_URL_EXCEPTION1(1013,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION1(1014,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION6(1012, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	MALFORMED_URL_EXCEPTION2(1013,"url error",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION2(1014,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	IOEXCEPTION3(1016,"INVALID INPUTS",HttpStatus.BAD_REQUEST.value()),
	PARSEEXCEPTION7(1017, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value()),
	PARSEEXCEPTION(1004, "PARSE ERROR", HttpStatus.NOT_ACCEPTABLE.value());

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
