/**
 * Created by trujun on 12/09/16.
 */

'use strict';

var config = require('./config');
const crypto = require('crypto');
console.log('HASH GENERATOR...');
function computeHash(algorithm, secret, contentToHash){
    return crypto
        .createHmac(algorithm, secret)
        .update(contentToHash)
        .digest('hex')
}

module.exports = function() {
    return function(req,res,next) {

        var dateKey = req.headers.datekey;
        var receivedHash = req.headers.hashcode;
        var nonce = req.headers.nonce;

        console.log('-----> request.headers.date: '+ dateKey);
        console.log('-----> request.headers.hashcode: '+ receivedHash);
        console.log('-----> request.headers.nonce: '+nonce);


        var apiKey = process.env.apiKey || 'testapikey';  // OR condition has to be removed while deploying
        var algorithm = config.Hash.algorithm;
        var httpInput;

        var contype = req.headers['content-type'];
        if (!contype || contype.indexOf('application/json') !== 0){
            res.status(400).json(
                {
                    status:'Invalid JSON',
                    message:'** content-type should be application/json'
                }
            )
        }
        else {
            if (req.body && req.method !== 'GET') {
                httpInput = JSON.stringify(req.body);
            }
            else {
                httpInput = JSON.stringify(req.url);
            }
        }

        if(dateKey && nonce && apiKey && httpInput !== undefined) {
            var contentToHash = apiKey + dateKey + nonce + httpInput;
            console.log('content: '+contentToHash);
            if (receivedHash === undefined || contentToHash === undefined) {
                res.status(403);
                res.send({'error': 'Access forbidden'});
            }

            var computedHash = computeHash(algorithm, apiKey, contentToHash);
            console.log('computedHash: ' + computedHash);

            if (receivedHash === computedHash) {
                next();
            }
            else {
                res.status(403);
                res.send({'error': 'Access forbidden'});
            }
        }
        else{
            res.status(400);
            res.send({'error': 'Bad Request'});
        }
    }
};

