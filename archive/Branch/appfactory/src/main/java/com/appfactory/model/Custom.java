package com.appfactory.model;

import java.io.Serializable;

public class Custom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2872784650710583767L;
	private String channel, channelprovider;

	private Custom() {

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

}
