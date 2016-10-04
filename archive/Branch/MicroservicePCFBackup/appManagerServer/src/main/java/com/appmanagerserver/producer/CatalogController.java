package com.appmanagerserver.producer;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerservice.sendinterface.IStubManager;

/**
 * File : CatalogController.java 
 * Description : It have all the catalog related services
 * Revision History : 
 * Version 	Date  	Author Reason 
 * 0.1 	13-June-2016 559296 Initial version
 */
@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class CatalogController {
	 @Autowired
		private IStubManager apfactory;
	/**
	 * This API will provide the details about the deployment status. 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_LAYOUTS+"/{layoutHeaderID}", method = RequestMethod.GET,headers="Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the layouts", notes = "No parameter needed")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public  ResponseEntity<JSONObject> getLayouts(@PathVariable final String layoutHeaderID) throws MyException {
		JSONObject response= apfactory.getLayouts(layoutHeaderID);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
	/**
	 * This API will provide the details about the deployment status. 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_PLATFORMS, method = RequestMethod.GET,headers="Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the platforms", notes = "No parameter needed")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public  ResponseEntity<JSONObject> getPlatform() throws MyException {
		JSONObject response= apfactory.getPlatforms();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
	/**
	 * This API will provide the list of organisations user have. 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_ORGANISATIONS, method = RequestMethod.GET,headers="Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the list of organisations", notes = "Accesstoken,ApiURL needed in header")
	  @ApiImplicitParams({
	        @ApiImplicitParam(name = "token", value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue="baseasdasd"),
	        @ApiImplicitParam(name = "api_url", value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue="localhost:8080/")

	  })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public  ResponseEntity<JSONObject> getOrganisations(HttpServletRequest request) throws MyException {
		String token=request.getHeader("token");
		String apiurl=request.getHeader("api_url");
		JSONObject response= apfactory.getOrganisations(token,apiurl);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
	/**
	 * This API will provide the list of regions user have. 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_REGIONS+"/{regionID}", method = RequestMethod.GET,headers="Accept=application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public  ResponseEntity<JSONObject> getRegions(@PathVariable("regionID") String regionID) throws MyException {
		JSONObject response= apfactory.getRegions(regionID);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
	}
	/**
	 * This API will provide the list of space user have. 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_SPACE, method = RequestMethod.GET,headers="Accept=application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	 @ApiOperation(value = "Get the list of spaces", notes = "Accesstoken,ApiURL,Organistation guid needed in header")
	  @ApiImplicitParams({
	        @ApiImplicitParam(name = "token", value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue="baseasdasd"),
	        @ApiImplicitParam(name = "api_url", value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue="localhost:8080/"),
	        @ApiImplicitParam(name = "orgguid", value = "Selected organisation's guid", required = true, dataType = "string", paramType = "header", defaultValue="asdasdasdadasdsadsa")

	  })
	@ResponseBody
	public  ResponseEntity<JSONObject> getSpace(HttpServletRequest request) throws MyException {
		String token=request.getHeader("token");
		String apiurl=request.getHeader("api_url");
		String orguid=request.getHeader("orgguid");
		JSONObject response= apfactory.getSpace(token,apiurl,orguid);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
	/**
	 * This API will provide the list of space user have. 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_APPNAME, method = RequestMethod.GET,headers="Accept=application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	 @ApiOperation(value = "Get the list of spaces", notes = "Accesstoken,ApiURL,Organistation guid needed in header")
	  @ApiImplicitParams({
	        @ApiImplicitParam(name = "token", value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue="baseasdasd"),
	        @ApiImplicitParam(name = "api_url", value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue="localhost:8080/"),
	        @ApiImplicitParam(name = "spaceguid", value = "Selected space's guid", required = true, dataType = "string", paramType = "header", defaultValue="asdasdasdadasdsadsa"),
	        @ApiImplicitParam(name = "appname", value = "Application name", required = true, dataType = "string", paramType = "header", defaultValue="TestApp1")

	  })
	@ResponseBody
	public  ResponseEntity<JSONObject> checkAppName(HttpServletRequest request) throws MyException {
		String token=request.getHeader("token");
		String apiurl=request.getHeader("api_url");
		String spaceguid=request.getHeader("spaceguid");
		String appname=request.getHeader("appname");
		JSONObject response= apfactory.checkAppName(token,apiurl,spaceguid,appname);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
	/**
	 * This API will provide the service plan GUID. 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_SERVICEPLANGUID, method = RequestMethod.GET,headers="Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the platforms", notes = "ApiURL,Serviceguid is need in header")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	
	@ResponseBody
	public  ResponseEntity<JSONObject> getServicePlanGUID(HttpServletRequest request) throws MyException {
		String token=request.getHeader("token");
		String apiurl=request.getHeader("api_url");
		String serviceguid=request.getHeader("serviceguid");
		JSONObject response= apfactory.getServicePlanGUID(token,apiurl,serviceguid);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
		
	}
	/**
	 * This API will provide the existing instances. 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_EXISTINGINSTANCES, method = RequestMethod.GET,headers="Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Get the platforms", notes = "ApiURL,Serviceguid is need in header")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	
	@ResponseBody
	public  ResponseEntity<JSONObject> getExistingInstances(HttpServletRequest request) throws MyException {
		String token=request.getHeader("token");
		String apiurl=request.getHeader("api_url");
		String serviceguid=request.getHeader("serviceguid");
		JSONObject response= apfactory.getExistingInstances(token,apiurl,serviceguid);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (response.containsKey("Error")){
			return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(response,httpHeaders,HttpStatus.OK);
		
	}
}
