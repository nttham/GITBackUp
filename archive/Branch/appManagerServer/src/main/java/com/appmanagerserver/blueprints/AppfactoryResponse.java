package com.appmanagerserver.blueprints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AppfactoryResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455516059573038531L;
	private String cfAppURL;

	
	private List<Logs> logsList = new ArrayList<Logs>();


	public String getCfAppURL() {
		return cfAppURL;
	}

	public void setCfAppURL(String cfAppURL) {
		this.cfAppURL = cfAppURL;
	}
	public List<Logs> getLogsList() {
		return logsList;
	}

	public void setLogsList(List<Logs> logsList) {
		this.logsList = logsList;
	}


	
}
