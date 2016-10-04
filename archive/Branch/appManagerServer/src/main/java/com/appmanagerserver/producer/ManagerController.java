package com.appmanagerserver.producer;

import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.blueprints.PaasAuthentication;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.messages.Messages;
import com.appmanagerservice.sendinterface.IStubManager;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * File : ManagerController.java 
 * Description : It navigates to authenitcation,catalog,session
 * Revision History : 
 * Version 	Date  	Author Reason 
 * 0.1 	13-June-2016 559296 Initial version
 */
@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class ManagerController {
	 @Autowired
	private IStubManager apfactory;
	 /**
	 * below method is for authenticating user 
	 * in multiple platforms and return an 
	 * access token with space guid
	 * @author 559296
	 * @throws MyException 
	 */
	@RequestMapping(value = ApplicationConstants.PAAS_AUTHENTICATION, method = RequestMethod.POST,headers=ApplicationConstants.REQUEST_HEADERS,produces = ApplicationConstants.RESPONSE_HEADERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
	         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
	         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Do a login for the user in selected platform", notes = "It will give the response with access token")
	public ResponseEntity<JSONObject> authenticatePaas(@RequestBody final PaasAuthentication auth) throws MyException{
		
		/*return apfactory.doPassAuth(auth);*/
		final JSONObject response =apfactory.doPassAuth(auth);
		final HttpHeaders responseHeaders = new HttpHeaders();
		
			 responseHeaders.set(Messages.getString("ManagerController.Authentication"), ApplicationConstants.SUCCESS);  
			 return ResponseEntity.accepted().header(Messages.getString("ManagerController.Authentication"), ApplicationConstants.SUCCESS).body(response);  
		}
	
	 /**
		 * below method is for getting 
		 * all the catalog services
		 * @author 559296
	 * @throws MyException 
		 */
	@RequestMapping(method=RequestMethod.GET,value = ApplicationConstants.GET_ALL_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
	         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
	         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the catalogs", notes = "")
	public ResponseEntity<JSONObject> getCatalog(@RequestParam final String type) throws MyException{
		
		JSONObject response = apfactory.getCatalog(type);
		 HttpHeaders responseHeaders = new HttpHeaders();

			 responseHeaders.set(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS);  
			 return ResponseEntity.accepted().header(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS).body(response);  
	}
	
	 /**
	 * below method is for getting
	 *  all the catalog services
	 * @author 559296
	 * @throws MyException 
	 */
	@RequestMapping(method=RequestMethod.GET,value = ApplicationConstants.GET_RELATIVE_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
	         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
	         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	public ResponseEntity<JSONArray> getRelativeCatalog(@RequestParam final String id,@RequestParam final String platform,@RequestParam final String regionid) throws MyException{
		
		JSONArray response= apfactory.getSpecificCatalog(id, platform,regionid);
		 HttpHeaders responseHeaders = new HttpHeaders();
			
				 responseHeaders.set(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS);  
				 return ResponseEntity.accepted().header(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS).body(response);  
			
	}
	 /**
	 * below method is for getting 
	 * all the catalog services
	 * @author 559296
	 * @throws MyException 
	 */
	@RequestMapping(method=RequestMethod.GET,value = ApplicationConstants.SEARCH_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
	         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
	         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	public ResponseEntity<JSONObject> search(@RequestParam final String searchString,@RequestParam final String type) throws MyException{
		
		JSONObject response= apfactory.searchCatalog(searchString,type);
		 HttpHeaders responseHeaders = new HttpHeaders();
			
				 responseHeaders.set(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS);  
				 return ResponseEntity.accepted().header(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS).body(response);  
	}
	 /**
	 * below method is for getting
	 *  all the catalog services
	 * @author 559296
	 * @throws MyException 
	 */
	@RequestMapping(method=RequestMethod.GET,value = ApplicationConstants.AUTH_CATEORY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
	         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
	         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	public ResponseEntity<JSONObject> getAuthCategory(@RequestParam final String type,@RequestParam final String category) throws MyException{
		ResponseEntity<JSONObject> managerresponse;
		final JSONObject  response= apfactory.authenticationCatalog(type,category);
		final HttpHeaders responseHeaders = new HttpHeaders();
			
				 responseHeaders.set(ApplicationConstants.CATALOG,ApplicationConstants.SUCCESS);  
				 managerresponse= ResponseEntity.accepted().header(ApplicationConstants.CATALOG, ApplicationConstants.SUCCESS).body(response);  
			
			return managerresponse;
	}
	
	
}
