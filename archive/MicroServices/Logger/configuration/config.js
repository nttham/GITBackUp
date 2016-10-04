/**
 * Created by Suryakala on 24/05/16.
 */

var configuration = {
    mongoDB: {
        server_url: "mongodb://54.251.97.71:10040", // static connection string
    },
    Hash:{
        algorithm:'sha512'
    },
    Gist:{
        url:'https://api.github.com/gists/a0a7db06eb449c231937504a565ce06b',
        Authorization: 'Basic U3VyeWFrYWxhLkJvdHRhQGNvZ25pemFudC5jb206U3VyeWExMjM=',
        method:'GET',
        useragent: 'node.js',
        Accept: 'application/json'

    }
};
module.exports = configuration;