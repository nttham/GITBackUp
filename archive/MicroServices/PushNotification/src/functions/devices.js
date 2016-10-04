var device = require('../models/device');

var constants = require('../constants/constants.json');
var DALObj = require('../DAL/DAL.js');

// making deviceCollectionName as global such that all functions can use the same Device collection name
var deviceCollectionName = constants.device_collection_name || 'Devices';


// Registering the device details to database. Needs dbInstance, deviceDetails and callback function
//
//  deviceDetails object needs values for keys userId, deviceId, deviceToken, platform, createdMode, isBlackListed
exports.register = function(dbInstance,deviceDetails,callback){

    // Query object to search the device if already registered or not
    var queryObj = {deviceId : deviceDetails.deviceId};

    DALObj.getData(dbInstance,deviceCollectionName,device,queryObj,function(err,devices) {
        var totalDevices = devices.length;

        //If there is no device registered with given deviceId then save details to database else throw error that device already registered
        // Storing time in GMT
        if (totalDevices == 0) {
            var deviceDetailsToSave = {
                userId			: deviceDetails.userId,
                deviceId		: deviceDetails.deviceId,
                deviceToken		: deviceDetails.deviceToken,
                platform		: deviceDetails.platform,
                createdTime		: new Date().getTime(),
                lastUpdatedTime	: "",
                createdMode		: deviceDetails.createdMode,
                isBlackListed	: deviceDetails.isBlackListed
            };

            DALObj.saveData(dbInstance,deviceCollectionName,device,deviceDetailsToSave,function(err){
                if (!err) {
                    callback(constants.success.msg_reg_success);
                } else {
                    callback(constants.error.msg_reg_failure);
                }
            });
        }
        else {
            if (devices[0].deviceToken === deviceDetails.deviceToken) {
                callback(constants.success.msg_reg_already_success);

            } else {
                callback(constants.error.msg_reg_exists);
            }
        }
    });
};



//Get all registered devices from database
exports.listDevices = function(dbInstance,request,callback) {

    //Sending empty object which returns all records from Devices collection
    var queryObj = {};
    if(request && request.headers && request.headers.deviceid && request.headers.userid) {
        queryObj = {
            deviceId : request.headers.deviceid,
            userId   :  request.headers.userid
        };
    }
    else if(request && request.headers && request.headers.deviceid) {
        queryObj = {deviceId : request.headers.deviceid};
    }
    else if(request && request.headers && request.headers.userid) {
        queryObj = {userId : request.headers.userid};
    }
    //Query options to hide deviceToken for security purpose and also other parameters which are not required
    var queryOptions = {_id : false,__v : false};

    DALObj.getData(dbInstance,deviceCollectionName,device,queryObj,queryOptions, function(err,devices){
        if(err){
            console.log("searchDevices function => Something wrong with the database :"+err);
            return callback(constants.error.msg_dbservice_unavailable);
        }
        else{
            return callback(null,devices);
        }
    });
};


//Updating device details based on deviceId
//TODO :: Check if updation can be done with other details like userId or deviceToken
exports.updateDevice = function(dbInstance,deviceId,deviceDetails,callback){

    var objForUpdate = {};

    //Fetch device details to be updated
    if (deviceDetails.userId) {
        objForUpdate.userId = deviceDetails.userId;
    }
    if (deviceDetails.deviceToken) {
        objForUpdate.deviceToken = deviceDetails.deviceToken;
    }
    if (deviceDetails.isBlackListed) {
        objForUpdate.isBlackListed = deviceDetails.isBlackListed;
    }

    //modification datetime for record. Storing in GMT time
    objForUpdate.lastUpdatedTime = new Date().getTime();

    //Criteria is to find the document with deviceId to update
    var criteria = {deviceId:deviceId};

    //setting options to return the new document after updating
    var queryOptions = {new: true};

    //update the device with key as deviceId
    DALObj.findOneAndUpdate(dbInstance,deviceCollectionName,device,criteria,objForUpdate,queryOptions,function(err, doc){
        if (!err) {
            if(doc) {
                callback(constants.success.msg_update_success);
            }
            else {
                callback(constants.error.msg_update_failure);
            }
        } else {
            callback(constants.error.msg_update_failure);
        }
    });
};

