/**
 * Created by Srividya on 19/05/16.
 */
var request = require('request');

var async = require('async');

var hashMap = require('hashmap');
var map = new hashMap();

var config = require('./config.json');

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

var DataCall = function () {
};

var platformID, regionID;

/*
 This module is used to create the GIST structure.
 This api call needs data in body to create GIST data structure like description and files which we need to create.
 */
/*
var createData = function (requestData, callback) {
    var body = '';
    requestData.on('data', function (data) {
        body += data;
    });

    requestData.on('end', function () {
        //forming http request string
        var postOptions = {
            url: 'https://api.github.com/gists',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': config.auth,
                'user-agent': 'node.js'
            },
            body: body
        };

        request(postOptions, function (err, response, body) {
            if (err) {
                return callback(err);
            }
            else {
                fs.writeFileSync('gistID.txt', JSON.parse(body).id, 'utf8');
                return callback(null, JSON.stringify(response));
            }
        });
    });
};
*/
function tempGetData(callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/' + config.gistID,
        method: 'GET',
        headers: {
            'Authorization': config.auth,
            'user-agent': 'node.js'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            return callback(null, JSON.stringify(response));
        }
    });
};

/*
 This module is used to Edit the data.
 This api call needs data in body to edit  data structure like description and files which we need to edit.
 */
/*
var editData = function (editedStr, callback) {
    fs.readFile('gistID.txt', 'utf-8', function (err, gistID) {
        if (err) {
            return callback(err);
        }
        else {
            var editedBody = '';
            editedStr.on('data', function (data) {
                editedBody += data;
            });

            editedStr.on('end', function () {
                //forming http request string
                var postOptions = {
                    url: 'https://api.github.com/gists/' + gistID,
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': config.auth,
                        'user-agent': 'node.js'
                    },
                    body: editedBody
                };

                request(postOptions, function (err, response) {
                    if (err) {
                        return callback(err);
                    }
                    else {
                        return callback(null, JSON.stringify(response));
                    }
                });
            });
        }
    });
};
*/

function replaceStringCall(stringsDataArray, replaceCallBack) {
    var finalStr;
    async.each(stringsDataArray, function (eachresult, forCallback) {

        Object.keys(eachresult).forEach(function (key) {
            finalStr = eachresult[key];
            finalStr = finalStr.replace(/\\n/g, '').replace(/\\/g, '').replace("\"", "").replace(new RegExp("\"" + '$'), "").replace(/\\r/g, "").replace(/\\t/g, "").replace(/\\f/g, "");
            map.set(key, JSON.parse(finalStr));
        });
        forCallback();
    }, function (err) {
        if (err) {
            return replaceCallBack(err);
        }
        else {
            return replaceCallBack(null, finalStr);
        }
    }
    );
};

//this will return the heap data based on the provided type.
function switchResult(queryType, callback) {
    switch (queryType) {
        case "category":
            callback(null, map.get("categoriesCollection"));
            break;
        case "services":
            callback(null, map.get("servicesCollection"));
            break;
        case "hooks":
            callback(null, map.get("hooksCollection")); 
            break;
        case "loggers":
            callback(null, map.get("loggerCollection"));
            break;
        case "notification":
            callback(null, map.get("notificationProtocolsCollection"));
            break;
        case "notificationconfiguration":
            callback(null, map.get("notificationConfigurationsCollection"));
            break;
        default:
            callback(null, { "Error": "kindly check for required parameters." });
    }
}

/*
 1) This module is used to get the entire data.
 2) Once we got the data we will maintain that data in heap. so for next calls if we have data in heap we will take and give otherwise we will get the data from
 GIST again.
 3) Here we are maintaining three collection for categories, services & hooks.
 */
