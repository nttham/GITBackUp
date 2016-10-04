package com.appManager.core;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appManager.GitProcessing.GithubOperations;
import com.appManager.exceptions.MyException;
import com.appManager.ioprocessing.MultiIOprocessing;
import com.appManager.ioprocessing.MultiZipProcessing;
import com.appManager.platformpush.BindService;
import com.appManager.platformpush.PushTheApp;
import com.appManager.resources.Messages;
import com.appManager.service.AccessData;

/**
 * File : GenerateApp.java Description : This class will have small small
 * implimentation of creating the dynamic app. Revision History : Version Date
 * Author Reason 0.1 14-June-2016 559296 Initial version
 */
@Service
public class GenerateApp {
	/**
	 * These two fields will be used for checking methods success or failure
	 * 
	 **/
	private static final String FAILURE = Messages.getString("ManagerController.Failed");
	private static final String SUCCESS = Messages.getString("ManagerController.Success");
	private static final Logger LOG = Logger.getLogger(GenerateApp.class.getName());

	@Autowired
	private AccessData accessdata;

	/**
	 * This method will create a base directory where all the operation will
	 * happen
	 * @throws MyException 
	 **/
	public String baseDirectory() throws MyException {
		MultiIOprocessing miop = new MultiIOprocessing();
		String parentdirectory = Messages.getString("Base_path") + System.currentTimeMillis();
		accessdata.setParentdirectory(parentdirectory);
		File createdir = miop.createFolder(parentdirectory, accessdata.getBlueprint().getServicename());
		if (!(createdir.exists())) {
			LOG.error("Base directory is not created");
			return FAILURE;
		}
		accessdata.setServicefolderpath(createdir);
		LOG.info("baseDirectory method is sucess");
		return cloneGit();
	}

	/**
	 * This method will clone the required codebase from the selected inputs
	 * @throws MyException 
	 **/
	private String cloneGit() throws MyException {
		ArrayList<String> returnarr = new ArrayList<String>();
		GithubOperations gito = new GithubOperations();
		returnarr = gito.cloneGit(accessdata.getServicefolderpath(), accessdata);
		if (returnarr.get(0).equalsIgnoreCase("error")) {
			LOG.error("Not able to get folders from git");
			return FAILURE;
		}
		accessdata.setDownloadfolders(returnarr);
		LOG.info("cloneGit method is sucess");
		return generateURL();
	}

	/**
	 * This method will merge all the package.json files into one And will
	 * delete not used package.json
	 **/
	@SuppressWarnings("unused")
	private String mergePackage() {
		MultiIOprocessing miop = new MultiIOprocessing();

		JSONObject jobj = miop.mergedPackage(accessdata.getDownloadfolders(),
				accessdata.getBlueprint().getServicename());
	//	for (int i = 0; i < accessdata.getDownloadfolders().size(); i++) {
		for(String folderPath : accessdata.getDownloadfolders()){
			File directory = new File(folderPath + Messages.getString("AppFactorySave.5"));
			miop.removeDirectory(directory);
		}
		if (jobj.length() <= 0) {
			return FAILURE;
		}
		LOG.info("mergePackage method is sucess");
		return mergeApp();
	}

