/**
 * Created by Suryakala on 24/05/16.
 * This class is a wrapper for slack apis(authentication, createchannel, archiveChannel, unarchiveChannel, channelInvite,
 * channelJoin, channelLeave, channelsList, chatPostMessage, searchMessage).
 */

//var express = require('express');
//var app = express();
var httpreq = require('request');
var connection = require('./httpConnections');


var vcap_services = JSON.parse(process.env.VCAP_SERVICES || "{}");
var apikey = "";//vcap_services.LoggerServiceBrokerQA[0].credentials.apiKey;
var serviceurl = "";//vcap_services.LoggerServiceBrokerQA[0].credentials.baseurl || config.baseurl;



//constructor
var loggertemplate = function () {

};


var bodyParser = require('body-parser')

loggertemplate.prototype.hitService = function (app)
{
    app.use( bodyParser.json() );       // to support JSON-encoded bodies
    app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
        extended: true
    }));

    app.use(function(req, res, next) {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        next();
    });

    app.post("/save", function (req, res) {
        console.log('authtest route hitted...');
        var url = serviceurl+"/save";
        var body = {"level":"info","message":"Info message test surya from dev instance", "appid":"OneC","priority":"Critical"};
        var headers = {'content-type':'application/json',"apikey":apikey};

        console.log('req url: ', url);
        console.log('req body', body);
        console.log('req header',headers);

       connection.postRequest(url,headers,body, function(error, response, body){
           if(error != undefined){
               res.end(error);
           }
           else{
               res.end(body);
           }
       });
    });

    app.post("/searchMessage", function (req, res) {
        console.log('authtest route hitted...');
        var url = serviceurl+"/searchMessage";
        var body = {"level":"info", "appid":"OneC","priority":"Critical"};
        var headers = {'content-type':'application/json',"apikey":apikey};

        console.log('req url: ', url);
        console.log('req body', body);
        console.log('req header',headers);

        connection.postRequest(url,headers,body, function(error, response, body){
            if(error != undefined){
                res.end(error);
            }
            else{
                res.end(body);
            }
        });
    });
    
    /* GET home page. */
    app.get('/', function(req, res, next) {
    console.log('Inside GET path');
    res.end('Welcome to Logger App');
});

}



module.exports = loggertemplate;

