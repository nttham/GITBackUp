/**
 * Created by Suryakala on 24/05/16.
 */

var environmentalVariables = require('./configuration/userdefined_env_parser');
var httpreq = require('request');
var graylogObj = require('./connectors/graylogconnector');
var mongologObj = require('./connectors/mongoconnector');
var Enum = require('enum');
var logEntriesObj = require('./connectors/logentriesconnector');
var logPriorities = new Enum(['CRITICAL', 'HIGH', 'MEDIUM', 'LOW']);
var logLevels = new Enum(['INFO','DEBUG','WARNING','ERROR','EMERGENCY','ALERT','CRITICAL','NOTICE']);

var config = environmentalVariables.userDefinedENV;
console.log('CONFIG ENV: '+JSON.stringify(config));
var logProvider = config.logger.name;
var self;
//constructor
var logger = function ()
{

};

// This method will initiate the selected log provider. This method has been invoked inside the api call.
var createLogger = function(requestObj , options, callback)
{
    if(options.logStore === 'graylog')
    {
        logToGraylog(requestObj, callback);
    }
    else if(options.logStore === 'mongodb')
    {
        logToMongoDB(requestObj, callback);
    }
    else if(options.logStore === 'splunk')
    {
        logToSplunk(requestObj, callback)
    }
    else if(options.logStore === 'logentries')
    {
        logToLogentries(requestObj, callback)
    }
}


function formMongoRequest(mongolog, request, extra, callback){
    var message = request.body.message;
    var priority = request.body.priority;
    var extra = extra.appid;
    var type = request.body.level;
    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
    newAppLog.save(function(err)
    {
        if(err) {
            callback(err, null);
        }
        else{
            new mongologObj().mongoDisconnect();
            callback(null, "Log saved as Info to mongodb");

        }
    });
}

function formSplunkRequest(request, extra, callback){
    var username = config.logger.username;
    var password = config.logger.password;//"conedigi";
    var host = config.logger.hostname;//"54.169.162.137";
    var port = config.logger.portNo;

    console.log('username: '+username);
    console.log('password: '+password);
    console.log('host: '+host);
    console.log('port: '+port);

    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

    var message = request.body.message;
    var extra = extra.appid+'_'+Date.now();
    var type = request.body.level;

    var data = {name:extra,
        value: message,
        severity:type };
    var options = {
        'auth' : {
            'username':username,
            'password':password
        },
        url: "https://"+host+":"+port+"/services/messages",
        form:    data
    }
    console.log('OPTIONS: '+JSON.stringify(options));
    httpreq.post(options, function(err,res,body) {
        if(err) {
            console.log('STEP18: Error raised in connection: ' + err);
            callback(err, null);
        }
        else{
            console.log("STEP18: Response Body: "+body);
            callback(null, body);
        }
    });

}

function formLogentriesRequest(logEntriesObj, request, extra, callback){
    var message = request.body;
    try
    {
        var level = request.body.level;

        switch(level.toUpperCase()){
            case 'CRITICAL':
                level = 'crit';
                break;
            case 'EMERGENCY':
                level = 'emerg';
                break;
            case 'ERROR':
                level = 'err';
                break;
        }

        //if(level === 'critical'){level = 'crit'}
        //else  if(level === 'emergency'){level = 'emerg'}
        //else if(level === 'error'){level = 'err'}

        logEntriesObj.log(level, message);
        callback(null, "Message Sent to server successfully");
    }
    catch (err) {
        callback(err);
    }
}

