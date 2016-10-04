
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       27-June-2016	 559296  Initial version
 */

package com.appmanagerserver.producer;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.blueprints.AppfactoryResponse;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.status.AppfactoryService;
import com.appmanagerservice.sendinterface.IMessagePublisher;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * @author 559296
 *
 */
@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class MessageSender {
	@Autowired
	private IMessagePublisher messagePublisher;
	@Autowired
	private AppfactoryService factoryservice;
		
		
	/**
	 * 
	 * @param blueprint
	 * @return
	 * @throws MyException
	 */
		@SuppressWarnings("unchecked")
		@RequestMapping(value = ApplicationConstants.DEPLOYEMENT, method = RequestMethod.POST,headers=ApplicationConstants.REQUEST_HEADERS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
		         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
		         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
		public  ResponseEntity<JSONObject> appFactory(@RequestBody JSONObject blueprint) throws MyException {
			String requestUUID =messagePublisher.publishMessage(blueprint);
			final JSONObject responseobj = new JSONObject();
			responseobj.put("key", requestUUID);
			return new ResponseEntity<JSONObject>(responseobj, HttpStatus.OK);
			
		}
		/**
		 * This API will provide the details about the deployment status. appfactory will
		 * call this API.
		 * @return
		 * @throws MyException
		 */
		@RequestMapping(value = ApplicationConstants.SAVE_STATUS+"/{messageId}/{gistId}", method = RequestMethod.POST,headers=ApplicationConstants.REQUEST_HEADERS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
		         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
		         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
		public  ResponseEntity<AppfactoryResponse> saveStatus( @PathVariable("messageId") String messageId,@RequestBody String statusmsg,@PathVariable("gistId") String gistId) throws MyException {
		
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<AppfactoryResponse>(factoryservice.deployProcessorStatus(gistId,statusmsg),httpHeaders,HttpStatus.OK);
			
		}
		/**
		 * This API will provide the details about the deployment status. UI will
		 * call this API.
		 * @return
		 * @throws MyException
		 */
		@RequestMapping(value = ApplicationConstants.SAVE_STATUS+"/{messageId}", method = RequestMethod.GET,headers=ApplicationConstants.REQUEST_HEADERS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
		         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
		         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
		 @ApiOperation(value = "Get the status", notes = "Message key in parameter")
		  @ApiImplicitParams({
		        @ApiImplicitParam(name = "messageId", value = "Message id received form appfactory", required = true, dataType = "string", paramType = "queryparameter", defaultValue="baseasdasd")

		  })
		public  ResponseEntity<AppfactoryResponse> getStatus( @PathVariable("messageId") String messageId) throws MyException {
			AppfactoryResponse aresponse=factoryservice.deployStatus(messageId);
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);	
			if(aresponse.getCfAppURL().equalsIgnoreCase("NOT_MODIFIED")){
				return new ResponseEntity<AppfactoryResponse>(aresponse,httpHeaders,HttpStatus.NOT_MODIFIED);
			}
			else{
			return new ResponseEntity<AppfactoryResponse>(aresponse,httpHeaders,HttpStatus.OK);
			}
			
		}
		/**
		 * This API will provide the download url of gist
		 * @return
		 * @throws MyException
		 */
		@RequestMapping(value = ApplicationConstants.DOWNLOAD+"/{messageId}", method = RequestMethod.GET,headers=ApplicationConstants.REQUEST_HEADERS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = ApplicationConstants.SUCCESS, response = Result.class),
		         @ApiResponse(code = 401, message = ApplicationConstants.UNAUTHORIZED), 
		         @ApiResponse(code = 500, message = ApplicationConstants.FAILURE) })
		public void downloadGist( @PathVariable("messageId") String messageId,HttpServletResponse response) throws MyException {
			String rawUrl =factoryservice.downloadGist(messageId);
			try {
				response.sendRedirect(rawUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
}
