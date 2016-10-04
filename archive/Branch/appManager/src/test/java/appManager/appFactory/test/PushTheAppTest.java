package appManager.appFactory.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;

import com.appManager.platformpush.PushTheApp;

public class PushTheAppTest {
PushTheApp pta = new PushTheApp();
	@Test
	public void testcheckFortheplatform(){
		ArrayList<String> platforms = new ArrayList<>();
		platforms.add("pivotal"); //$NON-NLS-1$
		platforms.add("bluemix"); //$NON-NLS-1$
		platforms.add("onec"); //$NON-NLS-1$
		
		String url;
		for(String each:platforms){
			if(each.equalsIgnoreCase("pivotal")){ //$NON-NLS-1$
				url = "http://appname.cfapps.io/"; //$NON-NLS-1$
			}else if(each.equalsIgnoreCase("bluemix")){ //$NON-NLS-1$
				url = "http://appname.mybluemix.net/"; //$NON-NLS-1$
			}else{
				url = "http://appname.54.208.194.189.xip.io/"; //$NON-NLS-1$
			}
			assertEquals(url,pta.checkFortheplatform(each, "appname")); //$NON-NLS-1$

		}
	}
	@Test
	public void testcheckFortheAppPush(){
		JSONObject appManager_response= new JSONObject();
		appManager_response.put("accesstoken", Messages.getString("PushTheAppTest.accesstoken")); //$NON-NLS-1$ //$NON-NLS-2$
		appManager_response.put("space_guid", Messages.getString("PushTheAppTest.spaceid")); //$NON-NLS-1$ //$NON-NLS-2$
		ArrayList<String> platforms = new ArrayList<>();
		platforms.add("pivotal"); //$NON-NLS-1$
		platforms.add("bluemix"); //$NON-NLS-1$
		platforms.add("onec"); //$NON-NLS-1$
		String zipPath="";
		String url;
		for(String each:platforms){
			if(each.equalsIgnoreCase("pivotal")){ //$NON-NLS-1$
				url = "http://appname.cfapps.io/"; //$NON-NLS-1$
			}else if(each.equalsIgnoreCase("bluemix")){ //$NON-NLS-1$
				url = "http://appname.mybluemix.net/"; //$NON-NLS-1$
			}else{
				url = "http://appname.54.208.194.189.xip.io/"; //$NON-NLS-1$
			}
			assertEquals(url,pta.checkFortheplatform(each, "appname",appManager_response,zipPath)); //$NON-NLS-1$

		}
	}
}
