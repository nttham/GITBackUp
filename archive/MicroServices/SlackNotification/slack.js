/**
 * Created by Suryakala on 24/05/16.
 * This class is a wrapper for slack apis(authentication, createchannel, archiveChannel, unarchiveChannel, channelInvite,
 * channelJoin, channelLeave, channelsList, chatPostMessage, searchMessage).
 */

//var express = require('express');
//var app = express();
var httpreq = require('request');
var connection = require('./httpConnections');
var config = require('./config.json');


//constructor
var slack = function () {

};


var bodyParser = require('body-parser')

slack.prototype.postNotification = function (app)
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

    app.get("/apitest", function (req, res) {
        console.log('STEP1: /apitest route hitted');
        connection.getRequest(config.baseurl+config.testUrl, function(error, response, body){
            if(error != undefined){
                res.end(error);
            }
            else
            {
              res.end(body);
            }
        });


    });

    app.post("/authtest", function (req, res) {
        console.log('authtest route hitted...');
        var url = config.baseurl+config.testAuth;
        var token = req.body.token ;
        var body = {"token":token};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelcreate", function (req, res) {
        console.log('createChannel route hitted...');
        var url = config.baseurl+config.createChannel;
        var token = req.body.token ;
        var channelName = req.body.name ;
        var body = {"token":token,"name":channelName};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelarchive", function (req, res) {
        console.log('channelarchive route hitted...');
        var url = config.baseurl+config.archiveChannel;
        var token = req.body.token ;
        var channelId = req.body.channel ;
        var body = {"token":token,"channel":channelId};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelunarchive", function (req, res) {
        console.log('channelunarchive route hitted...');
        var url = config.baseurl+config.channelsUnarchive;
        var token = req.body.token ;
        var channelId = req.body.channel ;
        var body = {"token":token,"channel":channelId};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelinvite", function (req, res) {
        console.log('channelinvite route hitted...');
        var url = config.baseurl+config.channelsInvite;
        var token = req.body.token ;
        var channelId = req.body.channel ;
        var userId = req.body.user;
        var body = {"token":token,"channel":channelId,"user":userId};
        var headers = {'content-type':'application/json'};

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

    app.post("/channeljoin", function (req, res) {
        console.log('createJoin route hitted...');
        var url = config.baseurl+config.channelsJoin;
        var token = req.body.token ;
        var name = req.body.name ;
        var body = {"token":token,"name":name};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelsleave", function (req, res) {
        console.log('channelsleave route hitted...');
        var url = config.baseurl+config.channelsLeave;
        var token = req.body.token ;
        var channel = req.body.channel ;
        var body = {"token":token,"channel":channel};
        var headers = {'content-type':'application/json'};

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

    app.post("/channelslist", function (req, res) {
        console.log('channelslist route hitted...');
        var url = config.baseurl+config.channelsList;
        var token = req.body.token ;
        var exclude_archived = req.body.exclude_archived;
        var body = {"token":token,"exclude_archived":exclude_archived};
        var headers = {'content-type':'application/json'};

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

    app.post("/postMessage", function (req, res) {
        console.log('postMessage route hitted...');
        var url = config.baseurl+config.channelsList;
        var token = req.body.token ; //required
        var channel = req.body.channel; //required
        var text = req.body.text; //required
        var parse = req.body.parse; //optional
        var link_names = req.body.link_names; //optional
        var attachments = req.body.attachments;//optional
        var unfurl_links = req.body.unfurl_links;//optional
        var unfurl_media = req.body.unfurl_media;//optional
        var username = req.body.username;//optional
        var as_user = req.body.as_user;//optional
        var icon_url = req.body.icon_url;//optional
        var icon_emoji = req.body.icon_emoji;//optional

        var body = {"token":token,"channel":channel,"text":text,"parse":parse,"link_names":link_names,"attachments":attachments,
            "unfurl_links":unfurl_links,"unfurl_media":unfurl_media,"username":username,"as_user":as_user,
            "icon_url":icon_url,"icon_emoji":icon_emoji};
        var headers = {'content-type':'application/json'};

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
        console.log('postMessage route hitted...');
        var url = config.baseurl+config.searchMessages;
        var token = req.body.token ; //required
        var query = req.body.query; //required

        var sort = req.body.sort; //optional
        var sort_dir = req.body.sort_dir; //optional
        var highlight = req.body.highlight; //optional
        var count = req.body.count;//optional
        var page = req.body.page;

        var body = {"token":token,"query":query,"sort":sort,"sort_dir":sort_dir,"highlight":highlight,"count":count,
            "page":page};
        var headers = {'content-type':'application/json'};

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
}



module.exports = slack;

