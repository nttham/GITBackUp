package com.appManager.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appManager.constants.ApplicationConstants;
import com.appManager.utils.ExceptionUtils;

/**
 * 
 * @author 559296
 *
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class MyExceptionHandler {
	/**
	 * Exception handler for Custom Exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ MyException.class })
	public ResponseEntity<String> handleCustomException(final MyException exception) {
		return new ResponseEntity<String>(ExceptionUtils.getErrorResponse(exception.getErrorCode(), exception.getMessage()), null,
				HttpStatus.valueOf(exception.getStatusCode()));
	}
	
	/**
	 * Exception handler for system exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public ResponseEntity<String> handleGeneralException(final Exception exception) {
	
		return new ResponseEntity<String>(ExceptionUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ApplicationConstants.INTERNAL_SERVER_ERROR), null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
