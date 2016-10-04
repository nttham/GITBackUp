package hello;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VcapReader {
	
	private String apiKey;
	private String serviceUrl;
	
	@Autowired
	private PropertyFileReader propertyFileReader;

	 /**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}



	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}



	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}



	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}



	/**
     * Method which will be used to get the apikey and service Url from VCAPS 
     * 
      * @return
	 * @throws IOException 
     */
    @PostConstruct
    public void getEnv() {
    	
    	System.out.println("inside getEnv");
    	/*StringBuffer buffer = null;    	
    	try{
    	BufferedReader	br = new BufferedReader(new FileReader("C:\\test\\Sample.json"));
    	 buffer = new StringBuffer();
    	
    	 while(true){
    		 final String line = br.readLine();
				if (line == null)
					break;
				buffer.append(line);
    	 }
    
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	*/
    	String vcapServices = System.getenv("VCAP_SERVICES");
    	System.out.println("vcap data---"+vcapServices);
        ObjectMapper mapper = new ObjectMapper();

    	System.out.println("VCAP_SERVICES: \n" +vcapServices);   	
    	 	
    	
    	if (vcapServices != null) {
            try {
                // Parse VCAP_SERVICES into Jackson JsonNode, then map the
                // 'new oauth' entry
                // to an instance of NewAuthEvironment.            	
            	
                JsonNode vcapServicesJson = mapper.readValue(vcapServices, JsonNode.class);
                //vcapServicesJson=vcapServicesJson.get(propertyFileReader.getMessage("facebook.vcapservices"));
                
                JsonNode newAuthjson=vcapServicesJson.get(propertyFileReader.getMessage("facebook.serviceName"));
                
                Iterator<JsonNode> it = newAuthjson.elements();
                // Find the newoauth service bound to this application.
                while (it.hasNext() ) {
                	JsonNode parent = it.next();
                	if(parent.get("credentials")!=null){
                		JsonNode child = parent.get("credentials");
                		setApiKey(child.get("apiKey").asText());
                        setServiceUrl(child.get("serviceUrl").asText());
                		
                	}

                }
                System.out.println(getApiKey());
                System.out.println(getServiceUrl());


            } catch (final Exception e) {
            	
                e.printStackTrace();
               
            }
        } else {
            System.out.println("VCAP_SERVICES environment variable is null.");
           
        }
    	
    }
   
	
}
