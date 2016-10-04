package com.appfactory.GitProcessing.test;

import java.io.File;

import org.junit.Test;

import com.appfactory.GitProcessing.GithubOperations;

import appfactory.factory.test.Messages.Messages;

public class GithubOperationsTest {
GithubOperations gito = new GithubOperations();
	@Test
	public void testdownloadGitFolder(){ 
		String svnurl="https://github.com/DrishyaPillai/drishyatest.git";  
		String svnUserName="DrishyaPillai";  
		String svnPassword="#mymom01:)";  
		File dir =new File("D:\\JUNIT\\gitpush");
		File copy_dir=new File("D:\\JUNIT\\Learn");
		String commit_user="kasyap.aditya223@gmail.com";
		String appname="junit_tests";
		//assertEquals("Files are pushed",gito.push(appname,svnUserName,svnPassword,svnurl,dir,copy_dir,commit_user));
	}
	@Test 
	public void negativetestdownloadGitFolder(){
		String svnurl=Messages.getString("GithubOperationsTest.0");  
		String svnUserName=Messages.getString("GithubOperationsTest.2");  
		String svnPassword=Messages.getString("GithubOperationsTest.3");  
		File dir =new File("D:\\JUNIT\\gitpush");
		File copy_dir=new File("D:\\JUNIT\\Learn");
		String commit_user="";
		String appname="";
		//assertEquals("Files are pushed",gito.push(appname,svnUserName,svnPassword,svnurl,dir,copy_dir,commit_user));
	}
	
	/*@Test
	public void testdownloadGitFolder2(){
		String svnurl=Messages.getString("https://github.com/sibabrata-acharya/test22.git");  
		String svnUserName=Messages.getString("admin");  
		String svnPassword=Messages.getString("admin");  
		File dir =new File("D:\\JUNIT\\gitpush");
		File copy_dir=new File("D:\\JUNIT\\Learn");
		String commit_user="sibabrata.acharya@cognizant.com";
		String appname="junit_tests";
		assertEquals("Files are pushed",gito.push(appname,svnUserName,svnPassword,svnurl,dir,copy_dir,commit_user));
	
	}*/
	
}


