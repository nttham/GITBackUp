package com.appManager.ioprocessing.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

import com.appManager.ioprocessing.MultiIOprocessing;

public class MultiIOProcessingTest {

	@Test
	public void createFolderTest() {
		
		MultiIOprocessing multiProcessing = new MultiIOprocessing();
		
		assertNotNull(multiProcessing.createFolder("", ""));
		
	}
	
	
	@Test
	public void mergedPackageTest(){
		
		MultiIOprocessing multiProcessing	= new MultiIOprocessing();

	ArrayList<String> directories= new ArrayList<String>();
	directories.add(0, "D:/abc/a");
	directories.add(0, "D:/abc/b");
	directories.add(0, "D:/abc/c");
	directories.add(0, "D:/abc/d");
	directories.add(0, "D:/abc/e");
	
	String appName = "result";
	
	JSONObject json= new JSONObject();
	
    json= multiProcessing.mergedPackage(directories,appName);
    
    assertNotNull(json);
	}

}
