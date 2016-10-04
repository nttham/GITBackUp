var index = require('../sendgridservice.js');
var expect = require('chai').expect;

describe("check whether sendgridservice is present or not", function () {
    it("should exist", function () {
        expect(index).to.not.be.undefined;
    });
});

describe("check Email Functionality", function () {
    var sendgridservice = new index();
    var options = {
        "accountSID": "r8skU2912a",
        "accountSID": "r8skU2912a",
        "authToken": "BPRV4rL9N7jM9272",
        "toRecipient": "neethu0746@gmail.com",
        "fromMail": "uppugandlasri1987@gmail.com",
        "subject": "Hello",
        "text": "Testing Sendgrid"
    };
    it("should exist", function (done) {

        sendgridservice.sendMail(options, function (err, result) {
            expect(err).to.be.a('null');
            done();


        });
    });

});