//Search eligible device(s) from the given array of deviceIds/deviceId
exports.searchDevices = function(dbInstance,deviceId,callback) {

    //Query with array of deviceIds/deviceId and isBlackListed should be false
    var queryObj = {
        deviceId: { $in: deviceId},
        isBlackListed : {$eq : false}
    };

    DALObj.getData(dbInstance,deviceCollectionName,device,queryObj, function (err, docs) {
        if(err){
            console.log("searchDevices function => Something wrong with the database :"+err);
            return callback(constants.error.msg_dbservice_unavailable);
        }
        else{
            return callback(null,docs);
        }
    });
};


//Get all eligible devices for push notification irrespective of any platform
exports.getAllEligibleDevices = function(dbInstance, callback) {
    var deviceModel = dbInstance.model('Device', device);

    //Query to look for all devices which are not blacklisted
    var queryObj = {
        isBlackListed : {$eq : false}
    };

    DALObj.getData(dbInstance,deviceCollectionName,device,queryObj, function (err, docs) {
        if(err){
            console.log("getAllEligibleDevices function => Something wrong with the database :"+err);
            console.log(err);
            return callback(constants.error.msg_dbservice_unavailable);
        }
        else{
            return callback(null,docs);
        }
    });
};

//Get bulk devices from the database which are eligible for push notification.
// By default it gets devices related to all platforms(apns,gcm,wns). To avoid any platform devices the parameter of platform should be false(eg: apns=false or wms=false or gcm=false)
exports.getBulkDevices = function(dbInstance,req,callback) {

    //scope of this these variable is for all functions and condition. update this queryObj and array with the platforms for querying
    var queryObj;
    var objForPlatform = [];

    var getBulkDevicesCallback = function(){
        DALObj.getData(dbInstance,deviceCollectionName,device,queryObj, function (err, docs) {
            if(err){
                return callback(err);
            }
            else{
                return callback(null,docs);
            }
        });
    };

    //Check if bulkOptions available in request
    if((req && req.bulkOptions) && (req.bulkOptions.apns === false || req.bulkOptions.wns === false || req.bulkOptions.gcm === false)) {

        if(req.bulkOptions.apns !== false) {
            objForPlatform.push("APNS");
        }
        if(req.bulkOptions.gcm !== false) {
            objForPlatform.push("GCM");
        }
        if(req.bulkOptions.wns !== false) {
            objForPlatform.push("WNS");
        }

        queryObj = {
            platform: { $in: objForPlatform},
            isBlackListed : {$eq : false}
        };

        getBulkDevicesCallback();
    }
    else {
        queryObj = {
            platform: { $in: ["APNS","GCM","WNS"]},
            isBlackListed : {$eq : false}
        };
        getBulkDevicesCallback();
    }
};


//Delete the device record using deviceId
exports.removeDevice = function(dbInstance, deviceId, callback){

    var queryObj = {deviceId:deviceId};

    //Delete the device record using deviceId
    //TODO :: This deletion can be expanded to delete device records based on userId or deviceToken
    DALObj.findOneAndRemove(dbInstance, deviceCollectionName, device, queryObj, function(err,doc){
        if (!err) {
            if(doc){
                callback(constants.success.msg_del_success);
            }
            else {
                callback(constants.error.msg_del_failure);
            }
        } else {
            callback(constants.error.msg_del_failure);
        }
    });
};

//Delete the device record using deviceToken
exports.removeFaultyDevice = function(dbInstance, deviceToken, callback){

    var queryObj = {deviceToken:deviceToken};

    //Delete the device record using deviceToken
    DALObj.findOneAndRemove(dbInstance, deviceCollectionName, device, queryObj, function(err,doc){
        if (!err) {
            if(doc){
                callback(constants.success.msg_del_success);
            }
            else {
                callback(constants.error.msg_del_failure);
            }
        } else {
            callback(constants.error.msg_del_failure);
        }
    });
};