DataCall.prototype.getData = function (request, response, callbackHandler) {
    var cachedJSON = map.get("categoriesCollection");


    if (cachedJSON) {
        var callbackWithResult = function (err, result) {
            if (err) {
                callbackHandler(error);
            }
            else {
                callbackHandler(null, result);
            }
        };
        switchResult(request.query.type.toLowerCase(), callbackWithResult);
    }
    else {
        var callBack = function (error, result) {
            if (error) {
                callbackHandler(error);
            }
            else {
                callbackHandler(null, result);
            }
        };
        var clientGetData = function (callBack) {
            tempGetData(callBack);
        };
        async.waterfall([clientGetData, resultedDataBasedOnSelection], callBack);
    }

    function resultedDataBasedOnSelection(entireData, resultedDataFinalcallback) {
        var stringData = JSON.parse(JSON.parse(entireData).body);

        var categoriesContent = JSON.stringify(stringData.files[config.categories].content);
        var servicesContent = JSON.stringify(stringData.files[config.services].content);
        var hooksContent = JSON.stringify(stringData.files[config.hooks].content);
        var bluemixContent = JSON.stringify(stringData.files[config.bluemix].content);
        var loggerContent = JSON.stringify(stringData.files[config.loggerDetails].content);
        var notificationProtocolsContent = JSON.stringify(stringData.files[config.notificationProtocols].content);
        var notificationConfigurationsContent = JSON.stringify(stringData.files[config.notificationConfigurations].content);

        var dataJSON = [{
            "categoriesCollection": categoriesContent,
            "servicesCollection": servicesContent,
            "hooksCollection": hooksContent,
            "bluemixCollection": bluemixContent,
            "loggerCollection": loggerContent,
            "notificationProtocolsCollection": notificationProtocolsContent,
            "notificationConfigurationsCollection": notificationConfigurationsContent
        }];

        var functioncallback = function () {
            var callbackWithResult = function (err, result) {
                if (err) {
                    resultedDataFinalcallback(error);
                }
                else {
                    resultedDataFinalcallback(null, result);
                }
            };
            switchResult(request.query.type.toLowerCase(), callbackWithResult);
        };

        replaceStringCall(dataJSON, functioncallback);
    }
};

/*
 This module is used to Search the data by using provided search string.
 */
DataCall.prototype.searchData = function (request, response, callback) {
    /**
     * @namespace request.query.searchString
     */
    /**
     * @namespace resultedJson.categories
     */
    var cachedJSON = map.get("categoriesCollection");

    if (cachedJSON) {
        searchFunction(cachedJSON, function (err, result) {
            callback(null, result);
        });
    }
    else {
        var callBack = function (error, result) {
            if (error) {
                callback(error);
            }
            else {
                callback(null, result);
            }
        };

        var clientGetData = function (callBack) {
            getData(request, response, callBack);
        };

        async.waterfall([clientGetData, searchFunction], callBack);
    }


    function searchFunction(resultedJson, callback) {
        //resultedJson = resultedJson.replace(/\\n/g, '').replace(/\\/g, '').replace(/ /g,'').replace("\"","").replace(new RegExp("\"" + '$'), "").replace(/\\r/g, "").replace(/\\t/g, "").replace(/\\f/g, "");
        var returnedArray = [];
        var resultedJsonArray;

        if (!resultedJson.hasOwnProperty('categories')) {
            callback('No Results Found');
        }
        else {
            resultedJsonArray = resultedJson.categories;

            async.each(resultedJsonArray,
                function (eachJSONResult, forCallback) {
                    if (eachJSONResult.keys.indexOf(request.query.searchString.toLowerCase()) != -1) {
                        returnedArray.push(eachJSONResult);
                    }
                    else {
                        var desc = eachJSONResult.description.toLowerCase();
                        if (desc.search(request.query.searchString.toLowerCase()) != -1) {
                            returnedArray.push(eachJSONResult);
                        }
                    }
                    forCallback();
                },
                function (err) {
                    if (err) {
                        callback(err);
                    }
                    else {
                        callback(null, { result: returnedArray });
                    }
                }
            );
        }
    }
};

//to get the OneCData
var getOneCData = function (cognizantOnecallBack) {
    var cachedData = map.get("categoriesCollection");
    var categoriesArray = cachedData.categories;

    async.each(categoriesArray,
        function (eachJSONResult, forCallback) {
            if (eachJSONResult.id === platformID) {
                cognizantOnecallBack(null, eachJSONResult);
                //break;
            }
            forCallback();
        },
        function (err) {
            if (err) {
                cognizantOnecallBack(err);
            }
            // else {
            //     callback(null, {result:returnedArray});
            // }
        }
    );
};

