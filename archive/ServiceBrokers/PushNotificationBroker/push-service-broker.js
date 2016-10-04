/**
 *
 * Push Notification Service Broker , complying to version 2.4 of the interface specification
 * http://docs.cloudfoundry.org/services/api.html
 * This will serve all the request for Push notification services which is coming from the cloud
 * controller
 */

'use strict';

var config = require('./config/push_service_broker');
var restify = require('restify');
var async = require('async');
var uuid = require('uuid');
var dbConnector = require("./DAL/Connector.js");
var dbConfig = require("./DAL/dbconfig.json");


var instanceModelObj = require('./models/Instances');
var templateModelObj = require('./models/template');
var constants = require('./constants.json');
var DALObj = require('./DAL/DAL.js');

// making collection names as global such that all functions can use the same collection names
var instanceCollectionName = constants.instance_collection_name || 'Instances';
var templateCollectionName = constants.template_collection_name || 'Templates';


//configuring the server for the service broker
var server = restify.createServer({
    name: 'push_service_broker'
});

server.use(apiVersionChecker({
    'major': 2,
    'minor': 4
}));
server.use(restify.authorizationParser());
server.use(authenticate(config.credentials));
server.use(restify.fullResponse());
server.use(restify.bodyParser());
server.pre(restify.pre.userAgentConnection());


// To check the consistency of configurations

function checkConsistency() {
    var i, id, p, msg, plans, catalog;

    catalog = config.catalog;
    plans = config.plans;

    for (i = 0; i < catalog.services.length; i += 1) {
        for (p = 0; p < catalog.services[i].plans.length; p += 1) {
            id = catalog.services[i].plans[p].id;
            if (!plans.hasOwnProperty(id)) {
                msg = "ERROR: plan '" + catalog.services[i].plans[p].name + "' of service '" + catalog.services[i].name + "' is missing a specification.";
                throw new Error(msg);
            }
        }
    }
}

// get catalog
server.get('/v2/catalog', function (request, response, next) {

    response.send(config.catalog);
    next();
});

//router for provisioning a service when a cf create-service is called
server.put('/v2/service_instances/:id', function (request, response, next) {
    console.log("create-service params : " + JSON.stringify(request.params));

    var instanceObj = {};
    instanceObj.instanceID = request.params.id;
    instanceObj.dbName = "appDBinstance"+uuid.v1();
    instanceObj.apiKey = uuid.v1();
    instanceObj.bindedTo = null;

    //Save instance details to pushCoreDB
    var saveInstanceDetails = function(callback){
        var pushCoreDBConnection = dbConnector.getDB();
        DALObj.saveData(pushCoreDBConnection,instanceCollectionName,instanceModelObj,instanceObj,function(err,results){
            if (!err) {
                callback(null,results);
            } else {
                callback(err);
            }
        });
    };

    var saveTemplates = function(results,callback){
        var switchToDB = function(onCallback) {
            dbConnector.switchDB(instanceObj.dbName,function (err,dbInstance) {
                if (!err && dbInstance) {
                    onCallback(null,dbInstance["dbConnection"]);
                }
                else if (err) {
                    onCallback(err);
                }
                else {
                    onCallback({"error": "Something went wrong while creating an instance"});
                }
            });
        };

        var saveTemplateData = function(dbConnector,onCallback){
            var templates = request.params.parameters.templates;
            async.each(templates,
                function(template, asyncCallback){

                    var templateObjToSave = {};
                    if(template && template.templateId) {
                        templateObjToSave.templateId = template.templateId;
                        if(template.gcm) {
                            templateObjToSave.gcm   = JSON.stringify(template.gcm);
                        }
                        if(template.apn) {
                            templateObjToSave.apns  = JSON.stringify(template.apn);
                        }
                        if(template.wns) {
                            templateObjToSave.wns   = JSON.stringify(template.wns);
                        }
                    }

                    DALObj.saveData(dbConnector,templateCollectionName,templateModelObj,templateObjToSave,function(err,results){
                        if (!err) {
                            asyncCallback();
                        } else {
                            asyncCallback(err);
                        }
                    });
                },
                function(err){
                    if(err) {
                        onCallback(err);
                    }
                    else {
                        onCallback(null,true);
                    }
                }
            );
        }

        var saveTemplatesCallback = function(err,result){
            if(err){
                return callback(err);

            }
            else {
                return callback(null,true);
            }
        };

        async.waterfall([switchToDB,saveTemplateData], saveTemplatesCallback);
    };

    var finalCallback = function (err) {
        if (err) {
            response.send(400, {
                'description': err
            });
        }
        else {
            response.send(200, {
                'description': 'created an instance of push notification.'
            });

            next();
        }
    };

    if(request && request.params && request.params.parameters && request.params.parameters.templates){
        console.log("Templates JSON received in parameters : "+JSON.stringify(request.params.parameters.templates));
        async.waterfall([saveInstanceDetails,saveTemplates], finalCallback);
    }
    else {
        saveInstanceDetails(finalCallback);
    }
    next();
});

