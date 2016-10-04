var constants = require('../constants/constants.json');
var multer = require('multer');
var path = require('path');
var fs = require('fs');
var async = require("async");
var coreDBConnectorObj = require("../DAL/Connector.js");
var settingsObj = require('../functions/Settings.js');
var authValidationObj = require('../functions/authenticateAPI.js');

//Accept files which are of pfx type
function fileFilter (req, file, cb) {
    if(file && file.mimetype === "application/x-pkcs12") {
        // To accept the file pass `true`
        cb(null, true)
    }
    else {
        // To reject this file pass `false`
        cb(null, false)
    }
}

var upload = multer({ dest: './uploads/', fileFilter}).single('certificate');

//Check for certificate and passPhrase parameters
var verifyCertificate = function (req, res, next) {
    upload(req, res, function (err) {
        if (err) {
            // An error occurred when uploading
            console.error(err);
            res.status(400).send({error:"Not a valid request for uploading certificate"});
        }
        else if(req.file && req.file.filename){
            if(req.body.passPhrase) {
                next();

            }
            else {
                var filename = req.file.filename;
                var filePath = process.cwd() + '/uploads/' + filename;
                fs.unlink(filePath, function () {
                    res.status(400).send({error:"passPhrase missing in body parameters"});
                    return
                });
            }
        }
        else {
            res.status(400).send({error:"certificate missing in body parameters"});
        }

    })
};

//Check for authentication
var verifyAuthentication = function (req, res, next) {

    authValidationObj.authenticateAPI(req,function (error,results) {
        if(error) {
            res.status(400).send(error);
        }
        else if(results.length) {
            next();
        }
        else {
            res.status(401).send({"error":"Unauthorised request"});
        }
    });
};