/**
 * @namespace bluemixCachedData.metadata
 */
var getBluemixData = function (resultsOnecObj, bluemixcallBack) {
    var finalArray = [];
    var bluemixCachedData = map.get("bluemixCollection");

    console.log('bluemixCachedData:' + JSON.stringify(bluemixCachedData));

    var resultObj = bluemixCachedData.metadata;

    var regionsEndPointURL = resultObj['regions'][regionID];

    //delete resultObj['regions'];

    console.log('***' + regionsEndPointURL);

    resultObj['authorization_endpoint'] = regionsEndPointURL['authorization_endpoint'];

    finalArray.push(resultsOnecObj);
    finalArray.push(resultObj);

    bluemixcallBack(null, finalArray);
};

/*
 This rest api is used to get the categories data.
 This api call needs id of category & platforms as query parameters. Based on provided platforms & category this will return the services list.
 */
DataCall.prototype.getRelatedData = function (request, callback) {
    var platformsStr = request.query.platform;
    platformID = request.query.id;
    regionID = request.query.regionID;

    var seletedPlatformsArray = platformsStr.split(',');

    var finalArray = [];

    async.each(seletedPlatformsArray,
        function (results, forCallback) {
            if (results) {
                console.log("*****getRelatedData*****" + results);
                switch (results.toLowerCase()) {
                    case "2004":
                    case "2001":
                        var cognizantOnecallBack = function (error, data) {
                            if (error) {
                                finalArray.push(error);
                                callback(error);
                            }
                            else {
                                finalArray.push(data);
                                callback(null, finalArray);
                            }
                        };

                        getOneCData(cognizantOnecallBack);
                        break;
                    case "2002":
                        var bluemixcallBack = function (error, data) {
                            if (error) {
                                callback(error);
                            }
                            else {
                                callback(null, data);
                            }
                        };
                        async.waterfall([getOneCData, getBluemixData], bluemixcallBack);
                        break;
                    case "2003":
                        break;
                }
            }
        },
        function (err) {
            if (err) {
                return callback(err);
            }
            else {
                return callback(null, { results: platformsStr });
            }
        }
    );



};

//this is to get the regions data from data store (like GIST,Mongo etc).
DataCall.prototype.getRegions = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/' + config.gistID,
        method: 'GET',
        headers: {
            'Authorization': config.auth,
            'user-agent': 'node.js'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            var stringData = JSON.parse(response.body);


            var regionsContent = stringData.files[config.regions].content;
            if (requestOptions.query.platformID && regionsContent && JSON.parse(regionsContent)[requestOptions.query.platformID]) {
                var regionsForPlatform = JSON.parse(regionsContent)[requestOptions.query.platformID];

                var keys = Object.keys(regionsForPlatform.regions);

                var optionsArray = [];

                keys.forEach(function (data) {
                    optionsArray.push({ "id": data, "name": regionsForPlatform.regions[data].name, "api_url": regionsForPlatform.regions[data].api_url, "login_url": regionsForPlatform.regions[data].authorization_endpoint, "host": regionsForPlatform.regions[data].host });
                });


                return callback(null, { "regions": optionsArray });
            }
            else {
                return callback({ "Error": "No data available" });
            }
        }
    });
};

//this is to get the organizations from CF.
//Need accesstoken to get the organizations list.
DataCall.prototype.getOrganizations = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        url: requestOptions.headers.api_url + '/v2/organizations',
        method: 'GET',
        headers: {
            'Authorization': requestOptions.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {

            if (response.statusCode && response.statusCode === 200) {
                return callback(null, JSON.parse(response.body));
            }
            else {
                return callback(null, { 'Error': JSON.parse(response.body).description });
            }
        }
    });
};

//this is to get the spaces from CF.
//Need accesstoken to get the organizations list.
DataCall.prototype.getSpaces = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        url: requestOptions.headers.api_url + '/v2/organizations/' + requestOptions.headers.orgguid + '/spaces',
        method: 'GET',
        headers: {
            'Authorization': requestOptions.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            if (response.statusCode && response.statusCode === 200) {
                return callback(null, JSON.parse(response.body));
            }
            else {
                return callback(null, { 'Error': JSON.parse(response.body).description });
            }
        }
    });
};

