// dependencies
var express = require('express');
var bodyParser = require('body-parser');
var app      = express();
var path = require('path');
var port     = process.env.PORT || 3001;
var dbConnectorObj = require("./DAL/Connector.js");
var dbConfig = require("./DAL/dbConfig.json");

var cors = require('cors');

var corOptions = {
    origin : true,
    methods : ['GET', 'PUT', 'POST', 'OPTIONS', 'DELETE'],
    "preflightContinue": true
};

app.set('views', __dirname + './../views');
app.set('view engine', 'jade');
app.use(bodyParser.json());
app.use(cors(corOptions));
app.use(function(err, req, res, next) {
    console.log("in app ",err);
    if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
        console.error('Not a valid JSON body request');
        res.status(400).send({error:"Not a valid JSON body request"})
    }
    else {
        next();
    }
});


//connecting to DB with the DB configured
dbConnectorObj.connectToDB(dbConfig.dbType,function(err,db)
{
    if(err) {
        console.error('Error! Database connection failed : ',err);
    }
    else {

        require('./routes/deviceRoutes')(app);
        require('./routes/settingsRoutes')(app);
        require('./routes/channelRoutes')(app);
        require('./routes/notificationRoutes')(app);
        require('./routes/templateRoutes')(app);
        require('./routes/slackRoutes')(app);
    }

});

app.listen(port, function () {
    console.log("Push Notification microservice is listening at port : " + port);
});

process.on('uncaughtException', function(err) {
    console.log("uncaught exception",err);
});