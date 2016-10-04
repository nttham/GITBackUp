package com.datamanagement.controller;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datamanagement.exception.DataManagementException;
import com.datamanagement.service.IDataManagementService;
import com.datamanagement.utils.JsonReader;
import com.datamanagement.vo.EnvironmentJsonVO;



@Controller
public class DataMangementController  {
	
	@Autowired(required = true)
	private JsonReader jsonReader;	
	
	@Autowired
	private IDataManagementService dataManagementService;

    @RequestMapping(value="/deployStream", method = RequestMethod.POST)
   	public void deployStream(@RequestBody String  input) throws DataManagementException {
    	    
    	 System.out.println("input ----------->"+input);
       	
    	try{
       		
       boolean flag = dataManagementService.isJsonValid(input.trim());
        
       if(flag) {
    	   
    	JSONParser jsonParser =new JSONParser();    	
		JSONObject object = (JSONObject) jsonParser.parse(input.trim());		
		EnvironmentJsonVO  environmentJsonVO =jsonReader.iterateJSON(object);		
		dataManagementService.createStreamDefinition(environmentJsonVO);		
	
       } 
     }
     catch(Exception e){
    		throw new DataManagementException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
    	}
   
    }
   
}

