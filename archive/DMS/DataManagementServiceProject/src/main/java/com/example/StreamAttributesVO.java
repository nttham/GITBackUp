package com.example;

import java.util.List;

public class StreamAttributesVO {
	private String type;
	private List<StreamAttributesValueVO> attributes;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<StreamAttributesValueVO> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<StreamAttributesValueVO> attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		StreamAttributesVO other = (StreamAttributesVO) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StreamAttributesVO [type=" + type + ", attributes=" + attributes + "]";
	}
	
	
}
