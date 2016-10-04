package com.micro.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.micro.common.AppZip;
import com.micro.common.Colors;
import com.micro.common.DeleteDirectory;


public class GetInput {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputjson =System.getProperty("user.dir")+"/Input-modified.json";
		String destination;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(inputjson));
			JSONObject jsonObject = (JSONObject) obj;
			GetDetails gd = new GetDetails();
			String application_name =gd.readJson(jsonObject);
			String localPath = System.getProperty("user.dir")+"/"+application_name;
			if (Files.isDirectory(Paths.get(localPath))) {
				destination=gd.updateMicroService(jsonObject,gd.git_url, localPath);
				  //System.out.println("i have to delete it or update it");
				}else{
					 destination= gd.getmicroService(jsonObject,gd.git_url, localPath);
					 System.out.println(Colors.getAnsiGreen()+"Required Microservices Downloaded"+Colors.getAnsiReset());
				}
				
			CreateFiles cf = new CreateFiles();
			cf.createManifest(destination,application_name);
			cf.changeConfig(destination,application_name);
	        File delete_dir = new File(localPath+"/.git");
			DeleteDirectory.removeDirectory(delete_dir);
			AppZip zip = new AppZip();
			String zip_path = zip.submain(destination,application_name);
			 
			
			PushToCloudfoundry pcf = new PushToCloudfoundry();
			String login_details = pcf.login(gd.platform_user,gd.platform_password);
			JSONObject jsonLoginResponse = (JSONObject) parser.parse(login_details);
			String responseSpace = pcf.getSpace(jsonLoginResponse);
			JSONObject response = (JSONObject) parser.parse(responseSpace);
			AppServices ca = new AppServices();
			String responseApp =ca.createApp(application_name,response,zip_path);
			JSONObject jsoncreateAppObj =(JSONObject)parser.parse(responseApp);
			
			
			PivotalServices pivotalServices = new PivotalServices();
			// Get all services
			String getAllserviceREsponse = pivotalServices.getAllServices(jsonLoginResponse);
			JSONObject jsongetAllSrvcREsponse = (JSONObject) parser.parse(getAllserviceREsponse);	
			// Get Service Plans
			String getSevicePlans = pivotalServices.getServicePlans(jsongetAllSrvcREsponse,
						jsonLoginResponse);
			JSONObject jsonGEtSevicePlansREsponse = (JSONObject) parser.parse(getSevicePlans);				
			// Create New Service Instance
			
			
			String createServiceInstance=	pivotalServices.createServiceInstance(jsonGEtSevicePlansREsponse, 
					jsonLoginResponse, response,
					gd.getService_name());
		
			JSONObject jsoncreateServiceInstanceObj = (JSONObject) parser.parse(createServiceInstance);	
			
			
			//Bind Service
				pivotalServices.bindServiceInstanceToApp(jsoncreateServiceInstanceObj, jsoncreateAppObj, jsonLoginResponse,gd.getService_name());
			//Restaging Service
			pivotalServices.restageApp(jsoncreateAppObj, jsonLoginResponse);

			//Push the final app
				GitPush.push(gd.getGit_push_user(),gd.getGit_push_password(),gd.getGit_push_url(),application_name,gd.getGit_user());
		
		} catch (IOException e){
			e.printStackTrace();
				
	}
				catch(ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GitAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
