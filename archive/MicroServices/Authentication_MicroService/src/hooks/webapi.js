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

var request = require('request');

var ConditionalHooks = require('./conditionalhooks.js');
var conditionalHooks = new ConditionalHooks();

//constructor
var WebApi = function () {

};


//Read the config key value from env variables. This will return a JSON string with '=>' symbol in place of ':'
//Replace '=>' symbol with ':' to convert to JSON string and parse to retrieve JSON object
var envJson;
var config;
if (process.env.config) {
    envJson = process.env.config;
    envJson = envJson.replace(/=>/g, ':');
    config = JSON.parse(envJson);
    webApiConfig = config.configuration.httprequest;

}


// validating the Jwt and get the decodedInfo
var validateJwt = function (request, callback) {


    var jwtToken = request.headers.token; //getting the Jwt token from headers
    var onCallback = function (err, result) {
        if (err) {

            return callback(err);
        }
        else {
            var RequestObj = {};
            RequestObj.tokenDetails = result;//decrypted token form the JWT is assigned to tokenDetails for further processing
            RequestObj.request = request; // inserting the request input params for further processing in proceeding calls

            return callback(null, RequestObj)
        }
    }

    Jwt.validateJWT(jwtToken, onCallback);
};

// This api is used to get the configJson of the apiType from the environment JSON

var getConfigJson = function (restApiDetails, callback) {



    if (Object.keys(webApiConfig).length === 0) {
        return callback({ error: "kindly configure for webapi" });
    } else {
        var options = {};
        var webConfig = {};
        var req = restApiDetails.request;

        // gets the configuration depending upon the apiType
        if (req.apiType === constants.generate) {
            webConfig = webApiConfig.contentGenerationEndPoint;

        } else if (req.apiType === constants.validate) {
            webConfig = webApiConfig.validationEndpoint;
        }
        webConfig.headers.clientID = req.headers.clientid;
        webConfig.headers.clientSecret = req.headers.clientsecret;
        //forming the inputObject for the next call
        webConfig.apiType = req.apiType;   // contains generate or validate
        webConfig.tokenDetails = restApiDetails.tokenDetails; // token details from the input JWT
        webConfig.input = req;  // input request object
        return callback(null, webConfig);


    }


};
// checks whether both the object parameters are same
function validateJson(jsonObj1, jsonObj2) {


    var aKeys = Object.keys(jsonObj1).sort();
    var bKeys = Object.keys(jsonObj2).sort();
    return JSON.stringify(aKeys) === JSON.stringify(bKeys);
};

//This api is used to compare the input with configured values
var validateInputJson = function (inputRequest, callback) {

    //madates accept header is present
    var checkHeaders = function (inputObj, callback) {
        if (inputObj.input.headers.accept) {

            return callback(null, inputObj);

        }
        else {
            return callback({ "error": "Headers are missing" });
        }

    };

    //compares whether the object parameters are same
    // for both the Query Params and input if it is same all  Query Params are copied

    var checkQueryParams = function (inputObj, callback) {
        var inputQueryObj = inputObj.input.query;
        var configQueryObj = inputRequest.query;
        if (Object.keys(inputQueryObj).length) {
            if (validateJson(inputQueryObj, configQueryObj)) {
                return callback(null, inputObj);
            }
            else {
                return callback({ "error": "Query  are missing" });
            }
        } else {
            return callback(null, inputObj);
        }
    };

    //compares whether the object parameters are same
    // for both the Path Params and input if it is same all  Path Params are copied

    var checkPathParams = function (inputObj, callback) {
        var inputPathObj = inputObj.input.path;

        if (inputPathObj[inputPathObj.length - 1] === '/') {
            inputPathObj = inputPathObj.slice(0, inputPathObj.length - 1);
        }
        var path = inputPathObj.split('/');

        var pathParams = path.slice(3, path.length).sort();
        if (inputRequest.path) {
            var configPathObj = Object.keys(inputRequest.path);

            if (pathParams.length === configPathObj.length) {
                delete inputObj.input.path;

                inputObj.urlPath = pathParams;

                return callback(null, inputObj);
            } else {
                return callback({ "error": "Path Params mismatch from the configured values" });
            }
        } else {
            return callback(null, inputObj);
        }
    };

    //compares whether the object parameters are same
    // for both the body Params and input if it is same all  body Params are copied

    var checkBody = function (inputObj, callback) {
        var inputBodyObj = inputObj.input.body;
        var configBodyObj = inputRequest.body;
        if (Object.keys(inputBodyObj).length) {
            if (validateJson(inputBodyObj, configBodyObj)) {
                return callback(null, inputObj);
            }
            else {
                return callback({ "error": "body params mismatch from the configured values" });
            }
        } else {
            return callback(null, inputObj);
        }
    };

    // The control flow is defined as below , for further processing of the input the request object is
    // passed to checkHeaders()


    async.waterfall([checkHeaders.bind(null, inputRequest), checkQueryParams, checkPathParams, checkBody], callback)
};

