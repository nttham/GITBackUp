var DALObj = require('../DAL/DAL.js');
var settingsModelObj =  require('../models/Settings.js');
var constants = require('../constants/constants.json');

// making settingsCollectionName as global such that all functions can use the same Device collection name
var settingsCollectionName = constants.settings_collection_name || 'Settings';

exports.saveSettingsInfo = function(dbInstance,instanceId,settings,callback) {
    DALObj.getData(dbInstance,settingsCollectionName,settingsModelObj,{instanceID : instanceId}, function(err,instances){
        var totalInstances = instances.length;

        if (totalInstances === 0) {

            var settingsDetailsToSave = {
                apnsPassword: settings.apnsPassword,
                apnsCertificate: settings.apnsCertificate,
                instanceID: instanceId
            };

            DALObj.saveData(dbInstance,settingsCollectionName,settingsModelObj,settingsDetailsToSave,function(err){
                if (err) {
                    return callback({"error":"Error in Saving File"});

                } else {
                    return callback(null,settings);
                }
            });
            
        } else {
            DALObj.updateData(dbInstance,settingsCollectionName,settingsModelObj,{instanceID : instanceId}, {$set: settings}, {new: true},function (error,settings) {
                if (error) {
                    //returning error
                    return callback(error);
                }
                else  {
                    return callback(null,settings);
                }
            });
        }
    });
};


exports.deleteAPNSSettingsInfo = function(dbInstance,instanceId,settings,callback) {
    DALObj.getData(dbInstance["dbConnection"],settingsCollectionName,settingsModelObj,{instanceID : instanceId}, function(err,instances){
        var totalInstances = instances.length;
        var cerFileName = instances[0]["apnsCertificate"];

        if (totalInstances === 0 || cerFileName === null) {
            return callback({"error":"No data found to delete"});
        } else {
            DALObj.updateData(dbInstance["dbConnection"],settingsCollectionName,settingsModelObj,{instanceID : instanceId},{$set: settings}, {new: true},function (error,settings) {
                if (error) {
                    //returning error
                    return callback({"error":"Error in updating File"});
                }
                else  {
                    //deleting file from gridStrore
                    dbInstance.gfs.remove({filename: cerFileName}, function (err) {
                        if (err) {
                            return callback(err);
                        }
                        else {
                            return callback(null,'');
                        }
                    });
                }
            });
        }
    });
};

exports.deleteSettingsInfo = function(dbInstance,instanceId,settings,callback) {
    DALObj.getData(dbInstance,settingsCollectionName,settingsModelObj,{instanceID : instanceId}, function(err,instances){
        var totalInstances = instances.length;

        if (totalInstances === 0) {
            return callback({"error":"No data found to delete"});
        } else {
            DALObj.updateData(dbInstance,settingsCollectionName,settingsModelObj,{instanceID : instanceId},{$set: settings}, {new: true},function (error,settings) {
                if (error) {
                    //returning error
                    return callback(error);
                }
                else  {
                    return callback(null,settings);
                }
            });
        }
    });
};

exports.getSettings= function(dbInstance,instanceId,callback) {

    DALObj.getData(dbInstance,settingsCollectionName,settingsModelObj,{"instanceID":instanceId},function(err,result){
        if(err){

            return callback(err);
        }
        else{

            return callback(null,result);
        }
    });
};