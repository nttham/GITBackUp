/**
 * Created by 423919 on 6/17/2016.
 */
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
var expect = require('chai').expect;
var request = require('supertest');
var server = require('../src/app.js');
chai.use(chaiHttp);
var otpCode, otpKey;
var index = require('../src/hooks/sendgridservice.js');
var twilio = require('../src/hooks/twilioservice.js');

/**
 * Test Suites
 */
describe('Web Api Test Cases', function () {
    // Start the server before the test case with delay of 1second to instantiate the routers

    before(function (done) {
        this.request = chai.request(server);
        setTimeout(function () {
            done();
        }, 1000);
    });
    describe('Method generate web api', function () {
        it('should be able to Generate web api', function (done) {
            this.request.post("webapi/generate")
                .send()
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .set('token','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNmI1ZGEyYjQ3NmE4MWNlOWRjMDJlYmU2NDRlOWUxMGJkMjBiNzQ5MzU1YWM3NTMwM2M0OTZiOGNhZDliMWJkMjEyMGY4M2FhNGNiOWRjMGU5NGZhZmU0MDEzYjkxZDhjNzkxMGQ2ZTAxNWNhYWE5Zjk4YzZiNTQwMzY3YzBjYTlhMTUxMzhhMjEwZDY2Yjg0NTYxZjNmM2U0MTE1OTM3MWVjMDA1NmFkZDBkNTRkNTUwZTFhNDgwZDk1ZDEyOTY5NDhlZWUxZjZlZjJkYjQyYjMyYmExOWYxMjI5MDgyZjcxZWE3ZmQyMDAwMzI4ZjZiNGZlODBkYTc3MmZmMGM0MGY3YTBiMjFiOGMxNDM1YmMzMGIyODYwMzY4ZGY3YjM1YmIyZmVkNTY0ZTJhZWE0MjZkNjZhOGJlODU5ZTgyZmZmOWM2OTMxN2EyZTJmNGU2MzRlZWFkMDlkMDkxMWMzYjA5OTIzMjZkZjY0ZWFhMDYyMjA3NzA0ZDllMTBhMjNiNjE2MWRhMWNlYzgzMWJhYWZjYmEyMTQxNjE4OWNmY2U0MGQ5M2RlZjRlOGI4MGYxNDVkODFjNDk2MjkwNGIxODM5NzVmMTJmNTk4ZTU3NzRhYzNhMDFkY2YwMmE0NDY5NGFlNmZjMGJhZDI2NTMxZmQ5MGI3NDg5YWEzZjdjMWU3ZTdjZmRjNzU4NTUwOWM0NDU4NWNmZDgzMDU5M2I3OWM1YjVjNmM5MGYxMDE5ODc5YzMxZTNhZDA4Nzk4ZGExNDQ3MDkwNTMwZDY2MzFjZWFhNWUwZjQxOTYzNWJmYTI3YTI2ODA0NDg4YjhlYjZjOWU5MzI0NDc2ZmZmYTIzYTg1NTEzMTJhNzk5ZjExOTZmNzlkODg4MzEzYzFjZDU4IiwiaWF0IjoxNDc0NDU0NTE0fQ.HD-GvQ1mXsWmb_hFVXJuMnPMe6jwb5V5or-wCdmAXPw')
                .end(function (err, res) {
                    console.log(res);

                    done();
                });


        });
    });
    describe('Method generate web api', function () {
        it('should be able to validate web api', function (done) {
            this.request.post("webapi/validate")
                .send()
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json')
                .set('token','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNmI1ZGEyYjQ3NmE4MWNlOWRjMDJlYmU2NDRlOWUxMGJkMjBiNzQ5MzU1YWM3NTMwM2M0OTZiOGNhZDliMWJkMjEyMGY4M2FhNGNiOWRjMGU5NGZhZmU0MDEzYjkxZDhjNzkxMGQ2ZTAxNWNhYWE5Zjk4YzZiNTQwMzY3YzBjYTlhMTUxMzhhMjEwZDY2Yjg0NTYxZjNmM2U0MTE1OTM3MWVjMDA1NmFkZDBkNTRkNTUwZTFhNDgwZDk1ZDEyOTY5NDhlZWUxZjZlZjJkYjQyYjMyYmExOWYxMjI5MDgyZjcxZWE3ZmQyMDAwMzI4ZjZiNGZlODBkYTc3MmZmMGM0MGY3YTBiMjFiOGMxNDM1YmMzMGIyODYwMzY4ZGY3YjM1YmIyZmVkNTY0ZTJhZWE0MjZkNjZhOGJlODU5ZTgyZmZmOWM2OTMxN2EyZTJmNGU2MzRlZWFkMDlkMDkxMWMzYjA5OTIzMjZkZjY0ZWFhMDYyMjA3NzA0ZDllMTBhMjNiNjE2MWRhMWNlYzgzMWJhYWZjYmEyMTQxNjE4OWNmY2U0MGQ5M2RlZjRlOGI4MGYxNDVkODFjNDk2MjkwNGIxODM5NzVmMTJmNTk4ZTU3NzRhYzNhMDFkY2YwMmE0NDY5NGFlNmZjMGJhZDI2NTMxZmQ5MGI3NDg5YWEzZjdjMWU3ZTdjZmRjNzU4NTUwOWM0NDU4NWNmZDgzMDU5M2I3OWM1YjVjNmM5MGYxMDE5ODc5YzMxZTNhZDA4Nzk4ZGExNDQ3MDkwNTMwZDY2MzFjZWFhNWUwZjQxOTYzNWJmYTI3YTI2ODA0NDg4YjhlYjZjOWU5MzI0NDc2ZmZmYTIzYTg1NTEzMTJhNzk5ZjExOTZmNzlkODg4MzEzYzFjZDU4IiwiaWF0IjoxNDc0NDU0NTE0fQ.HD-GvQ1mXsWmb_hFVXJuMnPMe6jwb5V5or-wCdmAXPw')
                .end(function (err, res) {
                    console.log(res);

                    done();
                });


        });
    });


});







