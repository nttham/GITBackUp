package com.datamanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datamanagement.utils.VcapReader;

@Component
public class DataMangementServiceDaoImpl implements IDataManagementServiceDao  {
	
	@Autowired
	VcapReader vcapReader;
	
	public void createDataLake(Map<String, String> schemaData){
		
		
		try{
			
		String URI= vcapReader.getUri();
		String username= vcapReader.getUserName();
		String password= vcapReader.getPassword();
		
		String sqlQuery =generateSql("DMStable",schemaData);		
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con= DriverManager.getConnection(URI,username,password);		 
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery(sqlQuery);
		
		while(rs.next()){  
		System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3)); 
		}
		con.close();  
		
		}
		catch(Exception e){ 
			System.out.println(e);
		}  


	}
	
	private  String generateSql(String tableName, Map<String, String> columns) {
		StringBuilder builder = new StringBuilder("INSERT INTO ");
		StringBuilder questionMarks = new StringBuilder(") VALUES (");
		builder.append(tableName).append("(");
		int i = 0;	
		
		for (Map.Entry<String, String> entry : columns.entrySet()){
          if (i++ > 0) {                	             	 
				builder.append(", ");
				questionMarks.append(", ");
			}
			
         builder.append(entry.getKey());
		 questionMarks.append(':' + entry.getValue());
		}
		
		builder.append(questionMarks).append(")");
		return builder.toString();
	}


}
