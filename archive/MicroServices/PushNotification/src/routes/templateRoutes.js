var constants = require('../constants/constants.json');
var templatesFunObj = require('../functions/templates');
var authValidationObj = require('../functions/authenticateAPI.js');
var coreDBConnectorObj = require("../DAL/Connector.js");

//Channels functions
var channelsClientObj = require('../functions/channelsClient.js');

module.exports = function(app) {

    //Return all the registered devices from db
    app.get('/templates', function (req, res) {

        var getTemplatesCallback = function (error, results) {
            if (error) {
                res.status(404).send(error);
            }
            else {
                if (results.length) {
                    var dbName = results[0]["dbName"];

                    coreDBConnectorObj.switchDB(dbName, function (err, dbInstance) {
                        templatesFunObj.listTemplates(dbInstance["dbConnection"],req, function (err, result) {
                            if (err) {
                                console.log("get templates API => error in getting the templates info from db : " + JSON.stringify(err));
                                res.status(result.code).send(result);
                            }
                            else {
                                res.json(result);
                            }
                        });
                    })
                }
                else {
                    //sending error if it is unauthorised
                    res.status(401).send({"error": "Unauthorised request"});
                }
            }
        };

        authValidationObj.authenticateAPI(req, getTemplatesCallback);
    });
};