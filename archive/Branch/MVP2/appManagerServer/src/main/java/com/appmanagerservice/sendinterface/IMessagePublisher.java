
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerservice.sendinterface;

import org.json.simple.JSONObject;

import com.appmanagerserver.blueprints.MQpojo;
import com.appmanagerserver.exception.MyException;

/**
 * @author 559296
 *
 */
public interface IMessagePublisher {
	public String publishMessage(JSONObject blueprint) throws MyException;
	
	public String sendMessages(MQpojo mqpojo) throws MyException;
	
	public String recvMessages(String queueName) throws MyException;
	
	public String delMessages(String queueName) throws MyException;
}