function formGrayLogRequest(graylog, req, extra, callback){

    var message = req.body.message;
    var level = req.body.level.toUpperCase();
    var output = 'Log saved to graylog';
    switch (level)
    {
        case 'INFO':
            graylog.info(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'DEBUG':
            graylog.debug(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'WARNING':
            graylog.warning(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'ERROR':
            graylog.error(req, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'EMERGENCY':
            graylog.emergency(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'ALERT':
            graylog.alert(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'CRITICAL':
            graylog.critical(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
        case 'NOTICE':
            graylog.notice(message, extra, function(err, saveoutput){
                if(err) {
                    output = {'status': 'failed', 'message': err};
                    callback(output,null);
                }
                else{
                    callback(null,output);
                }
            });
            break;
    }
}

function logToMongoDB(requestObj, callback) {

    var priority;
    var type;

    new mongologObj().mongoConnect(requestObj,function(mongolog){

        if(mongolog !== undefined && mongolog.name === 'MongoError'){
            console.log('STEP12: MONGO CONNECTION ERROR');
            callback(mongolog, null);
        }
        else{
            self = {
                save:function (request, extra, saveCallback)
                {
                    formMongoRequest(mongolog, request, extra, function(err, result){
                        if(err)
                        {
                            saveCallback(err);
                        }
                        else{
                            saveCallback(null, result);
                        }
                    });
                }
            };
            console.log('STEP12: Initiating self Obj successfully.. callback mongolog obj');
            callback(null, mongolog);
        }


    });
}

function logToGraylog(requestObj, callback){

    graylogObj.graylogConnection(requestObj, function(err, graylog){

        if(err)
        {
            console.log('STEP12: Error in graylog connection');
            callback(err, null);
        }
        else
        {
            console.log('STEP12: connected to graylog server...');
            self = {
                save: function (request, extra, saveCallback) {
                    formGrayLogRequest(graylog, request, extra, function(err, result){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            saveCallback(null, result);
                        }
                    })
                }
        }
            callback(null, graylog);
        }

    });

}

function logToSplunk(requestObj, callback){

    self = {
        save: function (request, extra, saveCallback)
        {
            formSplunkRequest(request, extra, function (err, response) {
                if (err) {
                    console.log('error: ' + err);
                    saveCallback(err, null);
                }
                else {
                    console.log("STEP18: Response Body: " + body);
                    saveCallback(null, body);
                }
            });
        }
    };

    console.log('STEP12: Initiating self Obj successfully.. callback splunk obj');
    callback(null, 'splunk api');
}

function logToLogentries(requestObj, callback){

    logEntriesObj.logentriesConnection(requestObj, function(err, logEntriesObj) {
        if (err) {
            console.log('STEP12: Error in logentries connection');
            callback(err, null);
        }
        else {
            self = {
                save: function (request, extra, saveCallback) {
                    formLogentriesRequest(logEntriesObj, request, extra, function(err, result){
                        if(err)
                        {
                            saveCallback(err);
                        }
                        else{
                            saveCallback(null, result);
                        }
                    });
                }
            }
            console.log('STEP12: Initiating self Obj successfully.. callback logentries obj');
            callback(null, logEntriesObj);
        }
    });
}

module.exports = self;

logger.prototype.postLog = function (app) {


    app.post("/save", function (req, res) {
        console.log('\n\n\nSTEP1: /save route hitted...');
        validateRequestParams(req, res, 'save', function (err, result) {
            if (err) {
                res.send(err);
                console.log('STEP3: Error occurred while request param validation-- Execution STOPPED -- '+JSON.stringify(err));
                return;
            }
            else {
                    console.log('STEP4: Proceed to create logger for provider: '+logProvider);
                    var options = {"logStore": logProvider};
                    var requestObject = {request: req, response: res};

                    createLogger(requestObject, options, function (err, result)
                    {
                        if (!err) {
                            console.log('STEP13: NO ERROR in logger creation');
                            saveMessage(req, res, function (err, output) {
                                if (!err) {
                                    console.log('STEP15: messages saved successfully!');
                                    output = {"status": "Success", "message": "Message saved successfully"};
                                    res.send(output);
                                    return;
                                    }
                                    else {
                                        console.log('STEP15: Error in saving message'+err);
                                        res.send(err);
                                        return;
                                    }
                                });
                            }
                            else {
                                console.log('STEP13: *** ERROR RAISED in logger creation ***');
                                res.send({'status': 'failed', 'message': err});
                                return;
                            }
                    });
               }
        });

    });


    app.get("/searchLog", function (req, res) {
        console.log('\n\n\nSTEP1: /searchLog route hitted...');
        validateRequestParams(req, res, 'searchLog', function (err, result) {
            if (err) {
                res.send(err);
                console.log('STEP3: Error occurred while request param validation-- Execution STOPPED -- '+JSON.stringify(err));
                return;
            }
            else {
                     console.log('STEP8: Proceed to create logger for provider: '+logProvider);
                        if(logProvider !== 'mongodb'){
                            res.send({'status':'failed','message':'searchLog applicable to Mongodb Logs'});
                            console.log('searchLog applicable to Mongo Logs -- Execution STOPPED');
                            return;
                        }


                        var options = {logStore: logProvider};
                        var requestObject = {request: req, response: res};
                        createLogger(requestObject, options, function (err, result) {
                            if (!err) {
                                console.log('STEP13: NO ERROR in logger creation');
                                searchRFilterLogs(result, req, function (err, output) {
                                    if (!err) {
                                        console.log('STEP15: search executed successfully!');
                                        res.send(output);
                                        return;
                                    }
                                    else {
                                        console.log('STEP15: Error in saving message');
                                        res.send(err);
                                        return;
                                    }
                                });
                            }
                            else {
                                console.log('STEP13: *** ERROR RAISED in logger creation ***');
                                res.send({'status': 'failed', 'message': err});
                                return;
                            }
                        });


                    }
        });




    });
}

var saveMessage = function (req,res, callback)
{
    var extra = {appid: req.body.appid};
    console.log('STEP14 : In saveMessage '+ JSON.stringify(extra));
     self.save(req, extra, function(err, result){
         callback(err, result);
     })
}


var searchRFilterLogs = function(db, request, callback)
{
    console.log('STEP14: In searchRFilterLogs');

    var queryObj = {};
    var level = request.headers.level;
    var appid = request.headers.appid;
    var priority = request.headers.priority;
    console.log('appid, level, priority: '+level+':'+appid+':'+priority);

    if(priority !== undefined){
        queryObj.priority = { $regex : new RegExp(priority, "i") };
    }
    if(level !== undefined) {
        queryObj.level = { $regex : new RegExp(level, "i") };
    }
    if(appid !== undefined){
        queryObj.appid = { $regex : new RegExp(appid, "i") };
    }

    console.log("queryObj ********** "+JSON.stringify(queryObj));


    db.find(queryObj, '-_id appid msg level priority datetime')
        .sort({'datetime':'-1'}).exec(
        function (err, docs) {
            new mongologObj().mongoDisconnect();
            if(!err)
            {
                if (JSON.stringify(docs) === '[]') { //This will check if the object is empty
                    var err = {'status': 'success', 'message': 'No records found.'}
                    callback(err);

                }
                else {
                    callback(null, docs);
                }
            }
            else
            {
                callback(err);
            }

        });



};

function validateRequestParams(req, res, routename, callback)
{
    var output;
    console.log('STEP2: validation of request params started ==>>>>>');
    console.log('PROVIDER: '+logProvider);
    if (routename === 'save' &&(!req.body.level || !req.body.message || !req.body.appid || !req.body.priority)) {
        var message = ' parameter has been missed';
        var paramName;
        if(req.body.level === undefined){
            paramName = '\'level\'';
        }
        else if(req.body.message === undefined && routename === 'save'){
            paramName = '\'message\'';
        }
        else if(req.body.appid === undefined){
            paramName = '\'appid\'';
        }
        else if(req.body.priority === undefined){
            paramName = '\'priority\'';
        }
        output = {"status": "Bad Request", "message": paramName+message};
        callback(output);
        return;
    }

    if (logProvider === 'splunk'){
        if(req.method === 'GET'){
            callback(null);
            return;
        }
        else
        {
            if(req.body.level.toUpperCase() === 'INFO' || req.body.level.toUpperCase() === 'ERROR' || req.body.level.toUpperCase() === 'WARNING'){
                //do nothing
                callback(null);
                return;
            }
            else
            {
                output = {"status": "failed", "message": 'log level should be either \'info/warning/error\''};
                callback(output);
                return;
            }
        }
    }
    else if (logProvider === 'mongodb' || logProvider === 'graylog' || logProvider === 'logentries') {
       // console.log('LOG PROVIDER: ' + logProvider);
    }
    else {
        output = {"status": "failed", "message": 'Invalid log provider'};
        callback(output);
        return;
    }


    if (req.body.priority !== undefined || req.headers.priority !== undefined) {
        var isInPriorityList = 0, priority;

        if (routename === 'save') {
            priority = req.body.priority.toUpperCase();
        }
        else if (routename === 'searchLog') {
            priority = req.headers.priority.toUpperCase();
        }
        // iterating over an enum of log priority.
        logPriorities.enums.forEach(function (enumItem) {
            if (enumItem.key === priority) {
                isInPriorityList = 1;
            }
        });

        if (!isInPriorityList) {
            output = {'status': 'failed', message: 'priority should be either \'Critical/High/Medium/Low\''};
            new mongologObj().mongoDisconnect();
            callback(output);
            return;
        }
    }

    if (req.body.level !== undefined || req.headers.level !== undefined) {
        var isInLevelList = 0, level;

        if (routename === 'save') {
            level = req.body.level.toUpperCase();
        }
        else if (routename === 'searchLog') {
            level = req.headers.level.toUpperCase();
        }

        logLevels.enums.forEach(function (enumItem) {
            if (enumItem.key === level) {
                isInLevelList = 1;
            }
        });

        if (!isInLevelList) {

            if (logProvider !== 'splunk') {
                output = {
                    'status': 'failed',
                    message: 'log level should be either \'info/debug/warning/error/emergency/alert/critical/notice\''
                };
            }
            else {
                output = {'status': 'failed', message: 'log level should be either \'info/warning/error\''};
            }
            new mongologObj().mongoDisconnect();
            callback(output);
            return;
        }
        else {
            callback(null);
        }
    }
    else {
        callback(null);
    }





}
module.exports = logger;

