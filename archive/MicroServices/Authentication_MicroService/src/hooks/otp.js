/**
 * Created by 423919 on 5/18/2016.
 * This is the Otp module which generate a token and validate the same
 */
// dependencies for this module

var randomString = require("randomstring");
var bodyParser = require('body-parser');
var jsonParser = bodyParser.json();
var Jwt = require("./../jwt/jwt.js");
var async = require('async');
var constants = require('./../constants.json');
var sendmail = require("./sendgridservice.js");
var twilio = require("./twilioservice.js");
var ConditionalHooks = require('./conditionalhooks.js');
var conditionalHooks = new ConditionalHooks();

var statusCode;
var createOTPTokenDetails;

//constructor
var Otp = function () {

};


//Read the config key value from env variables. This will return a JSON string with '=>' symbol in place of ':'
//Replace '=>' symbol with ':' to convert to JSON string and parse to retrieve JSON object
var envJson;
var config;
if (process.env.config) {
    envJson = process.env.config;
    envJson = envJson.replace(/=>/g, ':');
    config = JSON.parse(envJson);
}



// validating the Jwt and get the decodedInfo
var validateJwt = function (request, callback) {


    var jwtToken = request.headers.token; //getting the Jwt token from headers
    var onCallback = function (err, result) {
        if (err) {
            return callback(err);
        }
        else {

            result.request = request; // inserting the request input params for further processing in proceeding calls
            return callback(null, result)
        }
    }
    Jwt.validateJWT(jwtToken, onCallback);
};


//This will generate Otp according to user defined configurations
var createOtp = function (callback) {
    if (createOTPTokenDetails.currentHook && createOTPTokenDetails.hooks && createOTPTokenDetails.totalNoOfhooks) {
        var otpConfig = config.channels.otp;

        var otpOptions = {};
        //creating the otpOptions from the req.body.json
        otpOptions.length = (otpConfig && otpConfig.length) ? otpConfig.length : 4;
        otpOptions.charset = (otpConfig && otpConfig.type) ? otpConfig.type : 'numeric';
        var expiryTime = (otpConfig && otpConfig.expiryTime) ? otpConfig.expiryTime : 15;
        var otpCode = randomString.generate(otpOptions);

        createOTPTokenDetails.otpCode = otpCode;
        delete createOTPTokenDetails.expiresIn;
        createOTPTokenDetails.expiresIn = expiryTime * 60;
        return callback(null, createOTPTokenDetails);
    }
    else {

        return callback({ "error": "Not Authorised" });
    }

};


var sendOtp = function (tokenDetails, callback) {

    if (tokenDetails.hooks[tokenDetails.currentHook - 1].channelprovider === 'twilio') {
        if (tokenDetails.request.body.toRecipient && tokenDetails.request.body.fromNo) {
            //hooking the twilio to OTP

            var twilioObj = new twilio();
            //creating the options for twilio
            var twilioMsgObj = {
                "to": tokenDetails.request.body.toRecipient,
                "from": tokenDetails.request.body.fromNo,
                "body": "OTP pin is " + tokenDetails.otpCode
            };

            twilioObj.sendMessage(twilioMsgObj, function (err, result) {
                if (err) {
                    return callback(err);
                }
                else {
                    return callback(null, tokenDetails);
                }

            });
        }
        else {
            return callback({ "error": "From Number / To Number is missing" });
        }
    } else if (tokenDetails.hooks[tokenDetails.currentHook - 1].channelprovider === 'sendgrid') {

        var sendmailObj = new sendmail();
        if (tokenDetails.request.body.toRecipient && tokenDetails.request.body.fromMail) {
            //creating the options for sendgrid
            var sendGridMsgObj = {
                "toRecipient": tokenDetails.request.body.toRecipient,
                "fromMail": tokenDetails.request.body.fromMail,
                "subject": "Please find the otp",
                "text": "OTP pin is " + tokenDetails.otpCode
            };
            sendmailObj.sendMail(sendGridMsgObj, function (err, result) {

                if (err) {

                    return callback(err);
                }
                else {
                    return callback(null, tokenDetails);

                }
            });
        }
        else {
            return callback({ "error": "fromMail / To Mail is missing" });
        }
    }
    else {
        return callback({ "error": tokenDetails.hooks[tokenDetails.currentHook - 1].channelprovider + " not supported now" });
    }
};


