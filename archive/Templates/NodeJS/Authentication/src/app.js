// dependencies
var fs = require('fs');
var express = require('express');
var path = require('path');
//var config = require('./config.json');
var hookConfig =  {
    "prehooktemplate": "index",
    "posthooktemplate": "account"
}

var app = express();
var request = require('request');

//Config to enable the providers. If false it wont be shown in UI for login
var config = {
    "facebook": true,
    "google": true,
    "linkedin": true,
    "twitter": true,
    "ldap": true,
    "custom": true
}

// configure Express
app.configure(function() {
    app.set('views', __dirname + './../views');
    app.set('view engine', 'jade');
    app.use(express.logger());
    app.use(express.cookieParser());
    app.use(express.bodyParser());
    app.use(express.methodOverride());
    app.use(app.router);
    app.use(express.static(__dirname + './../public'));

});

//Reading VCAP_APPLICATION information for current application URL
var services_env = JSON.parse(process.env.VCAP_APPLICATION || "{}");
var callbackUrl;
if(services_env.uris) {
    callbackUrl = "http://"+services_env.uris[0]+"/callback";
}

// routes
app.get('/', function(req, res){
    res.redirect('/login');
});

// GET /login
//  Login page to show the provider options to login
app.get('/login', function(req, res){
    res.render(hookConfig.prehooktemplate, { title: "Authentication", config: config});
});

// GET /OAuth
//  This API is called from index.jade with the query parameter as provider.
//  Later it will call the authentication service with query parameter as callbackUrl
app.get('/authenticate', function(req, res){
    try {
        //Reading VCAP information for OauthService base URL
        var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
        var serviceUrl = services_vcap.Authentication[0].credentials.serviceUrl;

        //Prepare the first authentication initiation request URL by checking provider
        var provider = req.query.provider;
        var firstAuthRequestURL = serviceUrl + "/auth/" + provider;

        //Reading apikey from VCAP
        var apiKey = services_vcap.Authentication[0].credentials.apiKey;

        //Headers for POST call
        var headers = {
            apiKey : apiKey
        }

        //This API will initiate the authentication to providers. Checks if any prehooks are available for provider.
        //Here we are handling only for without prehooks. If response says any prehooks available then throw the message
        //  So only during statusCode 302 or 303 when nextCall is /ldap or /provider/:token process the next request else throw the appropriate message
        request({
            url: firstAuthRequestURL, //URL to hit
            method: 'POST',
            headers: headers
        }, function(error, response, body){
            if(error) {
                console.log(error);
                res.send(400, JSON.stringify(error));

            } else if(response.statusCode === 302 || response.statusCode === 303){
                var responseBody = JSON.parse(body);
                var checkNextCall = "/generateOtp";
                console.log("Body after call "+ body);

                if(responseBody.nextCall === checkNextCall) {
                    res.redirect("/hooksmessage?hookType=prehook");
                    //res.send(response.statusCode, "There are some prehooks available for provider. This sample template application cannot proceed further");
                }
                else if(responseBody.nextCall === "/ldap") {
                    res.render('ldap_authenticate', { title: "LDAP Authentication", config: {token:responseBody.token}});
                }
                else {
                    //Calling /facebook/:token|/twitter/:token|/google/:token|/linkedin/:token service of authentication service
                    var baseUrl = serviceUrl+responseBody.nextCall+"?callbackUrl="+callbackUrl;
                    res.redirect(baseUrl);
                }
            }
            else if(response.statusCode === 401 || response.statusCode === 400 ) {
                res.send(response.statusCode, body);
            }
            else {
                res.send(404,body);
            }
        });
    }
    catch (ex) {
        console.log("ERROR Exception :"+ex);
        res.send(400,"Something went wrong. Check VCAP information");
    }

});

// GET /callback
//  After successful authentication with provider authentication service will redirect
//  back to the callback which is passed during the /authenticate call with token as path parameter
app.get('/callback/:token', function(req, res){
    try {
        //Check if query params(code) are present and access the information. Else redirect to login page
        if(req.params.token){

            //Prepare the next request URL with base serviceUrl i;e /auth/complete
            var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
            var serviceUrl = services_vcap.Authentication[0].credentials.serviceUrl+"/auth/complete";

            //Read apiKey from VCAP
            var apiKey = services_vcap.Authentication[0].credentials.apiKey;

            //Headers required for POST call /auth/complete
            var headers = {
                token : req.params.token,
                apiKey : apiKey
            }

            //After receiving token. Request for profile data with /auth/complete API
            //Here we are handling only for without posthooks. If response says any posthooks available then throw the message
            //  So only during statusCode 200 and when there is no nextCall available profile will be retrieved
            request({
                url: serviceUrl, //URL to hit
                method: 'POST',
                headers: headers
            }, function(error, response, body){
                if(error) {
                    console.log(error);
                    res.send(response.statusCode, JSON.stringify(error));

                } else if(response.statusCode === 302 || response.statusCode === 303 || response.statusCode === 200){
                    var responseBody = JSON.parse(body);
                    var checkNextCall = "/generateOtp";
                    console.log("Body after call "+ body);

                    if(responseBody.nextCall === checkNextCall) {
                        res.redirect("/hooksmessage?hookType=posthook");
                        //res.send(response.statusCode, "There are some Posthooks available for provider. This sample template application cannot proceed further");
                    }
                    else {
                        res.render(hookConfig.posthooktemplate, { user: JSON.parse(body) });
                    }
                }
                else if(response.statusCode === 401 || response.statusCode === 400 ) {
                    res.send(response.statusCode, body);
                }
                else {
                    res.send(404,body);
                }
            });
        }
        else {
            res.redirect('/');
        }
    }
    catch (ex) {
        console.log("ERROR Exception :"+ex);
        res.send(400,"Something went wrong. Check VCAP information");
    }

});

