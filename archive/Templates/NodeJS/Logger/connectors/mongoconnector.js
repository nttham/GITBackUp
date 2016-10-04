
var mongoose = require('mongoose');
var internalConfig = require('./../configuration/config');
var environmentalVariables = require('./../configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;

function mongoConnection()
{
    this.mongoConnect=function(requestObj,callback)
    {
        if (requestObj.mongoConnect !== undefined) {
            console.log('STEP8: mongo connection exists so returning the same connection object');
            callback(requestObj.mongoConnect);
        }
        else {
            console.log('STEP8: mongo connection Initiation');
            var collectionName = config.logger.collectionname || 'applog';
            if(config.logger.hostname !== undefined && config.logger.portNo !== undefined)
            {
                console.log('As user provided host and port assume external server');
                var host = config.logger.hostname;
                var port =  config.logger.portNo;
                var dbname = config.logger.dbName;
                var username = config.logger.username;
                var password = config.logger.password;
                url = "mongodb://"+ host + ":" + port + "/" + dbname;
            }
            else{
                console.log('As host and port are undefined assume log to our server');
                var dbname = process.env.app_guid || 'sampledb';
                console.log('**** APPGUID from appfactory for dbname: ',dbname);
                url = internalConfig.mongoDB.server_url+"/"+dbname;
            }
            console.log('STEP9: mongo connection URL: ',url);
            var responseObj = '';

            mongoose.connect(url, {auth: {authdb: 'admin'}}, function (err, db) {
                if (!err)
                {
                    console.log('STEP10: mongo connected successfully DB');
                    var logItemScema = new mongoose.Schema({
                        priority: String,
                        level: String,
                        datetime: Date,
                        msg: String,
                        appid: String
                    });

                    requestObj.mongoConnect = mongoose.model(collectionName, logItemScema);
                    responseObj = mongoose.model(collectionName, logItemScema);
                }
                else
                {
                    console.log('STEP10: MONGO CONNECTION ERROR', err);
                    requestObj.mongoConnect = err;
                    responseObj = err;
                }
                callback(responseObj);
            });


        }
    }

    this.mongoDisconnect = function(){
        console.log('STEP19: In side connection disconnect....');
        delete mongoose.models.applog;
        mongoose.connection.close(function (err,res) {
            console.log("STEP20: Any error "+err)
            console.log('STEP21: Mongoose disconnected successfully');
        });


    }
}

module.exports = mongoConnection;