// This api will append the path parameters to the url

var getPathUrl = function (inputRequest, callback) {

    //checks whether the last character is / if it is not present appending it
    var url = inputRequest.url
    if (url[url.length - 1] !== '/') {
        url = url + '/';
    }

    // This api will return the url appended with path params
    var append = function (replaceStr, callback) {
        url = url + replaceStr + '/';

        return callback(null, url);
    };
    var finalCallback = function (err, result) {
        if (err) {
            return callback(err);
        }
        else {
            delete inputRequest.url;
            inputRequest.url = result[result.length - 1];
            return callback(null, inputRequest);
        }
    }
    if (inputRequest.urlPath && inputRequest.urlPath.length) {

        // Url is appended with each path param

        async.map(inputRequest.urlPath, append, finalCallback);
    }
    else {

        delete inputRequest.url;
        inputRequest.url = url;
        callback(null, inputRequest);
    }
};

// creates a JWT token
var createJwt = function (input, callback) {

    //Jwt token is created with existing token Details ,modified values are entered after deleting the existing values
    //mainly next Call and status code are formed below

    var nextCall = '', userProfile = {};

    var statusCode;
    var channelprovider, message;

    var tokenDetails = input.tokenDetails;
    tokenDetails.isPrehookClear = true;
    if (input.apiType === constants.generate) {

        delete tokenDetails.iat;
        delete tokenDetails.request
        var currentDate = new Date();
        var gmtValue = currentDate.getTime() + (currentDate.getTimezoneOffset() * 60000)
        tokenDetails.iat = Math.floor(gmtValue / 1000) - 30 //TODO :: Check this if this is reqd for expiry -- backdate a jwt 30 seconds
        tokenDetails.statusCode = 303;
        tokenDetails.nextCall = '/webapi/validate/*';
    }
    else {

        // if api Type is validate removes unwanted entries from the token Details
        delete tokenDetails.otpExpiryTime;
        delete tokenDetails.otpCode;
        delete tokenDetails.expiresIn;
        delete tokenDetails.request;
        tokenDetails.expiresIn = constants.expiresIn;

        //creates payload for Jwt
        if (tokenDetails.currentHook < tokenDetails.totalNoOfhooks) { //  checks whether if there are further more hooks to process
            //curentHook pointer is incremeted to next hook
            tokenDetails.currentHook = (tokenDetails.currentHook + 1);
            //Depending upon the channel of next hook to be executed nextCall,channelprovider
            //and statusCode are formed here
            if (tokenDetails.hooks[tokenDetails.currentHook - 1].channel === 'OTP') {
                tokenDetails.nextCall = '/generateOtp';
                channelprovider = tokenDetails.hooks[tokenDetails.currentHook - 1].channelprovider;
                tokenDetails.statusCode = 303
            }
            if (tokenDetails.hooks[tokenDetails.currentHook - 1].channel === 'Captcha') {
                tokenDetails.nextCall = '/generateCaptcha';
                tokenDetails.statusCode = 303;
            }
            if (tokenDetails.hooks[tokenDetails.currentHook - 1].channel === 'WebApIHook') {
                tokenDetails.nextCall = '/webapi/generate/*';
                tokenDetails.statusCode = 303;
            }
        } else if (tokenDetails.hookType == 'prehook') { // if there are no furthemore hooks and this was end of prehooks
            delete tokenDetails.hooks;
            delete tokenDetails.totalNoOfhooks;
            delete tokenDetails.currentHook;
            delete tokenDetails.hookType;
            //tokenDetails.isPrehookClear = true;
            tokenDetails.nextCall = '/' + tokenDetails.authenticationType;
            tokenDetails.statusCode = 302;
            tokenDetails.message = "pass callbackUrl as query param"

        }
        else if (tokenDetails.hookType == 'posthook') {// if there are no furthemore hooks and this was end of posthooks
            delete tokenDetails.hooks;
            delete tokenDetails.totalNoOfhooks;
            delete tokenDetails.currentHook;
            delete tokenDetails.hookType;
            userProfile = tokenDetails.userProfile;
            tokenDetails.statusCode = 200;

        }


        delete tokenDetails.iat;
        var currentDate = new Date();
        var gmtValue = currentDate.getTime() + (currentDate.getTimezoneOffset() * 60000)
        tokenDetails.iat = Math.floor(gmtValue / 1000) - 30 //TODO :: Check this if this is reqd for expiry -- backdate a jwt 30 seconds


    }

    var oncallback = function (err, token) {
        if (err) {
            return callback(err);
        }
        else {
            if (input.apiType === constants.generate) {
                input.token = token;
                return callback(null, input);
            }
            else {

                var resp = {};
                //this will append the token to the next call and the message to intimate the developers to pass
                if (tokenDetails.nextCall === ("/" + tokenDetails.authenticationType) && tokenDetails.protocol === "oauth") {

                    var nextCall = tokenDetails.nextCall;
                    delete tokenDetails.nextCall;
                    delete tokenDetails.message;
                    tokenDetails.nextCall = nextCall + "/" + token;
                    tokenDetails.message = "pass callbackUrl as query param";
                    tokenDetails.statusCode = 302;

                }
                else if (tokenDetails.nextCall === ("/" + tokenDetails.authenticationType)) { // for anyother call other than oauth (ie ldap ..) will have 303 status
                    delete tokenDetails.nextCall;
                    delete tokenDetails.token;
                    delete tokenDetails.statusCode
                    tokenDetails.nextCall = nextCall;
                    tokenDetails.token = token;
                    tokenDetails.statusCode = 303;

                }


                if (tokenDetails.nextCall === '/generateOtp') {
                    delete tokenDetails.channelprovider
                    tokenDetails.channelprovider = channelprovider;
                }

                if (tokenDetails.statusCode === 200) {
                    delete tokenDetails.nextCall;
                }


                if (userProfile && Object.keys(userProfile).length) {
                    input.userProfile = userProfile;
                    return callback(null, input);
                }
                else {
                    input.token = token;


                    return callback(null, input);
                }
            }

        }
    }
    Jwt.generateJWT(tokenDetails, oncallback);

};


