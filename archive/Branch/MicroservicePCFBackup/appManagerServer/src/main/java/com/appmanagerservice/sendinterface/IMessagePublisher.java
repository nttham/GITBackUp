
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerservice.sendinterface;

import org.json.simple.JSONObject;

import com.appmanagerserver.exception.MyException;

/**
 * @author 559296
 *
 */
public interface IMessagePublisher {
	public String publishMessage(JSONObject blueprint) throws MyException;
}
