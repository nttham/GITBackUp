
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appfactory.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.appfactory.route.ICacheHandler;

/**
 * @author 559296
 *
 */
@Component
public class MemoryCacheHandler implements ICacheHandler {
	private Map<String, Object> appfactoryCache = new HashMap<String, Object>();
	@Override
	public Object getCacheData(String key) {
		return this.appfactoryCache.get("key");
	}

	@Override
	public void putCacheData(String key, Object value) {
	
		this.appfactoryCache.put(key, value);
	}

}
