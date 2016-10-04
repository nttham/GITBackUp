package com.datamanagement.vo;

public class StreamAnalyticsValuesVO {
	
	private String name;
	 private String field_name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field_name == null) ? 0 : field_name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		StreamAnalyticsValuesVO other = (StreamAnalyticsValuesVO) obj;
		if (field_name == null) {
			if (other.field_name != null)
				return false;
		} else if (!field_name.equals(other.field_name))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "StreamAnalyticsValuesVO [name=" + name + ", field_name=" + field_name + "]";
	}
	 
	
}
