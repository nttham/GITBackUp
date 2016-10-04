package com.appfactory.platformpush;
/**
 * 
 * @author Drishya
 *
 */import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.MalformedURLException;
 import java.net.ProtocolException;
 import java.net.URL;

import com.appfactory.constants.ApplicationConstants;
public class RollBackApp {
public static void deleteTheApp(final String token, final String appguid, final String PlatformURL) {
		
		
		try {
			HttpURLConnection conn = null;
			URL url = new URL(PlatformURL + ApplicationConstants.PLATFORM_URL_PATH+"/"+ appguid);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "bearer " + token);

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
