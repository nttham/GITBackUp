package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class OtherServices implements Serializable{
	/**
	 * Value objects.
	 */
	private static final long serialVersionUID = -988118013034825969L;
	private String servicename,serviceplanguid;

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getServiceplanguid() {
		return serviceplanguid;
	}

	public void setServiceplanguid(String serviceplanguid) {
		this.serviceplanguid = serviceplanguid;
	}

	
	

}
