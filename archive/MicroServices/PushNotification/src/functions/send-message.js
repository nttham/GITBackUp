var apn = require('apn');
var path = require('path');
var constants = require('../constants/constants.json');
var gcm = require('node-gcm');
var wns = require('wns');
var devicesFunction = require('./devices');
var async = require('async');

//Function to send notification to APNS devices
exports.notifyAPNSDevices = function(message,deviceToken,settings,options,template,dbInstance,callback){


	try {
		var apnsError = function(err){
			console.log("APNS Error(errorCallback) :", err);
			return callback(err);
		};

        //TODO :: need to get this production settings from developer?
        options.production = true;

		options.errorCallback = apnsError;

        //Prepare the connection with options
		var connection = new apn.Connection(options);
		var note;


		var feedback = new apn.Feedback(options);

		//check if apns template available. else proceed with default
		if(template.length === 1 && template[0] && template[0].apns) {
			//Initiate notification object with template settings
			note = new apn.Notification(JSON.parse(template[0].apns));

			//message to be sent in notification
			note.alert = message;

			//assign the payload info received from request
			if(settings && settings.payload) {
				note.payload = settings.payload;
			}
		}
		else {
			//Initiate notification object with settings received from request
			note = new apn.Notification(settings);

			//message to be sent in notification
			note.alert = message;
		}

        //Send the notification to all device tokens fetched
		connection.pushNotification(note, deviceToken);

        // A submission action has completed. This just means the message was submitted, not actually delivered.
		connection.on('completed', function(a) {
			console.log('APNS: Completed sending push notification');
			callback({"message":"APNS: Completed sending push notification"});
		});

		// A message has been transmitted.
		connection.on('transmitted', function(notification, device) {
			console.log('APNS: Successfully transmitted message to '+device);
		});

		// There was a problem sending a message.
		connection.on('transmissionError', function(errorCode, notification, device) {
			var deviceToken = device.toString('hex').toUpperCase();
			if (errorCode === 8) {
				console.log('APNS: Transmission error -- invalid token', errorCode, deviceToken);
				//callback(constants.error.msg_send_failure);
				// Do something with deviceToken here - delete it from the database?
				devicesFunction.removeFaultyDevice(dbInstance['dbConnection'],deviceToken,function(result) {
					console.log("Status in removing APNS device with token :"+deviceToken);
					console.log(result);

				});
			} else {
				console.error('APNS: Transmission error', errorCode, deviceToken);
				return callback(errorCode);
			}
		});

		connection.on('connected', function() {
			console.log('APNS: Connected');
		});

		connection.on('timeout', function() {
			console.error('APNS: Connection timeout');
		});

		connection.on('disconnected', function() {
			console.error('APNS: Lost connection');
		});

		connection.on('socketError', console.log);


		//Feedback Events
		feedback.on("feedbackError", function(error) {
			console.log("feedbackError in APNS :",error);
		});

		feedback.on("error", function(error) {
			console.log("feedback error in APNS :",error);
			return callback(error);
		});

		feedback.on("feedback", function(devices) {
			console.log("feedback for the devices : ",devices);
			//devices.forEach(function(item) {
			//	//TODO Do something with item.device and item.time;
			//});
		});

	}
	catch (ex) {
		console.log("ERROR Exception in APNS :"+ex);
		callback(ex);
	}
};

//Function to send notification to Android devices
exports.notifyGCMDevices = function(alertMessage,registrationId,settings,gcmKey,template,callback){

	try {
		var message;

		//Check if template available for gcm. If not available then proceed with default settings
		if(template.length === 1 && template[0] && template[0].gcm) {

			//Initialize the notification object with template settings
			message = new gcm.Message(JSON.parse(template[0].gcm));

			//Add notification parameters
			message.addNotification(JSON.parse(template[0].gcm));
		}
		else {
			//Initialize the notification object with settings received from request
			message = new gcm.Message(settings);
		}

		//Adding body parameter to notification object
		message.addNotification('body', alertMessage);

		//If settings available then adding it to data parameter
		if(settings && settings.data) {
			message.addData(settings.data);
		}

		// Set up the sender with you API key, prepare your recipients' registration tokens.
		var regTokens = registrationId;
		var sender = new gcm.Sender(gcmKey);
		sender.send(message, { registrationTokens: regTokens }, function (err, response) {
			if (err){
				console.error("GCM Push notification error ",err);
				callback(err);
			} else 	{
				console.log("GCM Push notification response ",response);
				callback(response);
			}
		});

	}
	catch (ex) {
		console.log("ERROR Exception in GCM :"+ex);
		callback(ex);
	}
};


exports.notifyWNSDevices = function(message,channelurls,wnsSettings,options,template,callback){
	try {
		//Check if template and other payload details available
		if(template.length === 1 && template[0] && template[0].wns && wnsSettings && wnsSettings.payload && wnsSettings.payload.type) {
			var type = JSON.parse(template[0].wns).type;
			var payload = wnsSettings.payload;
			var subType = JSON.parse(template[0].wns).subType;
			var TTL = JSON.parse(template[0].wns).ttl;
			payload.type = subType;
			var responses = [];

			//If TTL available in templates then assign it to X-WNS-TTL header
			if(TTL) {
				options.headers  =  {
					"X-WNS-TTL" : TTL
				};
			}

			//Check for type based on that call the appropriate function of WNS
			//Calling type toast
			if(type && type === "toast" && subType) {
				async.each(channelurls,
					function(channelurl, asyncCallback){
						wns.sendToast(channelurl,  payload, options, function (error, result) {
							if (error) {
								console.error(error);
								responses.push(error)
								asyncCallback()
							}
							else {
								console.log(result);
								responses.push(result)
								asyncCallback()
							}
						});
					},
					function(err){
						if(err) {
							callback(err);
						}
						else {
							callback(responses);
						}
					}
				);

			}
			//Calling type tile
			else if (type && type === "tile" && subType) {
				async.each(channelurls,
					function(channelurl, asyncCallback){
						wns.sendTile(channelurl,  payload, options, function (error, result) {
							if (error) {
								console.error(error);
								responses.push(error)
								asyncCallback()
							}
							else {
								console.log(result);
								responses.push(result)
								asyncCallback()
							}
						});
					},
					function(err){
						if(err) {
							callback(err);
						}
						else {
							callback(responses);
						}
					}
				);
			}
			//Calling type raw
			else if(type && type === "raw") {
				async.each(channelurls,
					function(channelurl, asyncCallback){
						wns.sendRaw(channelurl,  message, options, function (error, result) {
							if (error) {
								console.error(error);
								responses.push(error)
								asyncCallback()
							}
							else {
								responses.push(result)
								asyncCallback()
							}
						});
					},
					function(err){
						if(err) {
							callback(err);
						}
						else {
							callback(responses);
						}
					}
				);
			}
			else {
				console.error("Invalid type chosen from command center");
				callback({"error":"Invalid type chosen from command center"});
			}
		}
		//If no templates are available then by default it will go for raw messaging format
		else {
			channelurls.forEach(function(channelurl) {

				wns.sendRaw(channelurl,  message, options, function (error, result) {
					if (error)
						console.error(error);
					else
						console.log(result);
				});
			});
		}
	}
	catch (ex) {
		console.log("ERROR Exception in WNS :"+ex);
	}
};



