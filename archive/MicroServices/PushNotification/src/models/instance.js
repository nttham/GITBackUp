var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//Schema for instance details. For checking apiKey security and fetching the db instance details
var InstanceSchema = new Schema({
    instanceID		: String,
    dbName			: String,
    apiKey		    : String,
    bindedTo		: String

});

module.exports = InstanceSchema;