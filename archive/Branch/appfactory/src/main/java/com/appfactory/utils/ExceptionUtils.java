package com.appfactory.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExceptionUtils {
	/**
	 * @param customErrorMessage
	 * @param values
	 * @return
	 */
	public MyException myException(CustomErrorMessage customErrorMessage, List<String> values) {
		if (null == values ) {
			return new MyException(customErrorMessage.getErrorCode(),
					"",
					customErrorMessage.getStatusCode());
		} else {
			return new MyException(customErrorMessage.getErrorCode(),
					"",
					customErrorMessage.getStatusCode());
		}
	}
	/**
	 * this method get called when error accrued. 
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	public static String getErrorResponse(int errorCode, String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(errorCode);
		errorResponse.setMessage(errorMessage);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(errorResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonInString;

	}
	/**
	 * 
	 * @param customErrorMessage
	 * @param message
	 * @return
	 */
	public MyException myException(CustomErrorMessage customErrorMessage, String message) {
		if (null == message ) {
			return new MyException(customErrorMessage.getErrorCode(),
					message,
					customErrorMessage.getStatusCode());
		} else {
			return new MyException(customErrorMessage.getErrorCode(),
					message,
					customErrorMessage.getStatusCode());
		}
		
	}
}
