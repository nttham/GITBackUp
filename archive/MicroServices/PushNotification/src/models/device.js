var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//Device details schema
var DeviceSchema = new Schema({
    deviceId		: String,
    userId			: String,
    deviceToken		: String,
    platform		: String,
    createdTime		: Date,
    lastUpdatedTime	: Date,
    createdMode		: String,
    isBlackListed   : Boolean
});

module.exports = DeviceSchema;//mongoose.model('Device', DeviceSchema);
