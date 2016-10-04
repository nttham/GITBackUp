package com.appmanagerservice.sendinterface;

import java.io.IOException;

import com.appmanagerserver.blueprints.AppfactoryResponse;

public interface IGistHandler {

	public AppfactoryResponse readGistContent(String fileName) throws IOException;
	public void writeGistContent(String fileName,AppfactoryResponse inputJson);
	public String defaultGistContent(String messageid, String input);
	public String uploadTempapp(String gistid,String sourcePath,String destination);
	public String getRawUrl(String gistID);
}
