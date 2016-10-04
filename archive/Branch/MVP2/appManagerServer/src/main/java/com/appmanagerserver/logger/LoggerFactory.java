/**
 * 
 */
package com.appmanagerserver.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.messages.Messages;
import com.appmanagerservice.sendinterface.ILogger;

/**
 * @author 559296
 *
 */
@Component
public class LoggerFactory {
	/**
	 * method to choose the logger type based on the property value
	 * @return
	 */

	@Autowired
	private ILogger fileAppenderLog;
	
	@Autowired
	private ILogger grayLogAppenderLog;
	
	/**
	 * 
	 * @return
	 */
	public ILogger getLoggerInstance() {
		String loggerMode = Messages.getString(ApplicationConstants.LOGGER_SELECT);
		if (loggerMode.equalsIgnoreCase(ApplicationConstants.FILE)) {
			return fileAppenderLog;
		} 
		
		else if (loggerMode.equalsIgnoreCase(ApplicationConstants.GRAYLOG)) {
			return grayLogAppenderLog;
		}
		
		else {
			return null;
		}
	}
}