// router for un provision the service when a cf delete-service is called
server.del('/v2/service_instances/:id', function (req, response, next) {
    console.log('********Inside cf delete-service**********');


    var getInstanceDetails = function (callback) {

        var onCallback = function(err,result){
            if(err){
                return callback(err);

            }
            else {
                if(result.length ) {
                    console.log("result     " + JSON.stringify(result));
                    return callback(null, result[0]);
                }
                else{
                    return callback({error:"No instance found"});
                }
            }
        };

        var queryOBj = {
            instanceID :req.params.id
        };

        var dbInstnace = dbConnector.getDB();
        DALObj.getData(dbInstnace,instanceCollectionName,instanceModelObj,queryOBj, onCallback);
    };

    //deletes the entry from the Instance Collection for given instanceID
    var deleteRecord = function(instanceObj,callback){

        var queryOBj = {
            instanceID :instanceObj.instanceID
        };

        var onCallback = function(err,result){
          if(err){
              return callback(err);
          }
            else{
              return callback(null,instanceObj);
          }
        };
        var dbInstnace = dbConnector.getDB();
        DALObj.deleteData(dbInstnace,instanceCollectionName,instanceModelObj,queryOBj,onCallback);
    };
    //deletes the DB which is allocated for the app
    var deleteDB = function(instanceObj,callback){
        var dbName = instanceObj.dbName;

        dbConnector.deleteDB(dbName,function(err){
           if(err){
               return callback(err);
           }
            else{
               return callback(null,true);
           }
        });

    };


    var finalCallback = function (err) {
        if (err) {
            response.send(400, {
                'description': err
            });
        }
        else {
            response.send(200, {
                'description': 'de-provision done '
            });

            next();
        }
    }
    async.waterfall([getInstanceDetails,deleteRecord,deleteDB], finalCallback);

    next();
});


