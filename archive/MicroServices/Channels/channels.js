/**
 * Created by Srividya on 12/07/16.
 */
//Importing required node modules
var express = require('express');
var router = express.Router();

//requiring the "pushDBConnection.js" where the implementation of all the api's exists.
var pushDBConnObj = require('./pushDBConnection.js');

var async = require('async');

//This api will be used to createchannel based on the user provided channel name
//This is a post call.
router.route('/createchannel')
    .post(function (request, response) {

        //checking the presence of header "channelname" in the request.
        // If "channelname" header not present sending back the error with status code 400.
        if (request.headers["channelname"]) {

            //This is a callback function of createCollectionMethod.
            //This will be called once the execution of required methods are done.
            //This method will accept two parameters 'err' & 'result'.
            var callback = function (err, result) {
                //If "err": sending back the error with status code 400
                // or if "result": sending back the result with proper message in JSON object format.
                if (err) {
                    response.status(400).send({error: err});
                }
                else {
                    response.send({"success": "Channel named " + request.headers.channelname + " is created successfully"});
                }
            };

            //This is to call the createCollectionMethod which is exist in pushDBConnection.js
            //'pushDBConnObj' is the object of "pushDBConnection.js"
            var createCollection = function (callback) {
                pushDBConnObj.createCollectionMethod(request.headers.channelname, callback);
            };

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([createCollection], callback);
        }
        else {
            response.status(400).send({"error": {"message":"must pass channel name to create"}});
        }
    });


//This api will be used to deletechannel based on the user provided channel name.
//This is 'delete' call.
router.route('/deletechannel')
    .delete(function (request, response) {

        //checking the presence of header "channelname" in the request.
        // If "channelname" header not present sending back the error with status code 400.
        if (request.headers["channelname"]) {

            //This is a callback function of deleteCollectionMethod.
            //This will be called once the execution of required methods are done.
            //This method will accept two parameters 'err' & 'result'.
            var callback = function (err, result) {

                //If "err": sending back the error with status code 400
                // or if "result": sending back the result with proper message in JSON object format.
                if (err) {
                    response.status(400).send({error: err});
                }
                else {
                    response.send({success: 'Channel named ' + request.headers.channelname + ' is deleted successfully'});
                }
            };

            //This is to call the deleteCollectionMethod which is exist in pushDBConnection.js
            //'pushDBConnObj' is the object of "pushDBConnection.js"
            var deleteCollection = function (callback) {
                pushDBConnObj.deleteCollectionMethod(request.headers.channelname, callback);
            };

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([deleteCollection], callback);
        }
        else {
            response.status(400).send({"error": {"message":"must pass channel name to delete"}});
        }
    });


//This api will be used to subscribe channel based on the user provided channel name
//This is post call.
router.route('/subscribe')
    .post(function (request, response) {
        //checking the presence of headers "channelname" & "deviceid" in the request.
        // If "channelname" & "deviceid" headers not present sending back the error with status code 400.
        if (request.headers["channelname"] && request.headers["deviceid"]) {

            //This is a callback function of subscribeToChannel.
            //This will be called once the execution of required methods are done.
            //This method will accept two parameters 'err' & 'result'.
            var callback = function (err, result) {

                //If "err": sending back the error with status code 400
                // or if "result": sending back the result with proper message in JSON object format.
                if (err) {
                    response.status(400).send({error: err});
                }
                else {
                    response.send({"success":"This device is successfully subscribed to channel: " + request.headers.channelname});
                }
            };

            //This is to call the subscribeToChannel which is exist in pushDBConnection.js
            //'pushDBConnObj' is the object of "pushDBConnection.js"
            var subscribeToChannel = function (callback) {
                pushDBConnObj.subscribeToChannel(request, callback);
            };

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([subscribeToChannel], callback);
        }
        else if (!request.headers["channelname"]) {
            response.status(400).send({"error": {"message":"must pass channelname"}});
        }
        else {
            response.status(400).send({"error": {"message":"must pass deviceID"}});
        }
    });


//This api will be used to un subscribe the channel based on the user provided channel name.
//This is 'delete' call.
router.route('/unsubscribe')
    .delete(function (request, response) {

        //checking the presence of headers "channelname" & "deviceid" in the request.
        // If "channelname" & "deviceid" headers not present sending back the error with status code 400.
        if (request.headers["channelname"] && request.headers["deviceid"]) {

            //This is a callback function of unsubscribeToChannel.
            //This will be called once the execution of required methods are done.
            //This method will accept two parameters 'err' & 'result'.
            var callback = function (err, result) {

                //If "err": sending back the error with status code 400
                // or if "result": sending back the result with proper message in JSON object format.
                if (err) {
                    response.status(400).send({error: err});
                }
                else {
                    response.send({"success":"This device is successfully unsubscribed from channel: " + request.headers.channelname});
                }
            };

            //This is to call the unSubscribeToChannel which is exist in pushDBConnection.js
            //'pushDBConnObj' is the object of "pushDBConnection.js"
            var unSubscribeToChannel = function (callback) {
                pushDBConnObj.unSubscribeToChannel(request, callback);
            };

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([unSubscribeToChannel], callback);
        }
        else if (!request.headers["channelname"]) {
            response.status(400).send({"error": {"message":"must pass channelname"}});
        }
        else {
            response.status(400).send({"error": {"message":"must pass deviceID"}});
        }
    });


//This api will be used to push notifications to a channel based on channel name provided.
//This is post call.
router.route('/pushNotifyToChannel')
    .post(function (request, response) {

        //checking the presence of "channelname" & "message" in the request body.
        // If "channelname" & "message" headers not present sending back the error with status code 400.
        if (request.body["channelname"] && request.body["message"]) {

            //This is a callback function of pushNotifyToChannelHandler.
            //This will be called once the execution of required methods are done.
            //This method will accept two parameters 'err' & 'result'.
            var callback = function (err, result) {

                //If "err": sending back the error with status code 400
                // or if "result": sending back the result with proper message in JSON object format.
                if (err) {
                    response.status(400).send({error: err});
                }
                else {
                    response.status(202).send(result);
                }
            };

            //This is to call the pushNotifyToChannel which is exist in pushDBConnection.js
            //'pushDBConnObj' is the object of "pushDBConnection.js"
            var pushNotifyToChannelHandler = function (callback) {
                pushDBConnObj.pushNotifyToChannel(request, callback);
            };

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([pushNotifyToChannelHandler], callback);
        }
        else if (!request.body["channelname"]) {
            response.status(400).send({"error": {"message":"must pass channelname"}});
        }
        else {
            response.status(400).send({"error": {"message":"must pass message"}});
        }
    });

module.exports = router;