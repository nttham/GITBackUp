package com.datamanagement.service;

import com.datamanagement.exception.DataManagementException;
import com.datamanagement.vo.EnvironmentJsonVO;

public interface IDataManagementService {
	
	public String createStreamDefinition(EnvironmentJsonVO environmentJsonVO) throws DataManagementException;

	public void createDatalake(EnvironmentJsonVO environmentJsonVO) throws DataManagementException;
	
	
	public boolean isJsonValid(String jsonString) throws DataManagementException;
	
}
