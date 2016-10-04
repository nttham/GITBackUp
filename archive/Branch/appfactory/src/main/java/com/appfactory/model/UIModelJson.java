package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author rudhrapriya
 * 
 *
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class UIModelJson implements Serializable {

	/**
	 * Value objects.
	 *
	 */
	private static final long serialVersionUID = -9068557358794765065L;
	private String servicename, platform, accesstoken, spaceguid, devlopergiturl, devlopergitusername,
			devlopergitpassword, orgguid, category, app_name, appurl, protocol, api_url, host, serviceplanguid;

	public String getServiceplanguid() {
		return serviceplanguid;
	}

	public void setServiceplanguid(String serviceplanguid) {
		this.serviceplanguid = serviceplanguid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getApi_url() {
		return api_url;
	}

	public void setApi_url(String api_url) {
		this.api_url = api_url;
	}

	private String runningapp, gitpath;
	private String boundtype, gistid;
	private ArrayList<OtherServices> otherservices;
	private Environment_Json environment_json;

	public Environment_Json getEnvironment_json() {
		return environment_json;
	}

	public void setEnvironment_json(Environment_Json environment_json) {
		this.environment_json = environment_json;
	}

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

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// private PrimaryService primaryservice;

	public String getGistid() {
		return gistid;
	}

	public void setGistid(String gistid) {
		this.gistid = gistid;
	}

	public ArrayList<OtherServices> getOtherservices() {
		return otherservices;
	}

	public void setOtherservices(ArrayList<OtherServices> otherservices) {
		this.otherservices = otherservices;
	}

	public String getOrgguid() {
		return orgguid;
	}

	public void setOrgguid(String orgguid) {
		this.orgguid = orgguid;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public String getSpaceguid() {
		return spaceguid;
	}

	/*
	 * public String getServiceplanguid() { return serviceplanguid; }
	 * 
	 * public void setServiceplanguid(String serviceplanguid) {
	 * this.serviceplanguid = serviceplanguid; }
	 */

	public void setSpaceguid(String spaceguid) {
		this.spaceguid = spaceguid;
	}

	public String getDevlopergiturl() {
		return devlopergiturl;
	}

	public void setDevlopergiturl(String devlopergiturl) {
		this.devlopergiturl = devlopergiturl;
	}

	public String getDevlopergitusername() {
		return devlopergitusername;
	}

	public void setDevlopergitusername(String devlopergitusername) {
		this.devlopergitusername = devlopergitusername;
	}

	public String getDevlopergitpassword() {
		return devlopergitpassword;
	}

	public void setDevlopergitpassword(String devlopergitpassword) {
		this.devlopergitpassword = devlopergitpassword;
	}

	// public PrimaryService getPrimaryservice() {
	// return primaryservice;
	// }
	// public void setPrimaryservice(PrimaryService primaryservice) {
	// this.primaryservice = primaryservice;
	// }
	public String getRunningapp() {
		return runningapp;
	}

	public void setRunningapp(String runningapp) {
		this.runningapp = runningapp;
	}

	public String getGitpath() {
		return gitpath;
	}

	public void setGitpath(String gitpath) {
		this.gitpath = gitpath;
	}

	public String getBoundtype() {
		return boundtype;
	}

	public void setBoundtype(String boundtype) {
		this.boundtype = boundtype;
	}
}
