package appfactory;

import org.junit.Test;

import com.appfactory.core.AppFactorySave;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.OtherServices;
import com.appfactory.model.PostHooks;
import com.appfactory.model.PrimaryService;
import com.appfactory.service.AccessData;

public class AppFactorySaveTest {
AppFactorySave as =new AppFactorySave();
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
