package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PrimaryProviders implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -4184251137106977677L;
private facebook facebook;
private google google;
private linkedin linkedin;
private twitter twitter;


public facebook getFacebook() {
	return facebook;
}
public void setFacebook(facebook facebook) {
	this.facebook = facebook;
}
public google getGoogle() {
	return google;
}
public void setGoogle(google google) {
	this.google = google;
}
public linkedin getLinkedin() {
	return linkedin;
}
public void setLinkedin(linkedin linkedin) {
	this.linkedin = linkedin;
}
public twitter getTwitter() {
	return twitter;
}
public void setTwitter(twitter twitter) {
	this.twitter = twitter;
}
public class facebook implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1066094406441354923L;
	private String clientID,clientSecret,scope;

	public String getClientID() {
		return clientID;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}	
	
}
public class google implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6332499779695217176L;
	private String clientID,clientSecret,scope;

	public String getClientID() {
		return clientID;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}	
}
public class linkedin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022369322709684232L;
	private String clientID,clientSecret,scope;

	public String getClientID() {
		return clientID;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}	
}
public class twitter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -605306165373552775L;
	private String clientID,clientSecret;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}	
	
}

}
