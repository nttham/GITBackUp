package com.appmanagerserver.producer;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerservice.sendinterface.IStubManager;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * File : CatalogController.java Description : It have all the catalog related
 * services Revision History : Version Date Author Reason 0.1 13-June-2016
 * 559296 Initial version
 */
@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class CatalogController {
	@Autowired
	private IStubManager apfactory;

	/**
	 * This API will provide the details about the deployment status.
	 * 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_LAYOUTS
			+ "/{layoutHeaderID}", method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the layouts", notes = "No parameter needed")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseBody
	public ResponseEntity<JSONObject> getLayouts(@PathVariable final String layoutHeaderID) throws MyException {
		JSONObject response = apfactory.getLayouts(layoutHeaderID);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the details about the deployment status.
	 * 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_PLATFORMS, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the platforms", notes = "No parameter needed")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseBody
	public ResponseEntity<JSONObject> getPlatform() throws MyException {
		JSONObject response = apfactory.getPlatforms();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the list of organisations user have.
	 * 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_ORGANISATIONS, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the list of organisations", notes = "Accesstoken,ApiURL needed in header")
	@ApiImplicitParams({
			@ApiImplicitParam(name = ApplicationConstants.TOKEN_STRING, value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "baseasdasd"),
			@ApiImplicitParam(name = ApplicationConstants.API_URL, value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue = "localhost:8080/")

	})
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseBody
	public ResponseEntity<JSONObject> getOrganisations(HttpServletRequest request) throws MyException {
		String token = request.getHeader(ApplicationConstants.TOKEN_STRING);
		String apiurl = request.getHeader(ApplicationConstants.API_URL);
		String login_url =request.getHeader(ApplicationConstants.LOGIN_URL);
		JSONObject response = apfactory.getOrganisations(token, apiurl,login_url);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the list of regions user have.
	 * 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_REGIONS
			+ "/{regionID}", method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ResponseBody
	public ResponseEntity<JSONObject> getRegions(@PathVariable("regionID") String regionID) throws MyException {
		JSONObject response = apfactory.getRegions(regionID);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);
	}

	/**
	 * This API will provide the list of space user have.
	 * 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_SPACE, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ApiOperation(value = "Get the list of spaces", notes = "Accesstoken,ApiURL,Organistation guid needed in header")
	@ApiImplicitParams({
			@ApiImplicitParam(name = ApplicationConstants.TOKEN_STRING, value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "baseasdasd"),
			@ApiImplicitParam(name = ApplicationConstants.API_URL, value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue = "localhost:8080/"),
			@ApiImplicitParam(name = "orgguid", value = "Selected organisation's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa")

	})
	@ResponseBody
	public ResponseEntity<JSONObject> getSpace(HttpServletRequest request) throws MyException {
		String token = request.getHeader(ApplicationConstants.TOKEN_STRING);
		String apiurl = request.getHeader(ApplicationConstants.API_URL);
		String orguid = request.getHeader("orgguid");
		String login_url =request.getHeader(ApplicationConstants.LOGIN_URL);
		JSONObject response = apfactory.getSpace(token, apiurl, orguid,login_url);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the list of space user have.
	 * 
	 * @param request
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_APPNAME, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
	@ApiOperation(value = "Get the list of spaces", notes = "Accesstoken,ApiURL,Organistation guid needed in header")
	@ApiImplicitParams({
			@ApiImplicitParam(name = ApplicationConstants.TOKEN_STRING, value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "baseasdasd"),
			@ApiImplicitParam(name = ApplicationConstants.API_URL, value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue = "localhost:8080/"),
			@ApiImplicitParam(name = "spaceguid", value = "Selected space's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa"),
			@ApiImplicitParam(name = ApplicationConstants.SERVICEGUID, value = "Application name", required = true, dataType = "string", paramType = "header", defaultValue = "TestApp1") })
	@ResponseBody
	public ResponseEntity<JSONObject> checkAppName(HttpServletRequest request) throws MyException {
		String token = request.getHeader(ApplicationConstants.TOKEN_STRING);
		String apiurl = request.getHeader(ApplicationConstants.API_URL);
		String spaceguid = request.getHeader(ApplicationConstants.SPACEGUID);
		String appname = request.getHeader("appname");
		String login_url =request.getHeader(ApplicationConstants.LOGIN_URL);
		JSONObject response = apfactory.checkAppName(token, apiurl, spaceguid, appname,login_url);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the service plan GUID.
	 * 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping(value = ApplicationConstants.GET_SERVICEPLANGUID, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the platforms", notes = "ApiURL,Serviceguid is need in header")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })

	@ResponseBody
	public ResponseEntity<JSONObject> getServicePlanGUID(HttpServletRequest request) throws MyException {
		String token = request.getHeader(ApplicationConstants.TOKEN_STRING);
		String apiurl = request.getHeader(ApplicationConstants.API_URL);
		String serviceguid = request.getHeader(ApplicationConstants.SERVICEGUID);
		String login_url =request.getHeader(ApplicationConstants.LOGIN_URL);
		JSONObject response = apfactory.getServicePlanGUID(token, apiurl, serviceguid,login_url);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

	/**
	 * This API will provide the existing instances.
	 * 
	 * @return
	 * @throws MyException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ApplicationConstants.GET_EXISTINGINSTANCES, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the platforms", notes = "ApiURL,Serviceguid is need in header")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })

	@ResponseBody
	public ResponseEntity<JSONObject> getExistingInstances(HttpServletRequest request) throws MyException {
		JSONObject params = new JSONObject();
		params.put(ApplicationConstants.TOKEN_STRING, request.getHeader(ApplicationConstants.TOKEN_STRING));
		params.put(ApplicationConstants.API_URL, request.getHeader(ApplicationConstants.API_URL));
		params.put(ApplicationConstants.SPACEGUID, request.getHeader(ApplicationConstants.SPACEGUID));
		params.put(ApplicationConstants.PLATFORMID, request.getHeader(ApplicationConstants.PLATFORMID));
		params.put(ApplicationConstants.REGIONID, request.getHeader(ApplicationConstants.REGIONID));
		params.put(ApplicationConstants.CATEGORY_NAME, request.getHeader(ApplicationConstants.CATEGORY_NAME));
		params.put(ApplicationConstants.SPACE_NAME, request.getHeader(ApplicationConstants.SPACE_NAME));
		params.put(ApplicationConstants.LOGIN_URL, request.getHeader(ApplicationConstants.LOGIN_URL));
		JSONObject response = apfactory.getExistingInstances(params);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}
	/**
	 * This API will return the required service plan guid of microservice.
	 * 
	 * @return
	 * @throws MyException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ApplicationConstants.GET_DOMAINSERVICEPLANGUID, method = RequestMethod.GET, headers = ApplicationConstants.REQUEST_HEADERS)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get the platforms", notes = "ApiURL,Serviceguid is need in header")
	@ApiImplicitParams({
		@ApiImplicitParam(name = ApplicationConstants.TOKEN_STRING, value = "Login Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "baseasdasd"),
		@ApiImplicitParam(name = ApplicationConstants.API_URL, value = "Api end point of the platform", required = true, dataType = "string", paramType = "header", defaultValue = "localhost:8080/"),
		@ApiImplicitParam(name = ApplicationConstants.SPACEGUID, value = "Selected space's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa"),
		@ApiImplicitParam(name = ApplicationConstants.PLATFORMID, value = "Selected platform's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa"),
		@ApiImplicitParam(name = ApplicationConstants.REGIONID, value = "Selected region's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa"),
		@ApiImplicitParam(name = ApplicationConstants.CATEGORY_NAME, value = "Selected category's guid", required = true, dataType = "string", paramType = "header", defaultValue = "asdasdasdadasdsadsa")
	})
	@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
			@ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED),
			@ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })

	@ResponseBody
	public ResponseEntity<JSONObject> getDomainServiceplan(HttpServletRequest request) throws MyException {
		JSONObject params = new JSONObject();
		params.put(ApplicationConstants.TOKEN_STRING, request.getHeader(ApplicationConstants.TOKEN_STRING));
		params.put(ApplicationConstants.API_URL, request.getHeader(ApplicationConstants.API_URL));
		params.put(ApplicationConstants.SPACEGUID, request.getHeader(ApplicationConstants.SPACEGUID));
		params.put(ApplicationConstants.PLATFORMID, request.getHeader(ApplicationConstants.PLATFORMID));
		params.put(ApplicationConstants.REGIONID, request.getHeader(ApplicationConstants.REGIONID));
		params.put(ApplicationConstants.CATEGORY_NAME, request.getHeader(ApplicationConstants.CATEGORY_NAME));
		params.put(ApplicationConstants.SPACE_NAME, request.getHeader(ApplicationConstants.SPACE_NAME));
		params.put(ApplicationConstants.LOGIN_URL, request.getHeader(ApplicationConstants.LOGIN_URL));
		JSONObject response = apfactory.getDomainServiceplan(params);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<JSONObject>(response, httpHeaders, HttpStatus.OK);

	}

}