function executeWepAPI(req, res) {
    var performRequest = function (err, inputRequest) {

        if (err) {

            res.send({ "error": err }, 400);
        }
        else {
            //forming the options object with the url, query string , headers and body
            var options = {};
            //url and method are taken from the config values
            options.uri = inputRequest.url;
            options.method = inputRequest.method;

            // query string, headers and body are copied from the
            //input request
            if (Object.keys(inputRequest.input.query).length !== 0) {
                options.qs = inputRequest.query;
            }
            if (Object.keys(inputRequest.input.headers).length !== 0) {
                options.headers = inputRequest.headers;
            }
            if (Object.keys(inputRequest.input.body).length !== 0) {
                options.json = inputRequest.body;
            }


            console.log("Api is getting called " + JSON.stringify(options));

            //request call to the configured api is done here with the formed options
            request(options).on('response', function (response) {
                // if the response code is success we will edit the response according
                // to status code formed in the createJwt which indicates the next call
                // which has to be called by the developer
                if (response.statusCode === 200) {
                    delete response.statusCode;
                    response.statusCode = inputRequest.tokenDetails.statusCode;

                    ////setting the headers which is needed for the subsequent calls Of Oauth
                    if (inputRequest.token) {
                        response.headers.token = inputRequest.token;

                    }
                    if (inputRequest.tokenDetails.message) {
                        response.headers.message = inputRequest.tokenDetails.message;
                    }
                    if (inputRequest.tokenDetails.nextCall) {
                        response.headers.nextCall = inputRequest.tokenDetails.nextCall;
                    }
                    if (inputRequest.tokenDetails.channelprovider) {
                        response.headers.channelprovider = inputRequest.tokenDetails.channelprovider;
                    }
                    if (inputRequest.userProfile && Object.keys(inputRequest.userProfile).length !== 0) {
                        response.headers.accessToken = inputRequest.userProfile.accessToken;
                    }

                }

            })
                .pipe(res); // the response of the api is piped to the response Object here

        }

    };
    // The control flow is defined as below , for further processing of the input the request object is
    // passed to the validate JWT api.
    async.waterfall([validateJwt.bind(null, req), getConfigJson, validateInputJson, getPathUrl, createJwt], performRequest);
}


