package com.appfactory.rabbitmq;


import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.BluePrint;
import com.appfactory.model.UIModelJson;
import com.appfactory.resources.Messages;
import com.appfactory.route.IappFactory;
import com.appfactory.utils.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * /*** File : DeployListener.java 
 * Description : Mq Listener class and the message will be received on onMessage method
 *  Revision History :	Version	  Date			Author	Reason
 *   					0.1     27-June-2016	 559296 Initial version
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeployListener {
	/*
	 * it's a listener method which will receive message for further processing
	 * 
	 * @see org.springframework.amqp.core.MessageListener#onMessage(org.
	 * springframework.amqp.core.Message)
	 */
	    @Autowired
		private IappFactory apfactory;

		@Autowired
		private ExceptionUtils eUtils;
//	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${spring.rabbitmq.queuename}", durable = "false"), exchange = @Exchange(value = "${spring.rabbitmq.exchange}"), key = "${spring.rabbitmq.queuename}"))
	
	public void onMessage(Message arg0) {

		String messageID = new String(arg0.getMessageProperties().getCorrelationId());
		String messageContent = new String(arg0.getBody());
			
		ObjectMapper mapper = new ObjectMapper();

		//BluePrint blueprint = new BluePrint();
		UIModelJson modelJson = new UIModelJson();
		try {
			
			modelJson = mapper.readValue(messageContent,UIModelJson.class);
			apfactory.doAction(modelJson, Messages.getString("FactoryController.2"),messageID);
			apfactory.doAction(modelJson, Messages.getString("FactoryController.3"),messageID);
		} catch (IOException e1) {
			eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, e1.getMessage());
		} catch (Exception e) {
			eUtils.myException(CustomErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
