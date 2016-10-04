/**
 * Created by 365920 on 8/23/2016.
 */

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

var request = require('request');
var async = require('async');
var restify = require('restify');
var fs = require('fs');
var exec = require('child_process').exec;
var configVals;

var CommonUtils = function () {
};

//this call is to get the accesstoken for the selected platform
var loginAPICall = function (callback) {
    console.log('********Inside loginAPICall**********');
    var loginAPICallReqOptions = {
        url: configVals["url"],
        method: "POST",
        headers: {
            "Content-Type": configVals["contentType"],
            Accept: configVals["contentType"],
            charset: configVals["charSet"],
            Authorization: configVals["authorization"]
        }
    };
    request(loginAPICallReqOptions, function (err, result) {
        if (err) {
            console.log('********Inside loginAPICall err**********' + err);
            callback(err);
        }
        else {
            console.log('********Inside loginAPICall response**********' + JSON.stringify(result));
            if (result.statusCode && result.statusCode == 200) {
                console.log('********Inside loginAPICall IF**********' + result.body);
                //deleteAppFinalCallback(null, 'bearer ' + JSON.parse(result.body).access_token);
                callback(null, JSON.parse(result.body));
            }
            else {
                callback(JSON.parse(result.body).description);
            }
        }
    });
};


//his api will pushes app which has been cloned in pre install to cloud foundry
//In this first we will get the accesstoken using login call and then push the app to cloud foundry.
CommonUtils.prototype.pushApp = function (options, callback) {

    var endpoint = options["endpoint"];
    var Apps = new (require("cf-nodejs-client")).Apps(endpoint);
    var Routes = new (require("cf-nodejs-client")).Routes(endpoint);
    var ServiceInstances = new (require("cf-nodejs-client")).ServiceInstances(endpoint);
    var ServiceBindings = new (require("cf-nodejs-client")).ServiceBindings(endpoint);
    var path = process.cwd();
    var currentDir = process.cwd();
    var appGuid;
    configVals = options;

    //this is to create directories
    var createDirectories = function (loginResults, callback) {
        console.log("***In createDirectories***");
        var child = exec("cd ~", function (err, result) {
            if (err) {
                console.log("error in changing the home directory command");
                callback(err);
            }
            else {
                console.log("successfully changed to home directory and creating instanceID directory");
                child = exec("mkdir " + options["directoryName"], function (err, result) {
                    if (err) {
                        console.log("error in creating the instanceID directory command");
                        callback(err);
                    }
                    else {
                        console.log("successfully created the instanceID directory");
                        callback(null, loginResults);
                    }
                });
            }
        });
    };

    //this is to download the zipped file from github.
    var downloadZipFile = function (loginResults, callback) {
        console.log("***In downloadZipFile***GITURL:" + options["gitURL"] + "***zipFilePath:" + options["zipFilePath"]);

        var getOptions = {
            url: options["gitURL"],
            method: 'GET',
            headers: {
                'Authorization': 'token d67e03ec92d6ddd812495d7c54c84d0002fa97a2',
            }
        };

        //request to download the file
        request(getOptions)
            .pipe(fs.createWriteStream(options["zipFilePath"]))
            .on('close', function () {
                console.log('**File written**');
                callback(null, loginResults);
            }).on('error', function (err) { // Handle errors
                console.log('**File written error**');
                callback(err.message);
            });
    };

    //to push the app to cloudfoundry
    var push = function (app, callback) {
        var configJSON = {};
        configJSON["config"] = options.environment_json;
        configJSON["apiKey"] = options.apiKey;
        configJSON["secretKey"] = options.secretKey;

        var appOptions = {
            "name": options.appname,
            "space_guid": options["space_guid"],
            "environment_json": configJSON,
            "memory": options["memory"],
            "disk_quota": options["disk_quota"]
        };

        var token = app;//options.token;
        console.log('*** Started Pushing the app options***' + JSON.stringify(options));
        console.log('*** Started Pushing the app token ***' + JSON.stringify(token));
        Apps.setToken(token);
        Apps.add(appOptions).then(function (result) {
            appGuid = result.metadata.guid;

            var routeOptions = {
                "domain_guid": options["domain_guid"],
                "space_guid": options["space_guid"],
                "host": options.appname
            };
            Routes.setToken(token);

            return Routes.add(routeOptions);
        }).then(function (result) {

            Apps.setToken(token);
            return Apps.associateRoute(appGuid, result.metadata.guid);
        }).then(function (result) {
            Apps.setToken(token);
            return Apps.upload(appGuid, options["zipFilePath"], false);
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

    //this is to delete the created directory
    var deleteFolderRecursive = function () {
        console.log("*****In deleteFolderRecursive******");
        if (fs.existsSync(options["directoryPath"])) {
            fs.readdirSync(options["directoryPath"]).forEach(function (file, index) {
                //var currentPath = path + "/" + file;
                if (fs.lstatSync(options["zipFilePath"]).isDirectory()) { // recurse
                    deleteFolderRecursive();
                } else { // delete file
                    fs.unlinkSync(options["zipFilePath"]);
                }
            });
            fs.rmdirSync(options["directoryPath"]);
            console.log("*******In end of deleteFolderRecursive...calling final callback******");
        }
    };

    //sequential execution of function calls
    async.waterfall([loginAPICall, createDirectories, downloadZipFile, push], function (err, result) {

        deleteFolderRecursive();
        if (err) {
            console.log("Error in push app tasks : " + err);
            return callback(err);
        }
        else {
            result.appGuid = appGuid;
            return callback(null, result);
        }

        //deleteFolderRecursive({error:err,result:result}, pushFinalCallback);
    });
};

//this is to delete the service app which has been pushed while creating the instance.
CommonUtils.prototype.deleteApp = function (objToDelete, callback) {
    console.log('********Inside deleteApp**********' + JSON.stringify(objToDelete));

    var finalAppGUID;
    configVals = objToDelete;

    //get the app in particular space using appname
    var getAppDetails = function (loginResult, deleteAppFinalCallback) {
        console.log('********Inside getAppDetails**********' + JSON.stringify(loginResult));
        if (objToDelete) {
            var getAppDetailsReqOptions = {
                url: objToDelete.endpoint + "/v2/spaces/" + objToDelete.space_guid + "/apps?q=name:" + objToDelete.appname + "&inline-relations-depth=1",
                method: "GET",
                headers: {
                    Authorization: 'bearer ' + loginResult.access_token,
                    Accept: objToDelete["contentType"],
                    "Content-Type": objToDelete["contentType"]
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
                                accessToken: 'bearer ' + loginResult.access_token
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

    //deleting the route
    var deleteRoute = function (routeGUIDObj, deleteAppFinalCallback) {
        console.log('********Inside deleteRoute**********' + JSON.stringify(routeGUIDObj));
        var deleteRouteReqOptions = {
            url: objToDelete.endpoint + "/v2/routes/" + routeGUIDObj.routeGUID + "?async=true",
            method: "DELETE",
            headers: {
                Authorization: routeGUIDObj.accessToken,
                Accept: objToDelete["contentType"],
                "Content-Type": objToDelete["contentType"]
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
            url: objToDelete.endpoint + "/v2/jobs/" + jobGUIDObj.jobGUID,
            method: "GET",
            headers: {
                Authorization: jobGUIDObj.accessToken,
                Accept: objToDelete["contentType"],
                "Content-Type": objToDelete["contentType"]
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


    //delete app
    var deleteApp = function (accessToken, deleteAppFinalCallback) {
        console.log('********Inside deleteApp**********');
        var deleteAppReqOptions = {
            url: objToDelete.endpoint + "/v2/apps/" + finalAppGUID + "?async=true&recursive=true",
            method: "DELETE",
            headers: {
                Authorization: accessToken,
                Accept: objToDelete["contentType"],
                "Content-Type": objToDelete["contentType"]
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

    //sequential execution of function calls
    async.waterfall([loginAPICall, getAppDetails, deleteRoute, getJOBS], deleteAppFinalCallback);
};


// To check the consistency of configurations
//Obj will contains the "catalog" & "plans" details of broker.
CommonUtils.prototype.checkConsistency = function (obj) {
    var i, id, p, msg, plans, catalog;

    catalog = obj["catalog"];
    plans = obj["plans"];

    for (i = 0; i < catalog.services.length; i += 1) {
        for (p = 0; p < catalog.services[i].plans.length; p += 1) {
            id = catalog.services[i].plans[p].id;
            if (!plans.hasOwnProperty(id)) {
                msg = "ERROR: plan '" + catalog.services[i].plans[p].name + "' of service '" + catalog.services[i].name + "' is missing a specification.";
                throw new Error(msg);
            }
        }
    }
};

//to check the apiversion
//version is the object which contains major & minor. ex: {'major': 2, 'minor': 4}
CommonUtils.prototype.apiVersionChecker = function (version) {
    var header = 'x-broker-api-version';
    return function (request, response, next) {
        if (request.headers[header]) {
            var pattern = new RegExp('^' + version.major + '\\.\\d+$');
            if (!request.headers[header].match(pattern)) {
                console.log('Incompatible services API version: ' + request.headers[header]);
                response.status(412);
                next(new restify.PreconditionFailedError('Incompatible services API version'));
            }
        } else {
            console.log(header + ' is missing from the request');
        }
        next();
    };
};

//To authenticate the service broker.
//credentials is the object which contains usename & password of broker to authenticate
CommonUtils.prototype.authenticate = function (credentials) {
    return function (request, response, next) {
        if (credentials.authUser || credentials.authPassword) {
            if (!(request.authorization && request.authorization.basic && request.authorization.basic.username === credentials.authUser && request.authorization.basic.password === credentials.authPassword)) {
                response.status(401);
                response.setHeader('WWW-Authenticate', 'Basic "realm"="auth-service-broker"');
                next(new restify.InvalidCredentialsError('Invalid username or password'));
            } else {
                // authenticated!
            }
        } else {
            // no authentication required.
            next();
        }
        next();
    };
};

module.exports = CommonUtils;