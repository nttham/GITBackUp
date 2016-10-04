package com.appmanagerserver.publisher.test;


import static org.junit.Assert.assertNotNull;

import org.json.simple.JSONObject;
import org.junit.Test;

import com.appmanagerserver.publisher.MessagePublisher;

public class MessagePublisherTest {

	@Test
	public void publishMessageTest(){
		
		MessagePublisher messagePublisher= new MessagePublisher();
		JSONObject blueprint= new JSONObject();
	assertNotNull(messagePublisher.publishMessage(blueprint));
	
	}

}
