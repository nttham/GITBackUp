package com.appfactory.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * @author rudhrapriya
 *
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment_Json implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3407034876306848847L;
	private Logger logger;
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private Configuration configuration;
	private Channels channels;
	private PreHooks prehooks;
	private List<Object> templates;
	
	
	public PreHooks getPrehooks() {
		return prehooks;
	}

	public void setPrehooks(PreHooks prehooks) {
		this.prehooks = prehooks;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setChannels(Channels channels) {
		this.channels = channels;
	}

	private PostHooks posthooks;
	public PostHooks getPosthooks() {
		return posthooks;
	}

	public void setPosthooks(PostHooks posthooks) {
		this.posthooks = posthooks;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public Channels getChannels() {
		return channels;
	}

	public List<Object> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Object> templates) {
		this.templates = templates;
	}

	
	
	

//	public boolean validateENVJson(){
//		
//		if(getmChannels()!=null)
//			if(getmConfiguration()!=null)
//			if( getmPrehooks()!=null)
//				if(getmPosthooks()!=null){
//					return true;
//				}
//		return false;
//		
//	}
	
//	private boolean checkPostHooks(){
//		
//		if(this.get)
//		return false;
//	}
//	
//	private boolean checkPreHooks(){
//		
//		return false;
//	}
//	private boolean checkConfiguration(){
//		
//		return false;
//	}

}
