package com.appManager.ioprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.appManager.model.PrimaryProviders;
import com.appManager.resources.Messages;

public class MultiIOprocessing {
	static Logger log = Logger.getLogger(MultiIOprocessing.class.getName());
	public File createFolder(String whichdirectory, String whatname) {
		File file = new File(whichdirectory);
		File application = new File(whichdirectory + "/" + whatname);  
		if (!file.exists()) { 
			if (file.mkdir()) {
				application.mkdir();
				log.info("Directory is created!=="+whichdirectory+"  "+whatname);				
			} else {
				log.error("Failed to create directory!=="+whichdirectory+"  "+whatname);	
			}
		}
		return application;
	}

	public JSONObject mergedPackage(ArrayList<String> directories, String appname) {
		JSONObject returnjson = new JSONObject();
		for (String each : directories) {
			File f = new File(each + "/package.json");  
			if (f.exists() && !f.isDirectory()) {
				try (BufferedReader br = new BufferedReader(new FileReader(f))) {
					StringBuilder sb = new StringBuilder();
					String sCurrentLine;

					while ((sCurrentLine = br.readLine()) != null) {
						sb.append(sCurrentLine);
					}
					sCurrentLine = sb.toString();
					JSONObject jsonObject = new JSONObject(sCurrentLine);

					returnjson = MultiIOprocessing.mergeJSONObjects(returnjson,
							jsonObject.getJSONObject("dependencies"));  
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		MultiIOprocessing.createPKGjson(returnjson, directories, appname);
		return returnjson;

	}

	private static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
		JSONObject mergedJSON = new JSONObject();
		if (!(json1.length() == 0)) {
			try {
				mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
				for (String key : JSONObject.getNames(json2)) {
					mergedJSON.put(key, json2.get(key));
				}

			} catch (JSONException e) {
				e.printStackTrace(); 
			}
		} else {
			mergedJSON = json2;
		}

		return mergedJSON;
	}

	private static File createPKGjson(JSONObject json, ArrayList<String> directories, String appname) {
		JSONObject scriptjson = new JSONObject();
		scriptjson.put("start", "node app.js");   //$NON-NLS-2$
		JSONObject packagejson = new JSONObject();
		packagejson.put("name", appname);  
		packagejson.put("main", "app.js");   //$NON-NLS-2$
		packagejson.put("dependencies", json);  
		packagejson.put("scripts", scriptjson);  
		Writer output = null;
		String jsonString = packagejson.toString();
		File parentfile = new File(directories.get(0)).getParentFile();
		File file = new File(parentfile + "/package.json");  
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(jsonString);
			output.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return file;

	}

	public String removeDirectory(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null && files.length > 0) {
					for (File aFile : files) {
						removeDirectory(aFile);
					}
				}
				dir.delete();
			} else {
				dir.delete();
			}
			return "Directory deleted";
		} else {
			return "There is no such directory";
		}

	}

	public String copyFiles(File source, File dest) {
		try {
			if (source.exists()) {
				if (!source.isDirectory()) {
					FileUtils.copyFileToDirectory(source, dest);
				} else {
					FileUtils.copyDirectory(source, dest);
				}
			} else {
				return "There is no such directory";
			}
			return "Files are copied";
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "Files are copied";
	}



	public void changeAppjs(String path, ArrayList<String> providername, String what) {
		String newpath = path + "/" + "app.js";
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			if (what.equalsIgnoreCase("OAUTH")) {
				fw = new FileWriter(newpath, true);
				bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
				for (int i = 0; i < providername.size(); i++) {
					out.println("var " + providername.get(i) + " = require('./" + providername.get(i) + "');");
					out.println("new " + providername.get(i) + "(app);");
				}

			} else if (what.equalsIgnoreCase("OTP")) {
				fw = new FileWriter(newpath, true);
				bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
				for (int i = 0; i < providername.size(); i++) {
					out.println("var " + providername.get(i) + " = require('./" + providername.get(i) + "');");
					out.println("new " + providername.get(i) + "(app);");
				}
				out.println(Messages.getString("OTP_Message1"));
				out.println(Messages.getString("OTP_Message2"));
				out.println(Messages.getString("OTP_Message3"));
				out.println(Messages.getString("OTP_Message4"));
			}	
			if (out != null){
				out.close();		
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// addd url
	public String changeConfig(String path, String appname, PrimaryProviders primaryProviders, JSONObject hookObj,
			String url) {
		String directory = path + "/src/config.json";  
		Path file = Paths.get(directory);
		JSONObject job = new JSONObject(primaryProviders);
		JSONObject clientobj = new JSONObject();
		/*for (Object key : job.keySet()) {
			clientobj = (JSONObject) (job.get(key.toString()));
			clientobj.put(key.toString(),"YES");
			
		}*/
		
			if (!(primaryProviders.getFacebook() == null)) {
				clientobj.put( "facebook", "YES");

			}
			if (!(primaryProviders.getTwitter() == null)) {
				clientobj.put( "twitter", "YES");
			}
			if (!(primaryProviders.getGoogle() == null)) {
				clientobj.put( "google", "YES");
			}
			if (!(primaryProviders.getLinkedin() == null)) {
				clientobj.put( "linkedin", "YES");
			}
		/*for (Object key : hookObj.keySet()) {
			newjob.put(key.toString(), hookObj.get(key.toString()));
		}*/
		/* newjob.put(key, value); */
		List<String> lines = Arrays.asList(clientobj.toString()); 
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return url;
	}

	public void removeOauthJade(String source, String dest) {
		try {
			Files.move(new File(source).toPath(), new File(dest).toPath(),
					java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public JSONObject envoirmentjson(PrimaryProviders primaryProviders){
		JSONObject envjson = new JSONObject();
		if(!(primaryProviders.getFacebook()==null)){
			envjson.put("facebook_clientID", primaryProviders.getFacebook().getClientID());
			envjson.put("facebook_clientSecret", primaryProviders.getFacebook().getClientSecret());
			envjson.put("facebook_scope", primaryProviders.getFacebook().getScope());
		}
		if(!(primaryProviders.getGoogle()==null)){
			envjson.put("google_clientID", primaryProviders.getGoogle().getClientID());
			envjson.put("google_clientSecret", primaryProviders.getGoogle().getClientSecret());
			envjson.put("google_scope", primaryProviders.getGoogle().getScope());
		}
		if(!(primaryProviders.getLinkedin()==null)){
			envjson.put("linkedin_clientID", primaryProviders.getLinkedin().getClientID());
			envjson.put("linkedin_clientSecret", primaryProviders.getLinkedin().getClientSecret());
			envjson.put("linkedin_scope", primaryProviders.getLinkedin().getScope());
		}
		if(!(primaryProviders.getTwitter()==null)){
			envjson.put("twitter_clientID", primaryProviders.getTwitter().getClientID());
			envjson.put("twitter_clientSecret", primaryProviders.getTwitter().getClientSecret());
		}
		return envjson;
	}
	public String changeConfig(String dir,String appname){
		
		String directory = dir+"/app.js";
		Path file = Paths.get(directory);
		List<String> lines = Arrays.asList("var express = require('express');","var app = express();","app.get('/', function (req, res) {",
				"res.send("+appname+");", "});",
				"var port = process.env.VCAP_APP_PORT || 7000;","app.listen(port, function() {","console.log('listening at port '+ port);",
				"});");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
}
