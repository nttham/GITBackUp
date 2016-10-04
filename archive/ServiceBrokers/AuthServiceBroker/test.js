/**
 * Created by 423919 on 6/17/2016.
 */
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
var expect = require('chai').expect;
//var request = require('supertest');
var server = require('./auth-service-broker.js');
chai.use(chaiHttp);

/**
 * Test Suites
 */



describe('AuthService Broker Listening on 8080', function () {
    // Start the server before the test case with delay of 1second to instantiate the routers
    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });


    describe('get catalog', function () {
        it('should be able to get the catalog of the service broker', function (done) {
            this.timeout(15000);
            this.request.get('/v2/catalog')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');

                    done();
                });
        });
    });

    describe('create-service instance put method', function () {
        it('should be able to provision the service instance ', function (done) {
            this.timeout(15000);
            var inputObj = {
                "organization_guid": "0f87ea0f-b95f-475d-b361-96dfd212add6",
                "plan_id": "0f87ea0f-b95f-475d-b361-96dfd212add6",
                "service_id": "0f87ea0f-b95f-475d-b361-96dfd212add6",
                "space_guid": "b169a527-a10a-4a84-a45a-2909fee6b1d9",
                "parameters": {
                    "endpoint": "https://api.54.208.194.189.xip.io",
                    "appname": "mochaApp9",
                    "space_guid": "b169a527-a10a-4a84-a45a-2909fee6b1d9",
                    "domain_guid": "56f6da1f-eed3-42fb-a629-b28101069137",
                    "host": "54.208.194.189.xip.io",
                    "environment_json": {
                        "facebook_clientID": "478519535677977",
                        "facebook_clientSecret": "a9bc36abea045066cd4be131e278ff80",
                        "google_clientID": "625227390094-m47bnlnuaguvq3phn5t5kmp503fsiagd.apps.googleusercontent.com",
                        "google_clientSecret": "k0vpP0Tp5dP2oqXmcF9v10G8",
                        "twitter_clientID": "6F5cvS3hPsh4QIVfS6PsJ10uz",
                        "twitter_clientSecret": "DHDIojA6kdzi77nH3eLXmh3fvHN68AAL0zSxC11yh0N2huBFr1",
                        "linkedin_clientID": "75shq40xwna1yl",
                        "linkedin_clientSecret": "kUomrXzdex8PfY3e"
                    },
                    "token": {
                        "access_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI5NmFhODIxNDQ5MjI0MjI4OTBhYWIzMmU5N2NlYjRhMCIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NjQwODU3MSwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY2NDA4NTcxLCJleHAiOjE0NjY0MDkxNzEsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.u-nrujMLfGb00sdU7sqFHiGTX6AJs_QSYS1bT5sKPWehrIHS3YLLzsdH3gshfo-PkDiRJDph8C0TDHsNVyiWntfag8GV2Th5VC3vMc4P6tY31MIDYqFaa0LAdHqLt08Xx51GeQ4g4oXg9T3HMVeY2W1fawHL9hSJfntH00ibuI4",
                        "token_type": "bearer",
                        "refresh_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI5NmFhODIxNDQ5MjI0MjI4OTBhYWIzMmU5N2NlYjRhMC1yIiwic3ViIjoiYzc0NDhkZmYtZGZmOS00YmZkLTkyNjEtN2IyYjZiMTY4ZWUzIiwic2NvcGUiOlsib3BlbmlkIiwic2NpbS5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5hZG1pbiIsInVhYS51c2VyIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzLnJlYWQiLCJjbG91ZF9jb250cm9sbGVyLnJlYWQiLCJwYXNzd29yZC53cml0ZSIsImNsb3VkX2NvbnRyb2xsZXIud3JpdGUiLCJkb3BwbGVyLmZpcmVob3NlIiwic2NpbS53cml0ZSJdLCJpYXQiOjE0NjY0MDg1NzEsImV4cCI6MTQ2OTAwMDU3MSwiY2lkIjoiY2YiLCJjbGllbnRfaWQiOiJjZiIsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfbmFtZSI6ImFkbWluIiwib3JpZ2luIjoidWFhIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInJldl9zaWciOiJlZWYwMzQ4YyIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.L8U_ycDBON3lYf_mOoolMwsth0nJBMMhBm0mNIJRJQ4pWP8Y1bNkBDgD67x-dOzbTAUQHzmZdab2sHzLAJhEnlqvHydAGiJ-g7CBlJlfJyg9aBL9aXUVcbVocAYwCgnGUrnqNcLECLCKC06zHMOc1lylgkL_yrZHNpRWJIsCloE",
                        "expires_in": 599,
                        "scope": "openid scim.read cloud_controller.admin uaa.user routing.router_groups.read cloud_controller.read password.write cloud_controller.write doppler.firehose scim.write",
                        "jti": "96aa82144922422890aab32e97ceb4a0"
                    }
                }
            };
            this.request.put("/v2/service_instances/a9bc36abea045066cd4be131e278ff80")
                .send(inputObj)
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    var out = JSON.parse(res.text);
                    out.should.have.property('description');
                    done();
                });


        });
    });

    describe('create-bind-service instance put method', function () {
        it('should be able to bind the service instance ', function (done) {
            this.timeout(15000);
            var inputObj = {
                "parameters": {
                    "appname": "mochaApp9",
                    "host": "54.208.194.189.xip.io",
                    "facebook": true,
                    "google": true,
                    "twitter": true,
                    "linked": true
                }
            };
            this.request.put("/v2/service_instances/6F5cvS3hPsh4QIVfS6PsJ10uz/service_bindings/kUomrXzdex8PfY3e")

                .send(inputObj)
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .end(function (err, res) {
                    res.should.have.status(201);
                    res.should.have.property('text');
                    var out = JSON.parse(res.text);
                    out.should.have.property('credentials');
                    done();
                });


        });
    });


    describe('create-unbind-service instance DELETE method', function () {
        it('should be able to unbind the service instance ', function (done) {
            this.timeout(15000);
            var inputObj = {
                "parameters": {
                    service_id: "6F5cvS3hPsh4QIVfS6PsJ10uz",
                    plan_id: "kUomrXzdex8PfY3e"
                }
            };
            this.request.delete("/v2/service_instances/6F5cvS3hPsh4QIVfS6PsJ10uz/service_bindings/kUomrXzdex8PfY3e")

                .send(inputObj)
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .end(function (err, res) {
                    res.should.have.status(200);
                    done();
                });


        });
    });

    describe('delete-service instance DELETE method', function () {
        it('should be able to delete the service instance ', function (done) {
            this.timeout(15000);
            var inputObj = {
                "parameters": {
                    service_id: "6F5cvS3hPsh4QIVfS6PsJ10uz",
                    plan_id: "kUomrXzdex8PfY3e",
                    accepts_incomplete: false
                }
            };
            this.request.delete("/v2/service_instances/6F5cvS3hPsh4QIVfS6PsJ10uz")

                .send(inputObj)
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .end(function (err, res) {
                    res.should.have.status(200);
                    done();
                });


        });
    });

});