/**
 * Created by Srividya on 23/06/16.
 */
var request = require('request');
var async = require('async');

var getData = function (callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/1ae11efd8719c2bce1400f2ba62a74c3',
        method: 'GET',
        headers: {
            'Authorization': 'Basic c3JpdmlkeWEudXBwdWdhbmRsYUBjb2duaXphbnQuY29tOlNyMXYxZHlh',
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


var editData = function (obj, callback) {
    var editGetCallback = function (error,result) {
        if (error) {
            callback(error);
        }
        else {
            var filesData = JSON.parse(result.body).files['OAuth.json'].content;
            var contentArray = JSON.parse(filesData);
            contentArray.push(obj);

            var body = {
                description: "the description for this gist",
                files: {
                    "OAuth.json": {
                        content: JSON.stringify(contentArray)
                    }
                }
            };

            //forming http request string
            var postOptions = {
                url: 'https://api.github.com/gists/1ae11efd8719c2bce1400f2ba62a74c3',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Basic c3JpdmlkeWEudXBwdWdhbmRsYUBjb2duaXphbnQuY29tOlNyMXYxZHlh',
                    'user-agent': 'node.js'
                },
                body: JSON.stringify(body)
            };

            request(postOptions, function (err, response) {
                if (err) {
                    return callback(err);
                }
                else {
                    return callback(null, JSON.stringify(response));
                }
            });
        }
    };

    async.waterfall([getData], editGetCallback);
};


var deleteData = function (obj, callback) {

    var deleteGetCallback = function (error,result) {
        if (error) {
            callback(error);
        }
        else {
            var filesData = JSON.parse(result.body).files['OAuth.json'].content;
            var contentArray = JSON.parse(filesData);
            var index = contentArray.findIndex(x => x.instance_id==obj.instance_id);
            contentArray.splice(index,1);

            var body = {
                description: "the description for this gist",
                files: {
                    "OAuth.json": {
                        content: JSON.stringify(contentArray)
                    }
                }
            };

            //forming http request string
            var postOptions = {
                url: 'https://api.github.com/gists/1ae11efd8719c2bce1400f2ba62a74c3',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Basic c3JpdmlkeWEudXBwdWdhbmRsYUBjb2duaXphbnQuY29tOlNyMXYxZHlh',
                    'user-agent': 'node.js'
                },
                body: JSON.stringify(body)
            };

            request(postOptions, function (err, response) {
                if (err) {
                    return callback(err);
                }
                else {
                    return callback(null, JSON.stringify(response));
                }
            });
        }
    };
    async.waterfall([getData], deleteGetCallback);
};

exports.getData = getData;
exports.editData = editData;
exports.deleteData = deleteData;
