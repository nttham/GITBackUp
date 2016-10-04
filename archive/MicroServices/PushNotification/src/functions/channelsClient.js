/**
 * Created by Srividya on 13/07/16.
 */
//Importing required node modules
var async = require('async');
var coreDBConnectorObj = require("../DAL/Connector.js");

//Requiring the channels.js model file
var channelsModel = require('../models/channel.js');
var constants = require('../constants/constants.json');
var DALObj = require('../DAL/DAL.js');
var notification = require('./notification');

// making chennelsCollectionName as global such that all functions can use the same Channels collection name
var channelsCollectionName = constants.channel_collection_name || 'Channels';

//This api will be used to createchannel based on the user provided channel name
exports.createChannelFunc = function (dbName,requestOptions, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //Check if the channelname is already exists
        var queryObj = {channelName:requestOptions.headers.channelname};
        DALObj.getData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,queryObj, function (err,channels) {
            //If error returning the error as callback
            if (err){
                return callback(err);
            }
            //If no channels found creating the channel
            else if (channels.length === 0) {

                //creating the object to save in DB's channels collection.
                var channelDetailsToSave = {
                    channelName		    : requestOptions.headers.channelname,
                    deviceID		    : [],
                    channelDescription	: requestOptions.headers.channeldescription || ""
                };
                //saving channelObj to DB
                DALObj.saveData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,channelDetailsToSave,function(err,channel){
                    if (err) {
                        return callback({"message":error});
                    }
                    else  {
                        return callback(null,channel);
                    }
                });
            }
            else {
                //returning the error as callback if channel name is already exists.
                return callback({"message":"Provided channel name already exists"});
            }
        });
    });
};


//This api will be used to update channel based on the user provided channel name
exports.updateChannelFunc = function (dbName,request, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //Check if the channelname is already exists
        var criteria = {channelName:request.headers["channelname"]};
        DALObj.findOne(dbInstance["dbConnection"],channelsCollectionName,channelsModel,criteria, function (err,channel) {
            //If error returning the error as callback
            if (err){
                return callback(err);
            }
            //If no channels found creating the channel
            else if (!channel || channel.length === 0) {
                //returning the error as callback if channel name is already exists.
                return callback({"message": "Provided channel name not exists."});
            }
            else {
                console.log('****updateChannelFunc***'+JSON.stringify(channel));

                //updating DB with provided description.
                DALObj.updateData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:request.headers["channelname"]}, { $set: { channelDescription: request.headers["channeldescription"] }}, {new: true},function (error,channel) {
                    if (error) {
                        //returning error as callback if error occurs while updating.
                        return callback({"message":error});
                    }
                    else  {
                        //returning success as callback.
                        return callback(null,channel);
                    }
                });
            }
        });
    });
};


//This api will be used to deletechannel based on the user provided channel name
exports.deleteChannelFunc = function (dbName,collectionName, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //This will find a document based on collectionName and remove the document
        DALObj.findOneAndRemove(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:collectionName},function (error,result) {

            if (error) {
                //return error if any occurs while delete.
                return callback({"message":error});
            }
            else {
                //If deleted results is '0' means there are no documents to delete.
                //So returing the error message as callback.
                console.log('deleteChannelFunc results'+JSON.stringify(result));
                if (result) {
                    //returning the success message on successful deletion.
                    return callback(null,result);
                }
                else {
                    //returning error if the provided channel name not exists.
                    return callback({"message": "Provided channel name not exists."});
                }
            }
        });
    });
};

//This api will be used to subscribe to channel based on the user provided channel name
exports.subscribeToChannel = function (dbName,request, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //Check if the channelname is already exists
        DALObj.findOne(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:request.headers.channelname}, function (err,channel) {
            //If error returning the error as callback
            if (err){
                return callback(err);
            }
            //If no channels found creating the channel
            else if (!channel || channel.length === 0) {
                //returning the error as callback if channel name is already exists.
                return callback({"message": "Provided channel name not exists."});
            }
            else {
                console.log('****'+JSON.stringify(channel));
                //getting the devices registred in channel and adding new device.
                var deviceIDsArray = channel["deviceID"];

                //Check if the device is registered already.
                if (deviceIDsArray.indexOf(request.headers["deviceid"]) > -1) {
                    //returning error as callback if device is already exists.
                    return callback({"message":"device is already subscribed to the provided channel"});
                }
                else {
                    deviceIDsArray.push(request.headers["deviceid"]);

                    //updating DB
                    DALObj.updateData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:request.headers.channelname}, { $set: { deviceID: deviceIDsArray }}, {new: true},function (error,channel) {
                        if (error) {
                            //returning error as callback if error occurs while updating.
                            return callback({"message":error});
                        }
                        else  {
                            //returning success as callback.
                            return callback(null,channel);
                        }
                    });
                }
            }
        });
    });
};

