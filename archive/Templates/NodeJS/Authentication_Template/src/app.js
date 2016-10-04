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

var config = {
    "facebook": "YES",
    "google": "YES",
    "linkedin": "YES",
    "twitter": "YES"
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
var callbackUrl = "http://"+services_env.uris[0]+"/callback";


// routes
app.get('/', function(req, res){
    res.redirect('/login');
});

// GET /login
//  Login page to show the provider options to login
app.get('/login', function(req, res){
    res.render(hookConfig.prehooktemplate, { title: "OAuth Authentication", config: config});
});

// GET /OAuth
//  This API is called from index.jade with the query parameter as provider.
//  Later it will call the authentication service with query parameter as callbackUrl
app.get('/OAuth', function(req, res){
    try {
        //Reading VCAP information for OauthService base URL
        var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
        var serviceUrl = services_vcap.NewflowOauthService[0].credentials.serviceUrl;

        var provider = req.query.provider;

        var firstAuthRequestURL = serviceUrl + "/auth/" + provider;

        //Reading apikey from VCAP
        var apiKey = services_vcap.NewflowOauthService[0].credentials.apiKey;

        //Headers for POST call
        var headers = {
            apiKey : apiKey
        }
        console.log("first==>"+firstAuthRequestURL);
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
                    res.send(response.statusCode, "There are some prehooks available for provider. This sample template application cannot proceed further");
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
//  back to the callback which is passed during the /OAuth call with token as path parameter
app.get('/callback/:token', function(req, res){
    try {
        //Check if query params(code) are present and access the information. Else redirect to login page
        if(req.params.token){

            var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");
            var serviceUrl = services_vcap.NewflowOauthService[0].credentials.serviceUrl+"/auth/complete";
            var apiKey = services_vcap.NewflowOauthService[0].credentials.apiKey;

            //Headers required for POST call /auth/complete
            var headers = {
                token : req.params.token,
                apiKey : apiKey
            }

            //After receiving token. Request for profile data with /auth/complete API
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
                        res.send(response.statusCode, "There are some Posthooks available for provider. This sample template application cannot proceed further");
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


// port
var port = process.env.PORT || 3001;
app.listen(port);

module.exports = app;
