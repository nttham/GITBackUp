# Logger Micro Service.

 This micro service is designed to help developers for storing the application logs to the selected provider.
 Currently the logger microservice supports mongodb, graylog, splunk providers. Incase of mondodb, microservice providing two options for connection.
         a) External mondodb, here enduser should provide the server and database details for initiating the connection.
         b) For existing mongodb instance in the target platform, here enduser has to provide service_plan_guid as input for initiating the connection.


## Prerequisites
#### For MongoDB

| Key        |            Value                      |
|------------|---------------------------------------|
| username   | mongodb username
| password   | mongodb password                   |
| host       | mongo server host                   |
| password   | mongo server port                   |
| database   | mongo database name                 |


#### For GrayLog, for sending messages in GELF (Graylog Extended Log Format).

| Key        |            Value                      |
|------------|---------------------------------------|
| host       | graylog server host                 |
| password   | graylog server port                 |
| protocol   | graylog protocol                    |


#### For Splunk

| Key        |            Value                      |
|------------|---------------------------------------|
| username   | mongodb username
| password   | mongodb password                    |
| host       | mongo server host                   |
| password   | mongo server port                   |


## Logger API Endpoints

#### - POST /save
- This API retrieves the storage status of the logged in messages.

#### Request
| HTTP       |                             Value                                           |
|------------|-----------------------------------------------------------------------------|
| Body       | {"level":"info","message":"Info test message", "appid":"mydemoapp","priority":"Critical"} |
| Header     | {"apikey":"YOUR_API_KEY_FROM_VCAP", "Content-Type":"application/json"}                     |

#### Response
| HTTP       |  Value                                                               |
|------------|----------------------------------------------------------------------|
| Body       | {"status": "Success", "message": "Message saved successfully"}       |




#### - POST /searchLog
- This API used to search logs from MongoDB, based on the user provided filters.

#### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"level":"INFO", "appid":"OneC", "priority":"low"} |
| Header     | {"apikey":"YOUR_API_KEY_FROM_VCAP", "Content-Type":"application/json"}                     |

#### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | [{"_id": "57764a04d5613aa7a0dd8b31", "datetime": "2016-07-01T10:46:28.000Z", "priority": "LOW", "level": "INFO", "msg": "Info message", "appid": "ONEC", "__v": 0}] |
