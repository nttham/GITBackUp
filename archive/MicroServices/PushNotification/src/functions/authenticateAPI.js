var instanceDalObj = require('./instances.js');

//verify if all required credentials available for API call
exports.authenticateAPI = function (request,callback) {

    //callback
    var apiKeyValidationCallback = function(err,result){
        if(err){
            //return response.status(400).send(err);
            return callback(err);
        }
        else {
            console.log("**** apiKeyValidation response ****" + JSON.stringify(result));
            return callback(null, result);
        }
    };

    var apikey;

    //check if headers have apikey.
    if (request.headers["apikey"]) {
        apikey = request.headers["apikey"];
    }


    //if apikey key has some value then check in database for the same else throw error
    if(apikey) {
        var queryObj = {
            apiKey :apikey
        };

        instanceDalObj.getInstance(queryObj,apiKeyValidationCallback);
    }
    else {
        console.log("authenticateAPI => Not Authorised. apiKey missing in headers");
        return callback({error:"Not Authorised. apiKey missing in headers"});
    }
};