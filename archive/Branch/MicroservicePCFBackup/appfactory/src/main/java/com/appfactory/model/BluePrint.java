package com.appfactory.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * File : BluePrint.java Description : This class is 
 * having all the blueprints :
 *
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class BluePrint implements Serializable {
	
	/**
	 * Value objects.
	 *
	 */
	private static final long serialVersionUID = -9068557358794765065L;
		private String servicename,platform,accesstoken,spaceguid,devlopergiturl,devlopergitusername,devlopergitpassword,orgguid,serviceplanguid;
		private ArrayList<OtherServices> otherservices;
		private PrimaryService primaryservice;
		private Logger logger;
		private String runningapp,gitpath;
		private String boundtype,gistid;




		public String getGistid() {
			return gistid;
		}
		public void setGistid(String gistid) {
			this.gistid = gistid;
		}
		public Logger getLogger() {
			return logger;
		}
		public void setLogger(Logger logger) {
			this.logger = logger;
		}
		public ArrayList<OtherServices> getOtherservices() {
			return otherservices;
		}
		public void setOtherservices(ArrayList<OtherServices> otherservices) {
			this.otherservices = otherservices;
		}
		public String getOrgguid() {
			return orgguid;
		}
		public void setOrgguid(String orgguid) {
			this.orgguid = orgguid;
		}
		public String getServicename() {
			return servicename;
		}
		public void setServicename(String servicename) {
			this.servicename = servicename;
		}
		public String getPlatform() {
			return platform;
		}
		public void setPlatform(String platform) {
			this.platform = platform;
		}
		public String getAccesstoken() {
			return accesstoken;
		}
		public void setAccesstoken(String accesstoken) {
			this.accesstoken = accesstoken;
		}
		public String getSpaceguid() {
			return spaceguid;
		}

		public String getServiceplanguid() {
			return serviceplanguid;
		}
		public void setServiceplanguid(String serviceplanguid) {
			this.serviceplanguid = serviceplanguid;
		}
		public void setSpaceguid(String spaceguid) {
			this.spaceguid = spaceguid;
		}
		public String getDevlopergiturl() {
			return devlopergiturl;
		}
		public void setDevlopergiturl(String devlopergiturl) {
			this.devlopergiturl = devlopergiturl;
		}
		public String getDevlopergitusername() {
			return devlopergitusername;
		}
		public void setDevlopergitusername(String devlopergitusername) {
			this.devlopergitusername = devlopergitusername;
		}
		public String getDevlopergitpassword() {
			return devlopergitpassword;
		}
		public void setDevlopergitpassword(String devlopergitpassword) {
			this.devlopergitpassword = devlopergitpassword;
		}
		public PrimaryService getPrimaryservice() {
			return primaryservice;
		}
		public void setPrimaryservice(PrimaryService primaryservice) {
			this.primaryservice = primaryservice;
		}
		public String getRunningapp() {
			return runningapp;
		}
		public void setRunningapp(String runningapp) {
			this.runningapp = runningapp;
		}
		public String getGitpath() {
			return gitpath;
		}
		public void setGitpath(String gitpath) {
			this.gitpath = gitpath;
		}
	       public String getBoundtype() {
               return boundtype;
        }
        public void setBoundtype(String boundtype) {
               this.boundtype = boundtype;
        }
	
}
