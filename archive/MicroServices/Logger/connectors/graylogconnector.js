/**
 * Created by trujun on 26/05/16.
 */
var log = require('gelf-pro');
var environmentalVariables = require('./../configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;

function graylogConnection(requestObj, callback)
{
    console.log('STEP9: CHECKING graylog obj before initializing: ',requestObj.graylogger);
    if(requestObj.graylogger !== undefined){
        callback(null, requestObj.graylogger);
    }
    else
    {
        if(config.logger.hostname !== undefined && config.logger.portNo !== undefined && config.logger.protocol !== undefined)
        {
            log.setConfig({
                fields: {facility: "logger-service", owner: "CognizantOne"},
                adapterName: "udp", // currently supported "udp" only
                adapterOptions: {
                    protocol: config.logger.protocol||'udp4', // udp adapter: udp4, udp6
                    host: config.logger.hostname,
                    port: config.logger.portNo,
                }
            });
            console.log('STEP11: graylog new OBJ create successfully!');
            requestObj.graylogger = log;
            callback(null,requestObj.graylogger);
        }
        else {
            console.log('STEP11: error in connection');
            var errObj = {'graylogconnectionerror':'required connection details missing'};
            callback(errObj);
        }

    }
}
exports.graylogConnection = graylogConnection;