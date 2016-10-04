var templateModelObj = require('../models/template');
var constants = require('../constants/constants.json');
var DALObj = require('../DAL/DAL.js');

// making templateCollectionName as global such that all functions can use the same Device collection name
var templateCollectionName = constants.template_collection_name || 'Templates';

//Get all registered devices from database
exports.listTemplates = function(dbInstance,request,callback) {
    //Sending empty object which returns all records from Devices collection
    var queryObj = {};
    if(request && request.headers && request.headers.templateid) {
        queryObj = {
            templateId : request.headers.templateid
        };
    }

    //Query options to hide deviceToken for security purpose and also other parameters which are not required
    var queryOptions = {_id : false,__v : false};

    DALObj.getData(dbInstance,templateCollectionName,templateModelObj,queryObj,queryOptions, function(err,templates){
        if(err){
            console.log("listTemplates function => Something wrong with the database :"+err);
            return callback(constants.error.msg_dbservice_unavailable);
        }
        else{
            return callback(null,templates);
        }
    });
};