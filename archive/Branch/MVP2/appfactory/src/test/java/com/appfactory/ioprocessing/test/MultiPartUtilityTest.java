package com.appfactory.ioprocessing.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.appfactory.ioprocessing.MultipartUtility;

public class MultiPartUtilityTest {

	@Autowired
	MultipartUtility multipartUtility;
	
	
	@Test
	public void MultipartUtilityTest() throws IOException{
		
		MultipartUtility multipartUtility= new MultipartUtility("http://login.54.208.194.189.xip.io/", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiIwNmZiY2FjZDMxYmM0MDZmODM4YjM1ZjY5YzU0NWU4OSIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2Nzk3OTM0NywicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3OTc5MzQ3LCJleHAiOjE0Njc5Nzk5NDcsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.UfYJ2Gw2P5WZkeulIii4WR1Jh9OzuQMEgzcOwILGHMlmfBq-gCcHrHp6hESM_ryqk9YGSu7gmYuhw59Wtm0H0BuXFUIEyevEquB7jRWsv5NC9dCqDrcdsfxxWEEa3W94HW18aqiA54rPppnvmJHXGX0MyCcQBBqKAoeMlohoVfs"); 
		

	}
	 
	@Test
	public void addFormFieldTest() throws IOException{
		
	multipartUtility.addFormField("Deago", "231");
		
	}
	
	@Test
	public void addFilePartTest() throws IOException{
		
	
		final File file = new File("D:/JUNIT/Learn/dp.txt");
		
		multipartUtility.addFilePart("", file);
	}
	
	@Test
	public void addHeaderFieldTest() throws IOException{
		
	
	multipartUtility.addHeaderField("", "");
	}
	@Test
	public void finishTest() throws IOException{
		
	assertNotNull(multipartUtility.finish());
	multipartUtility.finish();
	}
	
	
	
	
	
	
	
	
	
	
	

	
}