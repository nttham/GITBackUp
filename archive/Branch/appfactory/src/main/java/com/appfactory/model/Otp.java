package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;
@Component
public class Otp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 702709345440754895L;
	private String length, type, expiryTime;
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

}
