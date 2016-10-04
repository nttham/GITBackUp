var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//Schema for instance details. For checking apiKey security and fetching the db instance details
var TemplateSchema = new Schema({
    templateId		: String,
    gcm			    : String,
    apns		    : String,
    wns		        : String
});

module.exports = TemplateSchema;