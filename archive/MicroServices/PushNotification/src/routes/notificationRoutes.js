var constants = require('../constants/constants.json');
var notification = require('../functions/notification');
var authValidationObj = require('../functions/authenticateAPI.js');

//Channels functions
var channelsClientObj = require('../functions/channelsClient.js');

module.exports = function(app) {

    app.post('/notification',function(req,res) {
        console.log("Request body for notification : "+JSON.stringify(req.body));

        var notificationCall = function (error,results) {
            if (error) {
                res.status(400).send(error);
            }
            else if (results.length) {
                var message = req.body.message;
                var type = req.body.type;
                //var deviceId = req.deviceId;
                //var channelName = req.channelName;

                var notifyCallback = function(err,successResult) {
                    if(err) {
                        if(err.code && err.message) {
                            res.status(err.code).send(err);
                        }
                        else {
                            res.status(constants.error.msg_dbservice_unavailable.code).send(constants.error.msg_dbservice_unavailable);
                        }
                    }
                    else {
                        res.status(successResult.code).send(successResult);
                    }
                };

                if ( typeof type  === 'undefined' || typeof message  === 'undefined') {
                    console.log(constants.error.msg_invalid_param);
                    res.status(constants.error.msg_invalid_param.code).send(constants.error.msg_invalid_param);
                }
                else if(type === 'device') {
                    notification.notifyDevices(results,req.body,notifyCallback);
                }
                else if(type === 'channel') {
                    channelsClientObj.pushNotifyToChannel(results,req.body,notifyCallback);
                }
                else if(type === 'bulk') {
                    notification.notifyBulk(results,req.body,notifyCallback);
                }
                else {
                    res.status(400).send({"error":"Invalid type for push notification"});
                }
            }
            else {
                //sending error if it is unauthorised
                res.status(401).send({"error":"Unauthorised request"});
            }
        };

        authValidationObj.authenticateAPI(req,notificationCall);
    });
};