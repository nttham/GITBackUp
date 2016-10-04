
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appfactory.route;

/**
 * @author 559296
 *
 */
public interface ICacheHandler {
	public Object getCacheData(String key);
	public void putCacheData(String key, Object value);
}
