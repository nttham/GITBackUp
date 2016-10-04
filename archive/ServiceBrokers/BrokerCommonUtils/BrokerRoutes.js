/**
 *
 * MicroService  Service Broker , complying to version 2.4 of the interface specification
 * http://docs.cloudfoundry.org/services/api.html
 * This will serve all the request for AuthService services which is coming from the cloud
 * controller
 */

'use strict';
var config = require('./config/service_broker');
var restify = require('restify');
var NodeClient = require("./cf-node-client.js");
var nodeClient = new NodeClient();
var CommonUtilsObj = require('./middleLayer/commonUtils.js');
var commonUtilsObj = new CommonUtilsObj();
var myBrokerName;


class BrokerRoutesClass {
    constructor(brokerName) {
        myBrokerName = brokerName;
        console.log('**MyBrokerName**' + brokerName);
    }
}

//configuring the server for the service broker
var server = restify.createServer({
    name: myBrokerName
});
//apiVersionChecker
server.use(commonUtilsObj.apiVersionChecker({
    'major': 2,
    'minor': 4
}));

server.use(restify.authorizationParser());
//authenticate the service broker
server.use(commonUtilsObj.authenticate(config["credentials"]));
server.use(restify.fullResponse());
server.use(restify.bodyParser());
server.pre(restify.pre.userAgentConnection());

//to check the broker consistency
commonUtilsObj.checkConsistency({ catalog: config["catalog"], plans: config["plans"] });


// get catalog
server.get('/v2/catalog', function (request, response, next) {

    response.send(config["catalog"]);
    next();
});

//router for provisioning a service when a cf create-service is called
server.put('/v2/service_instances/:id', function (request, response, next) {

    //callback
    var finalCallback = function (err, result) {
        if (err) {
            response.status(400);
            response.end(JSON.stringify({ 'error': err }));

        }
        else {
            response.status(200);
            response.end(JSON.stringify({ 'description': result }));
        }
    };

    //createServiceInstance call
    nodeClient.createServiceInstance(request, finalCallback);

    next();
});

// router for un provision the service when a cf delete-service is called
server.del('/v2/service_instances/:id', function (req, response, next) {
    //callback
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

    //deleteServiceInstance call
    nodeClient.deleteServiceInstance(req, finalDeleteCallback);
    next();
});


// router for binding the service to an app when a cf bind-service is called
server.put('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {
    //callback
    var finalCallback = function (err, result) {
        if (err) {
            response.send(400, {
                'description': err
            });
        }
        else {
            console.log('**came to broker Binding finalcallback**' + JSON.stringify(result));
            response.status(201);

            response.end(JSON.stringify(result));
            next();
        }
    };

    //bindService call
    nodeClient.bindService(req, finalCallback);
    next();
});

// router for unbinding the service from an app when a cf unbind-service is called
server.del('/v2/service_instances/:instance_id/service_bindings/:id', function (req, response, next) {
    response.send(200, {
        'description': 'service un bounded successfully'
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



server.get(/\/?.*/, restify.serveStatic({
    directory: './public',
    default: 'index.html'
}));

/** According to the spec, the JSON return message should include a description field. */
server.on('uncaughtException', function (req, res, route, err) {
    console.log(err, err.stack);
    //res.send(500, { 'code' : 500, 'description' : err.message});
});

var port = Number(process.env.VCAP_APP_PORT || 8080);

server.listen(port, function () {
    console.log(myBrokerName+" is listening at port : " + port);
});

module.exports = BrokerRoutesClass;
