var dbConfig = require("./dbconfig.json");
var mongoose = require("mongoose");
var Grid = require('gridfs-stream');
var db;
var gfs;
var services_vcap = JSON.parse(process.env.VCAP_SERVICES || "{}");

exports.connectToDB = function(dbName,callback) {

    //checks for the db type
    if(dbName === dbConfig.dbType) {
        //Reading VCAP information
        //Check if all credentials available for db connection from VCAPS
        if(services_vcap && services_vcap["Mongo-Service"] && services_vcap["Mongo-Service"][0] && services_vcap["Mongo-Service"][0]["credentials"]) {
            var credentials = services_vcap["Mongo-Service"][0]["credentials"];
            var connectionUrl = "mongodb://"+credentials.username+":"+credentials.password+"@"+credentials.IP+":"+credentials.port+"/"+dbConfig.dbName;
            db = mongoose.createConnection(connectionUrl,{auth:{authdb:credentials.DBName}});
            Grid.mongo = mongoose.mongo;
            db.once('open', function () {
                console.log("DB Connection opened Successfully !!!!!!")
                return callback(null,db);
            });

            db.on('error', function (err) {
                console.log('Error! Database connection failed : ',err);
                return callback(err,db);
            });
        }
        else {
            //DB credentials missing. So throwing error
            return callback({"error":"No VCAP services available for Mongo-Service"},null);
        }
    }
    else {
        //Not a valid DB type
        return callback({"error":"Not a valid DB type"},null);
    }
};

//getter method to get the DB instance of push coreDB
exports.getDB = function(){
    return db;
};

//Switch to the givenDB and return callback
exports.switchDB = function(dbName,callback){
    var switchDB = db.useDb(dbName);
    gfs = Grid(switchDB.db);
    var dbObj = {
        dbConnection :switchDB,
        gfs:gfs
    };
    return callback(null,dbObj);
};


//switch to the given DB and Drop the database
exports.deleteDB = function(dbName,callback){
    var delDB = db.useDb(dbName);
    delDB.db.dropDatabase(callback);
};