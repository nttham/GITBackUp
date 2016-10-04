'use strict'

var brokerRoutes = require('./BrokerRoutes.js');

class LoggerServiceBroker extends brokerRoutes {
  constructor() {
    super('logger_service_broker');
  }
}

const loggerServiceBroker = new LoggerServiceBroker();
