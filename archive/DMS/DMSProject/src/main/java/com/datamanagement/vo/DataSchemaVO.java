package com.datamanagement.vo;

import java.util.List;

public class DataSchemaVO {
	
	private List<DataSchemaValuesVO> data_schema;

	public List<DataSchemaValuesVO> getData_schema() {
		return data_schema;
	}

	public void setData_schema(List<DataSchemaValuesVO> data_schema) {
		this.data_schema = data_schema;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data_schema == null) ? 0 : data_schema.hashCode());
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
		DataSchemaVO other = (DataSchemaVO) obj;
		if (data_schema == null) {
			if (other.data_schema != null)
				return false;
		} else if (!data_schema.equals(other.data_schema))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataSchemaVO [data_schema=" + data_schema + "]";
	}
	
	

}
