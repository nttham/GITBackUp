/**
 * Created by Srividya on 18/07/16.
 */
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('./app.js');
var should = chai.should();
chai.use(chaiHttp);

/**
 * Test Suites
 */
describe('Channels Test Cases', function () {
    // Start the server before the test case with delay of 1second to instantiate the routers
    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });

    describe('This api call will create a channel in mongodb', function () {
        it('should be able to create a channel without any problems', function (done) {
            this.timeout(15000);
            this.request.post('/channels/createchannel')
                .set({channelName:'healthcare1234'})
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will delete a channel from mongodb', function () {
        it('should be able to delete a channel without any problems', function (done) {
            this.timeout(15000);

            this.request.delete('/channels/deletechannel')
                .set({channelName:'healthcare1234'})
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will create a channel in mongodb', function () {
        it('should be able to return error is the channel is already exists', function (done) {
            this.timeout(15000);
            this.request.post('/channels/createchannel')
                .set({channelName:'retail'})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will delete a channel from mongodb', function () {
        it('should be able to return error if the channel is not exists to delete', function (done) {
            this.timeout(15000);

            this.request.delete('/channels/deletechannel')
                .set({channelName:'retail44445555'})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will subscribe the user to a channel', function () {
        it('should be able to subscribe to a channel without any problems', function (done) {
            this.timeout(15000);

            this.request.post('/channels/subscribe')
                .set({channelName:'retail',deviceID:123456})
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will unsubscribe the user from a channel', function () {
        it('should be able to unsubscribe from a channel without any problems', function (done) {
            this.timeout(15000);

            this.request.delete('/channels/unsubscribe')
                .set({channelName:'retail',deviceID:123456})
                .end(function (err, res) {
                    res.should.have.status(200);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will subscribe the user to a channel', function () {
        it('should be able to return the error if the device is already subscribed', function (done) {
            this.timeout(15000);

            this.request.post('/channels/subscribe')
                .set({channelName:'retail',deviceID:123456e5})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will subscribe the user to a channel', function () {
        it('should be able to return the error if the channel is not exists', function (done) {
            this.timeout(15000);

            this.request.post('/channels/subscribe')
                .set({channelName:'retail89899',deviceID:123456e5})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will unsubscribe the user from a channel', function () {
        it('should be able to return error if the device is already unsubscribed from the channel', function (done) {
            this.timeout(15000);

            this.request.delete('/channels/unsubscribe')
                .set({channelName:'retail',deviceID:123456})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will subscribe the user to a channel', function () {
        it('should be able to return the error if the channel is not exists', function (done) {
            this.timeout(15000);

            this.request.delete('/channels/unsubscribe')
                .set({channelName:'retail89899',deviceID:123456e5})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });

    describe('This api call will push the notification to a channel', function () {
        it('should be able to push the notification to the devices subscribed for that channel', function (done) {
            this.timeout(15000);

            var body = {
                "channelname":"test",
                "message" : "Helloooooo from srividya test",
                "settings" : {
                    "apns" : {
                        "sound" : "ping.aiff",
                        "badge"	: 3,
                        "payload" : {"sample":"message for APNS"}
                    }
                }
            };

            this.request.post('/channels/pushNotifyToChannel')
                .send(body)
                .end(function (err, res) {
                    res.should.have.status(202);
                    res.should.have.property('text');
                    done();
                });
        });
    });


    describe('This api call will push the notification to a channel', function () {
        it('should be able to return error if the channel is not exists', function (done) {
            this.timeout(15000);

            this.request.post('/channels/pushNotifyToChannel')
                .set({channelName:'BFS2142423'})
                .end(function (err, res) {
                    res.should.have.status(400);
                    res.should.have.property('text');
                    done();
                });
        });
    });
});

