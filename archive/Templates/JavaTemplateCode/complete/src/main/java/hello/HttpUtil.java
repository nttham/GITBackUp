package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;





@Component
public class HttpUtil {

	

	/**
	 * Util class to make a http GET call
	 * 
	 * @param urlLink
	 * @param connectionProperties
	 * @return
	 * 
	 */
	public String executeGetCall(String urlLink, Map<String, String> connectionProperties) {

		HttpURLConnection conn = null;
		StringBuffer buffer = null;
		try {
			URL url = new URL(urlLink);	
		
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			boolean errorFlag=false;
			conn.setDoOutput(true);
			
			conn.setRequestMethod("GET");
			if(connectionProperties.keySet().size()>0){

			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}
		}
			BufferedReader bufferedReader = null;
			int responseCode=Integer.valueOf(conn.getResponseCode());
			bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));			
			 buffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			conn.disconnect();
		
		}
		
		catch (Exception e) {
			conn.disconnect();
			List<String> values = new ArrayList<String>();
			values.add(e.getMessage());			
		}
		return buffer.toString();
	}

	/**
	 * Util class to make a http POST call
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @return
	 *  
	 */
	public Map<Integer, String> executePostCall(String urlLink, String input, Map<String, String> connectionProperties) throws  IOException {
		HttpURLConnection conn = null;
		OutputStream os =null;
		StringBuffer buffer = null;
		Map<Integer, String> response = new HashMap<Integer, String>();
		try {
			
			URL url = new URL(urlLink);		
  
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(ApplicationConstants.POST_REQUEST);
			conn.setRequestProperty("Content-Type", "application/json");
			boolean errorFlag=false;
			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}
			
			 os = conn.getOutputStream();
			 if(input!=null){
			os.write(input.getBytes());
			 }
			os.flush();

			BufferedReader bufferedReader = null;
			int responseCode=Integer.valueOf(conn.getResponseCode());			
			bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));		
			 buffer = new StringBuffer();
			while (true) {
				final String line = bufferedReader.readLine();
				if (line == null)
					break;
				buffer.append(line);
			}
			if(responseCode==303){
				response.put(new Integer(responseCode), buffer.toString());
			}
			else if(responseCode==302){
				response.put(new Integer(responseCode), buffer.toString());
			}
			else if(responseCode==200){
				response.put(new Integer(responseCode), buffer.toString());
			}
			conn.disconnect();
			os.close();			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			conn.disconnect();
			os.close();
			List<String> values = new ArrayList<String>();
			values.add(e.getMessage());
			
		}
		return response;
	}
	
	/**
	 * Util class to make a http POST call
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @return
	 *  
	 */
	public String executeGenarateOTPCall(String urlLink, String input, Map<String, String> connectionProperties) throws  IOException {
		HttpURLConnection conn = null;
		OutputStream os =null;
		StringBuffer buffer = null;
		Map<Integer, String> response = new HashMap<Integer, String>();
		try {
			
			URL url = new URL(urlLink);
			
//			System.setProperty("https.proxyHost", "proxy.cognizant.com");
//            System.setProperty("https.proxyPort",  "6050");
//            System.setProperty("http.proxyHost", "proxy.cognizant.com");
//            System.setProperty("http.proxyPort", "6050");
            
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(ApplicationConstants.POST_REQUEST);
			conn.setRequestProperty("Content-Type", "application/json");
			boolean errorFlag=false;
			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}
			
			 os = conn.getOutputStream();
			 if(input!=null){
			os.write(input.getBytes());
			 }
			os.flush();

			BufferedReader bufferedReader = null;
			int responseCode=Integer.valueOf(conn.getResponseCode());			
			bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));		
			 buffer = new StringBuffer();
			while (true) {
				final String line = bufferedReader.readLine();
				if (line == null)
					break;
				buffer.append(line);
			}
				
			conn.disconnect();
			os.close();			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			conn.disconnect();
			os.close();
			List<String> values = new ArrayList<String>();
			values.add(e.getMessage());
			
		}
		return buffer.toString();
	}
	
	
	/**
	 * Util class to make a http PUT call
	 * 
	 * @param urlLink
	 * @param input
	 * @param connectionProperties
	 * @return
	 *
	 */
	public String executePutCall(String urlLink, String input, Map<String, String> connectionProperties) throws  IOException {
		HttpURLConnection conn = null;
		OutputStream os = null;
		StringBuffer buffer = null;
		try {
			URL url = new URL(urlLink);
			
			System.setProperty("https.proxyHost", "proxy.cognizant.com");
            System.setProperty("https.proxyPort",  "6050");
            System.setProperty("http.proxyHost", "proxy.cognizant.com");
            System.setProperty("http.proxyPort", "6050");
            
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			boolean errorFlag=false;
			for (String headerKey : connectionProperties.keySet()) {
				conn.setRequestProperty(headerKey, connectionProperties.get(headerKey));
			}

			os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader bufferedReader = null;
			
			int responseCode=Integer.valueOf(conn.getResponseCode());			
			bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));			
			 buffer = new StringBuffer();
			while (true) {
				final String line = bufferedReader.readLine();
				if (line == null)
					break;
				buffer.append(line);
			}
			conn.disconnect();
			os.close();
	
		} 
		catch (Exception e) {
			e.printStackTrace();
			conn.disconnect();
			os.close();
			List<String> values = new ArrayList<String>();
			values.add(e.getMessage());
			
		}
	
	return buffer.toString();
}
}