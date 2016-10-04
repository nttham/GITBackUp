var async = require('async');
var vm = require('vm');
var ConditionalHooks = function () {
};

 ConditionalHooks.prototype.compareHooksCondition= function(req, resultedTokenDetails, compareHooksConditionCallback) {
    console.log('**inside compareHooksCondition function**');
    //getting the condtional hooks objects from environment variables
    if (process.env["config"] && resultedTokenDetails) {
        var processConfig = JSON.parse((process.env["config"]).replace(/=>/g, ':'));
        var hookType = resultedTokenDetails["hookType"];
        if (hookType === "prehook") {
            hookType = "prehooks";
        }
        else if (hookType === "posthook") {
            hookType = "posthooks";
        }
        var authenticationType = resultedTokenDetails["authenticationType"];
        var currentHook = resultedTokenDetails["currentHook"] - 1;
        //checking whether the provider exists in config of environment JSON.
        if (processConfig[hookType][authenticationType]) {
            //getting the condition array from environment variables
            var condition = processConfig[hookType][authenticationType][currentHook]["condition"];
            //if condition is available for the current prehooks form the condition expression.
            if (condition) {
                var configuredCondition, headersConfiguredCondition;
                //looping all the conditions to from expression.
                async.each(condition, function (conditionObj, asyncCallback) {

                    var conditionKeyName = conditionObj["key"].toLowerCase();
                    var conditionOperator = decodeURIComponent(conditionObj["operator"]);
                    var conditionValue = conditionObj["value"];
                    var conditionOperand = conditionObj["operand"];

                    if (configuredCondition && configuredCondition.length) {
                        configuredCondition = configuredCondition + ' ' + conditionKeyName + ' ' + conditionOperator + '\'' + conditionValue + '\'';
                    }
                    else {
                        configuredCondition = conditionKeyName + ' ' + conditionOperator + '\'' + conditionValue + '\'';
                    }

                    console.log("**configuredCondition**" + configuredCondition);

                    if (conditionOperand) {
                        configuredCondition = configuredCondition + ' ' + conditionOperand;
                    }
                    console.log("**final configuredCondition**" + configuredCondition);

                    // Async call is done, alert via callback
                    asyncCallback();
                },
                    function (err) {
                        if (err) {
                            console.log("**ERROR in final configuredCondition**");
                            compareHooksConditionCallback(err);
                        }
                    }
                );

                //loop for request headers to from expression based on configured key of environment variables.
                async.each(condition,
                    function (requestConditionObj, requestConditionObjCallback) {
                        var key = [requestConditionObj["key"]].toString().toLowerCase();
                        if (req["headers"][key]) {
                            if (headersConfiguredCondition && headersConfiguredCondition.length && headersConfiguredCondition.indexOf(key) === -1) {
                                headersConfiguredCondition = headersConfiguredCondition + ' ' + key + ' = ' + '\'' + req["headers"][key] + '\';';
                            }
                            else if (headersConfiguredCondition === undefined) {
                                headersConfiguredCondition = "" + key + ' = ' + '\'' + req["headers"][key] + '\';';
                            }
                        }
                        else {
                            if (headersConfiguredCondition && headersConfiguredCondition.length && headersConfiguredCondition.indexOf(key) === -1) {
                                headersConfiguredCondition = headersConfiguredCondition + ' ' + key + ' = ' + '\'' + null + '\';';
                            }
                            else if (headersConfiguredCondition === undefined) {
                                headersConfiguredCondition = "" + key + ' = ' + '\'' + null + '\';';
                            }
                        }
                        // Async call is done, alert via callback
                        requestConditionObjCallback();
                    },
                    function (err) {
                        // All tasks are done now

                        if (err) {
                            console.log("**ERROR in final requestConditionObj**");
                            compareHooksConditionCallback(err);
                        }
                    }
                );
                console.log("**headersConfiguredCondition**", headersConfiguredCondition);
            }
        }
    }

    //conditional hooks check. If we have conditional hook from request header & environment variable and eval of expression is true than we will generate the otp.
    //or if we have conditional hook from request header we will generate the otp
    //or If we don't have conditional hook from request header & environment variable we will generate the otp
    //Otherwise execution will skip and go to nextCall.
    var isExecutePreHooks = false;

    if (configuredCondition && headersConfiguredCondition) {
        headersConfiguredCondition = headersConfiguredCondition + configuredCondition;
        //isExecutePreHooks = eval(headersConfiguredCondition);
        var context = vm.createContext();
        var script = new vm.Script(headersConfiguredCondition);
        isExecutePreHooks = script.runInContext(context);
    }
    else if (!(configuredCondition || headersConfiguredCondition)) {
        isExecutePreHooks = true;
    }

    //call back to previous function call
    compareHooksConditionCallback(null, isExecutePreHooks);
};

module.exports = ConditionalHooks;