package appManager.Manager.test;
import static org.junit.Assert.assertNotNull;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.appManager.utils.AppUtils;

public class AppUtilsTest {

AppUtils au = new AppUtils();
	@Test
	public void testgetGuid(){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Messages.getString("AppUtilsTest.0"))); //$NON-NLS-1$
			JSONObject jsonObject = (JSONObject) obj;
			assertNotNull(au.getGuid(jsonObject.toString()));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testgetBindableServiceGuid(){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Messages.getString("AppUtilsTest.0"))); //$NON-NLS-1$
			JSONObject jsonObject = (JSONObject) obj;
			assertNotNull(au.getBindableServiceGuid(jsonObject.toString()));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testgetServicePlanGuid(){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Messages.getString("AppUtilsTest.0"))); //$NON-NLS-1$
			JSONObject jsonObject = (JSONObject) obj;
			assertNotNull(au.getServicePlanGuid(jsonObject.toString()));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testtoJSOn(){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Messages.getString("AppUtilsTest.0"))); //$NON-NLS-1$
			JSONObject jsonObject = (JSONObject) obj;
			assertNotNull(au.toJson(jsonObject.toString()));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testtoJsonArray(){
			String name="hi:siba";
			assertNotNull(au.toJsonArray(name));
	}
}
