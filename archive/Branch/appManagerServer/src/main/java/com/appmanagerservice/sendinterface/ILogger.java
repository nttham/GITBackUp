/**
 * 
 */
package com.appmanagerservice.sendinterface;

/**
 * @author 559296
 * Description : Interface file will be used for all logger implementation class.
* Revision History :
* Version      Date            Author       Reason
* 0.1          09-Jun-2016     559296  Initial version
 */
public interface ILogger {
	public void debug(String msg);

	public void info(String msg);

	public void error(String msg);
}
