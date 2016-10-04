/**
 * Created by Srividya on 23/06/16.
 */
var request = require('request');
var async = require('async');
var config = require('./config.json');
var GISTDB = function () {
};

//temp function to get the entire GIST data
var tempGetData = function (callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/'+config["gistID"],
        method: 'GET',
        headers: {
            'Authorization': 'Basic '+config["gistauthorization"],
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

//to get the entire GIST data
GISTDB.prototype.getData = function (callback) {

    var finalCallback = function (error, result) {
        if (error) {
            return callback(error);
        }
        else {
            return callback(null, result);
        }
    };
    tempGetData(finalCallback);
};

//to update GIST data obj. "Obj" is the object to update.
GISTDB.prototype.editData = function (obj, fileName, callback) {
    var editGetCallback = function (error,result) {
        if (error) {
            callback(error);
        }
        else {
            var filesData = JSON.parse(result.body).files[fileName].content;
            var contentArray = JSON.parse(filesData);
            contentArray.push(obj);

            var body = {
                description: "the description for this gist",
                files: {
                    [fileName] : {
                        content: JSON.stringify(contentArray)
                    }
                }
            };

            //forming http request string
            var postOptions = {
                url: 'https://api.github.com/gists/'+config["gistID"],
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Basic '+config["gistauthorization"],
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

    async.waterfall([tempGetData], editGetCallback);
};

//to delete GIST data obj. "Obj" is the object to delete.
GISTDB.prototype.deleteData = function (obj, fileName, callback) {

    var deleteGetCallback = function (error,result) {
        if (error) {
            callback(error);
        }
        else {
            var filesData = JSON.parse(result.body).files[fileName].content;
            var contentArray = JSON.parse(filesData);
            var index = contentArray.findIndex(x => x.instance_id==obj.instance_id);
            contentArray.splice(index,1);

            var body = {
                description: "the description for this gist",
                files: {
                    [fileName]: {
                        content: JSON.stringify(contentArray)
                    }
                }
            };

            //forming http request string
            var postOptions = {
                url: 'https://api.github.com/gists/'+config["gistID"],
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Basic '+config["gistauthorization"],
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
    async.waterfall([tempGetData], deleteGetCallback);
};

module.exports = GISTDB;
