package com.appfactory.ioprocessing.test;

import org.junit.Test;

import com.appfactory.ioprocessing.MultiZipProcessing;

public class MultiZipProcessingTest {
@Test
	public void testZip(){
		String source;
		String destination;
		String appname;
		String whatAction;
		source="D:\\JUNIT\\test";
		appname="unit_test";
		destination="D:\\JUNIT\\Learn";
		whatAction="zipit";
		MultiZipProcessing mZip = new MultiZipProcessing();
		/*assertEquals("zip operations are success",mZip.zipAction());*/
	}
	@Test
	public void testunZip(){
		String source="D:\\JUNIT\\test";
		String destination="D:\\JUNIT\\Learn.zip";
		String appname="unit_test";
		String whatAction="unzipit";
		MultiZipProcessing mZip = new MultiZipProcessing();
		/*assertEquals("zip operations are success",mZip.zipAction());*/
	}
	@Test
	public void testSwitchZip(){
		String source="D:\\JUNIT\\test";
		String destination="D:\\JUNIT\\Learn.zip";
		String appname="unit_test";
		String whatAction="noAction";
		MultiZipProcessing mZip = new MultiZipProcessing();
		/*assertEquals("Invalid Format for mulitzipprocessing",mZip.zipAction());*/
	}
	 
}