//to get the layout json data from GIST.
DataCall.prototype.getLayouts = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/' + config.gistID,
        method: 'GET',
        headers: {
            'Authorization': config.auth,
            'user-agent': 'node.js'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            var stringData = JSON.parse(response.body);

            var layoutsContent = stringData.files[config.layouts].content;
            if (layoutsContent) {
                var layoutHeaderIDVal = requestOptions.query.layoutHeaderID;
                var obj = {};
                obj[layoutHeaderIDVal] = JSON.parse(layoutsContent)[layoutHeaderIDVal];
                // var layoutsArray = [];
                // layoutsArray.push(test[layoutHeaderIDVal]);
                // console.log('****obj***'+JSON.stringify(layoutsArray[0])+'****'+layoutHeaderIDVal);
                return callback(null, obj);
            }
            else {
                return callback({ "Error": "No data available" });
            }
        }
    });
};

//this is to check whether the app name is exists in CF or not based on given appname.
DataCall.prototype.checkAppnameExistence = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        //http://api.54.208.194.189.xip.io/v2/spaces/b169a527-a10a-4a84-a45a-2909fee6b1d9/apps?q=name%3Apaascatalog1&inline-relations-depth=1
        url: requestOptions.headers.api_url + '/v2/spaces/' + requestOptions.headers.spaceguid + '/apps?q=name%3A' + requestOptions.headers.appname + '&inline-relations-depth=1',
        method: 'GET',
        headers: {
            'Authorization': requestOptions.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            if (response.statusCode && response.statusCode === 200) {
                if (JSON.parse(response.body).total_results === 1) {
                    return callback(null, { Error: "App name is already taken" });
                }
                else {
                    return callback(null, { Success: 'Success' });
                }
            }
            else {
                return callback(null, { 'Error': JSON.parse(response.body).description });
            }
        }
    });
};

//this is to get the list of cloud foundry platforms like pivotal, bluemix etc.
DataCall.prototype.getPlatforms = function (requestOptions, callback) {
    //forming http request string
    var getOptions = {
        url: 'https://api.github.com/gists/' + config.gistID,
        method: 'GET',
        headers: {
            'Authorization': config.auth,
            'user-agent': 'node.js'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {

            var stringData = JSON.parse(response.body);

            var platformsContent = stringData.files[config.platforms].content;
            if (platformsContent) {
                return callback(null, platformsContent);
            }
            else {
                return callback({ "Error": "No data available" });
            }
        }
    });
};

//this is to get the servicename from the GIST based on platform & regionID provided.
function getServiceDetailsData(requestOptions, callback) {
    if (requestOptions && requestOptions["headers"] && requestOptions["headers"]["regionid"] && requestOptions["headers"]["platformid"] && requestOptions["headers"]["categoryname"]) {
        //forming http request string
        var getOptions = {
            url: 'https://api.github.com/gists/' + config.gistID,
            method: 'GET',
            headers: {
                'Authorization': config.auth,
                'user-agent': 'node.js'
            }
        };

        request(getOptions, function (err, response) {
            if (err) {
                return callback(err);
            }
            else {

                var stringData = JSON.parse(response.body);

                var serviceDetailsContent = stringData.files[config["serviceDetails"]].content;
                if (serviceDetailsContent) {
                    console.log("***getServiceDetailsData***" + serviceDetailsContent);

                    var serviceDetailsObj = JSON.parse(serviceDetailsContent);
                    var servicesArray = serviceDetailsObj[requestOptions["headers"]["platformid"]][requestOptions["headers"]["regionid"]];

                    if (servicesArray.length) {
                        var serviceObj = servicesArray.filter(function (obj) {
                            return obj["categoryName"] === requestOptions["headers"]["categoryname"];
                        });
                        console.log("***servicesArray***" + JSON.stringify(serviceObj));

                        if (serviceObj.length) {
                            return callback(null, serviceObj[0]);
                        }
                        else {
                            return callback({ "Error": "No data available" });
                        }
                    }
                    else {
                        return callback({ "Error": "No data available" });
                    }
                }
                else {
                    return callback({ "Error": "No data available" });
                }
            }
        });
    }
    else {
        return callback({ "Error": "platformid or regionid are not provided in headers" });
    }
};

