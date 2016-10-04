package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;
/**
 * 
 * @author rudhrapriya
 *
 */
@Component
public class LDAP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682280989885875731L;
	private String channel, channelprovider;

	private LDAP() {

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
