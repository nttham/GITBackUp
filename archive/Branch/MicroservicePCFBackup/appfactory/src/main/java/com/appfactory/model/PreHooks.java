package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
@Component
public class PreHooks implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6467937786099997657L;
	private String channel,provider,hookchannelurl,providerurl,channeltype,provideraccountSID,providerauthToken,providertoRecipient,providerfrom;
//	private int channellength,channelexpiryTime;
	private ArrayList<Facebook> facebook;
	private ArrayList<Google> google;

	public ArrayList<Google> getGoogle() {
		return google;
	}
	public void setGoogle(ArrayList<Google> google) {
		this.google = google;
	}
	public ArrayList<LinkedIn> getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(ArrayList<LinkedIn> linkedin) {
		this.linkedin = linkedin;
	}
	public ArrayList<Twitter> getTwitter() {
		return twitter;
	}
	public void setTwitter(ArrayList<Twitter> twitter) {
		this.twitter = twitter;
	}
	private ArrayList<LinkedIn> linkedin;

	private ArrayList<Twitter> twitter;

	
	public ArrayList<Facebook> getFacebook() {
		return facebook;
	}
	public void setFacebook(ArrayList<Facebook> facebook) {
		this.facebook = facebook;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getHookchannelurl() {
		return hookchannelurl;
	}
	public void setHookchannelurl(String hookchannelurl) {
		this.hookchannelurl = hookchannelurl;
	}
	public String getProviderurl() {
		return providerurl;
	}
	public void setProviderurl(String providerurl) {
		this.providerurl = providerurl;
	}
	public String getChanneltype() {
		return channeltype;
	}
	public void setChanneltype(String channeltype) {
		this.channeltype = channeltype;
	}
	public String getProvideraccountSID() {
		return provideraccountSID;
	}
	public void setProvideraccountSID(String provideraccountSID) {
		this.provideraccountSID = provideraccountSID;
	}
	public String getProviderauthToken() {
		return providerauthToken;
	}
	public void setProviderauthToken(String providerauthToken) {
		this.providerauthToken = providerauthToken;
	}
	public String getProvidertoRecipient() {
		return providertoRecipient;
	}
	public void setProvidertoRecipient(String providertoRecipient) {
		this.providertoRecipient = providertoRecipient;
	}
	public String getProviderfrom() {
		return providerfrom;
	}
	public void setProviderfrom(String providerfrom) {
		this.providerfrom = providerfrom;
	}
//	public int getChannellength() {
//		return channellength;
//	}
//	public void setChannellength(int channellength) {
//		this.channellength = channellength;
//	}
//	public int getChannelexpiryTime() {
//		return channelexpiryTime;
//	}
//	public void setChannelexpiryTime(int channelexpiryTime) {
//		this.channelexpiryTime = channelexpiryTime;
//	}

//	public class Facebook implements Serializable{
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -4174620203378947218L;
//		private String 	channel, channelprovider;
//		public String getChannel() {
//			return channel;
//		}
//		public void setChannel(String channel) {
//			this.channel = channel;
//		}
//		public String getChannelprovider() {
//			return channelprovider;
//		}
//		public void setChannelprovider(String channelprovider) {
//			this.channelprovider = channelprovider;
//		}
//		
//	}
//	
//	public class Google implements Serializable{
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -4867570885725061456L;
//		/**
//		 * 
//		 */
//		private String 	channel, channelprovider;
//		public String getChannel() {
//			return channel;
//		}
//		public void setChannel(String channel) {
//			this.channel = channel;
//		}
//		public String getChannelprovider() {
//			return channelprovider;
//		}
//		public void setChannelprovider(String channelprovider) {
//			this.channelprovider = channelprovider;
//		}
//		
//	}
//	public class Twitter implements Serializable{
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -174843157769871571L;
//		/**
//		 * 
//		 */
//		private String 	channel, channelprovider;
//		public String getChannel() {
//			return channel;
//		}
//		public void setChannel(String channel) {
//			this.channel = channel;
//		}
//		public String getChannelprovider() {
//			return channelprovider;
//		}
//		public void setChannelprovider(String channelprovider) {
//			this.channelprovider = channelprovider;
//		}
//		
//	}
//	public class LinkedIn implements Serializable{
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -5052468180522223994L;
//		/**
//		 * 
//		 */
//		private String 	channel, channelprovider;
//		public String getChannel() {
//			return channel;
//		}
//		public void setChannel(String channel) {
//			this.channel = channel;
//		}
//		public String getChannelprovider() {
//			return channelprovider;
//		}
//		public void setChannelprovider(String channelprovider) {
//			this.channelprovider = channelprovider;
//		}
//		
//	}
//	
}
