
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerserver.publisher;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appmanagerserver.blueprints.MQpojo;
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
/**
 * @author anitha
 *
 */
@Service
public class MessagePublisher implements IMessagePublisher {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private IGistHandler gistOperations;
	@Autowired
	private AmqpAdmin amqpAdmin;
	@Autowired
	private RabbitTemplate amqpTemplate;
	

	/*
	 * method to publish the json message in to rabbit MQ server and returns the
	 * unique id
	 * 
	 * @see
	 * com.appManager.route.IMessagePublisher#publishMessage(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	/**
	 * auth = username,password,platform
	 */
	@Override
	public String publishMessage(JSONObject blueprint) throws MyException {
		MessageProperties messageProperties = new MessageProperties();
		String requestGuid = UUID.randomUUID().toString();
		String gistid = gistOperations.defaultGistContent(requestGuid, ApplicationConstants.EMPTY_STRING);
		messageProperties.setCorrelationId(requestGuid.getBytes());
		messageProperties.setMessageId(gistid);
		messageProperties.setContentType(ApplicationConstants.JSON_CONTENT_TYPE);
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		blueprint.put(ApplicationConstants.GIST_ID, gistid);
		Message message = new Message(blueprint.toJSONString().getBytes(), messageProperties);
		try {
			rabbitTemplate.sendAndReceive(ApplicationConstants.APPFACTORY_QUEUE_NAME, message);
		} catch (Exception e) {
			e.printStackTrace();
			throw eUtils.myException(CustomErrorMessage.QUEUE_CONNECTION_FAILED, e.getMessage());
		}
		return gistid;
	}

	/**
	 * This method is used to retrive the messages from queue
	 */
	@Override
	public String recvMessages(String queueName) throws MyException {
		amqpAdmin.declareQueue(new Queue(queueName));
		String foo = (String) amqpTemplate.receiveAndConvert(queueName);
		if (foo == null) {
			foo = ApplicationConstants.RETRIVING_MESSAGE;
		}
		return foo;
	}

	/**
	 * This method will create a queue,exchange and publish message.
	 * 
	 * @throws MyException
	 */
	@Override
	public String sendMessages(MQpojo mqpojo) throws MyException {
		try {
			amqpAdmin.declareQueue(new Queue(mqpojo.getQname()));
			amqpTemplate.convertAndSend(mqpojo.getQname(), mqpojo.getMessages());
		} catch (Exception e) {
			eUtils.myException(CustomErrorMessage.QUEUE_CONNECTION_FAILED, e.getMessage());
			return ApplicationConstants.FAILED_PUSHING_MESSAGE;
		}
		return ApplicationConstants.SUCCESSFULLY_PUSHING_MESSAGE;
	}



	@Override
	public String delMessages(String queueName) throws MyException {
		amqpAdmin.deleteQueue(queueName, true, false);
		return ApplicationConstants.SUCCESSFULLY_DELETED_QUEUE;
	}

}
