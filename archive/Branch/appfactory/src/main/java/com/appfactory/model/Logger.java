package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * get or set values for logger objects
 * @return
 */

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logger implements Serializable{
	
	/** 
	 * 
	 */
	private static final long serialVersionUID = 2384044446827826741L;
	private String name,type,hostname,portNo,dbName,username,password,service_plan_guid,service_instance_name,service_instance_id,protocol;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
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

	public String getService_plan_guid() {
		return service_plan_guid;
	}

	public void setService_plan_guid(String service_plan_guid) {
		this.service_plan_guid = service_plan_guid;
	}

	public String getService_instance_name() {
		return service_instance_name;
	}

	public void setService_instance_name(String service_instance_name) {
		this.service_instance_name = service_instance_name;
	}

	public String getService_instance_id() {
		return service_instance_id;
	}

	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}
	
	

}
