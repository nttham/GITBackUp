/**
 * Created by Suryakala on 24/05/16.
 */

var environmentalVariables = require('./configuration/userdefined_env_parser');
var config = environmentalVariables.userDefinedENV;
var self;
var logging = require('./util/logging/logging');

console.log('default logging config completed');
var logProvider = config.logger.name;
var httpreq = require('request');
var graylogObj = require('./connectors/graylogconnector');
var mongologObj = require('./connectors/mongoconnector');
var DB = require('./gistdb.js');
var Enum = require('enum');
var logPriorities = new Enum(['CRITICAL', 'HIGH', 'MEDIUM', 'LOW']);
var logLevels = new Enum(['INFO','DEBUG','WARNING','ERROR','EMERGENCY','ALERT','CRITICAL','NOTICE']);

//constructor
var logger = function ()
{

};

function getInstanceDetails(request_apikey,callback)
{
    console.log('apiKey: '+process.env.apiKey);
    if(process.env.apiKey != undefined && process.env.apiKey == request_apikey)
    {
        callback(null, process.env.apiKey);
    }
    else
    {
        callback({'error':'Access forbidden'});
    }
};


var bodyParser = require('body-parser')

// This method will initiate the selected log provider. This method has been invoked inside the api call.
var createLogger = function(requestObj , options, callback)
{
    console.log('STEP5: Inside createLogger()');
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
}


