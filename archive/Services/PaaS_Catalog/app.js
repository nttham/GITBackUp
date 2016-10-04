/**
 * Created by Srividya on 31/05/16.
 */
// Add Dependent modules.
var express = require('express');
var app = express();

// initializes routes
var httpRouter = require('./PaaSCatalog.js');
var port = process.env.PORT || 3030;

// Attach the routers for their respective paths
app.use('/PaaSCatalog', httpRouter);
// If no route is matched by now, it must be a 404
app.use(function (req, res) {
    res.status(404);
    res.send({error: 'Page Not found'});
    return;
});

// START THE SERVER
app.listen(port);
console.log("App listening on port " + port);

module.exports = app;