// router for binding the service to an app when a cf bind-service is called
server.put('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {


        //fetch the instanceDetails for the given instanceID
        var getInstanceDetails = function (callback) {

            var onCallback = function(err,result){
                if(err){
                    return callback(err);

                }
                else {
                    if(result.length){
                    if (result[0].bindedTo) {
                        return callback({"error": "Instance is already bounded to another app"});
                    }
                        else {
                        return callback(null, result);
                    }
                }
                else{
                        return callback({"error": "Instance not found"});
                    }
                }
            };

            var queryOBj = {
                instanceID :req.params.instance_id
            };

            var dbInstnace = dbConnector.getDB();
            DALObj.getData(dbInstnace,instanceCollectionName,instanceModelObj,queryOBj, onCallback);
        };

        //setting environment variables for the template app
        var setEnvironmentVariables = function (instanceObj, callback) {
            try {
                var respData = {
                    "credentials": {
                        "apiKey": instanceObj[0].apiKey,
                        "microservice_url": config["microservice_url"]
                    }
                };


                return callback(null, respData);
            }
            catch (err) {
                return callback(err);
            }
        };

        //checks whether the instance is already bounded
        // updates the bounded app guid for the given instance
        var updateBoundedApp = function(respData,callback){

            var onUpdate = function(err,result){
                if(err){
                    return callback(err);
                }
                else{
                    return callback(null,respData);
                }
            };

            var conditions = {
                    instanceID :req.params.instance_id
                };
            var update = {
                    bindedTo : req.params.id
                };


            var dbInstnace = dbConnector.getDB();
            DALObj.updateData(dbInstnace,instanceCollectionName,instanceModelObj,conditions,update,onCallback);
        };

        var finalCallback = function (err, result) {
            if (err) {
                response.send(400, {
                    'description': err
                });
            }
            else {
                response.status(201);

                response.end(JSON.stringify(result));
                next();
            }
        };
       async.waterfall([getInstanceDetails, setEnvironmentVariables,updateBoundedApp], finalCallback);

    }
);

// router for unbinding the service from an app when a cf unbind-service is called
server.del('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {



        // get instance details for the given instanceID
        var getInstanceDetails = function (callback) {
            var onCallback = function(err,result){
                if(err){
                    return callback(err);

                }
                else {
                    console.log("result    " + JSON.stringify(result));

                    if (result.length) {
                        if (!result[0].bindedTo) {
                            return callback({"error": "app already unbounded"});
                        }
                        else{
                            return callback(null, result);
                        }

                    }
                    else{
                        return callback({"error": "Instance not found"});

                    }
                }

            };
               var queryOBj = {
                instanceID :req.params.instance_id
            };

            var dbInstnace = dbConnector.getDB();
            DALObj.getData(dbInstnace,instanceCollectionName,instanceModelObj,queryOBj, onCallback);
        };

        //updates bindedTo as null for the given instance ID
        var updateDB =function(instanceObj,callback){
          var updateInstance =   instanceObj[0];
             delete updateInstance.bindedTo;
            updateInstance.bindedTo = null;
            var queryOBj = {
                instanceID :req.params.instance_id
            };
            var onCallback = function(err,result){
                if(err){
                    console.log("error *********** "+err);
                    return callback(err);
                }
                else{
                    console.log("result ************ "+result);
                    return callback(null,result);
                }
            }
            var dbInstnace = dbConnector.getDB();
            DALObj.updateData(dbInstnace,instanceCollectionName,instanceModelObj,queryOBj,updateInstance,onCallback);
        };
        var finalCallback = function(err,result){
          if(err){
              response.send(400, {
                  'description': err
              });
          }
            else{
              console.log("result ********* ",result);
              response.send(200, {
                  'description': 'service un bind successfull'
              });

          }
        };
        //flow control is done here
        async.waterfall([getInstanceDetails,updateDB],finalCallback);

        next();
    }
);


// list services (Not in spec :-)
server.get('/v2/service_instances', function (request, response, next) {

    response.send(501, {
        'description': JSON.stringify(response)
    });
    next();
});

function apiVersionChecker(version) {
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
}

function authenticate(credentials) {
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
}


/** According to the spec, the JSON return message should include a description field. */
server.on('uncaughtException', function (req, res, route, err) {
    console.log(err, err.stack);
    //res.send(500, { 'code' : 500, 'description' : err.message});
});

checkConsistency();
var port = Number(process.env.VCAP_APP_PORT || 3000);

//connecting to DB with the DB configured
dbConnector.connectToDB(dbConfig.dbType,function(err,db)
{
    if(err) {
        console.log('Error! Database connection failed : ',err);
    }
    else {
        server.listen(port, function () {
            console.log("Push Notification microservice is listening at port : " + port);
        });
    }

});

module.exports = server;