function logToMongoDB(requestObj, callback) {
    console.log('STEP6: Inside logToMongoDB()');
    var priority;
    var type;

    new mongologObj().mongoConnect(requestObj,function(mongolog){

        if(mongolog != undefined && mongolog.name == 'MongoError'){
            console.log('STEP11: MONGO CONNECTION ERROR');
            callback(mongolog, null);
        }
        else{
            console.log('STEP11: Initiating self Obj mongo');
            self = {
                info: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside INFO function');
                    var message = request.body.message;
                    var priority = request.body.priority;
                    var extra = extra.appid;
                    var type = request.body.level;

                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});

                    newAppLog.save(function(err)
                    {
                        if(err) {
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as Info to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved as Info to mongodb");

                        }
                    });
                },
                debug: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside DEBUG function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});

                    newAppLog.save(function(err){
                        if(err) {
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as Draft to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");

                        }
                    });
                },
                error: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside ERROR function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as Error to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
                        }
                    });
                },
                warning: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside WARNING function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as WARNING to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
                        }
                    });
                },
                emergency: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside EMERGENCY function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as EMERGENCY to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
                        }
                    });
                },
                critical: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside CRITICAL function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as CRITICAL to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
                        }
                    });
                },
                alert: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside ALERT function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as ALERT to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
                        }
                    });
                },
                notice: function (request, extra, saveCallback)
                {
                    console.log('STEP17: Inside NOTICE function');
                    var extra = extra.appid;
                    var priority = request.body.priority;
                    var message = request.body.message;
                    var type = request.body.level;
                    var newAppLog = mongolog({datetime: Date(), priority: priority, level: type, msg: message, appid: extra});
                    newAppLog.save(function(err){
                        if(err) {
                            console.log('error: ' + err);
                            saveCallback(err, null);
                        }
                        else{
                            console.log("STEP18: Log saved as NOTICE to mongodb");
                            new mongologObj().mongoDisconnect();
                            saveCallback(null, "Log saved to mongodb");
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

    graylogObj.graylogConnection(requestObj, function(graylog, err){
      if(err != undefined)
      {
          callback(err, null);
      }
      else
      {
          self = {
              info: function (request, extra, saveCallback) {
                  console.log('INFO() in graylog obj');
                  var message = request.body.message;
                  graylog.info(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              debug: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.debug(message,extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              warning: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.warning(message,extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              error: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.error(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              emergency: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.emergency(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              critical: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.critical(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              alert:  function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.alert(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              },
              notice: function (request, extra, saveCallback) {
                  var message = request.body.message;
                  graylog.notice(message, extra, function(err, bytesSent){
                      if(err) {
                          console.log('error: ' + err);
                          saveCallback(err, null);
                      }
                      else{
                          console.log("STEP18: Message Sent to server successfully");
                          saveCallback(null, "Message Sent to server successfully");
                      }
                  });
              }

          };
          console.log('STEP12: Initiating self Obj successfully.. callback graylog obj');
          callback(null, graylog);
      }

    });
    console.log('STEP11: self obj for graylog initiating..');

}

function logToSplunk(requestObj, callback){
    console.log('Step6 Inside logToSplunk()');

    var username = config.logger.username;
    var password = config.logger.password;//"conedigi";
    var host = config.logger.hostname;//"54.169.162.137";
    var port = config.logger.portNo;
    self = {

        info: function (request, extra, saveCallback)
        {
           console.log('Inside splunk self *** INFO ***');
            var message = request.body.message;
            var priority = request.body.priority;
            var extra = extra.appid+'_'+Date.now();
            var type = request.body.level;
            process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

            var data = {name:extra,
                value: message,
                severity: 'info'};
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
                    saveCallback(err, null);
                }
                else{
                    console.log("STEP18: Response Body: "+body);
                    saveCallback(null, body);
                }
            });


        },
        error: function (request, extra,saveCallback )
        {
            console.log('Inside splunk self *** ERROR ***');
            var extra = extra.appid+'_'+Date.now();
            var priority = request.body.priority;
            var message = request.body.message;
            var type = request.body.level;
            process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

            var data = {name:extra,
                value: message,
                severity: 'error'};
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
                    console.log('error: ' + err);
                    saveCallback(err, null);
                }
                else{
                    console.log("STEP18: Response Body: "+body);
                    saveCallback(null, body);
                }
            });
        },
        warning: function (request, extra, saveCallback)
        {
            console.log('Inside splunk self *** WARNING ***');
            var extra = extra.appid+'_'+Date.now();
            var priority = request.body.priority;
            var message = request.body.message;
            var type = request.body.level;
            process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

            var data = {name:extra,
                value: message,
                severity: 'warn'};
            var options = {
                'auth' : {
                    'username':username,
                    'password':password
                },
                url: "https://"+host+":"+port+"/services/messages",
                form:    data
            }
            console.log('OPTIONS: '+options);
            httpreq.post(options, function(err,res,body) {
                if(err) {
                    console.log('error: ' + err);
                    saveCallback(err, null);
                }
                else{
                    console.log("STEP18: Response Body: "+body);
                    saveCallback(null, body);
                }
            });
        }

    };
    console.log('STEP12: Initiating self Obj successfully.. callback splunk obj');
    callback(null, 'splunk api');

}

module.exports = self;



logger.prototype.postLog = function (app) {
    app.use(bodyParser.json());       // to support JSON-encoded bodies
    app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
        extended: true
    }));


    app.use(function (req, res, next) {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        var contype = req.headers['content-type'];
        if (!contype || contype.indexOf('application/json') !== 0){
            res.status(400).json(
                {
                    status:'Invalid JSON',
                    message:'content-type should be application/json'
                }
            )
        }
        else{
            next();
        }
    });



    app.post("/save", function (req, res) {
      //  logging.log('info','inside post save','logger','low');
        //console.log('\n\n\n ***STEP1 in Microservices/MicroServices/Logger: /save route hitted');
        validateRequestParams(req, res, 'save', function (err, result) {
            if (err) {
                res.send(err);
                console.log('Error occurred while request param validation-- Execution STOPPED -- '+JSON.stringify(err));
                return;
            }
            else {
                console.log('STEP1: Passed request param and body validation successfully ->');
                var request_apikey = req.headers.apikey;
                console.log('STEP2: apikey validation started *');
                console.log('STEP2a: apikey from request headers: ' + request_apikey);
                getInstanceDetails(request_apikey, function (err, result) {

                    if (err != undefined) {
                        console.log('STEP3: Error raised while fetching record from DB: ', err);
                        res.send(err);
                        return;
                    }
                    else {
                        console.log('STEP3: Record exists in DB with given api key');
                        console.log('STEP4: log_provider from user defined/Gist: ' + logProvider);
                        var options = {logStore: logProvider};
                        var requestObject = {request: req, response: res};

                        createLogger(requestObject, options, function (err, result) {
                            console.log('STEP13: Call to saveMessage/error');
                            if (!err) {
                                console.log('STEP14: *** NO ERROR ***');
                                saveMessage(req, res, function (err, output) {
                                    if (!err) {
                                        res.send(output);
                                        return;
                                    }
                                    else {
                                        res.send(err);
                                        return;
                                    }
                                });
                            }
                            else {
                                console.log('STEP14: *** ERROR RAISED ***');
                                res.send({'status': 'failed', 'message': err});
                                return;
                            }
                        });
                    }
                });
            }
        });

    });


    app.get("/searchLog", function (req, res) {
        console.log('\n\n\n ***STEP1: /searchLog route hitted');
        validateRequestParams(req, res, 'searchLog', function (err, result) {
            if (err) {
                res.send(err);
                console.log('Error occurred while request param validation-- Execution STOPPED');
                return;
            }
            else {


                console.log('STEP1: Passed request param and body validation successfully ->');

                var request_apikey = req.headers.apikey;

                console.log('STEP2: apikey validation started *');
                console.log('STEP2a: apikey from request headers: ' + request_apikey);
                getInstanceDetails(request_apikey, function (err, result) {
                    if (err != undefined) {
                        console.log('STEP3: Error raised while fetching record from DB: ', err);
                        res.send(err);
                        return;
                    }
                    else {
                        console.log('STEP3: Record exists in DB with given api key');
                        if(logProvider != 'mongodb'){
                            res.send({'status':'failed','message':'searchLog applicable to Mongo Logs'});
                            console.log('searchLog applicable to Mongo Logs -- Execution STOPPED');
                            return;
                        }


                        console.log('STEP4: log_provider from user defined/Gist: ' + logProvider);
                        var options = {logStore: logProvider};
                        var requestObject = {request: req, response: res};
                        createLogger(requestObject, options, function (err, result) {
                            console.log('STEP13: Call to searchRFilterLogs/error');
                            if (!err) {
                                console.log('STEP14: *** NO ERROR ***: '+result);
                                searchRFilterLogs(result, req, function (err, output) {
                                    if (!err) {
                                        res.send(output);
                                        return;
                                    }
                                    else {
                                        res.send(err);
                                        return;
                                    }
                                });
                            }
                            else {
                                console.log('STEP14: *** ERROR RAISED ***');
                                res.send({'status': 'failed', 'message': 'connection failed'});
                                return;
                            }
                        });


                    }
                });
            }
        });


    });
}

var saveMessage = function (req,res, callback)
{
    console.log('STEP15 : In saveMessage');

    var output;
    var level = req.body.level.toUpperCase();
    var extra = {appid: req.body.appid};
    console.log('STEP16: Selected level: '+level);
    output = {'status': 'Success', 'message': 'Message saved successfully'};
    switch (level)
    {
        case 'INFO':
            self.info(req, extra, function(err, saveoutput){
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
            self.debug(req, extra, function(err, saveoutput){
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
            self.warning(req, extra, function(err, saveoutput){
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
            self.error(req, extra, function(err, saveoutput){
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
            self.emergency(req, extra, function(err, saveoutput){
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
            self.alert(req, extra, function(err, saveoutput){
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
            self.critical(req, extra, function(err, saveoutput){
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
            self.notice(req, extra, function(err, saveoutput){
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


var searchRFilterLogs = function(db, request, callback)
{
    console.log('STEP15: In searchRFilterLogs');

    var queryObj = {};
    var level = request.headers.level;
    var appid = request.headers.appid;
    var priority = request.headers.priority;
    console.log('appid, level, priority: '+level+':'+appid+':'+priority);

    if(priority !== undefined){
        queryObj.priority = priority;
    }
    if(level != undefined) {
        queryObj.level = level;
    }
    if(appid != undefined){
       queryObj.appid = appid;
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
    console.log('********=>>>>>> validation *********==>>>>>');
    console.log('********=>>>>>> ROUTE: '+routename+' *********==>>>>>');
    console.log('req.body.level: '+req.body.level);
    if (routename === 'save' &&(!req.body.level || !req.body.message || !req.body.appid || !req.body.priority)) {
        var message = ' parameter has been missed';
        var paramName;
        if(req.body.level === undefined){
            paramName = '\'level\'';
        }
        else if(req.body.message === undefined && routename == 'save'){
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
   // console.log('req.body.level.toUpperCase(): '+req.body.level.toUpperCase());
    console.log('Log Provider: '+logProvider);

    if( logProvider === 'splunk' && (req.body.level.toUpperCase() == 'INFO' || req.body.level.toUpperCase() == 'ERROR'
        || req.body.level.toUpperCase() == 'WARNING')){
        //do nothing
    }
    else if(logProvider == 'mongodb' || logProvider == 'graylog'){
        //do nothing
    }
    else{
        output = {"status": "failed", "message": 'log level should be either \'info/warning/error\''};
        callback(output);
        return;
    }


    if(req.body.priority != undefined || req.headers.priority != undefined ) {
        var isInPriorityList = 0, priority;

        if(routename === 'save') {
            priority = req.body.priority.toUpperCase();
        }
        else if(routename === 'searchLog'){
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

    if(req.body.level != undefined || req.headers.level != undefined){
        var isInLevelList = 0, level;

        if(routename === 'save') {
           level  = req.body.level.toUpperCase();
        }
        else if(routename === 'searchLog'){
            level = req.headers.level.toUpperCase();
        }

        logLevels.enums.forEach(function (enumItem) {
            if (enumItem.key === level) {
                isInLevelList = 1;
            }
        });

        if (!isInLevelList) {

            if (logProvider != 'splunk') {
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
        else{
            console.log('No error in validation');
            callback(null);
        }
    }
    else{
        console.log('No error in validation');
        callback(null);
    }





}
module.exports = logger;

