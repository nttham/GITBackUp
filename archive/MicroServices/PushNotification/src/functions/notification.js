var async = require('async');
var constants = require('../constants/constants.json');
var sendFunction = require('./send-message');
var getTokens = require('./getTokens');
var dbConnector = require('../DAL/Connector.js');
var Devices = require('../functions/devices.js');
var settingsFunctions = require('../functions/Settings.js');
var ApnsCertificate  = require('../functions/ApnsCertificate');

var DALObj = require('../DAL/DAL.js');
var templateModelObj = require('../models/template');
// making collection names as global such that all functions can use the same collection names
var instanceCollectionName = constants.instance_collection_name || 'Instances';
var templateCollectionName = constants.template_collection_name || 'Templates';

//This method is for notifying devices.
exports.notifyDevices = function(results,req,callback) {
    var message = req.message;
    var deviceId = req.deviceId;

    if ( typeof deviceId  === 'undefined' ) {
        console.log("deviceId parameter missing in body JSON for type 'device'");
        callback(constants.error.msg_deviceId_missing);
    }
    else if ( deviceId.length === 0) {
        console.log("deviceId parameter missing in body JSON for type 'device'");
        callback(constants.error.msg_deviceId_missing);
    }
    else {

        var switchDB = function(onswitchDBCallback){
            switchToDB(results[0]["dbName"],req,results,onswitchDBCallback);
        };

        var finalCallback = function (err, result) {
            //afterResponse(dbInstance.dbConnection);
            if (err) {
                console.log(err);
                callback(err);

            }
            else {
                callback(null, constants.success.msg_notify_success)

            }
        };
        async.waterfall([switchDB,getTemplate,searchDevices,getTokenForAll,sendMessage],finalCallback);
    }
}

//This method is for notifying all devices.
exports.notifyBulk = function(results,req,callback) {
    var message = req.message;
    var deviceId = req.deviceId;

    //check for all bulkOptions. If everything is false then throw error
    if(req && req.bulkOptions && req.bulkOptions.apns === false && req.bulkOptions.gcm === false && req.bulkOptions.wns === false) {
        console.log(constants.error.bulk_all_platforms_disabled.message);
        callback(constants.error.bulk_all_platforms_disabled);
    }
    else {

        var switchDB = function(onswitchDBCallback){
            switchToDB(results[0]["dbName"],req,results,onswitchDBCallback);
        };

        var finalCallback = function (err, result) {
            //afterResponse(dbInstance.dbConnection);
            if (err) {
                console.log(err);
                callback(err);

            }
            else {
                callback(null, constants.success.msg_notify_success)

            }
        };
        async.waterfall([switchDB,getTemplate,getBulkDevices,getTokenForAll,sendMessage],finalCallback);
    }
}

//This method is for notifying a channel's devices.
exports.notifyChannel = function(results,req,callback) {
    var message = req.message;
    var deviceId = req.deviceId;

    if ( typeof deviceId  === 'undefined' ) {
        console.log("deviceId parameter missing in body JSON for type 'device'");
        callback(constants.error.msg_deviceId_missing);
    }
    else if ( deviceId.length < 1) {
        console.log("deviceId parameter missing in body JSON for type 'device'");
        callback(constants.error.msg_deviceId_missing);
    }
    else {

        var switchDB = function(onswitchDBCallback){
            switchToDB(results[0]["dbName"],req,results,onswitchDBCallback);
        };

        var finalCallback = function (err, result) {
            //afterResponse(dbInstance.dbConnection);
            if (err) {
                console.log(err);
                callback(err);

            }
            else {
                callback(null, constants.success.msg_notify_success)

            }
        };
        async.waterfall([switchDB,getTemplate,searchDevices,getTokenForAll,sendMessage],finalCallback);
    }
}

