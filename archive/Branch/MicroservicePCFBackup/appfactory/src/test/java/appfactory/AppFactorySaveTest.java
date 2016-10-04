package appfactory;

import static org.junit.Assert.*;

import org.junit.Test;

import com.appfactory.core.AppFactorySave;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.BluePrint;
import com.appfactory.model.OtherServices;
import com.appfactory.model.PostHooks;
import com.appfactory.model.PrimaryService;
import com.appfactory.service.AccessData;

public class AppFactorySaveTest {
AppFactorySave as =new AppFactorySave();
	BluePrint jsonlist = new BluePrint();
	PrimaryService ps = new PrimaryService();
	OtherServices os = new OtherServices();
	PostHooks ph = new PostHooks();
	AccessData accessdata= new AccessData();
	@Test
	public void setJsonlist() throws MyException {
		//Adding parameters to posthook
		ph.setChannel("otp");
//		ph.setChannelexpiryTime(4);
//		ph.setChannellength(5);
		ph.setChanneltype("numeric");
		ph.setHookchannelurl("NodeJS/Otp_Hook");
		ph.setProvider("twilio");
		ph.setProvideraccountSID("AC728b20a72ea48a175a0cf47d11a6aa56");
		ph.setProviderauthToken("1c84da28903505f762c727fe1bd65700");
		ph.setProviderfrom("+919539168770");
		ph.setProvidertoRecipient("+17758354685");
		ph.setProviderurl("NodeJS/Twilio_Hook");
		//Adding parameters to primaryservice
		ps.setAppurl("NodeJS/OAuth_Authentication");
		ps.setProtocol("OAuth");
		ps.setPosthook(ph);
		
		//Preparing the json
		jsonlist.setAccesstoken("eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNmQ1N2I3OTE4MzE0MGFhODgzZTQwZWRkM2RmOGU3YiIsInN1YiI6IjY5MjA2N2QwLWYwNzktNGFjMi04MDkxLTM3OTA1NmYxMWI5OSIsInNjb3BlIjpbIm9wZW5pZCIsInVhYS51c2VyIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiI2OTIwNjdkMC1mMDc5LTRhYzItODA5MS0zNzkwNTZmMTFiOTkiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiZW1haWwiOiJzaWJhYnJhdGEuYWNoYXJ5YUBjb2duaXphbnQuY29tIiwiYXV0aF90aW1lIjoxNDY1MjE1NzAzLCJyZXZfc2lnIjoiZDJiMWRiZiIsImlhdCI6MTQ2NTIxNTcwMywiZXhwIjoxNDY1MjE2MzAzLCJpc3MiOiJodHRwczovL3VhYS5ydW4ucGl2b3RhbC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInVhYSIsImNsb3VkX2NvbnRyb2xsZXIiLCJwYXNzd29yZCJdfQ.IrolzOgKUmQ4cr2_RCg7KAwA_G8fcqBdPaDCgMA6geQbR2-8fRaSHPubgBWiewVw5dtmU6_EBvrULvCls84HzFkjKiAUIhP6KiulP4bAdrWdEW_pwIR6ogcS4CXn5qvngMzBs6vUyUxvQjRrWXrVOLl0u--PCpBkOk_CDpuZvkKd0ZE1-pjIsY7gi7111bLyhTo-n9rxB6P1oocWwM3O3g25P2ynSPOYSeEVJ-B8wvwh77JssFs3TDk5DFzlpLgw2GcTro5hWNRq34q1eOBEX5dqoRuTiP_5UuXwKOT4u7-G9KntTd3LVTcc5-17980yJUILY9k5YFvk81j9C-GS2A");
		jsonlist.setDevlopergitpassword("shiv223acharya");
		jsonlist.setDevlopergiturl("https://github.com/sibabrata-acharya/test12.git");
		jsonlist.setDevlopergitusername("sibabrata.acharya@cognizant.com");
		jsonlist.setPlatform("pivotal");
		jsonlist.setServicename("first_app");
		jsonlist.setSpaceguid("05c3c599-9326-4998-a683-a782b12ee640");
		jsonlist.setPrimaryservice(ps);
		/*jsonlist.setOtherservices(os);*/	

//		accessdata.setBlueprint(jsonlist);
		String messageID= "b169a527-a10a-4a84-a45a-2909fee6b1d9";

//		assertNotNull(as.doAction(jsonlist, "save", messageID)); 
		/*assertNotNull(as.doAction(jsonlist, "download"));
		AccessData accessdata1 = null;
		assertNull(as.doAction(jsonlist, "save"));
		assertNull(as.doAction(jsonlist, "download"));*/
	}
}
