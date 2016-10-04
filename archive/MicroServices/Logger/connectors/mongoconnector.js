
var mongoose = require('mongoose');
var internalConfig = require('./../configuration/config');
var environmentalVariables = require('./../configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;
var Schema = mongoose.Schema;
var logItemScema = new Schema({
    priority: String,
    level: String,
    datetime: Date,
    msg: String,
    appid: String
});

function mongoConnection()
{
    this.mongoConnect=function(requestObj,callback)
    {
        if (requestObj.mongoConnect !== undefined) {
            console.log('STEP8: mongo connection exists so returning the same connection object');
            callback(requestObj.mongoConnect);
        }
        else {
            console.log('STEP9: mongo connection Initiation');
            var collectionName = config.logger.collectionname || 'applog';
            var dbname;
            if(config.logger.hostname !== undefined && config.logger.portNo !== undefined)
            {
                var host = config.logger.hostname;
                var port =  config.logger.portNo;
                dbname = config.logger.dbName;
                var username = config.logger.username;
                var password = config.logger.password;
                url = "mongodb://"+ host + ":" + port + "/" + dbname;
            }
            else{
                console.log('As host and port are undefined assume log to our server');
                dbname = process.env.app_guid || 'sampledb'; //or condition to be removed while deployment
                console.log('**** APPGUID from appfactory for dbname: ',dbname);
                url = internalConfig.mongoDB.server_url+"/"+dbname;
            }
            console.log('STEP10: mongo connection URL: ',url);
            var responseObj = '';

            mongoose.connect(url, {auth: {authdb: 'admin'}}, function (err, db) {
                if (!err)
                {
                    console.log('STEP11: mongo connected successfully DB');
                    requestObj.mongoConnect = mongoose.model('applogs', logItemScema);
                    responseObj = mongoose.model('applogs', logItemScema);
                }
                else
                {
                    console.log('STEP11: MONGO CONNECTION ERROR', err);
                    requestObj.mongoConnect = err;
                    responseObj = err;
                }
                callback(responseObj);
            });


        }
    }

    this.mongoDisconnect = function(){
        delete mongoose.models.applog;
        mongoose.connection.close(function (err,res) {
        if(err) {
            console.log("STEP16: Error while closing mongo connection " + err)
        }
        });


    }
}

module.exports = mongoConnection;


