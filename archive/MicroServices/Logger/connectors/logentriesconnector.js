/**
 * Created by trujun on 19/09/16.
 */

var logger = require('le_node');
var environmentalVariables = require('./../configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;


function logentriesConnection(requestObj, callback)
{
    console.log('STEP9: CHECKING logentries obj before initializing: ',requestObj.logentriesLogger);

    if(requestObj.logentriesLogger !== undefined){
        callback(null, requestObj.logentriesLogger);
    }
    else
    {
        var user_token;
        if(config !== undefined){

            if(config.logger !== undefined && config.logger.token !== undefined )
            {
                user_token = config.logger.token;
                console.log('STEP10: User token received');
            }
            else
            {
                console.log('STEP10: User token is undefined');
                callback({'error':'Not a valid token'});
            }
        }
        else{
            console.log('STEP10: configuration token is undefined');
            callback({'error':'Not a valid Configuration'});
        }

        if(user_token !== undefined)
        {
            log = new logger({
                token: user_token
            });
            console.log('STEP11: Logentries new OBJ create successfully!'+JSON.stringify(log));
            requestObj.logentriesLogger = log;
            callback(null, log);
        }
        else {
            console.log('STEP11: User token is undefined');
            var errObj = {'logentriesconnectionerror':'required connection details missing'};
            callback(errObj);
        }

    }
}
exports.logentriesConnection = logentriesConnection;