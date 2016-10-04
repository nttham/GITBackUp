package com.micro.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.micro.common.Colors;


public class CreateFiles {
	Boolean success =true;

	public Boolean createManifest(String path,String appname) throws IOException{
		String directory = path+"/manifest.yml";
		Path file = Paths.get(directory);
		List<String> lines = Arrays.asList("---","applications:","- name: " + appname + "",
				 "instances: 1",
				"random-route: true");
		Files.write(file, lines, Charset.forName("UTF-8"));
		System.out.println(Colors.getAnsiGreen()+"Mainefest file is now created");
		return success;
		
	}
	public Boolean createAppjs(String path,String appname) throws IOException{
		String directory = path+"/app.js";
		Path file = Paths.get(directory);
		List<String> lines = Arrays.asList("var express = require('express');","var app = express();","app.get('/', function (req, res) {",
				"res.send("+appname+");", "});",
				"var port = process.env.VCAP_APP_PORT || 7000;","app.listen(port, function() {","console.log('listening at port '+ port);",
				"});");
		Files.write(file, lines, Charset.forName("UTF-8"));
		return success;
		
	}
	public Boolean createPackageJSON(String path,String appname) throws IOException{
		String directory = path+"/package.json";
		Path file = Paths.get(directory);
		List<String> lines = Arrays.asList("{","'name': 'cf-nodejs',","'version': '1.0.0',",
				"'dependencies': {", "'express': '^4.13.4',",
				"'jade': '^1.11.0'","}","}");
		Files.write(file, lines, Charset.forName("UTF-8"));
		return success;
	}
	public Boolean changeConfig(String path,String appname) throws IOException{
		String directory = path+"/routes/config.js";
		Path file = Paths.get(directory);
		List<String> lines = Arrays.asList("var URIs = { \"redirecturi_homepage\" : \"http://"+appname+".54.208.194.189.xip.io/userpage\",","\"redirecturi\" : \"http://"+appname+".54.208.194.189.xip.io\"","};","module.exports = URIs;");
		//System.out.println("You can visit this link to see running app = http://"+appname+".54.208.194.189.xip.io");
		Files.write(file, lines, Charset.forName("UTF-8"));
		System.out.println("Modified the required config file");
		return success;
	}
}
