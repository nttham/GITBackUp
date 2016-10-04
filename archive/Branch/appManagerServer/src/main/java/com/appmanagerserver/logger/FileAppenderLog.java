/**
 * 
 */
package com.appmanagerserver.logger;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.appmanagerservice.sendinterface.ILogger;

/**
 * @author 559296
 *
 */
@Component
public class FileAppenderLog implements ILogger {
	private Logger logger;

	/**
	 * 
	 */
	public FileAppenderLog() {
		logger = LoggerFactory.getLogger(getClass());
	}

	/* (non-Javadoc)
	 * @see com.cognizant.starterkit.logger.ILogger#debug(java.lang.String)
	 */
	@Override
	public void debug(String msg) {
		this.logger.debug(msg);
	}

	/* (non-Javadoc)
	 * @see com.cognizant.starterkit.logger.ILogger#info(java.lang.String)
	 */
	@Override
	public void info(String msg) {
		this.logger.info(msg);

	}

	/* (non-Javadoc)
	 * @see com.cognizant.starterkit.logger.ILogger#error(java.lang.String)
	 */
	@Override
	public void error(String msg) {
		this.logger.error(msg);

	}
}