	/**
	 * This method will merge all the folders into one and will create it as a
	 * single app
	 **/
	private String mergeApp() {
		if (accessdata.getBlueprint().getPrimaryservice().getProtocol()
				.equalsIgnoreCase(Messages.getString("AppFactorySave.Oauth"))) {
			MultiIOprocessing miop = new MultiIOprocessing();
			ArrayList<String> providerarr = whatProviders(Messages.getString("AppFactorySave.Oauth"));
			for (String provider : providerarr) {
				for (int i = 0; i < accessdata.getDownloadfolders().size(); i++) {
					File directory = new File(accessdata.getDownloadfolders().get(i)
							+ Messages.getString("AppManager.frwdslash") + provider);
					miop.removeDirectory(directory);
				}
			}
			String path = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
					+ accessdata.getBlueprint().getServicename() + Messages.getString("AppFactorySave.10");
			createViewJade(path);
			File dest = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
					+ accessdata.getBlueprint().getServicename());
			File source = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
					+ accessdata.getBlueprint().getServicename() + Messages.getString("AppManager.frwdslash")
					+ accessdata.getBlueprint().getPrimaryservice().getProtocol());
			miop.copyFiles(source, dest);
			ArrayList<String> providerlist = whatProviders(Messages.getString("AppFactorySave.appjs"));
			if (accessdata.getBlueprint().getPrimaryservice().getOnhook() == null
					&& accessdata.getBlueprint().getPrimaryservice().getPosthook() == null
					&& accessdata.getBlueprint().getPrimaryservice().getPrehook() == null) {
				miop.changeAppjs(
						accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
								+ accessdata.getBlueprint().getServicename(),
						providerlist, Messages.getString("AppFactorySave.Oauth"));
			} else {
				miop.changeAppjs(
						accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
								+ accessdata.getBlueprint().getServicename(),
						providerlist, Messages.getString("AppFactorySave.18"));
			}

		} else {
			/// right condition for other applications without oauth

		}
		LOG.info("mergeApp method is sucess");
		return createhooks();
	}

	/**
	 * This method will add all the hooks and will remove the unnecessary ones
	 **/
	private String createhooks() {
		CheckHook chook = new CheckHook();
		chook.checkHooks(accessdata, accessdata.getParentdirectory(), accessdata.getDownloadfolders());
		accessdata.setHookObj(chook.createConfigobject(accessdata.getBlueprint().getPrimaryservice()));
		if (!(chook.isSuccess()) || accessdata.getHookObj().length() <= 0) {
			return FAILURE;
		}
		LOG.info("createhooks method is sucess");
		return generateURL();
	}

	/**
	 * It will create config files with the urls
	 **/
	private String generateURL() {
		PushTheApp pushOperation = new PushTheApp();
		MultiIOprocessing miop = new MultiIOprocessing();

		ArrayList<String> urls = pushOperation.checkFortheplatform(accessdata.getBlueprint().getPlatform(),
				accessdata.getBlueprint().getServicename());
		accessdata.setRunningapp(urls.get(0));
		accessdata.setEndpoint(urls.get(1));
		accessdata.setHost(urls.get(2));
		miop.changeConfig(
				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
						+ accessdata.getBlueprint().getServicename(),
				accessdata.getBlueprint().getServicename(),
				accessdata.getBlueprint().getPrimaryservice().getProviders(), accessdata.getHookObj(),
				accessdata.getRunningapp());
		if (urls.isEmpty()) {
			return FAILURE;
		}
		LOG.info("generateURL method is sucess");
		return generateZip();
	}

	/**
	 * It will create zip file of the generated app.
	 **/
	private String generateZip() {
		String source = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename();
		String destzip = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename() + Messages.getString("AppFactorySave.26");
		MultiZipProcessing mZip = new MultiZipProcessing();
		mZip.zipAction(source, destzip, accessdata.getBlueprint().getServicename(),
				Messages.getString("AppFactorySave.27"));

		LOG.info("generateZip method is sucess");
		return pushApp();
	}

	/**
	 * It will create push the app, and will start the app.
	 **/
	private String pushApp() {
		PushTheApp pushOperation = new PushTheApp();
		JSONObject appresponse = new JSONObject();
		appresponse.put(Messages.getString("AppFactorySave.accesstoken"), accessdata.getBlueprint().getAccesstoken());
		appresponse.put(Messages.getString("AppFactorySave.spaceguid"), accessdata.getBlueprint().getSpaceguid());
		String response = pushOperation.checkFortheplatform(accessdata.getBlueprint().getPlatform(),
				accessdata.getBlueprint().getServicename(), appresponse,
				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
						+ accessdata.getBlueprint().getServicename() + Messages.getString("AppFactorySave.29"));
		if (response.contains("failed")) {
			return FAILURE;
		}

		String[] res = response.split(",");

		accessdata.setAppguid(res[0]);
		accessdata.setDomainguid(res[1]);
		LOG.info("pushApp method is sucess");
		return createMicro();
	}

	/**
	 * It will create the service instance, will get all the service,service
	 * plans,Bind service, create service instance,Restage,App env
	 * 
	 **/

	private String createMicro() {
		BindService bservice = new BindService();
		MultiIOprocessing mip = new MultiIOprocessing();
		PushTheApp pushOperation = new PushTheApp();
		String response;
		String platform = accessdata.getEndpoint();
		String token = accessdata.getBlueprint().getAccesstoken();
		if (!(platform.isEmpty())) {
			/**
			 * response = all the service list
			 */
			response = bservice.getAllServices(token, platform);
			accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders()));
			JSONObject jobj = new JSONObject(response);
			JSONObject createservice = serviceobj(jobj);
			String serviceinstanceguid = bservice.createServiceInstance(token, accessdata.getBlueprint().getSpaceguid(),
					platform, createservice, jobj, accessdata.getAppguid(), accessdata.getBlueprint().getServicename());
			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			String serviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj();
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getBlueprint().getServicename(), accessdata.getAppguid(), jobj, bindobj,
						serviceaappguid);
				bservice.restageApp(accessdata.getAppguid(), accessdata.getBlueprint().getAccesstoken(), platform);
				bservice.getAppEnv(accessdata.getBlueprint().getAccesstoken(), accessdata.getAppguid(), platform);
				pushOperation.startApplication(accessdata.getBlueprint().getAccesstoken(), accessdata.getAppguid(),
						platform, accessdata.getDomainguid());
				/*
				 * bservice.restageApp(accessdata.getAppguid(),
				 * accessdata.getBlueprint().getAccesstoken(), platform);
				 */
			} else {
				return FAILURE;
			}
		} else {
			return FAILURE;
		}
		LOG.info("createMicro method is success");
		return gitPush();
	}

	/**
	 * It will push the app, to the devlopers github account.
	 **/
	private String gitPush() {
		GithubOperations gito = new GithubOperations();
		MultiIOprocessing miop = new MultiIOprocessing();
		File destination = miop.createFolder(accessdata.getParentdirectory(), Messages.getString("AppFactorySave.32"));
		File copydir = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename());
		String applicationname = accessdata.getBlueprint().getServicename();
		String result = gito.push(applicationname, accessdata.getBlueprint().getDevlopergitusername(),
				accessdata.getBlueprint().getDevlopergitpassword(), accessdata.getBlueprint().getDevlopergiturl(),
				destination, copydir, Messages.getString("AppFactorySave.commituser"));
		if (!(result.equalsIgnoreCase(SUCCESS))) {
			return FAILURE;
		}
		LOG.info("gitPush method is sucess");
		return SUCCESS;
	}

	/**
	 * This method checks what oauth or otp providers user have choosen
	 * 
	 * @param forwhat
	 **/
	private ArrayList<String> whatProviders(String forwhat) {
		ArrayList<String> providerlist = new ArrayList<String>();
		ArrayList<String> deletelist = new ArrayList<String>();
		deletelist.add(Messages.getString("AppManager.facebook")+Messages.getString("Appmanager.js"));
		deletelist.add(Messages.getString("AppManager.google")+Messages.getString("Appmanager.js"));
		deletelist.add(Messages.getString("AppManager.twitter")+Messages.getString("Appmanager.js"));
		deletelist.add(Messages.getString("AppManager.linkedin")+Messages.getString("Appmanager.js"));
		if (forwhat.equalsIgnoreCase(Messages.getString("AppFactorySave.Oauth"))) {
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getFacebook() != null) {
				providerlist.add(Messages.getString("AppManager.facebook")+Messages.getString("Appmanager.js"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getGoogle() != null) {
				providerlist.add(Messages.getString("AppManager.google")+Messages.getString("Appmanager.js"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getTwitter() != null) {
				providerlist.add(Messages.getString("AppManager.twitter")+Messages.getString("Appmanager.js"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getLinkedin() != null) {
				providerlist.add(Messages.getString("AppManager.linkedin")+Messages.getString("Appmanager.js"));
			}

			for (int i = 0; i < providerlist.size(); i++) {
				deletelist.remove(providerlist.get(i));
			}
			return deletelist;
		} else if (forwhat.equalsIgnoreCase(Messages.getString("AppFactorySave.appjs"))) {
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getFacebook() != null) {
				providerlist.add(Messages.getString("AppManager.facebook"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getGoogle() != null) {
				providerlist.add(Messages.getString("AppManager.google"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getTwitter() != null) {
				providerlist.add(Messages.getString("AppManager.twitter"));
			}
			if (accessdata.getBlueprint().getPrimaryservice().getProviders().getLinkedin() != null) {
				providerlist.add(Messages.getString("AppManager.linkedin"));
			}
			return providerlist;

		}
		return deletelist;
	}

	/**
	 * This method checks which hook should be used while creating the app
	 * 
	 * @param path
	 */
	private void createViewJade(String path) {
		MultiIOprocessing miop = new MultiIOprocessing();
		if (!(accessdata.getBlueprint().getPrimaryservice().getPrehook() == null)) {
			File rm = new File(path + Messages.getString("AppFactorySave.53"));
			miop.removeOauthJade(path + Messages.getString("AppFactorySave.54"),
					path + Messages.getString("AppFactorySave.55"));
			miop.removeDirectory(rm);
		} else {
			miop.removeOauthJade(path + Messages.getString("AppFactorySave.56"),
					path + Messages.getString("AppFactorySave.57"));
			File rm = new File(path + Messages.getString("AppFactorySave.58"));
			miop.removeDirectory(rm);
		}
		if (!(accessdata.getBlueprint().getPrimaryservice().getPosthook() == null)) {
			miop.removeOauthJade(path + Messages.getString("AppFactorySave.59"),
					path + Messages.getString("AppFactorySave.60"));
			File rm = new File(path + Messages.getString("AppFactorySave.61"));
			miop.removeDirectory(rm);
		} else {
			miop.removeOauthJade(path + Messages.getString("AppFactorySave.62"),
					path + Messages.getString("AppFactorySave.63"));
			File rm = new File(path + Messages.getString("AppFactorySave.64"));
			miop.removeDirectory(rm);
		}

	}

	/**
	 * 
	 * @param jobj
	 * @return
	 */
	private JSONObject serviceobj(JSONObject jobj) {
		String serviceplan = (String) jobj.get("service_plan_guid");
		JSONObject token = new JSONObject();
		token.put("access_token", accessdata.getBlueprint().getAccesstoken());
		token.put("token_type", "bearer");
		token.put("refresh_token", "");
		token.put("expires_in", "");
		token.put("scope", "");
		token.put("jti", "");

		JSONObject createservice = new JSONObject();
		createservice.put("endpoint", accessdata.getEndpoint());
		createservice.put("organization_guid", accessdata.getBlueprint().getOrgguid());
		createservice.put("plan_id", serviceplan);
		createservice.put("space_guid", accessdata.getBlueprint().getSpaceguid());
		createservice.put("appname", accessdata.getBlueprint().getServicename() + "service");
		createservice.put("domain_guid", accessdata.getDomainguid());
		createservice.put("host", accessdata.getHost());
		createservice.put("environment_json", accessdata.getEnv_json());
		createservice.put("token", token);
		return createservice;
	}

	private JSONObject bindserviceobj() {
		JSONObject jobj = new JSONObject();
		if (!(accessdata.getBlueprint().getPrimaryservice().getProviders() == null)) {
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getFacebook() == null)) {
				jobj.put(Messages.getString("AppManager.facebook"), true);

			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getTwitter() == null)) {
				jobj.put(Messages.getString("AppManager.twitter"), true);
			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getGoogle() == null)) {
				jobj.put(Messages.getString("AppManager.google"), true);
			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getLinkedin() == null)) {
				jobj.put(Messages.getString("AppManager.linkedin"), true);
			}
			jobj.put("appname", accessdata.getBlueprint().getServicename() + "service");
			jobj.put("host", accessdata.getHost());
		} else {
			return jobj;
		}
		return jobj;
	}
}
