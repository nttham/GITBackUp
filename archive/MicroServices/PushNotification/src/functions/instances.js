var DALObj = require('../DAL/DAL.js');
var connector = require('../DAL/Connector.js');
var constants = require('../constants/constants.json');
var InstanceModel = require('../models/instance.js');

// making instanceCollectionName as global such that all functions can use the same Instance collection name
var instanceCollectionName = constants.instance_collection_name || 'Instances';


//this api will fetch the info of instances from the DB
exports.getInstance = function(queryObj,callback){
    var dbInstnace = connector.getDB();
    DALObj.getData(dbInstnace,instanceCollectionName,InstanceModel,queryObj, callback);
};