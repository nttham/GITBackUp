package com.example;

public class StreamDataVO {
	private String streamname;
	private String tapstreamname;
	private String deploy;
	private StreamAttributesVO sink;
	private StreamAttributesVO source;
	private StreamAttributesVO processor;

	public String getStreamname() {
		return streamname;
	}

	public void setStreamname(String streamname) {
		this.streamname = streamname;
	}

	public String getTapstreamname() {
		return tapstreamname;
	}

	public void setTapstreamname(String tapstreamname) {
		this.tapstreamname = tapstreamname;
	}

	public String getDeploy() {
		return deploy;
	}

	public void setDeploy(String deploy) {
		this.deploy = deploy;
	}

	public StreamAttributesVO getSink() {
		return sink;
	}

	public void setSink(StreamAttributesVO sink) {
		this.sink = sink;
	}

	public StreamAttributesVO getSource() {
		return source;
	}

	public void setSource(StreamAttributesVO source) {
		this.source = source;
	}

	public StreamAttributesVO getProcessor() {
		return processor;
	}

	public void setProcessor(StreamAttributesVO processor) {
		this.processor = processor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deploy == null) ? 0 : deploy.hashCode());
		result = prime * result + ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + ((sink == null) ? 0 : sink.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((streamname == null) ? 0 : streamname.hashCode());
		result = prime * result + ((tapstreamname == null) ? 0 : tapstreamname.hashCode());
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
		if (deploy == null) {
			if (other.deploy != null)
				return false;
		} else if (!deploy.equals(other.deploy))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (sink == null) {
			if (other.sink != null)
				return false;
		} else if (!sink.equals(other.sink))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (streamname == null) {
			if (other.streamname != null)
				return false;
		} else if (!streamname.equals(other.streamname))
			return false;
		if (tapstreamname == null) {
			if (other.tapstreamname != null)
				return false;
		} else if (!tapstreamname.equals(other.tapstreamname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StreamDataVO [streamname=" + streamname + ", tapstreamname=" + tapstreamname + ", deploy=" + deploy
				+ ", sink=" + sink + ", source=" + source + ", processor=" + processor + "]";
	}
	
	
}
