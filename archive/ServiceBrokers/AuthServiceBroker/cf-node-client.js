/**
 * CfNodeClient  Client application which will connect to the
 * cloud and do the equivalent cli command operations
 *
 */
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
'use strict';

var async = require('async');
var request = require('request');
var gistdb = require('./gistdb.js');
var config = require('./config.json');
var zipFolder = require('zip-folder');
var CfNodeClient = function () {
};


// this api will clone the code from git and pushes it to cloud foundry
CfNodeClient.prototype.pushApp = function (options, callback) {

    var endpoint = options.endpoint;
    var Apps = new (require("cf-nodejs-client")).Apps(endpoint);
    var Routes = new (require("cf-nodejs-client")).Routes(endpoint);
    var ServiceInstances = new (require("cf-nodejs-client")).ServiceInstances(endpoint);
    var ServiceBindings = new (require("cf-nodejs-client")).ServiceBindings(endpoint);
    var simpleGit = require('simple-git')(process.cwd());
    var path = process.cwd();
    var currentDir = process.cwd();


    var zip = function (callback) {
        console.log('*** Started Zipping ***');
        
        zipFolder(process.cwd() + config.microservicePath, process.cwd() +config. microservicePath+ '/' + 'application.zip', function (err, result) {
            if (err) {
                console.log("Error in zip : " + err);
                return callback(err);
            } else {
                return callback(null, result);
            }
        });

    };

    var push = function (app, callback) {
        var appGuid;
        var appOptions = {
            "name": options.appname,
            "space_guid": options.space_guid,
            "environment_json": options.environment_json
        };

        var token = options.token;
        console.log('*** Started Pushing the app ***');
        Apps.setToken(token);
        Apps.add(appOptions).then(function (result) {
            appGuid = result.metadata.guid;

            var routeOptions = {
                "domain_guid": options.domain_guid,
                "space_guid": options.space_guid,
                "host": options.appname
            };
            Routes.setToken(token);

            return Routes.add(routeOptions);
        }).then(function (result) {

            Apps.setToken(token);
            return Apps.associateRoute(appGuid, result.metadata.guid);
        }).then(function (result) {
            Apps.setToken(token);
            return Apps.upload(appGuid, process.cwd() + config.microservicePath + '/' + config.appZip, false);
        }).then(function (result) {
            console.log('In start');
            Apps.setToken(token);
            return Apps.start(appGuid);
        }).then(function (result) {
            console.log('In final result');
            return callback(null, result);
        }).catch(function (reason) {
            console.log("Error in pushing the app : " + reason);
            return callback(reason);

        });
    };

    async.waterfall([zip, push], function (err, result) {
        if (err) {
            console.log("Error in push app tasks : " + err);
            return callback(err);
        }
        else {
            return callback(null, result);
        }

    });
};

