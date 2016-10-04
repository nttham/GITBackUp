package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3462580908230861794L;
	
	private facebook facebook;
	private google google;
	private linkedin linkedin;
	private twitter twitter;
	private SendGrid sendgrid;
	private Ldap ldap;
	private Custom custom;
	private Httpss httprequest;
	


	public Custom getCustom() {
		return custom;
	}

	public void setCustom(Custom custom) {
		this.custom = custom;
	}

	public Ldap getLdap() {
		return ldap;
	}

	public void setLdap(Ldap ldap) {
		this.ldap = ldap;
	}

	public SendGrid getSendgrid() {
		return sendgrid;
	}

	public void setSendgrid(SendGrid sendgrid) {
		this.sendgrid = sendgrid;
	}

	public Twilio getTwilio() {
		return twilio;
	}

	public void setTwilio(Twilio twilio) {
		this.twilio = twilio;
	}

	private Twilio twilio;

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

	




	public Httpss getHttprequest() {
		return httprequest;
	}

	public void setHttprequest(Httpss httprequest) {
		this.httprequest = httprequest;
	}






	public class facebook implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1066094406441354923L;
		private String clientID, clientSecret, protocol;
		private ArrayList<String> scope;

		public ArrayList<String> getScope() {
			return scope;
		}

		public void setScope(ArrayList<String> scope) {
			this.scope = scope;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

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

	public class google implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6332499779695217176L;
		private String clientID, clientSecret, protocol;

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		private ArrayList<String> scope;

		public ArrayList<String> getScope() {
			return scope;
		}

		public void setScope(ArrayList<String> scope) {
			this.scope = scope;
		}

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

	public class linkedin implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6022369322709684232L;
		private String clientID, clientSecret;
		private String protocol;

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		private ArrayList<String> scope;

		public ArrayList<String> getScope() {
			return scope;
		}

		public void setScope(ArrayList<String> scope) {
			this.scope = scope;
		}

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

	public class twitter implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -605306165373552775L;
		private String clientID, clientSecret, protocol;

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

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

	public class SendGrid implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2664876397229824369L;
		private String accountid, accounttoken;

		public String getAccountid() {
			return accountid;
		}

		public void setAccountid(String accountid) {
			this.accountid = accountid;
		}

		public String getAccounttoken() {
			return accounttoken;
		}

		public void setAccounttoken(String accounttoken) {
			this.accounttoken = accounttoken;
		}

	}

	public class Twilio implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2081229797890747426L;
		private String accountid, accounttoken;

		public String getAccountid() {
			return accountid;
		}

		public void setAccountid(String accountid) {
			this.accountid = accountid;
		}

		public String getAccounttoken() {
			return accounttoken;
		}

		public void setAccounttoken(String accounttoken) {
			this.accounttoken = accounttoken;
		}

	}

	public class Ldap implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6310549515150699993L;
		private String ldapURL, protocol;

		public String getLdapURL() {
			return ldapURL;
		}

		public void setLdapURL(String ldapURL) {
			this.ldapURL = ldapURL;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

	}

	public class Custom implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7112760617398113984L;
		private String protocol, clientID, clientSecret, authorizationURL, tokenURL, resource;

		public String getResource() {
			return resource;
		}

		public void setResource(String resource) {
			this.resource = resource;
		}

		public String getAuthorizationURL() {
			return authorizationURL;
		}

		public void setAuthorizationURL(String authorizationURL) {
			this.authorizationURL = authorizationURL;
		}

		private ArrayList<String> scope;

		public String getTokenURL() {
			return tokenURL;
		}

		public void setTokenURL(String tokenURL) {
			this.tokenURL = tokenURL;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

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

		public ArrayList<String> getScope() {
			return scope;
		}

		public void setScope(ArrayList<String> scope) {
			this.scope = scope;
		}

	}
	
	
	public class Httpss implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7112760617398113984L;
		private Object validationEndpoint, contentGenerationEndPoint;
		private String clientID, clientSecret;
		
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
		public Object getValidationEndpoint() {
			return validationEndpoint;
		}
		public void setValidationEndpoint(Object validationEndpoint) {
			this.validationEndpoint = validationEndpoint;
		}
		public Object getContentGenerationEndPoint() {
			return contentGenerationEndPoint;
		}
		public void setContentGenerationEndPoint(Object contentGenerationEndPoint) {
			this.contentGenerationEndPoint = contentGenerationEndPoint;
		}

		

	}
}
