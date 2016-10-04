/**
 * Created by Srividya on 12/07/16.
 */
// Add Dependent modules.
var express = require('express');
var app = express();
var bodyParser = require('body-parser');

// initializes routes
var httpRouter = require('./channels.js');
var port = process.env.PORT || 8080;

app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

// Attach the routers for their respective paths
app.use('/channels', httpRouter);

// If no route is matched by now, it must be a 404
app.use(function (req, res) {
    console.log('******'+req.body);
    res.status(404);
    res.send({error: 'Page Not found'});
    return;
});

// START THE SERVER
app.listen(port);
console.log("App listening on port " + port);

module.exports = app;