package appfactory;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appfactory.Application;
import com.appfactory.exceptions.MyException;
import com.appfactory.service.AccessData;

@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class GenerateAppTest {
	@Autowired
	private AccessData accessdata;

	@Test
	public void generetatetest() throws MyException {

		/* generateapp.baseDirectory(); */
	}
	
	@Test
	public void baseDirectory() throws Exception{
		com.appfactory.model.PrimaryService primaryService=new com.appfactory.model.PrimaryService();
		com.appfactory.model.PrimaryProviders primaryProviders=new com.appfactory.model.PrimaryProviders();

			accessdata.setAppguid("shiv223acharya");
	primaryService.setAppurl("NodeJS/Authentication_Template");
		
		JSONObject json=new JSONObject();
		
	    accessdata.setParentdirectory("");
		accessdata.setEnv_json(json);
		accessdata.setHookObj(json);
		accessdata.setRunningapp("Drishya");
		accessdata.setEndpoint("");
		accessdata.setDomainguid("");
		
		/*facebook fb= new facebook();
		
		//google Google= new google();
		
		fb.setClientID("468331803360754");
		fb.setClientSecret("8fc8690146770e0a637adcc7293280cc");
		//Google.setClientID("625227390094-m47bnlnuaguvq3phn5t5kmp503fsiagd.apps.googleusercontent.com");
		//Google.setClientSecret("k0vpP0Tp5dP2oqXmcF9v10G8");
		primaryProviders.setFacebook(fb);
		//primaryProviders.setGoogle(Google);
		GenerateApp gapp = new GenerateApp();
		String dr = "Success";
		assertEquals(dr,gapp.baseDirectory());*/
		
	} 

}
