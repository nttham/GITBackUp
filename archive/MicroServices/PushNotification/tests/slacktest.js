var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
var expect = require('chai').expect;
var request = require('supertest');
var server = require('../src/app.js');
chai.use(chaiHttp);

var SLACK_TOKEN = "xoxp-61766724487-61777461362-61794962354-537651351b";
var channelName = "TestChannel5";
var channelId;
var userId = 'U1TNVDKAN';

/**
 * Test Suites
 */
describe('Slack Test Cases', function () {
    // Start the server before the test case with delay of 1second to instantiate the routers
    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });
    describe('APITest', function () {
        it('should be able to get response from APTTest service', function (done)
        {
            this.request.get("/apitest")
                .set('Accept', 'application/json')
                .end(function (err, res)
                {
		           // res.should.have.status(200);
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });
        });
    });

    describe('AuthTest', function () {
        it('should be able to check auth test', function (done) {
            this.request.post("/authtest")
                .send({"token":SLACK_TOKEN})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });

    describe('CREATE CHANNEL', function () {
        it('should be able to create channel', function (done) {
            this.request.post("/channelcreate")
                .send({"token":SLACK_TOKEN,"name":channelName})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.channel.id);
                    channelId = resBody.channel.id
                    done();
                });


        });
    });

    describe('ARCHIVE CHANNEL', function () {
        it('should be able to Archive existing channel', function (done) {
            this.request.post("/channelarchive")
                .send({"token":SLACK_TOKEN,"channel":channelId})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });

    describe('UNARCHIVE CHANNEL', function () {
        it('should be able to unarchive the archived channel', function (done) {
            this.request.post("/channelunarchive")
                .send({"token":SLACK_TOKEN,"channel":channelId})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });


    describe('INVITE Channel', function () {
        it('should be able to inviting to a channel', function (done) {
            this.request.post("/channelinvite")
                .send({"token":SLACK_TOKEN,"channel":channelId,"user":userId})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });

    describe('JOIN Channel', function () {
        it('should be able to join to a channel', function (done) {
            this.request.post("/channeljoin")
                .send({"token":SLACK_TOKEN,"name":userId})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });

    describe('POST Message', function () {
        it('should be able to post message to a channel', function (done) {
            this.request.post("/postMessage")
                .send({"token":SLACK_TOKEN,"channel":channelId,"text":'Test message from mocha'})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });


    describe('SEARCH MESSAGE', function () {
        it('should be able to search messages posted to a channel', function (done) {
            this.request.post("/channeljoin")
                .send({"token":SLACK_TOKEN,"query":'mocha'})
                .set('Accept', 'application/json')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    done();
                });


        });
    });
});


