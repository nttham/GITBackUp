package com.appfactory.rabbitmq;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.core.GenerateApp;
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
		private static final Logger LOG = Logger.getLogger(GenerateApp.class.getName());

/*	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${spring.rabbitmq.queuename}", durable = "false"), exchange = @Exchange(value = "${spring.rabbitmq.exchange}"), key = "${spring.rabbitmq.queuename}"))
*/	
	public void onMessage(Message arg0) {
		String messageID = new String(arg0.getMessageProperties().getCorrelationId());
		String messageContent = new String(arg0.getBody());
		ObjectMapper mapper = new ObjectMapper();

		//BluePrint blueprint = new BluePrint();
		UIModelJson modelJson = new UIModelJson();
		try {
			LOG.info("====================Input JSON from UI============================"+"\n"+messageContent);
			modelJson = mapper.readValue(messageContent,UIModelJson.class);
			apfactory.doAction(modelJson, Messages.getString("FactoryController.2"),messageID);
			apfactory.doAction(modelJson, Messages.getString("FactoryController.3"),messageID);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
