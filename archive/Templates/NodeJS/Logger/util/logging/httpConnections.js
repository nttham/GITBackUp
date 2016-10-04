/**
 * Created by trujun on 26/07/16.
 */
var request = require('request');

exports.postRequest = function(url, headers, body, callback){

    request({
        url: url,
        method: "POST",
        //Lets post the following key/values as form
        form: body
    }, function (error, response, body){
        if(error) {
            console.log(error);
        } else {
            console.log(response.statusCode, body);
        }
        callback(error, response, body);
    });
}

exports.getRequest = function(url,callback){
    request({
        url: url,
        method: "GET",
    }, function (error, response, body){
        console.log(response);
        if(error != undefined){
            console.log('error raised');
            callback(error, null, null);
        }
        else{
            console.log('No error!');
            callback(null, response, body);
        }

    });
}

