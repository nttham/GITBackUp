/**
 * Created by Srividya on 13/07/16.
 */
//Importing required node modules
var MongoClient = require('mongodb').MongoClient;

//Requiring the config.json file where we have all the required configurations.
var configVals = require('./config.json');

var async = require('async');

var request = require('request');

//This api will be used to createchannel based on the user provided channel name
exports.createCollectionMethod = function (collectionName,callback) {

    // Connect to the db.
    //Parameters: configVals.mongoDBPath is the url of mongodb.
    MongoClient.connect(configVals.mongoDBPath, {auth:{authdb:'admin'}}, function(err, db) {
        //On connecting the DB if any error occurs, it will return the err.
        if(err) {
            return callback(err);
        }

        //create a collection based on the given channel name.
        //collection name is equivalent to channel name.
        db.createCollection(collectionName, {strict:true}, function(err, collection) {
            //On creating the collection if any error occurs, it will return the err and close the db
            if(err) {
                //Closing the DB connection is error occurs.
                db.close();
                return callback({"message":err.message});
            }
            //Closing the DB connection on success.
            db.close();
            //Once collection is created will return the success message as callback
            return callback(null,collection);
        });

    });
};

//This api will be used to deletechannel based on the user provided channel name
exports.deleteCollectionMethod = function (collectionName,callback) {
    // Connect to the db
    MongoClient.connect(configVals.mongoDBPath, {auth:{authdb:'admin'}}, function(err, db) {
        //On connecting the DB if any error occurs, it will return the err.
        if(err) {
            return callback(err);
        }
        //delete a collection based on the given channel name.
        //collection name is equivalent to channel name.
        db.collection(collectionName).drop( function(err, response) {
            //On deleting the collection if any error occurs, it will return the err as callback and close the db
            if(err) {
                db.close();
                //checking error code for the channel name.
                if (err.code === 26) {
                    return callback({"message":"Kindly provide proper channel name to delete."});
                }
                else {
                    return callback({"message":err.message,"code":err.code});
                }
            }
            //Closing the DB connection on success.
            db.close();
            //Once collection is deleted will return the success message as callback.
            return callback(null,response);
        });
    });
};

//This api will be used to subscribe to channel based on the user provided channel name
exports.subscribeToChannel = function (request,callback) {
    // Connect to the db
    MongoClient.connect(configVals.mongoDBPath, {auth:{authdb:'admin'}}, function(err, db) {
        //On connecting the DB if any error occurs, it will return the err.
        if(err) {
            return callback(err);
        }

        //list all the collections present in the DB and
        //check for the existence of collection based on channelname provided.
        //If error it will return the error as callback
        db.listCollections({name: request.headers.channelname})
            .next(function(err, collinfo) {
                //If provided channel is exists as a collection in db, will insert the provided deviceID as _id to the collection.
                if (collinfo) {
                    var document = {_id:request.headers.deviceid};//This id will be the device id of user.
                    db.collection(request.headers.channelname).insertOne(document,{strict:true}, function(err, response) {
                        if(err) {
                            //Closing the DB connection on error.
                            db.close();
                            //checking error code for the deviceID. If it is exists returning the error as a callback.
                            if (err.code === 11000) {
                                return callback({"message":"This device is already subscribed to the channel: "+request.headers.channelname});
                            }
                            else {
                                return callback({"message":err.message,"code":err.code});
                            }
                        }
                        //Closing the DB connection on success.
                        db.close();
                        //On successful subscribe returning the success message as callback.
                        return callback(null,response);
                    });
                }
                else {
                    //Closing the DB connection on error.
                    db.close();
                    //If provided channel name is not exists returning the callback as error.
                    return callback({"message":"Kindly provide the proper channel name to subscribe"});
                }
            });
    });
};

