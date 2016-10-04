package com.appmanagerserver.blueprints;
import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PaasAuthentication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username, password, platform;
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	

}
