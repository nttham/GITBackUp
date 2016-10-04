package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;
@Component
public class LinkedIn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 807675156786462807L;
	/**
	 * 
	 */
	private String channel, channelprovider,condition;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelprovider() {
		return channelprovider;
	}

	public void setChannelprovider(String channelprovider) {
		this.channelprovider = channelprovider;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}



}
