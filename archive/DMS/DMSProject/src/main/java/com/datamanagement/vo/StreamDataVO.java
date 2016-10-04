package com.datamanagement.vo;

public class StreamDataVO {
	
	
	private String servicename;
	private String platform;
	private String boundtype;
	private String category;
	private String spaceguid;
	private String serviceplanguid;
	private String accesstoken;
	private String devlopergiturl;
	private String devlopergitusername;
	private String devlopergitpassword;
	
	private EnvironmentJsonVO environment_json;

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

	public String getBoundtype() {
		return boundtype;
	}

	public void setBoundtype(String boundtype) {
		this.boundtype = boundtype;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSpaceguid() {
		return spaceguid;
	}

	public void setSpaceguid(String spaceguid) {
		this.spaceguid = spaceguid;
	}

	public String getServiceplanguid() {
		return serviceplanguid;
	}

	public void setServiceplanguid(String serviceplanguid) {
		this.serviceplanguid = serviceplanguid;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
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

	public EnvironmentJsonVO getEnvironment_json() {
		return environment_json;
	}

	public void setEnvironment_json(EnvironmentJsonVO environment_json) {
		this.environment_json = environment_json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accesstoken == null) ? 0 : accesstoken.hashCode());
		result = prime * result + ((boundtype == null) ? 0 : boundtype.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((devlopergitpassword == null) ? 0 : devlopergitpassword.hashCode());
		result = prime * result + ((devlopergiturl == null) ? 0 : devlopergiturl.hashCode());
		result = prime * result + ((devlopergitusername == null) ? 0 : devlopergitusername.hashCode());
		result = prime * result + ((environment_json == null) ? 0 : environment_json.hashCode());
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
		result = prime * result + ((servicename == null) ? 0 : servicename.hashCode());
		result = prime * result + ((serviceplanguid == null) ? 0 : serviceplanguid.hashCode());
		result = prime * result + ((spaceguid == null) ? 0 : spaceguid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreamDataVO other = (StreamDataVO) obj;
		if (accesstoken == null) {
			if (other.accesstoken != null)
				return false;
		} else if (!accesstoken.equals(other.accesstoken))
			return false;
		if (boundtype == null) {
			if (other.boundtype != null)
				return false;
		} else if (!boundtype.equals(other.boundtype))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (devlopergitpassword == null) {
			if (other.devlopergitpassword != null)
				return false;
		} else if (!devlopergitpassword.equals(other.devlopergitpassword))
			return false;
		if (devlopergiturl == null) {
			if (other.devlopergiturl != null)
				return false;
		} else if (!devlopergiturl.equals(other.devlopergiturl))
			return false;
		if (devlopergitusername == null) {
			if (other.devlopergitusername != null)
				return false;
		} else if (!devlopergitusername.equals(other.devlopergitusername))
			return false;
		if (environment_json == null) {
			if (other.environment_json != null)
				return false;
		} else if (!environment_json.equals(other.environment_json))
			return false;
		if (platform == null) {
			if (other.platform != null)
				return false;
		} else if (!platform.equals(other.platform))
			return false;
		if (servicename == null) {
			if (other.servicename != null)
				return false;
		} else if (!servicename.equals(other.servicename))
			return false;
		if (serviceplanguid == null) {
			if (other.serviceplanguid != null)
				return false;
		} else if (!serviceplanguid.equals(other.serviceplanguid))
			return false;
		if (spaceguid == null) {
			if (other.spaceguid != null)
				return false;
		} else if (!spaceguid.equals(other.spaceguid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StreamData [servicename=" + servicename + ", platform=" + platform + ", boundtype=" + boundtype
				+ ", category=" + category + ", spaceguid=" + spaceguid + ", serviceplanguid=" + serviceplanguid
				+ ", accesstoken=" + accesstoken + ", devlopergiturl=" + devlopergiturl + ", devlopergitusername="
				+ devlopergitusername + ", devlopergitpassword=" + devlopergitpassword + ", environment_json="
				+ environment_json + "]";
	}
	
	
	
	

}
