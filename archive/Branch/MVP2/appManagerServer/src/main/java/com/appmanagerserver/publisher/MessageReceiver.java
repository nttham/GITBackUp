/**
 * 
 */
package com.appmanagerserver.publisher;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author 559296
 *
 */
public class MessageReceiver implements MessageListener{

	public void onMessage(Message message) {
		String messageBody= new String(message.getBody());
	    System.out.println("Message received: "+messageBody);
	}

}
