 package com.datamanagement.exception;



public class DataManagementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6979862300369344257L;	

	private String errorMessage;
	
	private int statusCode;

	public DataManagementException( String errorMessage,int statusCode) {
		super(errorMessage);		
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	
}
