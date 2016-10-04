/**
 * Created by Srividya on 23/05/16.
 */
var express = require('express');
var router = express.Router();

var async = require('async');

var DataClient = require('./DataClient.js');
var dataClient = new DataClient();


/*
 This rest api is used to create the GIST structure.
 This api call needs data in body to create GIST data structure like description and files which we need to create.
 */
/*
router.route('/createCatalog')
    .post(function (request, response) {

        var callBack = function (error, result) {
            if (error) {
                response.send(error);
            }
            else {
                response.send(result);
            }
        };

        var createData = function (callBack) {
            dataClient.createData(request, callBack);
        };
        async.waterfall([createData], callBack);
    });
    */

//setting the response headers and sending the response
function setResponseHeaders(response,result){
    response.setHeader("Status-Code", 200);
    response.setHeader("Access-Control-Allow-Headers", "*");
    response.setHeader('Content-Type', 'application/json');
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
    response.send(result);
}


/*
 This rest api is used to get the entire catalog data from GIST.
 */
router.route('/catalog')
    .get(function (request, response) {
        function callbackHandler(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }
        dataClient.getData(request, response, callbackHandler);
    });

/*
 This rest api is used to Edit the data.
 This api call needs data in body to edit  data structure like description and files which we need to edit.
 */
/*
router.route('/editCatalog')
    .patch(function (request, response) {

        var callBack = function (error, result) {
            if (error) {
                response.send('Creation Error**' + error);
            }
            else {
                response.send(result);
            }
        };
        var editData = function (callBack) {
            dataClient.editData(request, callBack);
        };
        async.waterfall([editData], callBack);
    });
*/

/*
 This rest api is used to Search the catalog data.
 This api call needs searchstring & type of search as query parameters.
 */
router.route('/searchCatalog')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                if (result.length === 0) {
                    response.send('No Results Found');
                }
                else {
                    setResponseHeaders(response,result);
                }
            }
        }
        dataClient.searchData(request, response, callBack);
    });


/*
 This rest api is used to get the categories data.
 This api call needs id of category & platforms as query parameters. Based on provided platforms & category this will return the services list.
 */
router.route('/platformservices')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                if (result.length === 0) {
                    response.send('No Results Found');
                }
                else {
                    setResponseHeaders(response,result);
                }
            }
        }
        dataClient.getRelatedData(request, callBack);
    });


//this is to get the regions data from data store (like GIST,Mongo etc).
router.route('/regions')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }
        dataClient.getRegions(request, callBack);
    });

//this is to get the organizations from CF.
//Need accesstoken to get the organizations list.
router.route('/organizations')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }
        dataClient.getOrganizations(request, callBack);
    });

//this is to get the spaces from CF.
//Need accesstoken to get the organizations list.
router.route('/spaces')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }
        dataClient.getSpaces(request, callBack);
    });

////this is to get layouts data from GIST.
//router.route('/layouts')
//    .get(function (request, response) {
//        function callBack(error, result) {
//            if (error) {
//                response.send(error);
//            }
//            else {
//                setResponseHeaders(response,result);
//            }
//        };
//        dataClient.getLayouts(request, callBack);
//    });


//this is to check whether the app name is exists in CF or not based on given appname.
router.route('/appNameCheck')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }

        dataClient.checkAppnameExistence(request, callBack);
    });


//this is to get the list of cloud foundry platforms like pivotal, bluemix etc.
router.route('/platform')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }

        dataClient.getPlatforms(request, callBack);
    });

//Don't remove the code.May be useful in future

//router.route('/getServicePlanGUID')
//    .get(function (request, response) {
//        var callBack = function (error, result) {
//            if (error) {
//                response.send(error);
//            }
//            else {
//                response.setHeader("Status-Code", 200);
//                response.setHeader("Access-Control-Allow-Headers", "*");
//                response.setHeader('Content-Type', 'application/json');
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
//                response.send(result);
//            }
//        };
//        var getServicePlanGUIDData = function (callBack) {
//            dataClient.getServicePlanGUIDData(request, callBack);
//        };
//        async.waterfall([getServicePlanGUIDData], callBack);
//    });


//to get the existing instance of the provided service.
//using the provided service we will hit the CF api to get the instances created for that service.
router.route('/getExistingInstances')
    .get(function (request, response) {
        function callBack(error, result) {
            console.log('Final callback getExistingInstances');
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }

        dataClient.getExistingInstances(request, callBack);
    });

//to get the guid of the provided service from CF.
router.route('/getdomainserviceplanguid')
    .get(function (request, response) {
        function callBack(error, result) {
            if (error) {
                response.send(error);
            }
            else {
                setResponseHeaders(response,result);
            }
        }

        dataClient.getdomainserviceplanguid(request, callBack);
    });

module.exports = router;