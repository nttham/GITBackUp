var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
var expect = require('chai').expect;
var request = require('supertest');
var server = require('../app.js');
chai.use(chaiHttp);


/**
 * Test Suites
 */
describe('Logger Test Cases', function () {
    // Start the server before the test case with delay of 1second to instantiate the routers
    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });

    describe('POST TO Mongodb SERVER', function () {
        it('should be able to POST logs to Mongodb server', function (done)
        {
            this.request.post("/save")
                .send({"level":"INFO","message":"test info message","appid":"mochatests", "priority":"low"})
                .set('Accept', 'application/json')
                .set('datekey','20-09-16')
                .set('hashcode','02d83da9356c5c9084d070232fbb0648142e6708a3ac721ed0b005c0c7c67cc6db443f7a2dbd402f01850108a157b1261dbe4a10eafd284c08622e1f574e0fee')
                .set('nonce','testnonce')
                .end(function (err, res)
                {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    console.log(resBody.message);
                    done();
                });
        });
    });

    describe('POST TO Logentries SERVER', function () {
        it('should be able to POST logs to Logentries server', function (done)
        {
            this.request.post("/save")
                .send({"level":"INFO","message":"test info message","appid":"mochatests", "priority":"low"})
                .set('Accept', 'application/json')
                .set('datekey','20-09-16')
                .set('hashcode','02d83da9356c5c9084d070232fbb0648142e6708a3ac721ed0b005c0c7c67cc6db443f7a2dbd402f01850108a157b1261dbe4a10eafd284c08622e1f574e0fee')
                .set('nonce','testnonce')
                .end(function (err, res)
                {
		           // res.should.have.status(200);
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    console.log(resBody.message);
                    done();
                });
        });
    });

    describe('FILTER/SEARCH FROM Logentries', function () {
        it('Testing search api to logentries', function (done) {
            this.request.get("/searchLog")
                .send({})
                .set('Accept', 'application/json')
                .set('datekey','20-09-16')
                .set('hashcode','d95ac44c2bafce90f6bac52933451b579619b770af5193111b15ad976069a3ec91910626b4654f7d545af42f48bfab01b88868da29f9b6ef827159a9cd524a82')
                .set('nonce','testnonce')
                .end(function (err, res) {
                    var resBody = JSON.parse(res.text);
                    console.log('***********: '+JSON.stringify(resBody));
                    console.log(resBody.status);
                    console.log(resBody.message);
                    done();
                });


        });
    });

});


