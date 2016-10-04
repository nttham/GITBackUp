package com.datamanagement.utils;


import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.datamanagement.constants.ApplicationConstants;
import com.datamanagement.vo.DataSchemaVO;
import com.datamanagement.vo.DataSchemaValuesVO;
import com.datamanagement.vo.EnvironmentJsonVO;
import com.datamanagement.vo.StreamDefinitionVO;
import com.google.gson.Gson;


@Component
public class JsonReader {
	

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
			
			if(key.equals(ApplicationConstants.ENVIRONMENT_JSON)){
				
			JSONObject jsonObject = (JSONObject)obj.get(key);			
			JSONObject jsonstreamDef = (JSONObject)jsonObject.get(ApplicationConstants.STREAM_DEFINITION);			
			Gson gson = new Gson();
			StreamDefinitionVO streamDefinitionVO = gson.fromJson(jsonstreamDef.toString(), StreamDefinitionVO.class);			
			JSONArray jsonDataSchema = (JSONArray)jsonObject.get(ApplicationConstants.DATA_SCHEMA);
						
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

}
