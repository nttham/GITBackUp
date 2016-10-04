/**
 *
 * MicroService  Service Broker , complying to version 2.4 of the interface specification
 * http://docs.cloudfoundry.org/services/api.html
 * This will serve all the request for AuthService services which is coming from the cloud
 * controller
 */

'use strict';

var config = require('./config/auth_service_broker');
var restify = require('restify');
var async = require('async');
var DB = require('./gistdb.js');
var NodeClient = require("./cf-node-client.js");
var nodeClient = new NodeClient();
var uuid = require('uuid');
var constants = require('./config.json');

//configuring the server for the service broker
var server = restify.createServer({
    name: 'auth_service_broker'
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
	
    // pushes the app to cloud foundry with the appName given by the client
    var pushApp = function (pushHandler) {

        //forming the environment_json which will be passed to the microservice app while pushing
        //it to the cloud foundry which will inturn goes to its vcaps


        // creating the apikey for authentication
        request.params.parameters.environment_json.apiKey = uuid.v1();

        if (request.params.parameters.environment_json.facebook_clientID && request.params.parameters.environment_json.facebook_clientSecret) {
            if (request.params.parameters.environment_json.facebook_scope && request.params.parameters.environment_json.facebook_scope.length) {
                var scope = request.params.parameters.environment_json.facebook_scope;
                delete request.params.parameters.environment_json.facebook_scope;

                request.params.parameters.environment_json.facebook_scope = JSON.stringify(scope);
            }
            else {
                request.params.parameters.environment_json.facebook_scope = '"public_profile"';
            }
            request.params.parameters.environment_json.facebook_callbackURL = constants.protocol + request.params.parameters.appname + "." + request.params.parameters.host + constants.facebookCallback ;
        }
        if (request.params.parameters.environment_json.google_clientID && request.params.parameters.environment_json.google_clientSecret) {
            if (request.params.parameters.environment_json.google_scope && request.params.parameters.environment_json.google_scope.length) {
                var scope = request.params.parameters.environment_json.google_scope;
                delete request.params.parameters.environment_json.google_scope;
                request.params.parameters.environment_json.google_scope = JSON.stringify(scope);

            }
            else {
                request.params.parameters.environment_json.google_scope = '"https://www.googleapis.com/auth/plus.login,https://www.googleapis.com/auth/plus.profile.emails.read"';

            }
            request.params.parameters.environment_json.google_callbackURL = constants.protocol + request.params.parameters.appname + "." + request.params.parameters.host + constants.googleCallback;
        }
        if (request.params.parameters.environment_json.twitter_clientID && request.params.parameters.environment_json.twitter_clientSecret) {
            request.params.parameters.environment_json.twitter_callbackURL = constants.protocol + request.params.parameters.appname + "." + request.params.parameters.host + constants.twitterCallback;
        }
        if (request.params.parameters.environment_json.linkedin_clientID && request.params.parameters.environment_json.linkedin_clientSecret) {
            if (request.params.parameters.environment_json.linkedin_scope && request.params.parameters.environment_json.linkedin_scope.length) {
                var scope = request.params.parameters.environment_json.linkedin_scope;
                delete request.params.parameters.environment_json.linkedin_scope;
                request.params.parameters.environment_json.linkedin_scope = JSON.stringify(scope);

            }
            else {
                request.params.parameters.environment_json.linkedin_scope = '"r_basicprofile"';

            }
            request.params.parameters.environment_json.linkedin_callbackURL =constants.protocol  + request.params.parameters.appname + "." + request.params.parameters.host + constants.linkedinCallback;
        }


        nodeClient.pushApp(request.params.parameters, pushHandler);
    };


    var updateDB = function (obj, callback) {
        console.log('****App Instance ID in push response app guid****' + obj.metadata.guid);
        var reqObj = {
            instance_id: request.params.id,
            space_guid: request.params.parameters.space_guid,
            host: request.params.parameters.host,
            appname: request.params.parameters.appname,
            endpoint: request.params.parameters.endpoint,
            facebook: request.params.parameters.environment_json.facebook_callbackURL,
            google: request.params.parameters.environment_json.google_callbackURL,
            linkedin: request.params.parameters.environment_json.linkedin_callbackURL,
            twitter: request.params.parameters.environment_json.twitter_callbackURL,
            apiKey: request.params.parameters.environment_json.apiKey
        };
        DB.editData(reqObj, callback);
    };

    //callback for async calls
    var finalCallback = function (err, result) {
        if (err) {
            response.status(400);
            response.end(JSON.stringify({'error': err}));

        }
        else {

            response.status(200);
            response.end(JSON.stringify({'description': 'created instance of oAuth'}));

        }
    };
    // If you wanted to provision other things add the sequential task here
    async.waterfall([pushApp, updateDB], finalCallback);
    // }
    next();
});

