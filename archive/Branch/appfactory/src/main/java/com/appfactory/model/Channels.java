package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class Channels implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9005181691987941138L;
	private String expiryTime, length, type;
	private Otp otp;
	private Captcha captcha;

	public Otp getOtp() {
		return otp;
	}

	public void setOtp(Otp otp) {
		this.otp = otp;
	}

	public Captcha getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

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

}
