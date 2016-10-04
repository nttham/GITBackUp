/**
 * Created by cognizant on 20/07/16.
 */
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var InstanceSchema = new Schema({
    instanceID		: String,
    dbName			: String,
    apiKey		    : String,
    bindedTo		: String

});

module.exports = InstanceSchema;