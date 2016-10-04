package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.tools.ant.taskdefs.Javac.ImplementationSpecificArgument;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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


	public class facebook implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1066094406441354923L;
		private String clientID, clientSecret,protocol;
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
		
		private class Scope implements Serializable{

			/**
			 * 
			 */
			private static final long serialVersionUID = 8606294082372796526L;
			
			
		}

	}

	public class google implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6332499779695217176L;
		private String clientID, clientSecret;
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

}
