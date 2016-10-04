/**
 * CfNodeClient  Client application which will connect to the
 * cloud and do the equivalent cli command operations
 *
 */
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

'use strict';

var async = require('async');
var request = require('request');
var GistDB = require('./gistdb.js');
var gistdb = new GistDB();

var config = require('./config.json');
var uuid = require('uuid');
var CommonUtilsObj = require('./middleLayer/commonUtils.js');
var commonUtilsObj = new CommonUtilsObj();

var CfNodeClient = function () {
};


// this api will create the service instance in cloud foundry
CfNodeClient.prototype.createServiceInstance = function (request,callback) {
    console.log("create-service params : " + JSON.stringify(request.params));

    // pushes the app to cloud foundry with the appName given by the client
    var apiKey = uuid.v1();
    var pushAppCall = function (pushHandler) {
        request.params.parameters.instanceID = request.params.id;
        request.params.parameters.apiKey = apiKey;
        pushApp(request.params.parameters, pushHandler);
    };

    var baseReqObj = { // updating in GIST/DB
        instance_id: request.params.id,
        space_guid: config["space_guid"],
        host: config["host"],
        endpoint: config["endpoint"],
        apiKey: apiKey,
        log_provider: request.params.parameters.environment_json.logger.name,
        appname: request.params.parameters.appname
    };

    var APP_GUID;
    var updateDB = function (obj, callback) {
        console.log('**** updateDB push response app guid****' + obj["metadata"]["guid"]);
        APP_GUID = obj["metadata"]["guid"];
        gistdb.editData(baseReqObj, 'logger.json', callback);
    };

    //callback for async calls
    var finalCallback = function (err, result) {
        console.log('APP GUID: ',APP_GUID);
        if (err) {
            callback(err);
        }
        else {
            callback(null,result);
        }
    };

    async.waterfall([pushAppCall, updateDB], finalCallback);
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
    options["loggerbaseurl"] = config["loggerbaseurl"];
    options["loggerapikey"] = config["loggerapikey"];


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
        gistdb.deleteData({instance_id: request.params.id}, 'logger.json', finalDeleteCallback);
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
            var filesData = JSON.parse(result.body).files[config["loggerJSON"]].content;
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

    console.log("bind-service params : " + JSON.stringify(request.params));

    //getting instance details from GIST.
    var getInstanceDetails = function (callback)
    {
        var getDatacallback = function (err, result) {
            if (err) {
                console.log(err)
            }
            else {
                var filesData = JSON.parse(result.body).files['logger.json'].content;
                var contentArray = JSON.parse(filesData);
                //var index = contentArray.findIndex(x => x.instance_id == req.params.instance_id);

                var index;
                for(index = 0; index < contentArray.length; index++) {
                    var detail = contentArray[index];
                    if(detail.instance_id === request.params.instance_id) {
                        break;
                    }
                }
                console.log('Index: ',index);
                if (index == -1) {
                    console.log('Kindly provide the correct instanceid');
                    return callback('Kindly provide the correct instanceid');
                }
                else {
                    var resultObj = contentArray[index];

                    console.log('Result OBJ: ',resultObj);
                    return callback(null, resultObj);
                }
            }
        };
        gistdb.getData(getDatacallback);
    };

    //setting the environment variables
    var setEnvironmentVariables = function (instanceObj, callback)
    {
        console.log('Start setting env using data: ',instanceObj);
        try {
            var respData = {
                "credentials": {}
            };
            respData.credentials.apiKey = instanceObj.apiKey;
            respData.credentials.serviceurl = config["protocol"] + instanceObj.appname + "." + config["host"];
            console.log('final response data: ',respData);
            return callback(null, respData);
        }
        catch (err) {
            return callback(err);
        }
    };

    //callback
    var finalCallback = function (err, result) {
        if (err) {
            callback(err);
        }
        else {
            callback(null,result);
        }
    };
    async.waterfall([getInstanceDetails, setEnvironmentVariables], finalCallback);
};



module.exports = CfNodeClient;
