'use strict'

var brokerRoutes = require('./BrokerRoutes.js');

class AuthServiceBroker extends brokerRoutes {
  constructor() {
    super('auth-service-broker');
  }
}

console.log('****came here***');

const authServiceBroker = new AuthServiceBroker();
