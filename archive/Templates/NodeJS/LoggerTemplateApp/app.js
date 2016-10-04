/**
 * Created by Suryakala on 15/06/16.
 * This app will allow user to log the message to either mongo or graylog server.
 */
var express = require('express');
var app = express();
// here we are hooking the otp with this app
var loggertemplate = require('./loggertemplate.js');
var loggerTemplateObj = new loggertemplate();
// created route for postLog
loggerTemplateObj.hitService(app);


app.use(function(error, request, response, next){
    response.status(500).json(
        {
            message:error.message,
            error: error.stack
        }
    )
});

var port = process.env.PORT || 3000;
//started a server which is listening on port
app.listen(port);
console.log('LoggerTemplateApp is listening on port ' + port);

module.exports = app;
