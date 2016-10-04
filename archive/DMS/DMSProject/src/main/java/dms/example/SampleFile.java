package dms.example;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import dms.example.ApplicationConstants;
import com.google.gson.Gson;



@Component
public class SampleFile {
	
	
	StringBuffer buffer = null;
	JSONObject jObject =null;
	
	
	
	Map<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
	
	/**
	 * method to find the ID key and add the ID value in to the task list  
	 * @param obj
	 * @param jobList
	 */
	
	public EnvironmentJsonVO iterateJSON(JSONObject obj) {
	
		List<DataSchemaValuesVO> data_schema = new ArrayList<DataSchemaValuesVO>();
		DataSchemaValuesVO dataSchemaValuesVO = new DataSchemaValuesVO();
		DataSchemaVO dataSchemaVO  = new DataSchemaVO();
		EnvironmentJsonVO environmentJsonVO = new EnvironmentJsonVO();
	
		for (Object key : obj.keySet()) {			
			if(key.equals("environment_json")){				
			System.out.println("Inside environment_json ");	
			JSONObject jsonObject = (JSONObject)obj.get(key);			
			JSONObject jsonstreamDef = (JSONObject)jsonObject.get("stream_definition");			
			Gson gson = new Gson();
			StreamDefinitionVO streamDefinitionVO = gson.fromJson(jsonstreamDef.toString(), StreamDefinitionVO.class);			
			JSONArray jsonDataSchema = (JSONArray)jsonObject.get("data_schema");
						
			 for(int i = 0 ; i < jsonDataSchema.size() ; i++){
			       JSONObject jsonData = (JSONObject)jsonDataSchema.get(i);			      
			       dataSchemaValuesVO =gson.fromJson(jsonData.toString(), DataSchemaValuesVO.class);
			       data_schema.add(dataSchemaValuesVO);	    
			    }			
			 dataSchemaVO.setData_schema(data_schema);
			 environmentJsonVO.setData_schema(dataSchemaVO);
			 environmentJsonVO.setStream_definition(streamDefinitionVO);	
			}
		  }
	
		return environmentJsonVO;
	}
	
	
	public static void main(String[] args) throws ParseException,JSONException {
		SampleFile assemble = new SampleFile();
	//	String content = "{ \"appDetails\": { \"appname\": \"9testcups825\", \"appType\": \"reference\" }, \"paasDetails\": { \"access_token\": \"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0MDk4ZWM2MS02NTQ2LTRjOGItYjEyMi1jMGIyZGUwM2QxY2IiLCJzdWIiOiI0ZGNmOGVjYi1mMzQxLTQ1MGUtYmE4MS00Y2E0ZjVhOTJmOTYiLCJzY29wZSI6WyJvcGVuaWQiLCJjbG91ZF9jb250cm9sbGVyLnJlYWQiLCJwYXNzd29yZC53cml0ZSIsImNsb3VkX2NvbnRyb2xsZXIud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6IjRkY2Y4ZWNiLWYzNDEtNDUwZS1iYTgxLTRjYTRmNWE5MmY5NiIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6Imt1bWFyLmNoZW5uYWl5YW5AY29nbml6YW50LmNvbSIsImVtYWlsIjoia3VtYXIuY2hlbm5haXlhbkBjb2duaXphbnQuY29tIiwiYXV0aF90aW1lIjoxNDcyMTE5OTU1LCJyZXZfc2lnIjoiNWYwNjMwOSIsImlhdCI6MTQ3MjExOTk1NSwiZXhwIjoxNDczMzI5NTU1LCJpc3MiOiJodHRwczovL3VhYS5uZy5ibHVlbWl4Lm5ldC9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsImNsb3VkX2NvbnRyb2xsZXIiLCJwYXNzd29yZCJdfQ.kj2XgbFm5WFxMyojVs6tmUApJJLtEWKc6MdkeWUcpJs\", \"orgGuid\": \"d17569d7-adaa-4b90-878c-9bcfd32d3f7c\", \"spaceGuid\": \"dce1c864-3b17-44e2-9b75-d56b5f1de766\" }, \"gitDetails\": { \"url\": \"\", \"userName\": \"\" }, \"environmentVariables\": {  }, \"deploy\": [ { \"type\": \"framework\", \"id\": \"2246442410bb4f38a6be5efa6021e4bd\", \"values\": { \"name\": \"custom\", \"version\": \"\" }, \"selections\": [  ] }, { \"type\": \"services\", \"selections\": [ { \"id\": \"627f1f5951fe458aadf4125b7b2f9ac4\", \"name\": \"appservers\", \"selection\": [ { \"id\": \"8da96efdf4d744d8b9d5cf3daaa7363c\", \"values\": { \"name\": \"node\" } } ] }, { \"id\": \"70a16d8e7ed44e46badfdce9934c54cc\", \"name\": \"frontendframeworks\", \"selection\": [ { \"id\": \"38a08c18726d4fa19e105246a5417b23\", \"values\": { \"name\": \"angularjs\" } } ] }, { \"id\": \"0e03267222d74779adfc733fbe21b5da\", \"name\": \"dataservices\", \"selection\": [ { \"id\": \"c78c020b-0b12-430a-b5ad-3f2c89e17e69\", \"values\": { \"hostname\": \"hn\", \"name\": \"mongodb\", \"type\": \"external\", \"portNo\": \"1234\", \"dbName\": \"db\", \"username\": \"un\", \"drivers\": { \"id\": \"f3ef644df33e47f48ce76289d508c311\", \"name\": \"Mongoose\" } } } ] }, { \"id\": \"53726baf01914c5fa18a537fb8531d15\", \"name\": \"agents\", \"selection\": [ { \"id\": \"8ec77cdfe88747eb87602cd16f1fa9ba\", \"values\": { \"name\": \"twilio\", \"type\": \"external\", \"accountSID\": \"ACde11cb3a4cf41d80a2cf910b8df80d47\", \"authToken\": \"052a1123d321ab0b3b8a5747c3cf867d\" } }, { \"id\": \"6731bbf665274d51b394099e6274ef31\", \"values\": { \"name\": \"cfbot\", \"type\": \"external\", \"slack_token\": \"xoxb-70527231591-mdha5uf2zgWfSeB1G9UYybpv\", \"cf_api\": \"https://api.run.pivotal.io\", \"cf_username\": \"kumar.chennaiyan@cognizant.com\", \"cf_password\": \" \" } }, { \"id\": \"b0cf1d43304943609f4649e1c3a52275\", \"values\": { \"name\": \"sendgrid\", \"label\": \"sendgrid\", \"type\": \"external\", \"credentials\": { \"password\": \"oQzAjU6duFTX7318\", \"hostname\": \"smtp.sendgrid.net\", \"username\": \"EshREHuDWX\" } } } ] } ] } ] } ";
		
		
		StringBuffer buffer = null;    	
    	try{
    	BufferedReader	br = new BufferedReader(new FileReader("C:\\test\\DMS_ModelJson.json"));
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
	
    	try{
    		
    	boolean flag= isJSONValid(buffer.toString());
    		
        System.out.println("Validation flag "+flag);
    		
    	if(flag){
    		
		JSONParser jsonParser =new JSONParser();
		JSONObject object = (JSONObject) jsonParser.parse(buffer.toString());
		
		String stream_name =(String)object.get("servicename");
		System.out.println("stream_name---------->"+stream_name);
		
		EnvironmentJsonVO environmentJsonVO=assemble.iterateJSON(object);
		
		System.out.println("environmentJsonVO----------->"+environmentJsonVO);
		
		getSource(environmentJsonVO);
		getProcessor(environmentJsonVO);
		getStreamAnalytics(environmentJsonVO);		
		getStorage(environmentJsonVO);
		getSchemaData(environmentJsonVO);
		
		StringBuffer addspaceBuffer= new StringBuffer();
		addspaceBuffer.append(ApplicationConstants.SPACE);
		addspaceBuffer.append(ApplicationConstants.TAP);
		addspaceBuffer.append(ApplicationConstants.SPACE);
		
		String addstring=addspaceBuffer.toString();		
		//String input = sourceResp+ addstring+processorResp+addstring+sinkResp;
		
		//System.out.println("input------------------>"+input);
    	}
	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		
	}

	
	private static void getSource(EnvironmentJsonVO environmentJsonVO){
		
		StreamDefinitionVO streamDefinitionVO =  environmentJsonVO.getStream_definition();		
		List<StreamAttributesVO> sourceList = streamDefinitionVO.getSource() ;		
		String sourceData = create(sourceList);
		System.out.println("sourceData ------->"+sourceData);
	}
	
	private static void getProcessor(EnvironmentJsonVO environmentJsonVO){
		
		StreamDefinitionVO streamDefinitionVO =  environmentJsonVO.getStream_definition();
		List<StreamAttributesVO> processorList = streamDefinitionVO.getProcessor() ;	
		String processorData = create(processorList);
			
		System.out.println("processorData------>"+processorData);
	
		}
		
	private static void getStreamAnalytics(EnvironmentJsonVO environmentJsonVO){
		
		StreamDefinitionVO streamDefinitionVO =  environmentJsonVO.getStream_definition();
		List<StreamAnalyticsValuesVO> streamAnalyticsList = streamDefinitionVO.getStream_analytics();
		 StringBuffer streamAnalyticsdata = new StringBuffer();		
		for( StreamAnalyticsValuesVO  streamAnalyticsValueVO :streamAnalyticsList){			
			streamAnalyticsdata.append(streamAnalyticsValueVO.getName());
			streamAnalyticsdata.append(streamAnalyticsValueVO.getType());
		   }
		System.out.println("streamAnalyticsdata------>"+streamAnalyticsdata); 
	    }
	
	private static void getStorage(EnvironmentJsonVO environmentJsonVO) {

		StreamDefinitionVO streamDefinitionVO = environmentJsonVO.getStream_definition();
		StorageVO storageVO = streamDefinitionVO.getStorage();

		System.out.println("storageVO ----->" + storageVO);

	}
	
	private static void getSchemaData(EnvironmentJsonVO environmentJsonVO) {

		DataSchemaVO  dataSchemaVO = environmentJsonVO.getData_schema();		
		List<DataSchemaValuesVO> dataSchemaVOList = dataSchemaVO.getData_schema();	
		
		StringBuffer schemaData = new StringBuffer();
		for (DataSchemaValuesVO dataSchemaValuesVO : dataSchemaVOList) {
			schemaData.append(dataSchemaValuesVO.getKey());
			schemaData.append(dataSchemaValuesVO.getDescription());
			
		}
		System.out.println("schemaData------>" + schemaData);	
	}
    
	private static String create(List<StreamAttributesVO> streamAttributeList ){
		
		    StringBuffer commonformat=new StringBuffer();		    
			for(StreamAttributesVO sourceattributes : streamAttributeList){					
				String sourceType= sourceattributes.getType();
				List<AttributesValueVO> sourattribs =sourceattributes.getAttributes();
				
				commonformat.append(sourceType);
				commonformat.append(ApplicationConstants.SPACE);
				commonformat.append(ApplicationConstants.HYPENS);
			
				
				for( AttributesValueVO  attributesValueVO :sourattribs){					
					commonformat.append(attributesValueVO.getKey());
					commonformat.append(attributesValueVO.getValue());
				}
						
			}
			System.out.println("commonformat----->"+commonformat);
			return commonformat.toString();
		}
	
	 private static boolean isJSONValid(String jsonInString) {		 
		 Gson gson = new Gson();
	      try {
	          gson.fromJson(jsonInString, Object.class);
	          return true;
	      } catch(com.google.gson.JsonSyntaxException ex) { 
	          return false;
	      }
	  }
	
}
