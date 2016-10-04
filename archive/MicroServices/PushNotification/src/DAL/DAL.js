var connector = require('./Connector.js');

/**
 * Saves the document to Mongo db.
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} objToSave - Data object to be stored in database
 * @param {Function} callback -  callback function
 * @api public
 */
exports.saveData = function(dbInstance, collectionName, modelObj , objToSave , callback){
        var dbModel = dbInstance.model(collectionName, modelObj);
        var newSaveObj = new dbModel(objToSave);
        newSaveObj.save(callback);
};


/**
 * fetch the info/recoreds from the DB
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} queryObj - query object to fetch record(s) from the database
 * @param {object} [queryOptions] Optional parameter - Options to apply on return query results
 * @param {Function} callback -callback function
 * @api public
 */
exports.getData = function(dbInstance, collectionName, modelObj, queryObj, queryOptions, callback){
        var dbModel = dbInstance.model(collectionName, modelObj);

        //If queryOptions is not passed then check callback is there in place of queryOptions. If so assign queryOptions to callback and empty object to queryOptions
        if (typeof queryOptions === 'function') {
            callback = queryOptions;
            queryOptions = {};
        }

        dbModel.find(queryObj,queryOptions,callback);
};


/**
 * Update the document/record(s) in the database
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} conditions - Conditions object to query
 * @param {object} updateObj - data object to be updated
 * @param {object} [options] - optional parameter - options object to apply on mongoDB query
 * @param {Function} callback - callback function
 * @api public
 */
exports.updateData = function(dbInstance, collectionName, modelObj, conditions, updateObj, options, callback){

    //If options is not passed then check callback is there in place of options. If so assign options to callback and empty object to options
    if (typeof options === 'function') {
        callback = options;
        options = {};
    }
    var dbModel = dbInstance.model(collectionName, modelObj);
    dbModel.update(conditions, updateObj, options, callback);
};

/**
 * delete the document/record(s) in the database
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} queryObj - query object to delete record(s) from the database
 * @param {Function} callback - callback function
 * @api public
 */
exports.deleteData = function(dbInstance, collectionName, modelObj, queryObj,callback){
        var dbModel = dbInstance.model(collectionName, modelObj);
        dbModel.remove(queryObj, callback);
};


/**
 * Find one and delete the document in the database
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} queryObj - query object to delete record(s) from the database
 * @param {object} [options] - optional parameter - options object to apply on mongoDB query
 * @param {Function} callback - callback function
 * @api public
 */
exports.findOneAndRemove = function(dbInstance, collectionName, modelObj, queryObj, options, callback){
        var dbModel = dbInstance.model(collectionName, modelObj);

        //If options is not passed then check callback is there in place of options. If so assign options to callback and empty object to options
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }

        dbModel.findOneAndRemove(queryObj,options,callback);
};


/**
 * Find one and updates that document in the database
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} criteria - query object to check for the record to be updated
 * @param {object} doc - Data object to be updated in the database
 * @param {object} [options] - optional parameter - options object to apply on mongoDB query
 * @param {Function} callback - callback function
 * @api public
 */
exports.findOneAndUpdate = function(dbInstance, collectionName, modelObj, criteria, doc, options, callback){
    var dbModel = dbInstance.model(collectionName, modelObj);

    //If options is not passed then check callback is there in place of options. If so assign options to callback and empty object to options
    if (typeof options === 'function') {
        callback = options;
        options = {};
    }

    dbModel.findOneAndUpdate(criteria, doc, options, callback);
};



/**
 * Find one record
 *
 *
 * @param {object} dbInstance - Instance of dbConnection
 * @param {String} collectionName - Name of the collection to which data to be stored
 * @param {object} modelObj - Object pointing to the Model/Schema
 * @param {object} criteria - query object to check for the record
 * @param {object} [projection] - optional parameter - options object to apply on mongoDB query for projection
 * @param {Function} callback - callback function
 * @api public
 */
exports.findOne = function(dbInstance, collectionName, modelObj, criteria, projection, callback){
    var dbModel = dbInstance.model(collectionName, modelObj);

    //If options is not passed then check callback is there in place of options. If so assign options to callback and empty object to options
    if (typeof projection === 'function') {
        callback = projection;
        projection = {};
    }

    dbModel.findOne(criteria,projection, callback);
};

