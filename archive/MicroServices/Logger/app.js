/**
 * Created by Suryakala on 15/06/16.
 * This app will allow user to log the message to either mongo or graylog server.
 */
var express = require('express');
var bodyParser = require('body-parser');
var logger = require('./logger.js');
var hmac = require('./configuration/HashGenerator');
var app = express();
var loggerObj = new logger();


app.use(function(error, request, response, next){
    response.status(500).json(
        {
            message:error.message,
            error: error.stack
        }
    )
});

app.use(function(err, req, res, next) {
    if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
        console.error('Not a valid JSON body request');
        res.status(400).send({error:"Not a valid JSON body request"})
    }
    else {
        next();
    }
});

app.use(bodyParser.json());       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
    extended: true
}));
app.use(hmac());
// created route for postLog
loggerObj.postLog(app);


// port
var port = process.env.PORT || 3000;
//started a server which is listening on port
app.listen(port);
console.log('Logger started running on port ' + port);

module.exports = app;