var getBulkDevices = function(dbInstance,req,results,template,onGetBulkDevicesCallback) {
    Devices.getBulkDevices(dbInstance["dbConnection"],req, function(err,docs){
        if(err) {
            onGetBulkDevicesCallback(err);
        }
        else {
            onGetBulkDevicesCallback(null,docs,req,dbInstance,results,template);
        }
    });
}

//This method will internally call the APNS, WNS and GCM providers for sending push notification.
var sendMessage = function(device,req,dbInstance,results,template,callback){
    //This Api Will send message for APNS
    var message = req.message;
    var notifyAPNS = function(callback){

        if(device.apnsDevices.length){
            var apnsSettings;

            if(req.settings && req.settings.apns) {
                apnsSettings = req.settings.apns;
            }

            var onSettings = function(err,settingsObj){
                if(!err){
                    //Check if any settings info found
                    if(settingsObj.length && settingsObj[0].apnsCertificate && settingsObj[0].apnsPassword){

                        //fetch the Apns Certificate with the fileName and dbInstance as input
                        ApnsCertificate.getCertificate (settingsObj[0].apnsCertificate,dbInstance,function(err,options){
                            if (err) {
                                console.log("error      "+err);
                                return callback(null,true);
                            }
                            else{

                                options.passphrase = settingsObj[0].apnsPassword;

                                //Call function to send push notification APNS
                                sendFunction.notifyAPNSDevices(message, device.apnsDevices, apnsSettings, options,template,dbInstance, function(response) {
                                    console.log("Response of APNS notification : ",response);
                                });
                                return callback(null,true);

                            }
                        });


                    }
                    else{
                        console.log("No Settings available for APNS");
                        return callback(null,true);
                    }


                }
                else{
                    console.log("error onSettings "+err);
                    return callback(null,true);
                }

            };
            //fetch the settings info for the given instanceId
            settingsFunctions.getSettings(dbInstance["dbConnection"],results[0]["instanceID"],onSettings);


        }
        else{
            console.log("No Devices Found for APNS");
            return callback(null,true);
        }

    };//end of notifyAPNS()


    //This Api Will send message for GCM
    var notifyGCM = function(obj,callback){

        //Check if any device tokens found. If not throw log it in logger
        if(device.gcmDevices.length ) {

            var gcmSettings;
            if(req.settings) {
                if(req.settings.gcm) {
                    gcmSettings = req.settings["gcm"];
                }
            }
            var gcmKey ;

            //Check if any settings info found
            var onSettings =function(err,settingsObj) {
                if(!err){
                    if (settingsObj.length && settingsObj[0]["gcmApiKey"] ) {
                        gcmKey = settingsObj[0]["gcmApiKey"];
                        //Call function to send push notification GCM
                        sendFunction.notifyGCMDevices(message, device.gcmDevices,gcmSettings, gcmKey,template,function(response) {
                            console.log("Response of GCM notification : ",response);
                        });
                        return callback(null, device);
                    }
                    else{
                        console.log("No Settings available for GCM  ");
                        return callback(null,true);

                    }

                }
                else{
                    console.log("error onSettings "+err);
                }     return callback(null,true);

            };
            //fetch the settings info for the given instanceId
            settingsFunctions.getSettings(dbInstance["dbConnection"],results[0]["instanceID"],onSettings);

        }else{
            console.log("No Devices Found for GCM ");
            return callback(null,device);
        }

    }; //end of notifyGCM


    //This Api Will send message for GCM
    var notifyWNS = function(obj,callback){

        //Check if any device tokens found. If not throw log it in logger
        if(device.wnsDevices.length && req.wns !== false) {

            var wnsSettings;
            if(req.settings) {
                if(req.settings.wns) {
                    wnsSettings = req.settings["wns"];
                }
            }

            //Check if any settings info found
            var onSettings =function(err,settingsObj) {
                if(!err){
                    if (settingsObj.length && settingsObj[0]["wnsClientId"] &&  settingsObj[0]["wnsClientSecret"]) {
                        //if (constants.wns_client_id && constants.wns_client_secret) {
                        var options = {
                            client_id: settingsObj[0]["wnsClientId"],
                            client_secret: settingsObj[0]["wnsClientSecret"]
                        };
                        //Call function to send push notification WNS
                        sendFunction.notifyWNSDevices(message,device.wnsDevices,wnsSettings,options,template,function(response) {
                            console.log("Response of WNS notification : ",response);
                        });
                        return callback(null,true);

                    }
                    else{
                        console.log("No Settings available for WNS");
                        return callback(null,true);
                    }
                }
                else{
                    console.log("error onSettings "+err);
                }
                return callback(null,true);

            };
            //fetch the settings info for the given instanceId
            settingsFunctions.getSettings(dbInstance["dbConnection"],results[0]["instanceID"],onSettings);

        }
        else{
            console.log("No devices found for WNS");
            return callback(null,true);
        }
    };

    var waterfallCallback = function(err,results){
        return callback(null,true);
    };

    // Here the control flow for sending message is in series
    async.waterfall([notifyAPNS,notifyGCM,notifyWNS],waterfallCallback);


}; //end of sendMessage


