package com.appManager.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PrimaryService implements Serializable {
/**
	 *  Value objects.
	 */
	private static final long serialVersionUID = 9005851740301594006L;
private String protocol,appurl,username,password;
private PrimaryProviders providers;
private PreHook prehook;
private PostHook posthook;
private OnHook onhook;
public String getProtocol() {
	return protocol;
}
public void setProtocol(String protocol) {
	this.protocol = protocol;
}
public String getAppurl() {
	return appurl;
}
public void setAppurl(String appurl) {
	this.appurl = appurl;
}
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
public PrimaryProviders getProviders() {
	return providers;
}
public void setProviders(PrimaryProviders providers) {
	this.providers = providers;
}
public PreHook getPrehook() {
	return prehook;
}
public void setPrehook(PreHook prehook) {
	this.prehook = prehook;
}
public PostHook getPosthook() {
	return posthook;
}
public void setPosthook(PostHook posthook) {
	this.posthook = posthook;
}
public OnHook getOnhook() {
	return onhook;
}
public void setOnhook(OnHook onhook) {
	this.onhook = onhook;
}


}
