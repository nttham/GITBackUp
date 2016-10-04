var mongoose = require('mongoose');
var Schema = mongoose.Schema;

//Schema for all the required credentials of APNS, GCM and WNS Push notification
var SettingsSchema = new Schema({
    gcmApiKey		: String,
    apnsPassword	: String,
    apnsCertificate	: String,
    wnsClientId     : String,
    wnsClientSecret : String,
    instanceID      : String
});

module.exports = SettingsSchema;//mongoose.model('Settings', SettingsSchema);