module.exports = function(app) {

    app.put('/apnssettings', [verifyAuthentication,verifyCertificate], function(req,res) {

        if(req.file) {
            console.log("req file " + JSON.stringify(req.file));

            var apnsSettingsCallback = function (error,results) {
                if (error) {
                    res.status(400).send(error);
                }
                else if (results.length) {
                    try{
                        var dbName = results[0]["dbName"];

                        //This method will create a DB connection with db name as dbName
                        coreDBConnectorObj.switchDB(dbName,function(err,dbInstance){
                            //This Api will Save the uploaded File into GridFS
                            var SaveFileToGrid = function (callback) {
                                var fileName = req.file.filename;
                                var ApnsObj = require('../functions/upload.js');

                                //This will call the api which saves the file into GridFS
                                ApnsObj.uploadCertificate(fileName, dbInstance, function (err, result) {
                                    if (err) {
                                        return callback(err);
                                    }
                                    else {
                                        return callback(null, result);
                                    }
                                });

                            };

                            //This Api is used to save the settings info for a particular instanceId
                            var saveSettingsConfig = function (apnsObj, callback) {


                                var settings = {
                                    apnsPassword: req.body.passPhrase,
                                    apnsCertificate: apnsObj.filename,
                                    instanceID: results[0]["instanceID"]
                                };

                                settingsObj.saveSettingsInfo(dbInstance["dbConnection"], results[0]["instanceID"], settings, function (err) {
                                    if (err) {
                                        return callback(err);
                                    }
                                    else {
                                        return callback(null, apnsObj);
                                    }
                                });
                            };

                            var finalCallback = function (err, fileInfo) {
                                //afterResponse(dbInstance.dbConnection);
                                if (err) {
                                    res.json(err);
                                    res.status(400).end();

                                }
                                else {
                                    console.log("fileInfo", fileInfo);
                                    res.json({"message": "APNS settings updated Successfully"});
                                    res.status(204).end();
                                }
                            };
                            //Here the control flow is in sequential
                            async.waterfall([SaveFileToGrid, saveSettingsConfig], finalCallback);
                        });
                    }
                    catch(err){
                        res.json(err);
                        res.status(400).end();
                    }
                }
                else {
                    //sending error if it is unauthorised
                    res.status(401).send({"error":"Unauthorised request"});
                }
            };
            authValidationObj.authenticateAPI(req,apnsSettingsCallback);

        }
        else {
            res.status(400).send({"error":"Invalid certificate file"});
            return;
        }
    });



    app.delete('/apnssettings',function(req,res) {

        var apnsSettingsCallback = function (error,results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                var dbName = results[0]["dbName"];

                //This method will create a DB connection with db name as dbName
                coreDBConnectorObj.switchDB(dbName,function (err, dbInstance) {


                    var settings = {
                        apnsPassword: undefined,
                        apnsCertificate: undefined,
                        instanceID: results[0]["instanceID"]
                    };

                    settingsObj.deleteAPNSSettingsInfo(dbInstance, results[0]["instanceID"], settings, function (err) {
                        if (err) {
                            res.json(err);
                            res.status(400).end();
                        }
                        else {
                            res.json({"message": "File Deleted Successfully"});
                            res.status(204).end();
                        }
                    });
                });
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error":"Unauthorised request"});
            }
        };
        authValidationObj.authenticateAPI(req,apnsSettingsCallback);
    });


    app.put('/gcmsettings',function (request,response) {

        var gcmsettingCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
                var dbName = results[0]["dbName"];
                coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {

                    var settings = {
                        gcmApiKey: request.body.gcmApiKey,
                        instanceID: results[0]["instanceID"]
                    };

                    settingsObj.saveSettingsInfo(dbInstance["dbConnection"], results[0]["instanceID"], settings, function (err) {
                        if (err) {
                            response.json(err);
                            response.status(400).end();
                        }
                        else {
                            response.json({"message": "Settings updated Successfully"});
                            response.status(204).end();
                        }
                    });
                })
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };
        if(request.body && request.body.gcmApiKey) {
            authValidationObj.authenticateAPI(request,gcmsettingCallback);
        }
        else {
            response.status(400).send({"result":"error","message":"gcmApiKey missing in body parameters"});
        }
    });


    app.delete('/gcmsettings',function(req,res) {

        var gcmsettingsCallback = function (error,results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                var dbName = results[0]["dbName"];

                //This method will create a DB connection with db name as dbName
                coreDBConnectorObj.switchDB(dbName,function (err, dbInstance) {


                    var settings = {
                        gcmApiKey: undefined,
                        instanceID: results[0]["instanceID"]
                    };

                    settingsObj.deleteSettingsInfo(dbInstance["dbConnection"], results[0]["instanceID"], settings, function (err) {
                        if (err) {
                            res.json(err);
                            res.status(400).end();
                        }
                        else {
                            res.json({"message": "Settings Deleted Successfully"});
                            res.status(204).end();
                        }
                    });
                });
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error":"Unauthorised request"});
            }
        };
        authValidationObj.authenticateAPI(req,gcmsettingsCallback);
    });


    app.put('/wnssettings',function (request,response) {

        var wnssettingCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
                var dbName = results[0]["dbName"];
                coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {

                    var settings = {
                        wnsClientId: request.body.wnsClientId,
                        wnsClientSecret: request.body.wnsClientSecret,
                        instanceID: results[0]["instanceID"]
                    };

                    settingsObj.saveSettingsInfo(dbInstance["dbConnection"], results[0]["instanceID"], settings, function (err) {
                        if (err) {
                            response.json(err);
                            response.status(400).end();
                        }
                        else {
                            response.json({"message": "Settings updated Successfully"});
                            response.status(204).end();
                        }
                    });
                })
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        if(request.body && request.body.wnsClientId && request.body.wnsClientSecret) {
            authValidationObj.authenticateAPI(request,wnssettingCallback);
        }
        else {
            response.status(400).send({"result":"error","message":"Both wnsClientId and wnsClientSecret are required in body parameters"});
        }
    });


    app.delete('/wnssettings',function(req,res) {

        var wnssettingCallback = function (error,results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                var dbName = results[0]["dbName"];

                //This method will create a DB connection with db name as dbName
                coreDBConnectorObj.switchDB(dbName,function (err, dbInstance) {


                    var settings = {
                        wnsClientId: undefined,
                        wnsClientSecret: undefined,
                        instanceID: results[0]["instanceID"]
                    };

                    settingsObj.deleteSettingsInfo(dbInstance["dbConnection"], results[0]["instanceID"], settings, function (err) {
                        if (err) {
                            res.json(err);
                            res.status(400).end();
                        }
                        else {
                            res.json({"message": "Settings Deleted Successfully"});
                            res.status(204).end();
                        }
                    });
                });
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error":"Unauthorised request"});
            }
        };
        authValidationObj.authenticateAPI(req,wnssettingCallback);
    });
};