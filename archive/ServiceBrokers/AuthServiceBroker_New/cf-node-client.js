/**
 * CfNodeClient  Client application which will connect to the
 * cloud and do the equivalent cli command operations
 *
 */

'use strict';

var CommonUtilsObj = require('./middleLayer/commonUtils.js');
var commonUtilsObj = new CommonUtilsObj();
var config = require('./config.json');
var GistDB = require('./gistdb.js');
var gistdb = new GistDB();

var async = require('async');
var uuid = require('uuid');

var CfNodeClient = function () {
};


// this api will create the service instance in cloud foundry
CfNodeClient.prototype.createServiceInstance = function (request,callback) {
    console.log("create-service params : " + JSON.stringify(request.params));

    // pushes the app to cloud foundry with the appName given by the client
    var pushAppEnvJSON = function (pushHandler) {
        // creating the apikey for authentication
        request.params.parameters.apiKey = uuid.v1();
        request.params.parameters.secretKey = uuid.v1();
        request.params.parameters.instanceID = request.params.id;

        //forming the environment_json which will be passed to the microservice app while pushing
        //it to the cloud foundry which will inturn goes to its vcaps
        if (request.params.parameters.environment_json["configuration"]["facebook"] ) {

            request.params.parameters.environment_json.facebook_callbackURL = config.protocol + request.params.parameters.appname + "." + config["host"] + config["facebookCallback"] ;
        }
        if (request.params.parameters.environment_json["configuration"]["google"] ) {

            request.params.parameters.environment_json.google_callbackURL = config["protocol"] + request.params.parameters.appname + "." + config["host"] + config["googleCallback"];
        }
        if (request.params.parameters.environment_json["configuration"]["twitter"]) {
            request.params.parameters.environment_json.twitter_callbackURL = config["protocol"] + request.params.parameters.appname + "." + config["host"] + config["twitterCallback"];
        }
        if (request.params.parameters.environment_json["configuration"]["linkedin"] ) {

            request.params.parameters.environment_json.linkedin_callbackURL =config["protocol"]  + request.params.parameters.appname + "." + config["host"] + config["linkedinCallback"];
        }
        if (request.params.parameters.environment_json["configuration"]["custom"]) {

            request.params.parameters.environment_json.custom_callbackURL =config["httpsprotocol"]  + request.params.parameters.appname + "." + config["host"] + config["customCallback"];
        }
        pushApp(request.params.parameters, pushHandler);
    };


    //updating db with required information
    var updateDB = function (obj, finalCallback) {
        console.log('****App Instane ID in push response app guid****' + obj.metadata.guid);
        var reqObj = {
            instance_id: request.params.id,
            space_guid: config["space_guid"],
            host: config["host"],
            appname: request.params.parameters.appname,
            endpoint: config["endpoint"],
            facebook: request.params.parameters.environment_json.facebook_callbackURL,
            google: request.params.parameters.environment_json.google_callbackURL,
            linkedin: request.params.parameters.environment_json.linkedin_callbackURL,
            twitter: request.params.parameters.environment_json.twitter_callbackURL,
            custom:request.params.parameters.environment_json.custom_callbackURL,
            apiKey: request.params.parameters.apiKey
        };
        var Oncallback = function(err,result){
            if(err){
                return finalCallback(err);
            }
            else{
                return finalCallback(null,obj.appGuid);
            }
        };

        //updating required data in GIST
        gistdb.editData(reqObj, 'OAuth.json', Oncallback);
    };

    //callback for async calls
    var finalCallback = function (err, result) {
        if (err) {
            callback(err);

        }
        else {
            callback(null,result);
        }
    };
    // If you wanted to provision other things add the sequential task here
    async.waterfall([pushAppEnvJSON, updateDB], finalCallback);
};


// this api will pushes app which has been cloned in pre install to cloud foundry
var pushApp = function (options, callback) {
    //forming JSON object for login request
    options["url"] = config["loginProtocol"] + config["username"] + config["pass"] + config["password"];
    options["contentType"] = config["contentType"];
    options["charSet"] = config["charSet"];
    options["authorization"] = config["authorization"];
    options["endpoint"] = config["endpoint"];
    options["domain_guid"] = config["domain_guid"];
    options["space_guid"] = config["space_guid"];
    options["zipFilePath"] = process.cwd()+ "/" + options["instanceID"] + "/" + config["zipFilePath"];
    options["gitURL"] = config["gitURL"];
    options["directoryName"] = options["instanceID"];
    options["directoryPath"] = process.cwd()+ "/" + options["instanceID"];
    options["memory"] = config.memory;
    options["disk_quota"] = config.disk_quota;

    var pushAppCallback = function (error,result) {
        if (error) {
            callback(error);
        }
        else {
            callback(null,result);
        }
    };
    commonUtilsObj.pushApp(options,pushAppCallback);
};

