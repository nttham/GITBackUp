package com.appmanagerserver.exception;
/**
 * 
 * @author 559296
 *
 */
public class MyException extends Exception{
	
	private static final long serialVersionUID = 1908149828656940420L;
	private int errorCode;
	private String errorMessage;
	private int statusCode;
	
	
    public MyException(int errorCode, String errorMessage,int statusCode){
    	super(errorMessage);
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
