package com.datamanagement.vo;

import java.util.List;

public class StreamDefinitionVO {
	
	private String stream_name;	
	
	private List<StreamAttributesVO> source;
	private List<StreamAttributesVO> processor;	
	private String stream_analytics_source;	
	private List<StreamAnalyticsValuesVO> stream_analytics;	
	private StorageVO storage;
	public String getStream_name() {
		return stream_name;
	}
	public void setStream_name(String stream_name) {
		this.stream_name = stream_name;
	}
	public List<StreamAttributesVO> getSource() {
		return source;
	}
	public void setSource(List<StreamAttributesVO> source) {
		this.source = source;
	}
	public List<StreamAttributesVO> getProcessor() {
		return processor;
	}
	public void setProcessor(List<StreamAttributesVO> processor) {
		this.processor = processor;
	}
	public String getStream_analytics_source() {
		return stream_analytics_source;
	}
	public void setStream_analytics_source(String stream_analytics_source) {
		this.stream_analytics_source = stream_analytics_source;
	}
	public List<StreamAnalyticsValuesVO> getStream_analytics() {
		return stream_analytics;
	}
	public void setStream_analytics(List<StreamAnalyticsValuesVO> stream_analytics) {
		this.stream_analytics = stream_analytics;
	}
	public StorageVO getStorage() {
		return storage;
	}
	public void setStorage(StorageVO storage) {
		this.storage = storage;
	}
	@Override
	public String toString() {
		return "StreamDefinitionVO [stream_name=" + stream_name + ", source=" + source + ", processor=" + processor
				+ ", stream_analytics_source=" + stream_analytics_source + ", stream_analytics=" + stream_analytics
				+ ", storage=" + storage + "]";
	}		
	
	
	
}
