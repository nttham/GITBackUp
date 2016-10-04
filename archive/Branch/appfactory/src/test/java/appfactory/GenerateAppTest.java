package appfactory;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appfactory.model.BluePrint;
import com.appfactory.model.PrimaryProviders;
import com.appfactory.model.PrimaryProviders.facebook;
import com.appfactory.model.PrimaryProviders.google;
import com.appfactory.model.PrimaryService;
import com.appfactory.Application;
import com.appfactory.core.GenerateApp;
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
		com.appfactory.model.BluePrint 	bluePrint = new com.appfactory.model.BluePrint();
		com.appfactory.model.PrimaryService primaryService=new com.appfactory.model.PrimaryService();
		com.appfactory.model.PrimaryProviders primaryProviders=new com.appfactory.model.PrimaryProviders();
		bluePrint.setServicename("failtest2");
		bluePrint.setAccesstoken("");
		bluePrint.setSpaceguid("b169a527-a10a-4a84-a45a-2909fee6b1d9");
		bluePrint.setPlatform( "OneC");
		bluePrint.setOrgguid("0f87ea0f-b95f-475d-b361-96dfd212add6");
		bluePrint.setDevlopergiturl("https://github.com/sibabrata-acharya/test22.git");
		bluePrint.setDevlopergitusername("sibabrata.acharya@cognizant.com");
		bluePrint.setDevlopergitpassword("shiv223acharya");
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