// GET /vcapinfo
//  Get VCAP information like apiKey and serviceUrl. This will be useful for getting VCAP info from UI ajax call
app.get('/vcapinfo', function(req, res){
    try {
        //Reading VCAP information for Authentication service base URL
        var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
        var serviceUrl = services_vcap.Authentication[0].credentials.serviceUrl

        //Reading apikey from VCAP
        var apiKey = services_vcap.Authentication[0].credentials.apiKey;
        res.send(200,{apikey : apiKey,serviceUrl : serviceUrl});
    }
    catch (ex) {
        console.log("ERROR Exception :"+ex);
        res.send(400,"VCAP information is not set for Authentication service");
    }
});

// POST /ladp/authenticate
//  This API will initiate and authenticate via LDAP configured server
app.post('/ladp/authenticate',function(req, res){
    try {
        //Reading VCAP information for Authentication service base URL
        var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
        var serviceUrl = services_vcap.Authentication[0].credentials.serviceUrl;

        //First API to initiate ldap /auth/ldap
        var firstAuthRequestURL = serviceUrl + "/auth/ldap";

        //Reading apikey from VCAP
        var apiKey = services_vcap.Authentication[0].credentials.apiKey;

        //Headers for POST call
        var headers = {
            apiKey : apiKey
        }

        //This API will initiate the authentication to providers. Checks if any prehooks are available for provider.
        //Here we are handling only for without prehooks. If response says any prehooks available then throw the message
        request({
            url: firstAuthRequestURL, //URL to hit
            method: 'POST',
            headers: headers
        }, function(error, response, body){
            if(error) {
                console.log(error);
                res.send(400, JSON.stringify(error));

            } else if(response.statusCode === 302 || response.statusCode === 303){
                var responseBody = JSON.parse(body);
                var checkNextCall = "/generateOtp";
                console.log("Body after call "+ body);

                if(responseBody.nextCall === checkNextCall) {
                    res.send(response.statusCode, {message:"There are some prehooks available for provider. This sample template application cannot proceed further"});
                }
                //If nextCall is /ldap then process the second request /ldap with the token received in body of previous /auth/ldap call
                else if(responseBody.nextCall === "/ldap") {
                    var secondAuthRequestURL = serviceUrl + responseBody.nextCall;
                    headers.token = responseBody.token;
                    headers.dn = req.body.dn;
                    headers.password = req.body.password;
                    request({
                            url: secondAuthRequestURL, //URL to hit
                            method: 'POST',
                            headers: headers
                        }, function(error, response, body){
                            if(error) {
                                console.log(error);
                                res.send(400, error);
                            }
                            else if(response.statusCode === 302 || response.statusCode === 303){
                                res.send(response.statusCode, {message:"There are some posthooks available for provider. This sample template application cannot proceed further"});
                            }
                            else if(response.statusCode === 200 ){
                                res.send(response.statusCode, JSON.parse(body));
                            }
                            else if(response.statusCode === 401 || response.statusCode === 400 ) {
                                res.send(response.statusCode, JSON.parse(body));
                            }
                        }
                    )
                }

            }
            else if(response.statusCode === 401 || response.statusCode === 400 ) {
                res.send(response.statusCode, body);
            }
            else {
                res.send(404,body);
            }
        });
    }
    catch (ex) {
        console.log("ERROR Exception :"+ex);
        res.send(400,"Something went wrong. Check VCAP information");
    }
})

// get /hookmessage
//  This is used for rendering to show message from UI when application cannot proceed further
app.get("/hooksmessage",  function(req, res){
    if(req.query.hookType) {
        if(req.query.hookType === "posthook") {
            res.render('hooks_message', {message:"There are some posthooks available for provider. This sample template application cannot proceed further"});
        }
        else if(req.query.hookType === "prehook") {
            res.render('hooks_message', {message:"There are some prehooks available for provider. This sample template application cannot proceed further"});
        }
        else {
            res.redirect("/login");
        }
    }
    else {
        res.redirect("/login");
    }
})

// port
var port = process.env.PORT || 3001;
app.listen(port);

module.exports = app;
