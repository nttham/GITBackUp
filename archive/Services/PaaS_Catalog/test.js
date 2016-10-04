var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('./app.js');
var should = chai.should();
chai.use(chaiHttp);

var todoID;

/**
 * Test Suites
 */
describe('PaaSCatalog Test Cases', function () {
    var authToken = 'bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJkYjM0MTExYTE3ODY0NDc2YjM1ZTEzYTI1Mzc3OTEyZSIsInN1YiI6IjJmMTdjZDgyLTNkNzItNDZhMS05OGI0LTI3MDU4ZjlkNWVhYyIsInNjb3BlIjpbImNsb3VkX2NvbnRyb2xsZXIucmVhZCIsInBhc3N3b3JkLndyaXRlIiwiY2xvdWRfY29udHJvbGxlci53cml0ZSIsIm9wZW5pZCIsInVhYS51c2VyIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiIyZjE3Y2Q4Mi0zZDcyLTQ2YTEtOThiNC0yNzA1OGY5ZDVlYWMiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJzcmkiLCJlbWFpbCI6InNyaSIsInJldl9zaWciOiJkM2M1ODgzYSIsImlhdCI6MTQ2NzY5NTY4OCwiZXhwIjoxNDY3Njk2Mjg4LCJpc3MiOiJodHRwczovL3VhYS41NC4yMDguMTk0LjE4OS54aXAuaW8vb2F1dGgvdG9rZW4iLCJ6aWQiOiJ1YWEiLCJhdWQiOlsiY2xvdWRfY29udHJvbGxlciIsInBhc3N3b3JkIiwiY2YiLCJ1YWEiLCJvcGVuaWQiXX0.vSEhjcVsNJ_QVDZl_5Kj1RBU8FdwPFpg46JBKdgIlwN75S7_2mTuKYJymO3BmrBRfVuV13ikaP8yreMqA_JoNOB1x1O5nYiajHKMt12OYB2YJZw5OUEbVErNTXBG9OL_8kvzeUjWUoC1tmCiKd-iMQiwm1cBM_8zmfvNocMqOP4';
    // Start the server before the test case with delay of 1second to instantiate the routers
    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });
    describe('Getting of particular data based on type', function () {
        it('should be able to GET entire data without any problem', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/catalog?type=category')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    res.body.should.have.property('categories');
                    done();
                });
        });
    });
    describe('Getting of particular data based on type', function () {
        it('should be able to GET entire data without any problem', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/catalog?type=hooks')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });
    describe('Getting of particular data based on type', function () {
        it('should be able to GET entire data without any problem', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/catalog?type=services&category=Authentication')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('Method Search', function () {
        it('should able to search data based on user search string', function (done) {
            this.timeout(15000);
            var searchString = 'facebbok';
            this.request.get('/PaaSCatalog/searchCatalog?searchString=facebook&type=category')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    res.body.should.be.an.Array;
                    done();
                });
        });
    });

    describe('Method Get Data', function () {
        it('should be able to get required data without problems', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/platformservices?id=1234567890&platform=cognizantone,bluemix')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.body.should.have.property('keys');
                    done();
                });
        });
    });

    describe('Method Get organizations', function () {
        it('should be able to get organizations in the selected platform', function (done) {
            this.timeout(15000);
            var options = {
                Authorization:authToken,
                api_url:'http://api.54.208.194.189.xip.io'
            };
            this.request.get('/PaaSCatalog/organizations')
                .set(options)
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.body.should.be.an.Array;
                    done();
                });
        });
    });

    describe('Method Get Spaces', function () {
        it('should be able to get spaces in the selected platform', function (done) {
            this.timeout(15000);
            var options = {
                Authorization:authToken,
                api_url:'http://api.54.208.194.189.xip.io'
            };
            this.request.get('/PaaSCatalog/spaces')
                .set(options)
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.body.should.be.an.Array;
                    done();
                });
        });
    });

    describe('Method Get layouts Data', function () {
        it('should be able to get layouts json data without problems', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/layouts')
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.body.should.be.an.Array;
                    done();
                });
        });
    });

    describe('Method Get regions Data', function () {
        it('should be able to get regions json data without problems', function (done) {
            this.timeout(15000);
            this.request.get('/PaaSCatalog/regions?platformID=2004')
                .end(function (err, res) {
                    res.should.have.status(200);
                    done();
                });
        });
    });

    describe('Method to check AppName existence', function () {
        it('should be able to check the appname is existed in the selected platform or not', function (done) {
            this.timeout(15000);
            var options = {
                Authorization:authToken,
                api_url:'http://api.54.208.194.189.xip.io',
                spaceguid:'b169a527-a10a-4a84-a45a-2909fee6b1d9',
                appname:'paascatalog'
            };
            this.request.get('/PaaSCatalog/appNameCheck')
                .set(options)
                .end(function (err, res) {
                    res.should.have.status(200);
                    //res.body.should.be.an.Array;
                    done();
                });
        });
    });
});