//This api will be used to unsubscribe from channel based on the user provided channel name
exports.unSubscribeToChannel = function (dbName,request, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //Check if the channelname is already exists
        DALObj.findOne(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:request.headers.channelname}, function (err,channel) {
            //If error returning the error as callback
            if (err){
                return callback(err);
            }
            //If no channels found creating the channel
            else if (!channel) {
                //returning the error as callback if channel name is already exists.
                return callback({"message": "Provided channel name not exists."});
            }
            else {
                console.log('****'+JSON.stringify(channel));
                //getting the devices registred in channel and adding new device.
                var deviceIDsArray = channel["deviceID"];

                var index = deviceIDsArray.indexOf(request.headers["deviceid"]);
                //Check if the device is registered already.
                if (index > -1) {
                    //deleting the provided deviceID from deviceid's array and updating the db with updated deviceID's
                    deviceIDsArray.splice(index,1);

                    //updating DB
                    DALObj.updateData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:request.headers.channelname}, { $set: { deviceID: deviceIDsArray }}, {new: true},function (error,channel) {
                        if (error) {
                            //returning error as callback if error occurs while updating.
                            return callback({"message":error});
                        }
                        else  {
                            //returning success as callback.
                            return callback(null,channel);
                        }
                    });
                }
                else {
                    //returning error as callback if device is already exists.
                    return callback({"message":"device is either not subscribed or unsubscribed from the provided channel"});
                }
            }
        });
    });
};


//This api will be used to notification to a channel based on channel name provided
exports.pushNotifyToChannel = function (dbresults,requestObj, callback) {
    coreDBConnectorObj.switchDB(dbresults[0]["dbName"],function (error,dbInstance) {
        //This is to get the deviceID's from DB based on the channelname provided in body.
        var getDeviceIDS = function (deviceIDSCallback) {

            //Get all the registered devices except channelName.
            DALObj.findOne(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{channelName:requestObj["channelName"]},{_id : false,__v : false,channelName : false}, function (err,devices) {
                //Returning the error as callback
                if(err){
                    return deviceIDSCallback({"code":"400","message":err});
                }
                else if (!devices){
                    //Returning the error as callback if the channel name is not proper
                    return deviceIDSCallback({"code":"400","message": "Provided channel name not exists."});
                }
                else {
                    console.log('registred devices***'+devices);
                    if (devices["deviceID"].length > 0) {
                        //Returning array of deviceID's as callback.
                        return deviceIDSCallback(null,devices["deviceID"]);
                    }
                    else {
                        //Returning error as callback if no devices found for the provided channel.
                        return deviceIDSCallback({"code":"400","message":"No registered devices for the provided channel"});
                    }
                }
            });
        };


        //This call is for pushing the notification.
        var pushNotificationToDevices = function (deviceIDsArray, pushNotifyCallback) {
            requestObj["deviceId"] = deviceIDsArray;

            notification.notifyDevices(dbresults,requestObj,function(err,successResult) {
                if(err) {
                    return pushNotifyCallback(err);
                }
                else {
                    return pushNotifyCallback(null,successResult);
                }
            });

        };

        //This is a callback function of pushNotifyToChannel.
        //This will be called once the execution of required methods are done.
        //This method will accept two parameters 'err' & 'result'.
        var finalCallback = function (err, result) {
            //If "err": sending back the error with status code 400
            // or if "result": sending back the result with proper message in JSON object format.
            if(err) {
                return callback(err);
            }
            else {
                return callback(null, result);
            }
        };

        if (requestObj["channelName"]) {

            //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
            //First Param is array of functions & second parameter is callback.
            async.waterfall([getDeviceIDS, pushNotificationToDevices], finalCallback);
        }
        else {
            callback(constants.error.msg_channelName_missing);
        }
    });
};


//This api will be used to get the data of all the channels.
exports.getChannelsData = function (dbName,request, callback) {
    coreDBConnectorObj.switchDB(dbName,function (error,dbInstance) {
        //this is to get the entire data from db.
        //Get all the registered devices. Dont show deviceToken for security purpose.
        DALObj.getData(dbInstance["dbConnection"],channelsCollectionName,channelsModel,{},{_id : false,__v : false}, function(err,devices){
            if(!err){
                console.log('***devices***'+JSON.stringify(devices));
                if (devices && devices.length > 0) {
                    //returning the results response as callback
                    return callback(null,devices);
                }
                else {
                    //returning the results response as callback
                    return callback(null,{"message":"No channels data found."});
                }
            }
            else {
                //returning error as callback
                return callback({"message":err});
            }
        });
    });
};
