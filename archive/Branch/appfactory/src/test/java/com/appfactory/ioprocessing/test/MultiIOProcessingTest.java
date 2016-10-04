package com.appfactory.ioprocessing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.appfactory.ioprocessing.MultiIOprocessing;

public class MultiIOProcessingTest {
MultiIOprocessing miop = new MultiIOprocessing();

	@BeforeClass  
    public static void setUpBeforeClass() throws Exception {  
    }   
	@Test
	 public void testFolderCreation(){ 
		String which_directory="D:/"; 
		String what_name ="junit_test";
		File file = new File("D:/junit_test");
		assertEquals(file,miop.createFolder(which_directory,what_name));
	}
	@Test
	public void testMergedPackage(){
		ArrayList<String> directories = new ArrayList<>();
		directories.add("D:/Learn/facebook");
		String appname ="testApp";
		JSONObject return_json = new JSONObject();
		return_json.put("start", "node app.js");
		return_json.put("main", "app.js");
		return_json.put("passport-linkedin-oauth2", "*");
		return_json.put("passport-twitter", "^1.0.3");
		return_json.put("passport-google-oauth2", "^0.1.6");
		return_json.put("jade", "^0.34.1");
		return_json.put("passport-facebook", "^2.0.0");
		return_json.put("passport", "^0.3.2");
		return_json.put("express", "~3.3.4");
		assertNotNull(miop.mergedPackage(directories, appname));
	}
	/*@Test
	public void testremoveDirectory(){
		File dir = new File("D:\\JUNIT\\deletethis.txt");
		assertEquals("Directory deleted",miop.removeDirectory(dir));
	}*/
	@Test
	public void negativetestremoveDirectory(){
		File dir = new File("D:\\JUNIT\\deletethisasd.txt");
		assertEquals("There is no such directory",miop.removeDirectory(dir));
	}
	@Test
	public void testCopyDirectory(){
		File source = new File("D:\\JUNIT\\Learn");
		File dest = new File("D:\\JUNIT\\test");
		assertEquals("Files are copied",miop.copyFiles(source, dest));
	}
	@Test
	public void negativetestCopyDirectory(){
		File source = new File("D:\\JUNIT\\Learn\\asda");
		File dest = new File("D:\\JUNIT\\test\\asda");
		assertEquals("There is no such directory",miop.copyFiles(source, dest));
	}
	@Test
	public void testremoveOauthJade(){
		File source = new File("D:\\JUNIT\\removeOauth");
		File dest = new File("D:\\JUNIT\\copyOauth");
		assertEquals("There is no such directory",miop.copyFiles(source, dest));
	}
	@Test 
	public void testchangeAppjs(){
		String path="D:\\JUNIT\\changeappjs";
		ArrayList<String> providername= new ArrayList<>();
		providername.add("D:\\JUNIT\\Learn");
		ArrayList<String> what = new ArrayList<>();
		what.add("OAUTH");
		what.add("OTP");
		for(String each:what){
			miop.changeAppjs(path, providername, each);
		}
	}
}
