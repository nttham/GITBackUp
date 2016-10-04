package com.appmanagerserver.producer;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.blueprints.MQpojo;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerservice.sendinterface.IMessagePublisher;

@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class MQController {
	@Autowired
	private IMessagePublisher messagePublisher;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = ApplicationConstants.PUSH_TO_QUEUE, method = RequestMethod.PUT)
	public  ResponseEntity<JSONObject> getmessages(@RequestBody MQpojo mqpojo) throws MyException {
		String result=messagePublisher.sendMessages(mqpojo);
		JSONObject responseobj = new JSONObject();
		responseobj.put(ApplicationConstants.MESSAGE, result);
		return new ResponseEntity<JSONObject>(responseobj, HttpStatus.OK);

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = ApplicationConstants.RETRIVE_FROM_QUEUE+ "/{queueName}",method = RequestMethod.GET)
	public ResponseEntity<JSONObject> receiveMessage(@PathVariable("queueName") String queueName) throws MyException {
		String result=messagePublisher.recvMessages(queueName);
		JSONObject responseobj = new JSONObject();
		responseobj.put(ApplicationConstants.MESSAGE, result);
		return new ResponseEntity<JSONObject>(responseobj, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = ApplicationConstants.DELETE_QUEUE+ "/{queueName}",method = RequestMethod.GET)
	public ResponseEntity<JSONObject> deleteQueue(@PathVariable("queueName") String queueName) throws MyException {
		String result=messagePublisher.delMessages(queueName);
		JSONObject responseobj = new JSONObject();
		responseobj.put(ApplicationConstants.MESSAGE, result);
		return new ResponseEntity<JSONObject>(responseobj, HttpStatus.OK);
	}
}
