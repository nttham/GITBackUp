var async = require('async');

//This function is for getting the deviceTokens appropriate to platform from the device records
exports.getTokens = function(devices,callback){
    var apnsDevices =[];
    var gcmDevics = [];
    var wnsDevices = [];
    //Get all the tokens from the array of devices documents and also categorise based on APNS, GCM, WNS
    async.each(devices,
        function(devices, callback){
            if(devices.platform === "APNS") {
                apnsDevices.push(devices.deviceToken);
                callback();
            }
            else if(devices.platform === "GCM") {
                gcmDevics.push(devices.deviceToken);
                callback();
            }
            else if(devices.platform === "WNS") {
                wnsDevices.push(devices.deviceToken);
                callback();
            }
        },
        function(err){
            // All tasks are done. now take the categorised tokens and send back
            var tokens = {
                "apnsDevices" : apnsDevices,
                "gcmDevices" : gcmDevics,
                "wnsDevices" : wnsDevices
            };
            callback(tokens);
        }
    );
};