// validates the OTP code
var verifyOtp = function (tokenDetails, callback) {
    if (tokenDetails.currentHook && tokenDetails.hooks && tokenDetails.totalNoOfhooks) {
        var currentTime = new Date().getTime();
        var status = {};
        if (tokenDetails.request.body.otpCode === tokenDetails.otpCode) {
            status.status = "OTP is validated successfully";

            if (tokenDetails.numberOfHooksExecuted) {
                tokenDetails["numberOfHooksExecuted"] = tokenDetails["numberOfHooksExecuted"] + 1;
                console.log("**number of hooks executed**", tokenDetails["numberOfHooksExecuted"]);
            }
            else {
                tokenDetails["numberOfHooksExecuted"] = 1;
                console.log("**number of hooks executed**", tokenDetails["numberOfHooksExecuted"]);
            }

            return callback(null, tokenDetails);
        } else {
            status.status = "OTP validation failed ";
            return callback(status);
        }
    }
    else {
        return callback({ "error": "Not Authorised" });
    }

};


// creates a JWT token
var createJwtForNxtCalls = function (tokenDetails, callback) {
    var nextCall = '', userProfile = {};

    delete tokenDetails.otpExpiryTime;
    delete tokenDetails.otpCode;
    delete tokenDetails.expiresIn;
    delete tokenDetails.request;
    tokenDetails.expiresIn = constants.expiresIn;
    tokenDetails.isPrehookClear = true;

    //creates payload for Jwt
    if (tokenDetails.currentHook < tokenDetails.totalNoOfhooks) {

        tokenDetails.currentHook = (tokenDetails.currentHook + 1);

        if (tokenDetails.hooks[tokenDetails.currentHook - 1].channel === 'OTP') {
            nextCall = '/generateOtp';
            channelprovider = tokenDetails.hooks[tokenDetails.currentHook - 1].channelprovider;
            statusCode = 303
        }
        if (tokenDetails.hooks[tokenDetails.currentHook - 1].channel === 'Captcha') {
            nextCall = '/generateCaptcha';
            statusCode = 303;
        }

    } else if (tokenDetails.hookType == 'prehook') {
        delete tokenDetails.hooks;
        delete tokenDetails.totalNoOfhooks;
        delete tokenDetails.currentHook;
        delete tokenDetails.hookType;
        //tokenDetails.isPrehookClear = true;
        nextCall = '/' + tokenDetails.authenticationType;
        statusCode = 302;
        message = "pass callbackUrl as query param"
    }
    else if (tokenDetails.hookType == 'posthook') {
        delete tokenDetails.hooks;
        delete tokenDetails.totalNoOfhooks;
        delete tokenDetails.currentHook;
        delete tokenDetails.hookType;
        userProfile = tokenDetails.userProfile;
        statusCode = 200;

    }
    delete tokenDetails.nextCall;

    tokenDetails.nextCall = nextCall;

    delete tokenDetails.iat;
    tokenDetails.iat = Math.floor(new Date().getTime() / 1000) - 30 //TODO :: Check this if this is reqd for expiry -- backdate a jwt 30 seconds

    var onCallback = function (err, token) {
        if (err) {
            return callback(err);
        }
        else {
            var resp = {};

            if (nextCall === ("/" + tokenDetails.authenticationType) && tokenDetails.protocol === "oauth") {
                resp.nextCall = nextCall + "/" + token;
                resp.message = "pass callbackUrl as query param";
                statusCode = 302;

            }
            else if (nextCall === ("/" + tokenDetails.authenticationType)) { // for anyother call other than oauth (ie ldap ..) will have 303 status
                resp.nextCall = nextCall;
                resp.token = token;
                statusCode = 303;
            }
            if (nextCall === '/generateOtp') {
                resp.nextCall = nextCall;
                resp.channelprovider = channelprovider;
                resp.token = token;
            }

            if (userProfile && Object.keys(userProfile).length) {
                return callback(null, userProfile);
            }
            else {
                return callback(null, resp);
            }
        }
    }

    Jwt.generateJWT(tokenDetails, onCallback);
};

