var constants = require('../constants/constants.json');
var device = require('../models/device');
var authValidationObj = require('../functions/authenticateAPI.js');

//Channels functions
var channelsClientObj = require('../functions/channelsClient.js');

module.exports = function(app) {

    //*******Channels End Points*******

    //This api will be used to createchannel based on the user provided channel name
    //This is a post call.
    app.post('/createchannel', function (request, response) {

        var createChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
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

                    //This is to call the createChannelFunc which is exist in channelsClient.js
                    //'channelsClientObj' is the object of "channelsClient.js"
                    channelsClientObj.createChannelFunc(results[0]["dbName"],request, callback);
                }
                else {
                    response.status(400).send({"error": {"message": "must pass channel name to create"}});
                }
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,createChannelCallback);
    });


    //This api will be used to updatechannel based on the user provided channel name.
    //This is 'put' call.
    app.put('/updatechannel', function (request, response) {
        var updateChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
                //checking the presence of header "channelname" in the request.
                // If "channelname" header not present sending back the error with status code 400.
                if (request.headers["channelname"] && request.headers["channeldescription"]) {

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
                            response.send({success: 'Channel named ' + request.headers.channelname + ' is updated successfully'});
                        }
                    };

                    //This is to call the deleteChannelFunc which is exist in updateChannelFunc.js
                    //'channelsClientObj' is the object of "updateChannelFunc.js"
                    channelsClientObj.updateChannelFunc(results[0]["dbName"],request, callback);

                }
                else if (!request.headers["channelname"]) {
                    response.status(400).send({"error": {"message": "must pass channel name"}});
                }
                else {
                    response.status(400).send({"error": {"message": "must pass channel description to update"}});
                }
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,updateChannelCallback);
    });


    //This api will be used to deletechannel based on the user provided channel name.
    //This is 'delete' call.
    app.delete('/deletechannel', function (request, response) {
        var deleteChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
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

                    //This is to call the deleteChannelFunc which is exist in channelsClient.js
                    //'channelsClientObj' is the object of "channelsClient.js"
                    channelsClientObj.deleteChannelFunc(results[0]["dbName"],request.headers.channelname, callback);

                }
                else {
                    response.status(400).send({"error": {"message": "must pass channel name to delete"}});
                }
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,deleteChannelCallback);
    });


    //This api will be used to subscribe channel based on the user provided channel name
    //This is put call.
    app.put('/subscribe', function (request, response) {
        var subscribeChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
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
                            response.send({"success": "This device is successfully subscribed to channel: " + request.headers.channelname});
                        }
                    };

                    //This is to call the subscribeToChannel which is exist in channelsClient.js
                    //'channelsClientObj' is the object of "channelsClient.js"
                    channelsClientObj.subscribeToChannel(results[0]["dbName"],request, callback);
                }
                else if (!request.headers["channelname"]) {
                    response.status(400).send({"error": {"message": "must pass channelname"}});
                }
                else {
                    response.status(400).send({"error": {"message": "must pass deviceID"}});
                }
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,subscribeChannelCallback);
    });

    //This api will be used to un subscribe the channel based on the user provided channel name.
    //This is 'delete' call.
    app.delete('/unsubscribe', function (request, response) {
        var unsubscribeChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {

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
                            response.send({"success": "This device is successfully unsubscribed from channel: " + request.headers.channelname});
                        }
                    };

                    //This is to call the unSubscribeToChannel which is exist in channelsClient.js
                    //'channelsClientObj' is the object of "channelsClient.js"
                    channelsClientObj.unSubscribeToChannel(results[0]["dbName"],request, callback);

                }
                else if (!request.headers["channelname"]) {
                    response.status(400).send({"error": {"message": "must pass channelname"}});
                }
                else {
                    response.status(400).send({"error": {"message": "must pass deviceID"}});
                }
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,unsubscribeChannelCallback);
    });


    //This api will be used to get the data of all the channels.
    //This is get call.
    app.get('/getChannels', function (request, response) {
        var getChannelCallback = function (error,results) {
            if (error) {
                response.status(400).send(error);
            }
            else if (results.length) {
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
                        response.status(200).send(result);
                    }
                };

                //This is to call the getChannelsDataHandler which is exist in channelsClient.js
                //'channelsClientObj' is the object of "channelsClient.js"
                channelsClientObj.getChannelsData(results[0]["dbName"],request, callback);
            }
            else {
                //sending error if it is unauthorised
                response.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(request,getChannelCallback);
    });
};