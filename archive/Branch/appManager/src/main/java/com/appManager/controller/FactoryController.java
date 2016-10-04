package com.appManager.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appManager.exceptions.MyException;
import com.appManager.model.BluePrint;
import com.appManager.resources.Messages;
import com.appManager.route.IappFactory;
import com.appManager.url.RequestMappingUrl;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * File : FactoryController.java 
 * Description : This class helps to generate the
 *                reuired app dynamically.
 * Revision History : 13-June-2016
 * Version 	Date  	Author Reason 
 * 0.1 	13-June-2016 559296 Initial version
 */
@CrossOrigin
@RestController
@RequestMapping(RequestMappingUrl.BASE_URL)

public class FactoryController {
	@Autowired
	private IappFactory apfactory;
	/**
	 * Description : This method reuires a valid
	 *               blueprint helps to generate
	 *               the node js app.
	 * Revision History : 13-June-2016 
	 * Version 	Date  	Author Reason 
	 * 0.1 	13-June-2016 559296 Initial version
	 * @throws MyException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = RequestMappingUrl.DEPLOYEMENT, method = RequestMethod.POST,headers="Accept=application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public  ResponseEntity<JSONObject> appFactory(@RequestBody BluePrint jsonlists) throws MyException {
		List<String> arr = new ArrayList<String>();

				       apfactory.doAction(jsonlists, Messages.getString("FactoryController.2")); 
				 arr = apfactory.doAction(jsonlists, Messages.getString("FactoryController.3")); 
				 JSONObject jobj = new JSONObject();
				 jobj.put("App Url", arr.get(0));
				 jobj.put("Github", arr.get(1));
		final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				return new ResponseEntity<JSONObject>(jobj,HttpStatus.OK);
		
	}
}
