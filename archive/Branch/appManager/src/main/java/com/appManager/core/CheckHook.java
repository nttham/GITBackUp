package com.appManager.core;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import com.appManager.ioprocessing.MultiIOprocessing;
import com.appManager.model.PrimaryService;
import com.appManager.service.AccessData;

public class CheckHook {
	MultiIOprocessing miop = new MultiIOprocessing();
	private boolean success = false;
	
	public void checkHooks(AccessData accessdata,String parentdirectory, ArrayList<String> arr ){
		if (!(accessdata.getBlueprint().getPrimaryservice().getPosthook() == null)) {
			File channel = new File(accessdata.getBlueprint().getPrimaryservice().getPosthook().getChannel());
			File provider = new File(accessdata.getBlueprint().getPrimaryservice().getPosthook().getProvider());
			File dest = new File(parentdirectory + "/" + accessdata.getBlueprint().getServicename());
			for (int i = 0; i < arr.size(); i++) {
				File directory = new File(arr.get(i) + "/" + channel + ".js");
				File providers = new File(arr.get(i) + "/" + provider + ".js");
				File providersservice = new File(arr.get(i) + "/" + provider + "service.js");
				miop.copyFiles(directory, dest);
				miop.copyFiles(providers, dest);
				miop.copyFiles(providersservice, dest);
			}
		} if (!(accessdata.getBlueprint().getPrimaryservice().getPrehook() == null)) {
			File channel = new File(accessdata.getBlueprint().getPrimaryservice().getPrehook().getChannel());
			File provider = new File(accessdata.getBlueprint().getPrimaryservice().getPrehook().getProvider());
			File dest = new File(parentdirectory + "/" + accessdata.getBlueprint().getServicename());
			for (int i = 0; i < arr.size(); i++) {
				File directory = new File(arr.get(i) + "/" + channel + ".js");
				File providers = new File(arr.get(i) + "/" + provider + ".js");
				File providersservice = new File(arr.get(i) + "/" + provider + "service.js");
				miop.copyFiles(directory, dest);
				miop.copyFiles(providers, dest);
				miop.copyFiles(providersservice, dest);
			}
		}if (!(accessdata.getBlueprint().getPrimaryservice().getOnhook() == null)) {
			File channel = new File(accessdata.getBlueprint().getPrimaryservice().getOnhook().getChannel());
			File provider = new File(accessdata.getBlueprint().getPrimaryservice().getOnhook().getProvider());
			File dest = new File(parentdirectory + "/" + accessdata.getBlueprint().getServicename());
			for (int i = 0; i < arr.size(); i++) {
				File directory = new File(arr.get(i) + "/" + channel + ".js");
				File providers = new File(arr.get(i) + "/" + provider + ".js");
				File providersservice = new File(arr.get(i) + "/" + provider + "service.js");

				miop.copyFiles(directory, dest);
				miop.copyFiles(providers, dest);
				miop.copyFiles(providersservice, dest);
			}
		}
		//Set the status after execution
		this.success = true;
	}
	public JSONObject createConfigobject(PrimaryService primaryService){
		JSONObject hookObj = new JSONObject();
		JSONObject channeldetails = new JSONObject();
		
		if (!(primaryService.getOnhook() == null)) {
			JSONObject provideronhook = new JSONObject();
			channeldetails.put("otpLength", primaryService.getOnhook().getChannellength());
			channeldetails.put("otpType", primaryService.getOnhook().getChanneltype());
			channeldetails.put("otpExpiryTime", primaryService.getOnhook().getChannelexpiryTime());
			
			provideronhook.put("accountSID", primaryService.getOnhook().getProvideraccountSID());
			provideronhook.put("authToken", primaryService.getOnhook().getProviderauthToken());
			provideronhook.put("toRecipient", primaryService.getOnhook().getProvidertoRecipient());
			provideronhook.put("from", primaryService.getOnhook().getProviderfrom());
			
			hookObj.put("channel", primaryService.getOnhook().getProvider());
			hookObj.put(primaryService.getOnhook().getChannel(), channeldetails);
			hookObj.put(primaryService.getOnhook().getProvider(), provideronhook);
		}if (!(primaryService.getPosthook() == null)){			
			JSONObject providerposthook = new JSONObject();
			if(!(channeldetails.has("otpLength"))){
				channeldetails.put("otpLength", primaryService.getPosthook().getChannellength());
				channeldetails.put("otpType", primaryService.getPosthook().getChanneltype());
				channeldetails.put("otpExpiryTime", primaryService.getPosthook().getChannelexpiryTime());
			}
			
			
			providerposthook.put("accountSID", primaryService.getPosthook().getProvideraccountSID());
			providerposthook.put("authToken", primaryService.getPosthook().getProviderauthToken());
			providerposthook.put("toRecipient", primaryService.getPosthook().getProvidertoRecipient());
			providerposthook.put("from", primaryService.getPosthook().getProviderfrom());
			
			hookObj.put("channel", primaryService.getPosthook().getProvider());
			hookObj.put(primaryService.getPosthook().getChannel(), channeldetails);
			hookObj.put(primaryService.getPosthook().getProvider(), providerposthook);
			
		}if( !(primaryService.getPrehook() == null)){
			JSONObject providerpreook = new JSONObject();
			
			if(!(channeldetails.has("otpLength"))){
				channeldetails.put("otpLength", primaryService.getPrehook().getChannellength());
				channeldetails.put("otpType", primaryService.getPrehook().getChanneltype());
				channeldetails.put("otpExpiryTime", primaryService.getPrehook().getChannelexpiryTime());
			}
			
			providerpreook.put("accountSID", primaryService.getPrehook().getProvideraccountSID());
			providerpreook.put("authToken", primaryService.getPrehook().getProviderauthToken());
			providerpreook.put("toRecipient", primaryService.getPrehook().getProvidertoRecipient());
			providerpreook.put("from", primaryService.getPrehook().getProviderfrom());
			
			hookObj.put("channel", primaryService.getPrehook().getProvider());
			hookObj.put(primaryService.getPrehook().getChannel(), channeldetails);
			hookObj.put(primaryService.getPrehook().getProvider(), providerpreook);
		}
		return hookObj;
		
	}
	public boolean isSuccess() {
		return success;
	}
	

}