//this is to get the serviceGUID from the CF.
//From getServiceDetailsData we will get the service name, using that service name we will get the serviceGUID
//First it will check for the requested service in space level by appending space name to servicename.
//if service find in space it will return the serviceGUID, otherwise it will check for org/domain level service.
var isSpaceCheckDone = false;
function getServiceGUIDData(requestOptions, servDetsObj, callback) {
    console.log("***came to getServiceGUIDData***");
    //forming http request string
    var getOptions = {
        url: requestOptions.headers.api_url + '/v2/spaces/'+requestOptions["headers"]["spaceguid"]+'/services?q=label:'+servDetsObj["ServiceName"],
        method: 'GET',
        headers: {
            'Authorization': requestOptions.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            if (response.statusCode && response.statusCode === 200) {

                var resourcesArray = JSON.parse(response.body)['resources'];

                //console.log("***getServiceGUIDData resourcesArray***" + JSON.stringify(resourcesArray));

                if (resourcesArray.length > 0) {

                    var guidResult = resourcesArray.filter(function (obj) {
                        if (isSpaceCheckDone) {
                            isSpaceCheckDone = false;
                            return obj["entity"]["label"] === servDetsObj["ServiceName"];
                        }
                        else {
                            isSpaceCheckDone = true;
                            return obj["entity"]["label"] === requestOptions["headers"]["spacename"]+'_'+servDetsObj["ServiceName"];
                        }
                        
                    });

                    if (guidResult.length) {
                        console.log("***getServiceGUIDData***" + guidResult[0]);
                        isSpaceCheckDone = false;
                        return callback(null, guidResult[0]);
                    }
                    else {
                        if (isSpaceCheckDone) {
                            getServiceGUIDData(requestOptions, servDetsObj, callback);
                        }
                        else {
                            return callback({ Error: 'No service found found' });
                        }
                    }
                }
                else {
                    return callback({ Error: 'No service found found' });
                }
            }
            else {
                return callback({ Error: JSON.parse(response.body).description });
            }
        }
    });
};

