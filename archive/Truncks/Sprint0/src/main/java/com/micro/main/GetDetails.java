package com.micro.main;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.simple.JSONObject;

public class GetDetails {
	Boolean loggerserv;
	String appname, platform_name, platform_user, platform_password, git_user, git_password, git_url,git_repository, service_name,git_push_user,git_push_password,git_push_url;
	public String readJson(JSONObject obj) {
		System.out.println("Parsing the input json");
		appname = (String) obj.get("appname");
		loggerserv = (Boolean) obj.get("logger");
		JSONObject platformobj = (JSONObject) obj.get("platform");
		JSONObject gitobj = (JSONObject) obj.get("git_templatecode");
		JSONObject git_push = (JSONObject) obj.get("git_startercode");
		platform_user = (String) platformobj.get("username");
		platform_password = (String) platformobj.get("password");
		platform_name = (String) platformobj.get("name");
		service_name=(String)obj.get("servicename");
		git_url = (String) gitobj.get("url");
		git_user = (String) gitobj.get("username");
		git_password = (String) gitobj.get("password");
		//git_repository = (String) gitobj.get("repository");
		git_push_user= (String) git_push.get("username");
		git_push_password= (String) git_push.get("password");
		git_push_url= (String) git_push.get("url");
		obj.get("Database");
		obj.get("authprovider");

		return appname;
	}
	

	public String getGit_push_user() {
		return git_push_user;
	}


	public void setGit_push_user(String git_push_user) {
		this.git_push_user = git_push_user;
	}


	public String getGit_push_password() {
		return git_push_password;
	}


	public void setGit_push_password(String git_push_password) {
		this.git_push_password = git_push_password;
	}


	public String getGit_push_url() {
		return git_push_url;
	}


	public void setGit_push_url(String git_push_url) {
		this.git_push_url = git_push_url;
	}


	public String getGit_repository() {
		return git_repository;
	}


	public void setGit_repository(String git_repository) {
		this.git_repository = git_repository;
	}


	public String getService_name() {
		return service_name;
	}


	public void setService_name(String service_name) {
		this.service_name = service_name;
	}


	public String getPlatform_name() {
		return platform_name;
	}

	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}

	public String getPlatform_user() {
		return platform_user;
	}

	public void setPlatform_user(String platform_user) {
		this.platform_user = platform_user;
	}

	public String getPlatform_password() {
		return platform_password;
	}

	public void setPlatform_password(String platform_password) {
		this.platform_password = platform_password;
	}

	public String getGit_user() {
		return git_user;
	}

	public void setGit_user(String git_user) {
		this.git_user = git_user;
	}

	public String getGit_password() {
		return git_password;
	}

	public void setGit_password(String git_password) {
		this.git_password = git_password;
	}

	public String getGit_url() {
		return git_url;
	}

	public void setGit_url(String git_url) {
		this.git_url = git_url;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public Boolean getLoggerserv() {
		return loggerserv;
	}

	public void setLoggerserv(Boolean loggerserv) {
		this.loggerserv = loggerserv;
	}

	public String getmicroService(JSONObject obj, String url, String path) {
		try {
			Git.cloneRepository().setDirectory(new File(path)).setURI(url).call();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;

	}

	public String updateMicroService(JSONObject obj, String url, String path) {
		return path;
	}

	public String pustoGIT(String username, String password) {

		return null;

	}

}
