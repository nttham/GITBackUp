package com.appmanagerserver.status;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.blueprints.AppfactoryResponse;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerservice.sendinterface.ICacheHandler;
import com.appmanagerservice.sendinterface.IGistHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Component
public class AppfactoryService {
	@Autowired
	private ExceptionUtils eUtils;	
	@Autowired
	private ICacheHandler memoryCacheHandler;
	@Autowired
	private IGistHandler gist;
	private Map<String, ICacheHandler> cacheInstanceMap = new HashMap<String, ICacheHandler>();
	/**
	 * method to hold gist file ids, to hold the cache instance map
	 */
	@PostConstruct
	public void initMethod() {
		cacheInstanceMap.put(ApplicationConstants.CACHE_KEY, memoryCacheHandler);

	}

	/**
	 * This method will return the updated status to the ui, like deployed successfully.
	 * 
	 * @param messageID
	 *            -string instance contains bluePrintId id (uniqueid /hashid)
	 * @param input
	 * @return
	 * @throws MyException
	 */
	@SuppressWarnings("unused")
	public AppfactoryResponse deployStatus(String bluePrintID) throws MyException{
		try {
			String cacheSelect = ApplicationConstants.CACHE_KEY;
			ICacheHandler cacheHandler = cacheInstanceMap.get(cacheSelect);
			return  gist.readGistContent(bluePrintID);
		} catch (Exception e) {
			throw eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, e.getMessage());
		}

	}
	/**
	 * This method will return the updated status to the appmanager from the
	 * appfactory, like deployed successfully.
	 * 
	 * @param messageID
	 *            -string instance contains bluePrintId id (uniqueid /hashid)
	 * @param input
	 * @return
	 * @throws MyException
	 */
	@SuppressWarnings("unchecked")
	public AppfactoryResponse deployProcessorStatus(String bluePrintID, String input) throws MyException{
		try {
			Gson gson = new Gson();
			Type collectionType = new TypeToken<AppfactoryResponse>() {
			}.getType();
			AppfactoryResponse appfactoryresponse = gson.fromJson(input, collectionType);
			JSONObject object = new JSONObject();
			gist.writeGistContent(bluePrintID, appfactoryresponse);
			ICacheHandler cacheHandler = cacheInstanceMap.get(ApplicationConstants.CACHE_KEY);
			if (appfactoryresponse.getCfAppURL()!=null) {
				cacheHandler.putCacheData(bluePrintID, appfactoryresponse);
			}else {
				object.put(ApplicationConstants.ERROR, ApplicationConstants.INVALID_REQUEST);
			}
			return appfactoryresponse;
		} catch (JsonSyntaxException e) {
			throw eUtils.myException(CustomErrorMessage.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
	public String downloadGist(String gistID){
		
	String url= gist.getRawUrl(gistID);
	return url;
	}
}
