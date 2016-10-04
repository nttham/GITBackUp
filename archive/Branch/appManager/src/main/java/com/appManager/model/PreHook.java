package com.appManager.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;
@Component
public class PreHook implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6467937786099997657L;
	private String channel,provider,hookchannelurl,providerurl,channeltype,provideraccountSID,providerauthToken,providertoRecipient,providerfrom;
	private int channellength,channelexpiryTime;
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
	public int getChannellength() {
		return channellength;
	}
	public void setChannellength(int channellength) {
		this.channellength = channellength;
	}
	public int getChannelexpiryTime() {
		return channelexpiryTime;
	}
	public void setChannelexpiryTime(int channelexpiryTime) {
		this.channelexpiryTime = channelexpiryTime;
	}


	
}