//This method is for searching devices.
var searchDevices = function (dbInstance,req,results,template,onsearchDevicesCallback) {
    Devices.searchDevices(dbInstance["dbConnection"],req.deviceId, function(err,docs){
        if(err) {
            onsearchDevicesCallback(err);
        }
        else {
            onsearchDevicesCallback(null,docs,req,dbInstance,results,template);
        }
    });
}

//This method is for switching database.
var switchToDB = function(dbName,req,results,onSwitchToDBCallback) {
    dbConnector.switchDB(dbName,function (err,dbInstance) {
        if (!err && dbInstance) {
            onSwitchToDBCallback(null,dbInstance,req,results);
        }
        else if (err) {
            console.error("Something went wrong while switching to db for push notify. DBname :"+dbName);
            console.error(err);
            onSwitchToDBCallback(err);
        }
        else {
            console.error("Something went wrong while switching to db for push notify. DBname :"+dbName);
            onSwitchToDBCallback({"error": "Something went wrong while switching to db for push notify"});
        }
    });
};

//This method is for getting templates info.
var getTemplate = function(dbInstance,req,results,onGetTemplateCallback) {
    if(req.templateId) {
        DALObj.getData(dbInstance["dbConnection"],templateCollectionName,templateModelObj,{templateId:req.templateId},function(err,template){
            if(err) {
                onGetTemplateCallback(err);
            }
            else {
                if (template && template.length === 1) {
                    onGetTemplateCallback(null,dbInstance,req,results,template);
                }
                else {
                    onGetTemplateCallback(constants.error.msg_invalid_templateId);
                }
            }
        })
    }
    else {
        var template = [];
        onGetTemplateCallback(null,dbInstance,req,results,template);
    }
}

//This method is for getting all deviceToken related to APNS, GCM , WNS.
var getTokenForAll = function(docs,req,dbInstance,results,template,onGetTokenForAllCallback){
    if(docs.length){
        getTokens.getTokens(docs,function(tokens) {
            var Devices = {};
            Devices.apnsDevices=tokens.apnsDevices;
            Devices.gcmDevices=tokens.gcmDevices;
            Devices.wnsDevices=tokens.wnsDevices;
            console.log("Total number of devices found : "+(Devices.apnsDevices.length+Devices.gcmDevices.length+Devices.wnsDevices.length));
            console.log("Total number of APNS devices found : "+Devices.apnsDevices.length);
            console.log("Total number of GCM devices found : "+Devices.gcmDevices.length);
            console.log("Total number of WNS devices found : "+Devices.wnsDevices.length);
            return onGetTokenForAllCallback(null,Devices,req,dbInstance,results,template);
        });

    }
    else{
        console.log(constants.error.msg_no_devices_found.message);
        onGetTokenForAllCallback(constants.error.msg_no_devices_found);
    }

};//end of getTokenForAll()
