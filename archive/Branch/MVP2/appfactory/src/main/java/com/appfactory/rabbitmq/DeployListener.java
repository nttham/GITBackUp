package com.appfactory.rabbitmq;


import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.appfactory.core.PojoMapper;
/**
 * /*** File : DeployListener.java 
 * Description : Mq Listener class and the message will be received on onMessage method
 *  Revision History :	Version	  Date			Author	Reason
 *   					0.1     27-June-2016	 559296 Initial version
 */
public class DeployListener {
	/*
	 * it's a listener method which will receive message for further processing
	 * 
	 * @see org.springframework.amqp.core.MessageListener#onMessage(org.
	 * springframework.amqp.core.Message)
	 */

	    @Autowired
		private PojoMapper pojoMap;

	public void onMessage(Message arg0) {
		String messageID = new String(arg0.getMessageProperties().getCorrelationId());
		String messageContent = new String(arg0.getBody());
		String gistid = new String(arg0.getMessageProperties().getMessageId());
		pojoMap.processQueue(messageContent, messageID,gistid);
	}
}
