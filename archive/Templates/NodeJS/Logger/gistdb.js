/**
 * Created by Srividya on 23/06/16.
 */
var request = require('request');

var async = require('async');

var getData = function (callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/a0a7db06eb449c231937504a565ce06b',
        method: 'GET',
        headers: {
            'Authorization': 'Basic U3VyeWFrYWxhLkJvdHRhQGNvZ25pemFudC5jb206U3VyeWExMjM=',
            'user-agent': 'node.js',
            Accept: 'application/json'
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
