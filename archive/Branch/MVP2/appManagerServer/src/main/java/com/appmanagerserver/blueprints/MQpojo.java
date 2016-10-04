/**
 * 
 */
package com.appmanagerserver.blueprints;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * @author 559296
 *
 */
@Component
public class MQpojo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -333112757867509509L;
	private String qname,exchangename,messages;

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public String getExchangename() {
		return exchangename;
	}

	public void setExchangename(String exchangename) {
		this.exchangename = exchangename;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
	

}
