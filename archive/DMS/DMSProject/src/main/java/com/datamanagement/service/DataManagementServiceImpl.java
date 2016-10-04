package com.datamanagement.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.datamanagement.constants.ApplicationConstants;
import com.datamanagement.dao.IDataManagementServiceDao;
import com.datamanagement.exception.DataManagementException;
import com.datamanagement.utils.VcapReader;
import com.datamanagement.vo.AttributesValueVO;
import com.datamanagement.vo.DataSchemaVO;
import com.datamanagement.vo.DataSchemaValuesVO;
import com.datamanagement.vo.EnvironmentJsonVO;
import com.datamanagement.vo.StorageVO;
import com.datamanagement.vo.StreamAnalyticsValuesVO;
import com.datamanagement.vo.StreamAttributesVO;
import com.datamanagement.vo.StreamDefinitionVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component
public class DataManagementServiceImpl implements IDataManagementService {

	@Autowired
	private IDataManagementServiceDao dataMangementServiceDao;

	@Autowired
	private VcapReader vcapReader;

	public String createStreamDefinition(EnvironmentJsonVO environmentJsonVO) throws DataManagementException {

		String replacedContent = null;

		try {

			String jsonString = vcapReader.getJsonTemplate();

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Map<String, String>> jsonMap = new HashMap<String, Map<String, String>>();
			jsonMap = mapper.readValue(jsonString, new TypeReference<Map<String, Map<String, String>>>() {
			});

			Map<String, String> sourceDslMap = getSource(environmentJsonVO, jsonMap);			
			List<String> sourceToDestDsl =  createSourceStreamDsl(sourceDslMap);
			
			Map<String, String> processorDslMap = getProcessor(environmentJsonVO, jsonMap);
			Map<String, String> streamAnalyticsDslMap = getStreamAnalytics(environmentJsonVO, jsonMap);
			
			
			String schemaDataDsl = getStorage(environmentJsonVO,jsonMap);
			
			Map<String, String> schemaDataMap = getInputSchema(environmentJsonVO);

		} catch (Exception e) {
			throw new DataManagementException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return replacedContent;
	}

	private Map<String, String> getSource(EnvironmentJsonVO environmentJsonVO,
			Map<String, Map<String, String>> jsonMap) {

		StreamDefinitionVO streamDefinitionVO = environmentJsonVO.getStream_definition();
		List<StreamAttributesVO> sourceList = streamDefinitionVO.getSource();

		Map<String, String> sourceDslMap = new HashMap<String, String>();
		for (StreamAttributesVO streamAttributesVO : sourceList) {

			String sourceType = streamAttributesVO.getType();
			if (streamAttributesVO.getAttributes().isEmpty()) {
				String dslString = jsonMap.get(sourceType).get(ApplicationConstants.DSL);
				sourceDslMap.put(sourceType, dslString);
			}

			if (!streamAttributesVO.getAttributes().isEmpty()) {
				String dslString = jsonMap.get(sourceType).get(ApplicationConstants.DSL);
				for (AttributesValueVO attributesValueVO : streamAttributesVO.getAttributes()) {
					dslString = dslString.replace(ApplicationConstants.LESSERAT+ attributesValueVO.getKey() + ApplicationConstants.ATGREATER,
							attributesValueVO.getValue());
				}
				sourceDslMap.put(sourceType, dslString);
			}
		}
		System.out.println("sourceDslMap------------>" + sourceDslMap);
		return sourceDslMap;

	}

	private Map<String, String> getProcessor(EnvironmentJsonVO environmentJsonVO,
			Map<String, Map<String, String>> jsonMap) {

		StreamDefinitionVO streamDefinitionVO = environmentJsonVO.getStream_definition();
		List<StreamAttributesVO> processorList = streamDefinitionVO.getProcessor();
		Map<String, String> processorDslMap = new HashMap<String, String>();

		for (StreamAttributesVO streamAttributesVO : processorList) {
			String processorType = streamAttributesVO.getType();
			String processorName = streamAttributesVO.getName();
			if (!streamAttributesVO.getAttributes().isEmpty()) {
				String dslString = jsonMap.get(processorType).get(ApplicationConstants.DSL);
				for (AttributesValueVO attributesValueVO : streamAttributesVO.getAttributes()) {
					dslString = dslString.replace(ApplicationConstants.LESSERAT+ attributesValueVO.getKey() +  ApplicationConstants.ATGREATER,
							attributesValueVO.getValue());
				}
				if (processorName == null) {
					processorDslMap.put(processorType, dslString);
				} else {
					processorDslMap.put(processorName, dslString);
				}

			}
		}
		System.out.println("processorDslMap------------>" + processorDslMap);
		return processorDslMap;

	}

	private Map<String, String> getStreamAnalytics(EnvironmentJsonVO environmentJsonVO,
			Map<String, Map<String, String>> jsonMap) {

		StreamDefinitionVO streamDefinitionVO = environmentJsonVO.getStream_definition();
		List<StreamAnalyticsValuesVO> streamList = streamDefinitionVO.getStream_analytics();

		Map<String, String> streamAnalyticsDslMap = new HashMap<String, String>();

		for (StreamAnalyticsValuesVO streamAnalyticsValuesVO : streamList) {

			String streamAlayticsName = streamAnalyticsValuesVO.getName();
			String dslString = jsonMap.get(streamAlayticsName).get(ApplicationConstants.DSL);
			String timestamp = new SimpleDateFormat(ApplicationConstants.DATEFORMAT).format(new Date());
			dslString = dslString
					.replace(ApplicationConstants.LESSERAT + streamAnalyticsValuesVO.getName() + ApplicationConstants.ATGREATER, streamAnalyticsValuesVO.getField_name())
					.replace(ApplicationConstants.TIMESTAMPCONSTANT, timestamp);

			streamAnalyticsDslMap.put(streamAlayticsName, dslString);

		}
		System.out.println("streamAnalyticsDslMap------------>" + streamAnalyticsDslMap);

		return streamAnalyticsDslMap;

	}

	private String getStorage(EnvironmentJsonVO environmentJsonVO,Map<String, Map<String, String>> jsonMap) {

		StreamDefinitionVO streamDefinitionVO = environmentJsonVO.getStream_definition();		
		
		StorageVO storageVO = streamDefinitionVO.getStorage();
		StringBuffer schemaCSVData = new StringBuffer();
		DataSchemaVO dataSchemaVO = environmentJsonVO.getData_schema();
		List<DataSchemaValuesVO> dataSchemaVOList = dataSchemaVO.getData_schema();
		String dslMysqlString = jsonMap.get(storageVO.getType()).get(ApplicationConstants.DSL);
		
		for(DataSchemaValuesVO dataSchemaValuesVO : dataSchemaVOList){
		
			schemaCSVData.append(dataSchemaValuesVO.getKey());
			schemaCSVData.append(ApplicationConstants.COMMA);
		}
		
		String schemaDataDsl=dslMysqlString.replace(ApplicationConstants.SCHEMACOLUMNNAMES, schemaCSVData.toString());
		System.out.println(" schemaDataDsl------------>" + schemaDataDsl);
		
		return schemaDataDsl;
	}

	private Map<String, String> getInputSchema(EnvironmentJsonVO environmentJsonVO) {

		Map<String, String> schemaMap = new HashMap<String, String>();

		DataSchemaVO dataSchemaVO = environmentJsonVO.getData_schema();
		List<DataSchemaValuesVO> dataSchemaVOList = dataSchemaVO.getData_schema();

		for (DataSchemaValuesVO dataSchemaValuesVO : dataSchemaVOList) {
			schemaMap.put(dataSchemaValuesVO.getKey(), dataSchemaValuesVO.getDescription());

		}
		System.out.println("schemaMap------>" + schemaMap);

		return schemaMap;
	}

	public void createDatalake(EnvironmentJsonVO environmentJsonVO) {

		Map<String, String> schemaData = getInputSchema(environmentJsonVO);

		try {
			dataMangementServiceDao.createDataLake(schemaData);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isJsonValid(String jsonString) throws DataManagementException {
		Gson gson = new Gson();
		try {
			gson.fromJson(jsonString, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			throw new DataManagementException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

		}
	}

	private List<String> createSourceStreamDsl(Map<String, String> sourceMap) {
		List<String> sourceToDestination = new ArrayList<String>();
		for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
			// TODO
			// Name of the Destination is unique
			String myDestination = "myDestination";
			sourceToDestination.add(entry.getValue() + ApplicationConstants.SPACE + ApplicationConstants.GREATERSIGN
					+ ApplicationConstants.SPACE + ApplicationConstants.COLON + myDestination);

		}
		System.out.println("sourceToDestination---------------->" + sourceToDestination);
		
		return sourceToDestination;

	}
}