//setting the response headers and sending the response
function setResponseHeaders(response, result, resultedStatusCode) {
    response.setHeader("Access-Control-Allow-Headers", "*");
    response.setHeader('Content-Type', 'application/json');
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
    response.send(result, resultedStatusCode);
}


//this is to generate conditional hooks exp and eval with heade request. Based on result call generate OTP or skip OTP generation
function skipORGenerateOTP(req, resultedTokenDetails, skipORGenerateOTPCallback) {

    function callBackOfSkipORGenerate(error, isExecutePreHooks) {
        if (error) {
            skipORGenerateOTPCallback(error);
        }
        else {
            if (isExecutePreHooks) {
                createOTPTokenDetails = resultedTokenDetails;
                // creates a JWT token
                var createJwtForGenerateOtp = function (tokenDetails, checkingConditionalCallback) {

                    delete tokenDetails.iat;
                    delete tokenDetails.request
                    tokenDetails.iat = Math.floor(new Date().getTime() / 1000) - 30 //TODO :: Check this if this is reqd for expiry -- backdate a jwt 30 seconds
                    delete tokenDetails.nextCall;
                    tokenDetails.nextCall = '/validateOtp'
                    Jwt.generateJWT(tokenDetails, checkingConditionalCallback);
                };

                var checkingConditionalCallback = function (err, result) {

                    if (err) {
                        skipORGenerateOTPCallback(err);
                    }
                    else {
                        //setting the next api call
                        var resp = {};
                        resp.nextCall = '/validateOtp';
                        resp.token = result;
                        statusCode = 303;
                        skipORGenerateOTPCallback(null, resp);
                    }
                };
                async.waterfall([createOtp, sendOtp, createJwtForGenerateOtp], checkingConditionalCallback);
            }
            else {
                var conditionalHooksFinalCallback = function (err, result) {
                    if (err) {
                        skipORGenerateOTPCallback(err);
                    }
                    else {
                        skipORGenerateOTPCallback(null, result);
                    }
                };

                //calling JWT to get the token and than creating JWT for next calls.
                async.waterfall([validateJwt.bind(null, req), createJwtForNxtCalls], conditionalHooksFinalCallback);
            }
        }
    }

    conditionalHooks.compareHooksCondition(req, resultedTokenDetails, callBackOfSkipORGenerate);
}

// This api is use to generate an OTP based on the length,type and expiry
// time
Otp.prototype.generateOtp = function (app) {
    // route for generate Otp
    app.post("/generateOtp", [jsonParser, Jwt.verifyJWT], function (req, res) {

        if (req.headers.token) {

            //this is to check for conditional hooks in config JSON of environment variables and frame the exp;
            function checkingConditionalHooks(resultedTokenDetails, finalCallback) {
                //callback function
                function checkingConditionalFinalCallback(error, result) {
                    if (error) {
                        finalCallback(error);
                    }
                    else {
                        finalCallback(null, result);
                    }
                }

                skipORGenerateOTP(req, resultedTokenDetails, checkingConditionalFinalCallback);
            }


            //callback function
            function finalCallback(error, result) {
                if (error) {
                    setResponseHeaders(res, JSON.stringify(error), 400);
                }
                else {
                    setResponseHeaders(res, result, statusCode);
                }
            }

            //calling JWT to get the token
            async.waterfall([validateJwt.bind(null, req), checkingConditionalHooks], finalCallback);

        } else {
            setResponseHeaders(res, { "error": "Not authorised" }, 401);
        }
    });
};

// This api is used to validate the otp given by the user,
// with the key
Otp.prototype.validateOtp = function (app) {

    app.post("/validateOTP", [jsonParser, Jwt.verifyJWT], function (req, res) {

        if (req.headers.token) {

            var channelprovider, message;

            if (req.body.otpCode) {

                var finalCallback = function (err, result) {

                    if (err) {
                        setResponseHeaders(res, JSON.stringify(err), 400);
                    }
                    else {
                        setResponseHeaders(res, result, statusCode);
                    }
                };

                async.waterfall([validateJwt.bind(null, req), verifyOtp, createJwtForNxtCalls], finalCallback)

            }
            else {
                setResponseHeaders(res, { "error": "Otp code not provided" }, 400);
            }
        }
        else {
            setResponseHeaders(res, { "error": "Not authorised" }, 401);
        }
    });
};

module.exports = Otp;