// this api will delete the service app from cloud foundry
CfNodeClient.prototype.deleteApp = function (options, callback) {
    console.log('********Inside deleteApp**********' + JSON.stringify(options));

    var finalAppGUID;
    var resultObj;

    var loginAPICall = function (deleteAppFinalCallback) {
        console.log('********Inside loginAPICall**********');
        var loginAPICallReqOptions = {
            url: config.loginProtocol + config.host + config.loginPath + config.username + config.pass + config.password,
            method: "POST",
            headers: {
                "Content-Type": config.contentType,
                Accept:  config.contentType,
                charset: config.charSet,
                Authorization: config.authorization
            }
        };
        request(loginAPICallReqOptions, function (err, result) {
            if (err) {
                console.log('********Inside loginAPICall err**********' + err);
                deleteAppFinalCallback(err);
            }
            else {
                console.log('********Inside loginAPICall response**********' + JSON.stringify(result));
                if (result.statusCode && result.statusCode == 200) {
                    console.log('********Inside loginAPICall IF**********');
                    deleteAppFinalCallback(null, 'bearer ' + JSON.parse(result.body).access_token);
                }
                else {
                    deleteAppFinalCallback(JSON.parse(result.body).description);
                }
            }
        });
    };

    var getData = function (loginResult, deleteAppFinalCallback) {
        console.log('********Inside getData**********' + loginResult);
        var getDatacallback = function (err, result) {
            if (err) {
                deleteAppFinalCallback(err);
            }
            else {
                var filesData = JSON.parse(result.body).files[config.oAuthJson].content;
                var contentArray = JSON.parse(filesData);
                var index = contentArray.findIndex(x => x.instance_id == options.instance_id);
                if (index == -1) {
                    deleteAppFinalCallback('Kindly provide the correct instanceid');
                }
                else {
                    resultObj = contentArray[index];
                    resultObj.accessToken = loginResult;
                    deleteAppFinalCallback(null, resultObj);
                }
            }
        };
        gistdb.getData(getDatacallback)
    };


    //get the app in particular space using appname
    var getAppDetails = function (getDataResultObj, deleteAppFinalCallback) {
        console.log('********Inside getAppDetails**********' + JSON.stringify(getDataResultObj));
        if (getDataResultObj) {
            var getAppDetailsReqOptions = {
                url: getDataResultObj.endpoint + "/v2/spaces/" + getDataResultObj.space_guid + "/apps?q=name:" + getDataResultObj.appname + "&inline-relations-depth=1",
                method: "GET",
                headers: {
                    Authorization: getDataResultObj.accessToken,
                    Accept: config.contentType,
                    "Content-Type": config.contentType
                }
            };
            console.log('********Inside getAppDetails Auth**********' + JSON.stringify(getAppDetailsReqOptions.url) + "***" + JSON.stringify(getAppDetailsReqOptions.Authorization));
            request(getAppDetailsReqOptions, function (err, result) {
                if (err) {
                    console.log('********Inside getAppDetails error **********' + err);
                    deleteAppFinalCallback(err);
                }
                else {
                    console.log('********Inside getAppDetails success**********' + JSON.stringify(result));
                    if (result.statusCode && result.statusCode == 200) {
                       
                        var JSONObj = JSON.parse(result.body);
                        if (JSONObj.resources.length > 0) {
                            finalAppGUID = JSONObj.resources[0].metadata.guid;
                            deleteAppFinalCallback(null, {
                                routeGUID: JSONObj.resources[0].entity.routes[0].metadata.guid,
                                accessToken: getDataResultObj.accessToken
                            });
                        }
                        else {
                            callback(null, 'Success');
                        }
                    }
                    else {
                        deleteAppFinalCallback(JSON.parse(result.body).description);
                    }
                }
            });
        }
        else {
            deleteAppFinalCallback('Kindly provide the correct instanceid');
        }
    };

    var deleteRoute = function (routeGUIDObj, deleteAppFinalCallback) {
        console.log('********Inside deleteRoute**********' + JSON.stringify(routeGUIDObj));
        var deleteRouteReqOptions = {
            url: resultObj.endpoint + "/v2/routes/" + routeGUIDObj.routeGUID + "?async=true",
            method: "DELETE",
            headers: {
                Authorization: routeGUIDObj.accessToken,
                Accept: config.contentType,
                "Content-Type": config.contentType
            }
        };
        request(deleteRouteReqOptions, function (err, result) {
            if (err) {
                console.log('********Inside deleteRoute err**********' + err);
                deleteAppFinalCallback(err);
            }
            else {
                console.log('********Inside deleteRoute response**********' + JSON.stringify(result));
                if (result.statusCode && result.statusCode == 202) {
                    console.log('********Inside deleteRoute NExt Callback value**********' + JSON.parse(result.body).metadata.guid);
                    var JSONOBJ = JSON.parse(result.body);
                    deleteAppFinalCallback(null, {
                        jobGUID: JSONOBJ.metadata.guid,
                        accessToken: routeGUIDObj.accessToken
                    });
                }
                else {
                    deleteAppFinalCallback(JSON.parse(result.body).description);
                }
            }
        });
    };


    var getJOBS = function (jobGUIDObj, deleteAppFinalCallback) {
        console.log('********Inside getJOBS**********' + JSON.stringify(jobGUIDObj));
        var getJOBSReqOptions = {
            url: resultObj.endpoint + "/v2/jobs/" + jobGUIDObj.jobGUID,
            method: "GET",
            headers: {
                Authorization: jobGUIDObj.accessToken,
                Accept: config.contentType,
                "Content-Type": config.contentType
            }
        };
        request(getJOBSReqOptions, function (err, result) {
            if (err) {
                console.log('********Inside getJOBS err**********' + err);
                deleteAppFinalCallback(err);
            }
            else {
                console.log('********Inside getJOBS response**********' + JSON.stringify(result));
                if (result.statusCode && result.statusCode == 200) {
                    var JSONObj = JSON.parse(result.body);
                    if (JSONObj.entity.status && JSONObj.entity.status == 'queued') {
                        getJOBS(jobGUIDObj, deleteAppFinalCallback);
                    }
                    else {
                        deleteApp(jobGUIDObj.accessToken, deleteAppFinalCallback);
                    }
                }
                else {
                    deleteAppFinalCallback(JSON.parse(result.body).description);
                }
            }
        });
    };


    var deleteApp = function (accessToken, deleteAppFinalCallback) {
        console.log('********Inside deleteApp**********');
        var deleteAppReqOptions = {
            url: resultObj.endpoint + "/v2/apps/" + finalAppGUID + "?async=true&recursive=true",
            method: "DELETE",
            headers: {
                Authorization: accessToken,
                Accept: config.contentType,
                "Content-Type": config.contentType
            }
        };
        request(deleteAppReqOptions, function (err, result) {
            if (err) {
                console.log('********Inside deleteApp err**********' + err);
                deleteAppFinalCallback(err);
            }
            else {
                console.log('********Inside deleteApp response**********' + JSON.stringify(result));
                if (result.statusCode && result.statusCode == 204) {
                    deleteAppFinalCallback(null, result);
                }
                else {
                    deleteAppFinalCallback(JSON.parse(result.body).description);
                }
            }
        });
    };

    var deleteAppFinalCallback = function (err, result) {
        if (err) {
            console.log('ERROR: ' + JSON.stringify(err));
            callback(err);
        }
        else {
            console.log('result: ' + JSON.stringify(result));
            callback(null, result);
        }
    };

    async.waterfall([loginAPICall, getData, getAppDetails, deleteRoute, getJOBS], deleteAppFinalCallback);
};


module.exports = CfNodeClient;
