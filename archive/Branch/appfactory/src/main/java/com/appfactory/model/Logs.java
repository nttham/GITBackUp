/**
 * 
 */
package com.appfactory.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

/**
 * @author 559296
 *
 */
@Component
public class Logs implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2709274911959376342L;
	private int StatusCode;
	private Timestamp timeStamp;
	private String loglevel;
	private String message;
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLoglevel() {
		return loglevel;
	}
	public void setLoglevel(String loglevel) {
		this.loglevel = loglevel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
