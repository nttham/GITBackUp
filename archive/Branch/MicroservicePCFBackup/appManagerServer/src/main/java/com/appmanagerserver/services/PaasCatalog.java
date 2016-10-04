package com.appmanagerserver.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerserver.utils.SSLUtilities;


public class PaasCatalog {
	@Autowired
	private  ExceptionUtils eUtils;
	private static final Logger LOG = Logger.getLogger(PaasCatalog.class.getName());
	
	public String handleException(HttpURLConnection conn, Object e) {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
		} catch (IOException e1) { 
			eUtils.myException(CustomErrorMessage.IOEXCEPTION3, e1.getMessage());
		}
		JSONObject err = new JSONObject();
		err.put("status", "failed");
		err.put("message", ((Throwable) e).getMessage());
		JSONObject errjobj = new JSONObject();
		err.put("response", errjobj.put("Error", output));
		err.put("Reason", errjobj);
		LOG.error(err.toString());
		return err.toString();
	}
	public String getCatalog(String type) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.catalogurl + "?type=" + type);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		}
	}

	public String getCatalogForPlatform(String id, String platForm,String regionid) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;

		try {
			URL url = new URL(ApplicationConstants.speccaturl + "?id=" + id 
					+"&platform="+platForm+"&regionID="+regionid);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		}

		}
	
	 public String searchCatalog(String searchString, String type)
	 {SSLUtilities.disableSSLCertificateChecking();
		 HttpURLConnection conn=null;
			try {
				URL url = new URL(ApplicationConstants.searchcatalogurl + "?searchString=" + searchString 
						+"&type="+type);
				 conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output = "";
				String line = "";
				while ((line = br.readLine()) != null) {
					output = output + line;
				}

				conn.disconnect();
				return output;

			} catch (MalformedURLException e) {
				LOG.error(e.getMessage());
				return handleException(conn, e);

			} catch (IOException e) {
				LOG.error(e.getMessage());
				return handleException(conn, e);

			} catch (ParseException e) {
				LOG.error(e.getMessage());
				return handleException(conn, e);
			} 
	 }

	public String autheticationCatalog(String type, String category) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.catalogurl + "?type=" + type 
					+"&category="+category);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	public String getLayouts(String layoutHeaderID) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.layouturl+layoutHeaderID);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	public String getPlatforms() {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.platformurl);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	
	public String getServicePlanGUID(String token,String apiurl,String serviceguid) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.serviceplanguid);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "bearer " + token);
			conn.setRequestProperty("api_url",apiurl);
			conn.setRequestProperty("serviceguid", serviceguid);
		
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	
	public String getExistingInstances(String token,String apiurl,String serviceguid) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.existinginstances);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "bearer " + token);
			conn.setRequestProperty("api_url",apiurl);
			conn.setRequestProperty("serviceguid", serviceguid);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	
	public String getOrganisations(String token,String apiurl) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.orgurl);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "bearer "+token);
			conn.setRequestProperty("api_url", apiurl);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
		
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	public String getRegions(final String regions) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.regionurl+regions);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	
	
	public String getSpace(final String token,final String apiurl,final String orguid) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.spaceurl);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "bearer "+token);
			conn.setRequestProperty("orgguid", orguid);
			conn.setRequestProperty("api_url", apiurl);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
	            output = output + line;
			}	
			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
	public String checkAppName(final String token,final String apiurl,final String spaceguid,final String appname) {
		SSLUtilities.disableSSLCertificateChecking();
		HttpURLConnection conn=null;
		try {
			URL url = new URL(ApplicationConstants.checkappname);
			 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "bearer " + token);
			conn.setRequestProperty("api_url",apiurl);
			conn.setRequestProperty("spaceguid", spaceguid);
			conn.setRequestProperty("appname", appname);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();
			return output;

		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);

		} catch (ParseException e) {
			LOG.error(e.getMessage());
			return handleException(conn, e);
		} 
	}
}
