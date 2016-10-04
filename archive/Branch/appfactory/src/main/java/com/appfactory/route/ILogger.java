package com.appfactory.route;
/**
 * File            : ILogger.java
* Description      : Interface file will be used for all logger implementation class.
* Revision History :
 * @author 559296
 *
 */
public interface ILogger {
	public void debug(String msg);

	public void info(String msg);

	public void error(String msg);
}