// this api will delete the service instance in cloud foundry
CfNodeClient.prototype.deleteServiceInstance = function (request,callback) {
    console.log('********Inside cf delete-service**********');
    var deleteAppCall = function (callback) {
        var options = {
            instance_id: request.params.id
        };
        deleteApp(options, callback);
    };

    //deleting the instance data from GIST
    var deleteData = function (callback) {
        console.log('******inside deleteServiceInstance deleteData****');
        gistdb.deleteData({instance_id: request.params.id}, 'OAuth.json', finalDeleteCallback);
    };

    var finalDeleteCallback = function (err, result) {
        if (err) {
            callback(err);

        }
        else {
            callback(null,result);
        }
    };

    async.waterfall([deleteAppCall, deleteData], finalDeleteCallback);
};


// this api will delete the service app from cloud foundry
var deleteApp = function (options, callback) {

    //getting the instance ID to delete the service app
    console.log('********Inside deleteApp**********' + options);
    var getDatacallback = function (err, result) {
        if (err) {
            callback(err);
        }
        else {
            var filesData = JSON.parse(result.body).files[config["oAuthJson"]].content;
            var contentArray = JSON.parse(filesData);
            var index = contentArray.findIndex(x => x.instance_id == options["instance_id"]);
            if (index == -1) {
                callback('Kindly provide the correct instanceid');
            }
            else {
                var appObjToDelete = contentArray[index];
                appObjToDelete["url"] = config["loginProtocol"] + config["username"] + config["pass"] + config["password"];
                appObjToDelete["contentType"] = config["contentType"];
                appObjToDelete["charSet"] = config["charSet"];
                appObjToDelete["authorization"] = config["authorization"];

                //calling the commonutils delete app method.
                commonUtilsObj.deleteApp(appObjToDelete, function (error,result) {
                    if (error) {
                        callback(error);
                    }
                    else {
                        callback(null,result);
                    }
                });
            }
        }
    };
    gistdb.getData(getDatacallback);
};


// router for binding the service to an app when a cf bind-service is called
CfNodeClient.prototype.bindService = function (request,callback) {

    //getting instance details from GIST.
    var getInstanceDetails = function (finalCallback) {
        var getDatacallback = function (err, result) {
            console.log('**came to getDatacallback**');
            if (err) {
                console.log(err);
                return finalCallback(err);
            }
            else {
                var filesData = JSON.parse(result.body).files['OAuth.json'].content;
                var contentArray = JSON.parse(filesData);
                var index = contentArray.findIndex(x => x.instance_id == request.params.instance_id);

                if (index == -1) {
                    console.log('Kindly provide the correct instanceid');
                    return finalCallback('Kindly provide the correct instanceid');
                }
                else {
                    var resultObj = contentArray[index];

                    console.log('**getDatacallback resultObj**'+JSON.stringify(resultObj));
                    return finalCallback(null, resultObj);
                }
            }
        };
        gistdb.getData(getDatacallback);
    };


    //setting the environment variables
    var setEnvironmentVariables = function (instanceObj, finalCallback) {
        console.log('**came to setEnvironmentVariables**');
        try {
            var respData = {
                "credentials": {
                    "serviceUrl": config["protocol"] + instanceObj["appname"] + "." + instanceObj["host"]
                }
            };

            if (instanceObj.facebook) {
                respData.credentials.facebook_callbackURL = instanceObj["facebook"];
            }
            if (instanceObj.google) {
                respData.credentials.google_callbackURL = instanceObj["google"];
            }
            if (instanceObj.twitter) {
                respData.credentials.twitter_callbackURL = instanceObj["twitter"];
            }
            if (instanceObj.linkedin) {
                respData.credentials.linkedin_callbackURL = instanceObj["linkedin"];
            }
            if (instanceObj.custom) {
                respData.credentials.custom_callbackURL = instanceObj["custom"];
            }

            respData.credentials.apiKey = instanceObj["apiKey"];

            console.log('Binding env variables' + JSON.stringify(respData));

            return finalCallback(null, respData);
        }
        catch (err) {
            return finalCallback(err);
        }
    };
    //callback
    var finalCallback = function (err, result) {
        console.log('**came to Binding finalcallback**');
        if (err) {
            return callback(err);
        }
        else {
            return callback(null,result);
        }
    };

    async.waterfall([getInstanceDetails, setEnvironmentVariables], finalCallback);
};

module.exports = CfNodeClient;
