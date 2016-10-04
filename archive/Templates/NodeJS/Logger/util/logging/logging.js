/**
 * Created by trujun on 31/08/16.
 */
var httpreq = require('request');
var connection = require('./httpConnections');
var apikey, serviceurl;

if(process.env.loggerapikey != undefined){
    console.log('apikey found: '+process.env.loggerapikey);
    apikey = process.env.loggerapikey;
}
if(process.env.loggerbaseurl != undefined){
    console.log('loggerbaseurl found: '+process.env.loggerbaseurl);
    serviceurl = process.env.loggerbaseurl;
}




function log(level, message, appid, priority){
    console.log('authtest route hitted...');
    var url = serviceurl+"/savelog";
    var body = {"level":level,"message":message, "appid":appid,"priority":priority};
    var headers = {'content-type':'application/json',"apikey":apikey};

    console.log('req url: ', url);
    console.log('req body', body);
    console.log('req header',headers);

    connection.postRequest(url,headers,body, function(error, response, body){
        if(error != undefined){
            console.log('error in saving logs'+error);
        }
        else{
           // response.end(body);
            var timestamp = '[' + Date.now() + '] ';
            console.log('successfully saved at: '+timestamp);
        }
    });
}

module.exports.log = log;/**
 * Created by trujun on 01/09/16.
 */
