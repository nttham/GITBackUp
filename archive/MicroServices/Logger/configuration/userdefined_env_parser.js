/**
 * Created by trujun on 08/09/16.
 */
var config;
if(process.env.config){
    var envJson = process.env.config;
    envJson = envJson.replace(/=>/g, ':');
    config = JSON.parse(envJson);
}

console.log('CONFIG: '+JSON.stringify(config));

exports.userDefinedENV = config;