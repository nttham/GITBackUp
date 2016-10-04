package com.appmanagerserver.gist;

import org.springframework.stereotype.Component;

import com.appmanagerservice.sendinterface.IGistHandler;
@Component
public class GistConnectorObject {

	IGistHandler gistHandler;
	
	
	public void setIGist(IGistHandler mGistHandler) {
		this.gistHandler=mGistHandler;
	}
	
	public IGistHandler getGistObj(){
		return gistHandler;
	}
}
