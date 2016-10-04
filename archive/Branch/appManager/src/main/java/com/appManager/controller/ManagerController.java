package com.appManager.controller;

import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appManager.model.PaasAuthentication;
import com.appManager.resources.Messages;
import com.appManager.route.IStubManager;
import com.appManager.url.RequestMappingUrl;

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
@RequestMapping(RequestMappingUrl.BASE_URL)
public class ManagerController {
	 @Autowired
	private IStubManager apfactory;
	 /**
	 * below method is for authenticating user 
	 * in multiple platforms and return an 
	 * access token with space guid
	 * @author 559296
	 */
	@RequestMapping(value = RequestMappingUrl.PAAS_AUTHENTICATION, method = RequestMethod.POST,headers="Accept=application/json",produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<JSONObject> authenticatePaas(@RequestBody final PaasAuthentication auth){
		
		/*return apfactory.doPassAuth(auth);*/
		final JSONObject response =apfactory.doPassAuth(auth);
		final HttpHeaders responseHeaders = new HttpHeaders();
		if(response.isEmpty()){
			responseHeaders.set(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")); //$NON-NLS-1$ //$NON-NLS-2$
			 return ResponseEntity.badRequest().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
		}else{
			 responseHeaders.set(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Success")); //$NON-NLS-1$ //$NON-NLS-2$
			 return ResponseEntity.accepted().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Success")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	 /**
		 * below method is for getting 
		 * all the catalog services
		 * @author 290778
		 */
	@RequestMapping(method=RequestMethod.GET,value = RequestMappingUrl.GET_ALL_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<JSONObject> getCatalog(@RequestParam final String type){
		
		JSONObject response = apfactory.getCatalog(type);
		 HttpHeaders responseHeaders = new HttpHeaders();
		if(response.isEmpty()){
			responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Failed")); //$NON-NLS-1$ //$NON-NLS-2$
			 return ResponseEntity.badRequest().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
		}else{
			 responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")); //$NON-NLS-1$ //$NON-NLS-2$
			 return ResponseEntity.accepted().header(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	 /**
	 * below method is for getting
	 *  all the catalog services
	 * @author 290778
	 */
	@RequestMapping(method=RequestMethod.GET,value = RequestMappingUrl.GET_RELATIVE_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<JSONArray> getRelativeCatalog(@RequestParam final String id, final String platform){
		
		JSONArray response= apfactory.getSpecificCatalog(id, platform);
		 HttpHeaders responseHeaders = new HttpHeaders();
			if(response.isEmpty()){
				responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Failed")); //$NON-NLS-1$ //$NON-NLS-2$
				 return ResponseEntity.badRequest().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}else{
				 responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")); //$NON-NLS-1$ //$NON-NLS-2$
				 return ResponseEntity.accepted().header(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}
	}
	 /**
	 * below method is for getting 
	 * all the catalog services
	 * @author 290778
	 */
	@RequestMapping(method=RequestMethod.GET,value = RequestMappingUrl.SEARCH_CATALOG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<JSONObject> search(@RequestParam final String searchString,final String type){
		
		JSONObject response= apfactory.searchCatalog(searchString,type);
		 HttpHeaders responseHeaders = new HttpHeaders();
			if(response.isEmpty()){
				responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Failed")); //$NON-NLS-1$ //$NON-NLS-2$
				 return ResponseEntity.badRequest().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}else{
				 responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")); //$NON-NLS-1$ //$NON-NLS-2$
				 return ResponseEntity.accepted().header(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}
	}
	 /**
	 * below method is for getting
	 *  all the catalog services
	 * @author 290778
	 */
	@RequestMapping(method=RequestMethod.GET,value = RequestMappingUrl.AUTH_CATEORY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<JSONObject> getAuthCategory(@RequestParam final String type,final String category){
		ResponseEntity<JSONObject> managerresponse;
		final JSONObject  response= apfactory.authenticationCatalog(type,category);
		final HttpHeaders responseHeaders = new HttpHeaders();
			if(response.isEmpty()){
				responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Failed")); //$NON-NLS-1$ //$NON-NLS-2$
				managerresponse= ResponseEntity.badRequest().header(Messages.getString("ManagerController.Authentication"), Messages.getString("ManagerController.Failed")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}else{
				 responseHeaders.set(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")); //$NON-NLS-1$ //$NON-NLS-2$
				 managerresponse= ResponseEntity.accepted().header(Messages.getString("ManagerController.Catalog"), Messages.getString("ManagerController.Success")).body(response); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return managerresponse;
	}
	
	
}
