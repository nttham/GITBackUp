
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerserver.publisher;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerservice.sendinterface.IGistHandler;
import com.appmanagerservice.sendinterface.IMessagePublisher;



/**
 * @author 559296
 *
 */
/**
* File                          : MessagePublisher.java
* Description          : RabbitMQMessagePublisher implements MessagePublisher to publish the json message in to rabbit MQ server
* Revision History :
* Version      Date            Author       Reason
* 0.1          07-Jun-2016     559296  Initial version
 */
@Service
public class MessagePublisher implements IMessagePublisher{

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private IGistHandler gistOperations;
	/* 
	 * method to publish the json message in to rabbit MQ server and returns the unique id
	 * @see com.appManager.route.IMessagePublisher#publishMessage(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	/**
	 * auth = username,password,platform
	 */
	@Override
	public String publishMessage(JSONObject blueprint) throws MyException{
		MessageProperties messageProperties = new MessageProperties();
		String requestGuid = UUID.randomUUID().toString();
		String gistid=gistOperations.defaultGistContent(requestGuid, ApplicationConstants.EMPTY_STRING);
		messageProperties.setCorrelationId(requestGuid.getBytes());
		messageProperties.setMessageId(gistid);
		messageProperties.setContentType(ApplicationConstants.JSON_CONTENT_TYPE);
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		blueprint.put("gistid",gistid);
		Message message = new Message(blueprint.toJSONString().getBytes(), messageProperties);
		try {
			rabbitTemplate.sendAndReceive(ApplicationConstants.APPFACTORY_QUEUE_NAME, message);
		} catch (AmqpException e) {
			throw eUtils.myException(CustomErrorMessage.QUEUE_CONNECTION_FAILED, e.getMessage());
		}
		return gistid;
	}
}
