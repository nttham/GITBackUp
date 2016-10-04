package com.datamanagement.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.datamanagement.exception.DataManagementException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VcapReader {
	
	private String uri;
	private String userName;
	private String password;
	private String jsonTemplate;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getJsonTemplate() {
		return jsonTemplate;
	}

	public void setJsonTemplate(String jsonTemplate) {
		this.jsonTemplate = jsonTemplate;
	}
	

	/**
     * Method which will be used to get the uri,userName and password from VCAPS 
     * 
      * @return
	 * @throws IOException 
     */
  //  @PostConstruct
  /*  public void getEnv() throws DataManagementException {
    
        String vcapServices = System.getenv("VCAP_SERVICES");    
        ObjectMapper mapper = new ObjectMapper();
        
    	System.out.println("VCAP_SERVICES: \n" +vcapServices );	
    
    	if( vcapServices != null) {
    		
            try { 
            	
         JsonNode vcapServicesJson = mapper.readValue(vcapServices , JsonNode.class);                
          vcapServicesJson=vcapServicesJson.get("VCAP_SERVICES");                
          JsonNode newAuthjson=vcapServicesJson.get("pmysql");          
           Iterator<JsonNode> it = newAuthjson.elements();           
                while (it.hasNext() ) {
                	JsonNode parent = it.next();
                	if(parent.get("credentials")!=null){
                		JsonNode child = parent.get("credentials");
                		setUri(child.get("uri").asText());
                		setUserName(child.get("username").asText());
                		setPassword(child.get("password").asText());
                		
                	}

                }
                System.out.println(getUri());
                System.out.println(getUserName());
                System.out.println(getPassword());                

            } catch (final Exception e) {            	
            	throw new DataManagementException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
               
            }
        } else {
            System.out.println("VCAP_SERVICES environment variable is null.");
           
        }
    	
    }*/
   
    @PostConstruct
    public void loadJsonTemplate() throws DataManagementException {
    	
    	String result = "";		 
		 ClassLoader loader = Thread.currentThread().getContextClassLoader();
		 
		 try{			 
		 result = IOUtils.toString(loader.getResourceAsStream("ClickStreamDSL.json"));
		 setJsonTemplate(result);
		 
		 }catch(Exception e){
			 throw new DataManagementException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		 }
		
		
		 
    }
    public static void convertToJson(String str, JSONObject jObject) throws JSONException {
    	JSONObject finalJson;
    	String[] result  =  str.split(Pattern.quote("."));  
    	for(String value :result){
    		
    		finalJson =(JSONObject )jObject.get(value);
    		
    		System.out.println("finalJson-----"+finalJson);
    	}
    }
 
	
}