// router for un provision the service when a cf delete-service is called


server.del('/v2/service_instances/:id', function (req, response, next) {
    console.log('********Inside cf delete-service**********');
    var deleteApp = function (callback) {
        var options = {
            instance_id: req.params.id
            //accessToken: req.headers.authtoken
        };
        nodeClient.deleteApp(options, callback);
    };

    var deleteData = function (callback) {
        console.log('******inside Broker deleteData****');
        DB.deleteData({instance_id: req.params.id}, finalDeleteCallback);
    };

    var finalDeleteCallback = function (err, result) {
        if (err) {
            response.send(400, {
                'description': err
            });

        }
        else {
            response.send(200, {
                'description': 'de-provision done '
            });
        }
    };

    async.waterfall([deleteApp, deleteData], finalDeleteCallback);
    next();
});


// router for binding the service to an app when a cf bind-service is called

server.put('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {

        var getInstanceDetails = function (callback) {
            var getDatacallback = function (err, result) {
                if (err) {
                    console.log(err)
                }
                else {
                    var filesData = JSON.parse(result.body).files['OAuth.json'].content;
                    var contentArray = JSON.parse(filesData);
                    var index = contentArray.findIndex(x => x.instance_id == req.params.instance_id);

                    if (index == -1) {
                        console.log('Kindly provide the correct instanceid');
                        return callback('Kindly provide the correct instanceid');
                    }
                    else {
                        var resultObj = contentArray[index];

                        console.log(resultObj);
                        return callback(null, resultObj);
                    }
                }
            };
            DB.getData(getDatacallback);
        };


        var setEnvironmentVariables = function (instanceObj, callback) {
            try {
                var respData = {
                    "credentials": {
                        "serviceUrl": constants.protocol + instanceObj.appname + "." + instanceObj.host

                    }
                };

                if (instanceObj.facebook) {

                    respData.credentials.facebook_callbackURL = constants.protocol + instanceObj.appname + "." + instanceObj.host + constants.facebookCallback;
                }
                if (instanceObj.google) {

                    respData.credentials.google_callbackURL = constants.protocol + instanceObj.appname + "." + instanceObj.host + constants.googleCallback;
                }
                if (instanceObj.twitter) {
                    respData.credentials.twitter_callbackURL = constants.protocol + instanceObj.appname + "." + instanceObj.host + constants.twitterCallback;
                }
                if (instanceObj.linkedin) {

                    respData.credentials.linkedin_callbackURL = constants.protocol + instanceObj.appname + "." + instanceObj.host + constants.linkedinCallback;
                }

                respData.credentials.apiKey = instanceObj.apiKey;

                return callback(null, respData);
            }
            catch (err) {
                return callback(err);
            }
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
        }
        async.waterfall([getInstanceDetails, setEnvironmentVariables], finalCallback);

    }
);

// router for unbinding the service from an app when a cf unbind-service is called
server.del('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {
        response.send(200, {
            'description': 'service un bind successfull'
        });
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
};

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
};

server.get(/\/?.*/, restify.serveStatic({
    directory: './public',
    default: 'index.html'
}));

/** According to the spec, the JSON return message should include a description field. */
server.on('uncaughtException', function (req, res, route, err) {
    console.log(err, err.stack);
    //res.send(500, { 'code' : 500, 'description' : err.message});
});

checkConsistency();
var port = Number(process.env.VCAP_APP_PORT || 8080);

server.listen(port, function () {
    console.log("AuthService broker is listening at port : " + port);
});

module.exports = server;