//this is to get the ServicePlanGUID
//from getServiceGUIDData we will get the serviceGUID, using that serviceGUID we will get the servicePlans available.
function getServicePlanGUIDData(requestOptions, serviceGUIDResults, callback) {
    //forming http request string
    var getOptions = {
        url: requestOptions.headers.api_url + '/v2/services/' + serviceGUIDResults["metadata"]["guid"] + '/service_plans',
        method: 'GET',
        headers: {
            'Authorization': requestOptions.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    request(getOptions, function (err, response) {
        if (err) {
            return callback(err);
        }
        else {
            if (response.statusCode && response.statusCode === 200) {
                //var resourcesArray = [];

                var resourcesArray = JSON.parse(response.body)['resources'];

                if (resourcesArray.length > 0) {
                    var resultArray = [];
                    for (var i = 0; i < resourcesArray.length; i++) {
                        var obj = resourcesArray[i];
                        resultArray.push({ "guid": obj['metadata']['guid'], "name": obj['entity']['name'] });
                    }
                    return callback(null, { result: resultArray });
                }
                else {
                    return callback({ Error: 'No plans found' });
                }
            }
            else {
                return callback({ Error: JSON.parse(response.body).description });
            }
        }
    });
};

//this is to get the existing instances.
//from getServicePlanGUIDData we will get the service plan guid's, using that guid's we will get the existing instances.
function callToGetExistingInstances(options, planGUID, ExeInstancecallbackHandler) {
    console.log('callToGetExistingInstances');
    //forming http request string
    var getOptions = {
        url: options.headers.api_url + '/v2/service_plans/' + planGUID + '/service_instances',
        method: 'GET',
        headers: {
            'Authorization': options.headers.authorization,
            'Content-Type': 'application/json'
        }
    };

    console.log('URL***' + getOptions.url);

    request(getOptions, function (err, response) {
        console.log('**callToGetExistingInstances Got the response**' + err);
        if (err) {
            console.log('**callToGetExistingInstances ERROR**' + err);
            return ExeInstancecallbackHandler(err);
        }
        else {
            if (response && response.statusCode === 200) {
                return ExeInstancecallbackHandler(null, JSON.parse(response.body));
            }
            else {
                console.log('**callToGetExistingInstances Success**' + JSON.stringify(response));
                return ExeInstancecallbackHandler({ Error: response.body });
            }
        }
    });
};

//this is to get the existing instances.
//from getServicePlanGUIDData we will get the service plan guid's, using that guid's we will get the existing instances.
DataCall.prototype.getExistingInstances = function (requestOptions, callback) {

    var existingInstancesArray = [];

    var callbackHandler = function (error, result) {
        if (error) {
            return callback(error);
        }
        else {
            if (result['result'].length > 0) {

                async.each(result['result'],
                    function (results, forCallback) {
                        console.log('****came to callbackHandler****');
                        if (results) {
                            var ExeInstancecallbackHandler = function (error, instancesResult) {
                                if (error) {
                                    return callback(error);
                                }
                                else {
                                    console.log('**came to ExeInstancecallbackHandler**');
                                    if (instancesResult['resources'].length > 0) {
                                        async.each(instancesResult['resources'],
                                            function (eachresult, forCallback) {
                                                if (eachresult['entity']['last_operation']['type'] !== 'delete' && eachresult['entity']['last_operation']['state'] !== 'failed' && eachresult['entity']['space_guid'] === requestOptions.headers["spaceguid"]) {
                                                    existingInstancesArray.push({ guid: eachresult['metadata']['guid'], name: eachresult['entity']['name'] });
                                                }
                                            },
                                            function (err) {
                                                if (err) {
                                                    return callback(err);
                                                }
                                                else {
                                                    forCallback();
                                                }
                                            }
                                        );
                                        forCallback();
                                    }
                                    else {
                                        //return callback({message:'no existing instances found'});
                                        return forCallback(null, { ExistingInstances: [], Plans: result['result'] });
                                    }
                                }
                            };
                            var existanceInstanceData = function (existanceInstanceDataCallbackHandler) {
                                console.log('test call');
                                callToGetExistingInstances(requestOptions, results['guid'], existanceInstanceDataCallbackHandler);
                            };
                            async.waterfall([existanceInstanceData], ExeInstancecallbackHandler);
                            console.log('test call111');
                        }
                    },
                    function (err) {
                        if (err) {
                            console.log('callbackHandler getExistingInstances' + err);
                            return callback(err);
                        }
                        else {
                            return callback(null, { ExistingInstances: existingInstancesArray, Plans: result['result'] });
                        }
                    }
                );
            }
            else {
                return callback(null, { result: 'No plans found' });
            }
        }
    };

    //to get service plans guid
    var getServicePlanGUIDDataObj = function (serviceGUIDResults, callbackHandler) {
        getServicePlanGUIDData(requestOptions, serviceGUIDResults, callbackHandler);
    };

    //to get the service guid
    var getServiceGUID = function (serviceDetailsObj, callbackHandler) {
        getServiceGUIDData(requestOptions, serviceDetailsObj, callbackHandler);
    };

    //to get the service name
    var getServiceDeatils = function (callbackHandler) {
        getServiceDetailsData(requestOptions, callbackHandler);
    };

    async.waterfall([getServiceDeatils, getServiceGUID, getServicePlanGUIDDataObj], callbackHandler);
};

//to get all the service plan guid's of service.
DataCall.prototype.getdomainserviceplanguid = function (requestOptions, callback) {

    var planGUIDCallback = function (error, result) {
        if (error) {
            return callback(error);
        }
        else {
            return callback(null, result);
        }
    };


    //to get the service name
    var getServiceDeatils = function (callbackHandler) {
        getServiceDetailsData(requestOptions, callbackHandler);
    };

    //to get the service guid
    var getServiceGUID = function (serviceDetailsObj, callbackHandler) {
        getServiceGUIDData(requestOptions, serviceDetailsObj, callbackHandler);
    };

    //to get service plans guid
    var getServicePlanGUIDDataObj = function (serviceGUIDResults, callbackHandler) {
        getServicePlanGUIDData(requestOptions, serviceGUIDResults, callbackHandler);
    };

    async.waterfall([getServiceDeatils, getServiceGUID, getServicePlanGUIDDataObj], planGUIDCallback);
};

module.exports = DataCall;
