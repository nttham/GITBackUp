/**
 * 
 */
package com.appmanagerserver.blueprints;

import org.springframework.stereotype.Component;

/**
 * @author 559296
 *This singleton class object can be passed in
 * every method rather than passing multiple arguments.
 */
@Component
public class ParametersBean {
private String layoutHeaderID;

public String getLayoutHeaderID() {
	return layoutHeaderID;
}

public void setLayoutHeaderID(String layoutHeaderID) {
	this.layoutHeaderID = layoutHeaderID;
}

}
