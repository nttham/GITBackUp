package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherServices implements Serializable{
	/**
	 * Value objects.
	 */
	private static final long serialVersionUID = -988118013034825969L;
	private String service_instance_name,service_plan_guid,category,service_instance_id;
	private Logger provider;

	public String getService_instance_name() {
		return service_instance_name;
	}
	public void setService_instance_name(String service_instance_name) {
		this.service_instance_name = service_instance_name;
	}
	public String getService_plan_guid() {
		return service_plan_guid;
	}
	public void setService_plan_guid(String service_plan_guid) {
		this.service_plan_guid = service_plan_guid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getService_instance_id() {
		return service_instance_id;
	}
	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}
	public Logger getProvider() {
		return provider;
	}
	public void setProvider(Logger provider) {
		this.provider = provider;
	}



	
	
	
	


}
