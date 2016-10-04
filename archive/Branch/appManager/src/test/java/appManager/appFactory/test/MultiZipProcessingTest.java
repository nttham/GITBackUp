package appManager.appFactory.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.appManager.ioprocessing.MultiZipProcessing;

public class MultiZipProcessingTest {
@Test
	public void testZip(){
		String source;
		String destination;
		String appname;
		String whatAction;
		source="D:\\JUNIT\\Learn\\package.json";
		appname="unit_test";
		destination="D:\\JUNIT\\test";
		whatAction="zipit";
		MultiZipProcessing mZip = new MultiZipProcessing();
		assertEquals("zip operations are success",mZip.zipAction(destination, source, appname, whatAction));
	}
	@Test
	public void testunZip(){
		String source="D:\\JUNIT\\test";
		String destination="D:\\JUNIT\\Learn.zip";
		String appname="unit_test";
		String whatAction="unzipit";
		MultiZipProcessing mZip = new MultiZipProcessing();
		assertEquals("zip operations are success",mZip.zipAction(source, destination, appname, whatAction));
	}
	@Test
	public void testSwitchZip(){
		String source="D:\\JUNIT\\test";
		String destination="D:\\JUNIT\\Learn.zip";
		String appname="unit_test";
		String whatAction="noAction";
		MultiZipProcessing mZip = new MultiZipProcessing();
		assertEquals("Invalid Format for mulitzipprocessing",mZip.zipAction(source, destination, appname, whatAction));
	}
	 
}
