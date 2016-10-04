/**
 * Created by Srividya on 23/06/16.
 */
var request = require('request');
var config = require('./configuration/config');
var async = require('async');

var getData = function (callback) {
    //forming http request string
    var getOptions = {
        url: config.Gist.url,
        method: config.Gist.method,
        headers: {
            'Authorization': config.Gist.Authorization,
            'user-agent': config.Gist.useragent,
            Accept: config.Gist.Accept
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            return callback(null, response);
        }
    });
};

exports.getData = getData;
