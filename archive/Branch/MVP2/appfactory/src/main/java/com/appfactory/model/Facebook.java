package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class Facebook implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4174620203378947218L;
	private String channel, channelprovider;
	private ArrayList<Condition> condition;

	private Facebook(){
		
	}
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
	public ArrayList<Condition> getCondition() {
		return condition;
	}
	public void setCondition(ArrayList<Condition> condition) {
		this.condition = condition;
	}

	



}
