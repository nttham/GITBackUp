package com.appmanagerserver.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.appmanagerservice.sendinterface.ICacheHandler;

@Component
public class MemoryCacheHandler implements ICacheHandler {
	private Map<String, Object> AppFactpryCache = new HashMap<String, Object>();

	/* 
	 * method to fetch the metadata from hashmap based on key value
	 * (non-Javadoc)
	 */
	@Override
	public Object getCacheData(String key) {
			return this.AppFactpryCache.get(key);
	}
	
	/* 
	 * Method to put the cache data in the hashmap
	 * (non-Javadoc)
	 */
	@Override
	public void putCacheData(String key, Object value) {
		this.AppFactpryCache.put(key, value);
	}

}
