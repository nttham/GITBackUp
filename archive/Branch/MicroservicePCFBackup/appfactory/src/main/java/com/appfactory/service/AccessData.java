package com.appfactory.service;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.appfactory.model.BluePrint;
import com.appfactory.model.UIModelJson;

/**
 * Service + Data Access Layer.
 */
@Service
public class AccessData {

	// private BluePrint blueprint;
	private UIModelJson uIModelJson;

	private String parentdirectory, runningapp, endpoint, host, appguid, domainguid, messageid, modifiedprotocol,
			logstatus;

	public String getLogstatus() {
		return logstatus;
	}

	public void setLogstatus(String logstatus) {
		this.logstatus = logstatus;
	}

	public String getModifiedprotocol() {
		return modifiedprotocol;
	}

	public void setModifiedprotocol(String modifiedprotocol) {
		this.modifiedprotocol = modifiedprotocol;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	private File servicefolderpath;
	private ArrayList<String> downloadfolders;
	private JSONObject hookObj, createservice, bindservice, env_json;

	public String getDomainguid() {
		return domainguid;
	}

	public void setDomainguid(String domainguid) {
		this.domainguid = domainguid;
	}

	public JSONObject getEnv_json() {
		return env_json;
	}

	public void setEnv_json(JSONObject env_json) {
		this.env_json = env_json;
	}

	public String getAppguid() {
		return appguid;
	}

	public void setAppguid(String appguid) {
		this.appguid = appguid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public JSONObject getCreateservice() {
		return createservice;
	}

	public void setCreateservice(JSONObject createservice) {

		this.createservice = createservice;
	}

	public JSONObject getBindservice() {
		return bindservice;
	}

	public void setBindservice(JSONObject bindservice) {
		this.bindservice = bindservice;
	}

	public String getRunningapp() {
		return runningapp;
	}

	public void setRunningapp(String runningapp) {
		this.runningapp = runningapp;
	}

	public JSONObject getHookObj() {
		return hookObj;
	}

	public void setHookObj(JSONObject hookObj) {
		this.hookObj = hookObj;
	}

	// public BluePrint getBlueprint() {
	// return blueprint;
	// }
	//
	// public void setBlueprint(BluePrint blueprint) {
	// this.blueprint = blueprint;
	// }

	public String getParentdirectory() {
		return parentdirectory;
	}

	public void setParentdirectory(String parentdirectory) {
		this.parentdirectory = parentdirectory;
	}

	public File getServicefolderpath() {
		return servicefolderpath;
	}

	public void setServicefolderpath(File servicefolderpath) {
		this.servicefolderpath = servicefolderpath;
	}

	public ArrayList<String> getDownloadfolders() {
		return downloadfolders;
	}

	public void setDownloadfolders(ArrayList<String> downloadfolders) {
		this.downloadfolders = downloadfolders;
	}

	public UIModelJson getuIModelJson() {
		return uIModelJson;
	}

	public void setuIModelJson(UIModelJson uIModelJson) {
		this.uIModelJson = uIModelJson;
	}

}
