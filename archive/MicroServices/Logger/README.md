# Logger Micro Service.

 This micro service is designed to help developers for storing the application logs to the selected provider.
 Currently the logger microservice supports mongodb, graylog, splunk, logentries providers. Incase of mondodb, microservice providing two options for connection.
         a) External mondodb, here enduser should provide the server and database details for initiating the connection.
         b) For existing mongodb instance in the target platform, here enduser has to provide service_plan_guid as input for initiating the connection.


## Hash Creation
#### Hash has to be created using Algorithm SHA512 with the below details

| Key        |            Value                      |
|------------|---------------------------------------|
| apiKey     | Receive from env variables              |
| nonce      | Randomly generated unique value                  |
| dateKey    | Current Date with DD-MM-YYYY format                   |
| request Body/url   | Based on the request type, if body exists take body content else take url for eg: "/searchLog" should be appended as a string |


## Prerequisites
#### For MongoDB

| Key        |            Value                      |
|------------|---------------------------------------|
| username   | mongodb username
| password   | mongodb password                   |
| host       | mongo server host                   |
| password   | mongo server port                   |
| database   | mongo database name                 |
| collectioname| mongo collection name             |


#### For GrayLog, for sending messages in GELF (Graylog Extended Log Format).

| Key        |            Value                      |
|------------|---------------------------------------|
| host       | graylog server host                 |
| password   | graylog server port                 |
| protocol   | graylog protocol                    |


#### For Splunk

| Key        |            Value                      |
|------------|---------------------------------------|
| username   | Splunk username
| password   | Splunk password                    |
| host       | Splunk server host                   |
| password   | Splunk server port                   |

#### For Logentries

| Key        |            Value                      |
|------------|---------------------------------------|
| token      | user token for logentries account     |


## Logger API Endpoints

#### - POST /save
- This API retrieves the storage status of the logged in messages.

#### Request
| HTTP       |                             Value                                           |
|------------|-----------------------------------------------------------------------------|
| Body       | {"level":"info","message":"Info test message", "appid":"mydemoapp","priority":"Critical"} |
| Header     | {"datekey":"DD-MM-YYYY", "Content-Type":"application/json", "hashcode":"Generated Hash code", "nonce":"generated nonce"}|                     |

#### Response
| HTTP       |  Value                                                               |
|------------|----------------------------------------------------------------------|
| Body       | {"status": "Success", "message": "Message saved successfully"}       |




#### - GET /searchLog
- This API used to search logs from MongoDB, based on the user provided filters.

#### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Header     | {"datekey":"DD-MM-YYYY", "Content-Type":"application/json", "hashcode":"Generated Hash code", "nonce":"generated nonce"}, "appid":"application Identifier", "level":"Log Level", "priority":"log priority"|                     |

#### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | [{"datetime": "2016-07-01T10:46:28.000Z", "priority": "LOW", "level": "INFO", "msg": "Info message", "appid": "ONEC"}] |
