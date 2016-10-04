package com.appfactory.service;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
	
	/**
	 * 
	 * for enviromental setup all the below strings required
	 */
	private String base_dir,status_url,passcatalog_url,space_micro_guid;
	
	

	public String getBase_dir() {
		return base_dir;
	}

	public void setBase_dir(String base_dir) {
		this.base_dir = base_dir;
	}

	public String getStatus_url() {
		return status_url;
	}

	public void setStatus_url(String status_url) {
		this.status_url = status_url;
	}

	public String getPasscatalog_url() {
		return passcatalog_url;
	}

	public void setPasscatalog_url(String passcatalog_url) {
		this.passcatalog_url = passcatalog_url;
	}

	public String getSpace_micro_guid() {
		return space_micro_guid;
	}

	public void setSpace_micro_guid(String space_micro_guid) {
		this.space_micro_guid = space_micro_guid;
	}

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
