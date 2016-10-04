package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostHooks implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7651661888132721598L;
	private String channel, provider, hookchannelurl, providerurl, channeltype, provideraccountSID, providerauthToken,
			providertoRecipient, providerfrom;
	private ArrayList<Facebook> facebook;
	private ArrayList<Google> google;
	private ArrayList<LDAP> ldap;
	private ArrayList<Custom> custom;

	public ArrayList<Custom> getCustom() {
		return custom;
	}
	public void setCustom(ArrayList<Custom> custom) {
		this.custom = custom;
	}
	public ArrayList<LDAP> getLdap() {
		return ldap;
	}
	public void setLdap(ArrayList<LDAP> ldap) {
		this.ldap = ldap;
	}

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
//
//	public void setChannellength(int channellength) {
//		this.channellength = channellength;
//	}
//
//	public int getChannelexpiryTime() {
//		return channelexpiryTime;
//	}
//
//	public void setChannelexpiryTime(int channelexpiryTime) {
//		this.channelexpiryTime = channelexpiryTime;
//	}

}
