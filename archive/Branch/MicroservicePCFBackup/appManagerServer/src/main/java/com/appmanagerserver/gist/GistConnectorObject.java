package com.appmanagerserver.gist;

import com.appmanagerservice.sendinterface.IGistHandler;

public class GistConnectorObject {

	IGistHandler gistHandler;
	
	
	public void setIGist(IGistHandler mGistHandler) {
		this.gistHandler=mGistHandler;
	}
	
	public IGistHandler getGistObj(){
		return gistHandler;
	}
}
