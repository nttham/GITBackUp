
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author 559296
 *
 */
@Component
public class DeploymentResponseVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 710570736212190964L;
	private String cfAppURL;


	private List<Logs> logsList = new ArrayList<Logs>();

	public List<Logs> getLogsList() {
		return logsList;
	}

	public void setLogsList(List<Logs> logsList) {
		this.logsList = logsList;
	}
	public String getCfAppURL() {
		return cfAppURL;
	}

	public void setCfAppURL(String cfAppURL) {
		this.cfAppURL = cfAppURL;
	}


}