//This api will be used to unsubscribe from channel based on the user provided channel name
exports.unSubscribeToChannel = function (request,callback) {
    // Connect to the db
    MongoClient.connect(configVals.mongoDBPath, {auth:{authdb:'admin'}}, function(err, db) {
        //On connecting the DB if any error occurs, it will return the err as callback.
        if(err) {
            return callback(err);
        }

        //list all the collections present in the DB and
        //check for the existence of collection based on channelname provided.
        //If error it will return the error as callback
        db.listCollections({name: request.headers.channelname})
            .next(function(err, collinfo) {
                //If provided channel is exists as a collection in db, will delete the provided deviceID as _id to the collection.
                if (collinfo) {
                    var document = {_id:request.headers.deviceid};//This id will be the device id of user.
                    db.collection(request.headers.channelname).deleteOne(document, function(err, response) {
                        if(err) {
                            //Closing the DB connection on error.
                            db.close();
                            //checking error code for the deviceID. If it is not exists returning the error as a callback.
                            if (err.code === 11000) {
                                return callback({"message":"This device is already unsubscribed from the channel: "+request.headers.channelname});
                            }
                            else {
                                return callback({"message":err.message,"code":err.code});
                            }
                        }
                        //Closing the DB connection on success.
                        db.close();
                        //checking error code for the deviceID. If it is not exists returning the error as a callback.
                        if (response.deletedCount === 0) {
                            return callback({"message":"This device is already unsubscribed from the channel: "+request.headers.channelname});
                        }
                        else {
                            //On successful subscribe returning the success message as callback.
                            return callback(null,response);
                        }
                    });
                }
                else {
                    //Closing the DB connection on error.
                    db.close();
                    //If provided channel name is not exists returning the callback as error.
                    return callback({"message":"Kindly provide the proper channel name to unsubscribe"});
                }
            });
    });
};


//This api will be used to notification to a channel based on channel name provided
exports.pushNotifyToChannel = function (requestObj,callback) {

    //This is to get the deviceID's from DB based on the channelname provided in body.
    var getDeviceIDS = function (deviceIDSCallback) {
        // Connect to the db
        MongoClient.connect(configVals.mongoDBPath, {auth:{authdb:'admin'}}, function(err, db) {
            //On connecting the DB if any error occurs, it will return the err as callback.
            if(err) {
                return deviceIDSCallback(err);
            }

            //list all the collections present in the DB and
            //return the all the objects of that collection based on channel name as an array format.
            //If error it will return the error as callback
            db.listCollections({name: requestObj.body.channelname})
                .next(function(err, collinfo) {
                    if (collinfo) {
                        db.collection(requestObj.body.channelname).find().toArray(function(err, response) {
                            //Closing the DB connection on error.
                            db.close();
                            if(err) {
                                //while finding the collection if any error occurs return the error as callback
                                return deviceIDSCallback({"message":err.message,"code":err.code});
                            }
                            else if (response.length > 0) {
                                //Once the collection is found get all the deviceID's for the collection and return back as callback.
                                var resultsArray = [];
                                response.forEach (function (data){
                                    resultsArray.push(data["_id"]);
                                });
                                return deviceIDSCallback(null,resultsArray);
                            }
                            else {
                                //If no devices are subscribed return back error message as callback.
                                return deviceIDSCallback('No document(s) found');
                            }
                        });
                    }
                    else {
                        //Closing the DB connection on error.
                        db.close();
                        return deviceIDSCallback({"message":"Kindly provide the proper channel name"});
                    }
                });
        });
    };

    //This call is for pushing the notification.
    var pushNotificationToDevices = function (deviceIDsArray,pushNotifyCallback) {

        //constructing the body for the push call
        var body = {
            "message" : requestObj.body["message"],
            "deviceId" : deviceIDsArray
        };
        if (requestObj.body["settings"]) {
            body["settings"] = requestObj.body["settings"];
        }

        //forming http request string
        var options = {
            url: configVals.pushNotifyEndPoint,
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(body)
        };

        console.log('*****Push Notification body*****'+options.body);

        //making the request
        request(options, function (err, response) {
            if (err) {
                //return back the error as callback
                return pushNotifyCallback(err);
            }
            else {
                if (response.statusCode && response.statusCode == 202) {
                    //return back the success message as the callback.
                    return pushNotifyCallback(null,{"message":response.body});
                }
                else {
                    return pushNotifyCallback({"message":response.body});
                }
            }
        });
    };

    //This is a callback function of pushNotifyToChannel.
    //This will be called once the execution of required methods are done.
    //This method will accept two parameters 'err' & 'result'.
    var finalCallback = function (err,result) {
        //If "err": sending back the error with status code 400
        // or if "result": sending back the result with proper message in JSON object format.
        if (err) {
            return callback(err);
        }
        else {
            return callback(null,result);
        }
    };

    //This is to execute set of methods synchronously. callback will be called once array of functions execution is done.
    //First Param is array of functions & second parameter is callback.
    async.waterfall([getDeviceIDS,pushNotificationToDevices], finalCallback);
};