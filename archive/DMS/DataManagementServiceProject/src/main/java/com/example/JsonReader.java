package com.example;


import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;


@Component
public class JsonReader {
	
	
	
	/**
	 * method to find the ID key and add the ID value in to the task list  
	 * @param obj
	 * @param jobList
	 */
	
	public List<StreamDataVO> iterateJSON(JSONObject obj) {
	
		List<StreamDataVO> streamDataVOList = new ArrayList<StreamDataVO>();
		for (Object key : obj.keySet()) {
			JSONObject jsonObject = (JSONObject)obj.get(key);
			Gson gson = new Gson();
			StreamDataVO StreamDataVO = gson.fromJson(jsonObject.toString(), StreamDataVO.class);
			streamDataVOList.add(StreamDataVO);
		}
		return streamDataVOList;
	}

}
