package com.datamanagement.vo;

public class EnvironmentJsonVO {

	private StreamDefinitionVO stream_definition;
	
	private DataSchemaVO data_schema;

	public StreamDefinitionVO getStream_definition() {
		return stream_definition;
	}

	public void setStream_definition(StreamDefinitionVO stream_definition) {
		this.stream_definition = stream_definition;
	}

	public DataSchemaVO getData_schema() {
		return data_schema;
	}

	public void setData_schema(DataSchemaVO data_schema) {
		this.data_schema = data_schema;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data_schema == null) ? 0 : data_schema.hashCode());
		result = prime * result + ((stream_definition == null) ? 0 : stream_definition.hashCode());
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
		EnvironmentJsonVO other = (EnvironmentJsonVO) obj;
		if (data_schema == null) {
			if (other.data_schema != null)
				return false;
		} else if (!data_schema.equals(other.data_schema))
			return false;
		if (stream_definition == null) {
			if (other.stream_definition != null)
				return false;
		} else if (!stream_definition.equals(other.stream_definition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EnvironmentJsonVO [stream_definition=" + stream_definition + ", data_schema=" + data_schema + "]";
	}
	
	
	
}
