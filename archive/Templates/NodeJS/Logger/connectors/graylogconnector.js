/**
 * Created by trujun on 26/05/16.
 */
var log = require('gelf-pro');
var environmentalVariables = require('./../configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;
console.log('ENV CONFIG: ',config);

function graylogConnection(requestObj, callback)
{
    console.log('STEP5: IN graylogConnector: ',requestObj.graylogger);

    if(requestObj.graylogger !== undefined){
        callback(requestObj.graylogger, null);
    }
    else
    {
        console.log('STEP6: initiate graylogger');
        if(config.logger.hostname !== undefined && config.logger.portNo !== undefined && config.logger.protocol !== undefined)
        {
            log.setConfig({
                fields: {facility: "logger-service", owner: "CognizantOne"},
                adapterName: "udp", // currently supported "udp" only
                adapterOptions: {
                    protocol: config.logger.protocol, // udp adapter: udp4, udp6
                    host: config.logger.host,
                    port: config.logger.port,
                }
            });
            requestObj.graylogger = log;
            callback(requestObj.graylogger, null);
        }
        else {
            var errObj = {'graylogconnectionerror':'required connection details missing'};
            callback(null, errObj);
        }

    }
}
exports.graylogConnection = graylogConnection;