WebApi.prototype.manageWebApi = function (app) {
    // wild charater * is given to catch the dynamic parameters which will be passed by the developers

    app.post("/webapi/*", function (req, res) {

        var path = req.path.split('/');
        // checks whether the user has passed a token and the 1st parameter is generate or validate
        // This route accepts only /webapi/generate/* and /webapi/validate/*
        if (req.headers.token && ((path[2] === constants.generate) || (path[2] === constants.validate))) {

            //setting the apiType as generate or validate depending upon the request
            req.apiType = path[2];

            //this is to check for conditional hooks in config JSON of environment variables and frame the exp;
            function checkingConditionalHooks(resultedTokenDetails, finalCallback) {
                //callback function
                function checkingConditionalFinalCallback(error, isExecutePrehook) {
                    if (error) {
                        finalCallback(error);
                    }
                    else {
                        if (isExecutePrehook) {
                            executeWepAPI(req, res);
                        }
                        else {
                            var conditionalHooksFinalCallback = function (err, result) {
                                if (err) {
                                    res.send(err);
                                }
                                else {
                                    res.setHeader("Access-Control-Allow-Headers", "*");
                                    res.setHeader('Content-Type', 'application/json');
                                    res.setHeader("Access-Control-Allow-Origin", "*");
                                    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
                                    var resultObj = {
                                        "nextCall":result["tokenDetails"]["nextCall"],
                                        "channelprovider":result["tokenDetails"]["channelprovider"],
                                        "token":result["tokenDetails"]["token"]
                                    };
                                    res.send(resultObj, result.tokenDetails.statusCode);
                                }
                            };

                            //calling JWT to get the token and than creating JWT for next calls.
                            async.waterfall([validateJwt.bind(null, req), createJwt], conditionalHooksFinalCallback);
                        }

                    }
                }

                conditionalHooks.compareHooksCondition(resultedTokenDetails["request"], resultedTokenDetails["tokenDetails"], checkingConditionalFinalCallback);
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
            console.log("hdfjhdgfhjdgfhjfghfgdfgdhg")
            res.setHeader("Access-Control-Allow-Headers", "*");
            res.setHeader('Content-Type', 'application/json');
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
            res.send({ "error": "Not authorised" }, 401);
        }


    });


};

module.exports = WebApi;
