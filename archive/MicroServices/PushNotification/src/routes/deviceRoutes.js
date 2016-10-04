var constants = require('../constants/constants.json');
var devicesFunction = require('../functions/devices');
var notification = require('../functions/notification');
var coreDBConnectorObj = require("../DAL/Connector.js");
var authValidationObj = require('../functions/authenticateAPI.js');

module.exports = function(app) {

    //Registering the devices.
    //This needs deviceId, deviceToken, Platform as mandatory inputs
    //On response if device already exists return error else return success
    app.post('/devices', function (req, res) {

        var deviceRegCallback = function (error, results) {
            if (error) {
                res.status(400).send(error);
            }
            else {
                if (results.length) {
                    //registering the device once authorised
                    var dbRecordObj = results[0];

                    var deviceId = req.body.deviceId;
                    var deviceToken = req.body.deviceToken;
                    var platform = req.body.platform.toUpperCase();
                    var createdMode = "API";
                    var userId = "";
                    var isBlackListed = false;

                    //Check for mandatory parameters and also check if they are not null values
                    if (typeof deviceId === 'undefined' || typeof deviceToken === 'undefined' || typeof platform === 'undefined') {

                        console.log(constants.error.msg_invalid_param);
                        res.status(constants.error.msg_invalid_param.code).send(constants.error.msg_invalid_param);

                    } else if (!deviceId.trim() || !deviceToken.trim() || !platform.trim()) {

                        console.log(constants.error.msg_empty_param);
                        res.status(constants.error.msg_empty_param.code).send(constants.error.msg_empty_param);

                    } else if (req.body.isBlackListed && req.body.isBlackListed !== true && req.body.isBlackListed !== false) {

                        console.log(constants.error.msg_invalid_blacklist);
                        res.status(constants.error.msg_invalid_blacklist.code).send(constants.error.msg_invalid_blacklist);

                    } else if (platform === "APNS" || platform === "GCM" || platform === "WNS") {


                        //createdMode by default is API. If exists in body pick that value
                        if (req.body.createdMode) {
                            createdMode = req.body.createdMode;
                        }
                        //userId by default is empty string. If exists in body pick that value
                        if (req.body.userId) {
                            userId = req.body.userId;
                        }
                        //isBlackListed by default is true. If exists in body pick that value
                        if (req.body.isBlackListed) {
                            isBlackListed = req.body.isBlackListed;
                        }

                        //TODO :: pass the dynamic DB instanceName created in dbName
                        var dbName = dbRecordObj["dbName"];

                        //This method will create a DB connection with db name as dbName
                        coreDBConnectorObj.switchDB(dbName, function (dbError, dbInstance) {
                            if (dbError) {
                                res.status(400).send(dbError);
                            }
                            else {
                                var deviceDetails = {
                                    userId: userId,
                                    deviceId: deviceId,
                                    deviceToken: deviceToken,
                                    platform: platform,
                                    createdMode: createdMode,
                                    isBlackListed: isBlackListed
                                };
                                //Register device. Storing into db
                                devicesFunction.register(dbInstance["dbConnection"], deviceDetails, function (result) {
                                    //deleting the DB connection before responding to the client
                                    //afterResponse(dbInstance.dbConnection);

                                    res.status(result.code).send(result);
                                });
                            }
                        })

                    } else {
                        //Return error for invalid params
                        console.log(constants.error.msg_invalid_platform);
                        res.status(constants.error.msg_invalid_platform.code).send(constants.error.msg_invalid_platform);
                    }
                }
                else {
                    //sending error if it is unauthorised
                    res.status(401).send({"error": "Unauthorised request"});
                }
            }
        };

        authValidationObj.authenticateAPI(req, deviceRegCallback);

    });//end of post method /devices

    //Return all the registered devices from db
    app.get('/devices', function (req, res) {

        var getDevicesCallback = function (error, results) {
            if (error) {
                res.status(404).send(error);
            }
            else {
                if (results.length) {
                    //TODO :: pass the dynamic DB instanceName created in dbName
                    var dbName = results[0]["dbName"];


                    coreDBConnectorObj.switchDB(dbName, function (err, dbInstance) {
                        devicesFunction.listDevices(dbInstance["dbConnection"],req, function (err, result) {
                            if (err) {
                                console.log("get devices API => error in getting the devices from db : " + JSON.stringify(err));
                                res.status(result.code).send(result);
                            }
                            else {
                                res.json(result);
                            }
                        });
                    })
                }
                else {
                    //sending error if it is unauthorised
                    res.status(401).send({"error": "Unauthorised request"});
                }
            }
        };

        authValidationObj.authenticateAPI(req, getDevicesCallback);
    });


    //Updating the device details.
    //Take the deviceId as path param. UserId,deviceToken, platform are the available fields to update for a selected deviceId

    app.put('/devices/:deviceid', function (req, res) {

        var updateDeviceCallback = function (error, results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                //TODO :: Check if deviceid(device) is present
                var deviceId = req.params.deviceid;

                var deviceDetails = {
                    userId: req.body.userId,
                    deviceToken: req.body.deviceToken,
                    isBlackListed: req.body.isBlackListed
                };

                //Check if deviceId is defined
                if (typeof deviceId === 'undefined') {
                    console.log(constants.error.msg_invalid_param);
                    res.status(constants.error.msg_invalid_param.code).send(constants.error.msg_invalid_param);
                }
                else if (typeof deviceDetails.deviceToken !== 'undefined' && !deviceDetails.deviceToken.trim()) {
                    console.log(constants.error.msg_empty_token);
                    res.status(constants.error.msg_empty_token.code).send(constants.error.msg_empty_token);
                }
                //If no fields available in json to update then throw error
                else if (typeof deviceDetails.userId === 'undefined' && typeof deviceDetails.deviceToken === 'undefined' && typeof deviceDetails.isBlackListed === 'undefined') {
                    console.log(constants.error.msg_invalid_param);
                    res.status(constants.error.msg_invalid_param.code).send(constants.error.msg_invalid_param);
                }
                else if (req.body.isBlackListed && req.body.isBlackListed !== true && req.body.isBlackListed !== false) {
                    console.log(constants.error.msg_invalid_blacklist);
                    res.status(constants.error.msg_invalid_blacklist.code).send(constants.error.msg_invalid_blacklist);
                }
                else {
                    //TODO :: pass the dynamic DB instanceName created in dbName
                    var dbName = results[0]["dbName"];

                    //This method will create a DB connection with db name as dbName
                    coreDBConnectorObj.switchDB(dbName, function (err, dbInstance) {
                        devicesFunction.updateDevice(dbInstance["dbConnection"], deviceId, deviceDetails, function (result) {
                            //deleting the DB connection before responding to the client
                            //afterResponse(dbInstance.dbConnection);
                            res.status(result.code).send(result);
                        });
                    });
                }
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error": "Unauthorised request"});
            }
        };
        authValidationObj.authenticateAPI(req, updateDeviceCallback);
    });

    //Delete the device info from the database for a selected deviceId
    app.delete('/devices/:device', function (req, res) {

        var deleteDeviceCallback = function (error, results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                var deviceId = req.params.device;

                //TODO :: pass the dynamic DB instanceName created in dbName
                var dbName = results[0]["dbName"];

                //This method will create a DB connection with db name as dbName
                coreDBConnectorObj.switchDB(dbName, function (err, dbInstance) {
                    devicesFunction.removeDevice(dbInstance["dbConnection"], deviceId, function (result) {
                        res.status(result.code).send(result);
                    });
                });
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error": "Unauthorised request"});
            }
        };
        authValidationObj.authenticateAPI(req, deleteDeviceCallback);
    });
};