# PushNotification Micro Service.

### This micro service offers the required APIs for PushNotifications. APIs include push notifications for iOS, android, windows, slack and channels.

## Keywords

- deviceId       : The deviceId is the unique ID for the device for the application
- userId         : The userId is the unique user ID for the user for the application
- deviceToken    : deviceToken refers to registrationId in Android, device-token in iOS and channelUrl in windows. This token is retrieved after registering against GCM/iOS/WNS from device.
- platform       : Platform refers to GCM(for Android), APNS(for iOS) and WNS(for Windows)
- createdMode    : This is for identifying through where registration of device is done. This is a string value and can be anything. eg: SDK, API.
- isBlackListed  : This will denote whether a particular device is enabled for push notification
- apikey         : apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests.


## PushNotification Service API

## POST /devices

- Creates a new device registration with the PushNotification service. The device registrations happens from the device. The deviceId is the unique ID for the device for the application.
- apiKey required in headers.
- Body parameters for request are deviceId, userId, deviceToken, platform and createdMode. Mandatory parameters in the request are deviceId, deviceToken and platform.

      ```
       Eg for JSON body
       {
         "deviceId": "7654g67hhgt5433",
         "userId": "testuser001",
         "deviceToken": "765686eab297cc261cad2708553b2e6479824aed824f506219a5c9sdcfsd33485b31d01239997676",
         "platform": "GCM",
         "createdMode": "SDK",
         "isBlackListed" : false
       }
      ```

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| body  | {"deviceId":"7654g67hhgt5433","userId":"testuser001","deviceToken":"765686eab297cc261cad2708553b2e6479824aed824f506219a5c9sdcfsd33485b31d01239997676","platform":"GCM","createdMode":"SDK","isBlackListed" : false}   |

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful device registration  |
| 400   | During failure or update requires for a device  |


## GET /devices

- This api will fetch information of all the devices which is registered to our server.
- apiKey required in headers.

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| body         |      Description                          |
|----------------|--------------------------------------|
| userId   | The userId is the unique user ID for the user for the application  |
| deviceId   | The deviceId is the unique ID for the device for the application |
| platform   | Platform refers to GCM(for Android), APNS(for iOS) and WNS(for Windows)|
| createdTime   | The time at which the device is registered in the server|
| lastUpdatedTime   | The time at which the device registration details is updated in the server|
| createdMode   | This is for identifying through where registration of device is done. This is a string value and can be anything. eg: SDK, API.|
| isBlackListed   | This will denote whether a particular device is enabled for push notification|


      ```
       Eg for response JSON body
       [
         {
           "userId": "Notepad",
           "deviceId": "wnsPhoneTest001",
           "platform": "WNS",
           "createdTime": "2016-08-01T11:16:18.051Z",
           "lastUpdatedTime": "2016-08-01T11:19:18.051Z",
           "createdMode": "API",
           "isBlackListed": false
         },
         {
           "userId": "LGE Nexus 5",
           "deviceId": "358239056043849",
           "platform": "GCM",
           "createdTime": "2016-08-02T06:30:48.837Z",
           "lastUpdatedTime": "2016-08-01T11:19:18.051Z",
           "createdMode": "API",
           "isBlackListed": false
         },
         {
           "userId": "gcm_Device",
           "deviceId": "gcmtest001",
           "platform": "GCM",
           "createdTime": "2016-08-04T13:32:15.618Z",
           "lastUpdatedTime": null,
           "createdMode": "API",
           "isBlackListed": false
         }
       ]
      ```

## PUT /devices/:deviceid

- This api is used to update the registered device details in the server  .
- apiKey required in headers.
- The possible Body parameters are userId,deviceToken and isBlackListed
- In the path parameter you have to pass the deviceID of the device which has to be updated

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| body  | {"userId":"testuser001","deviceToken":"765686eab297cc261cad2708553b2e6479824aed824f506219a5c9sdcfsd33485b31d01239997676","isBlackListed" : false}    |

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |


      ```
       Eg for JSON requestbody
       {
       	"userId"		: "LGE Nexus 5",

       }
      ```

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful device registration  |
| 400   | During failure or update requires for a device  |



## DELETE /devices/:deviceId

- This api is used to delete the registered device details in the server  .
- apiKey required in headers.
- In the path parameter you have to pass the deviceID of the device which has to be deleted

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful device deletion  |
| 400   | During failure of delete operation  |



## POST /notification

- This api is used to send notification for registered users
- This API is capable of sending notification to devices, bulk notification and channel's devices.
- apiKey required in headers

- Body request for type "device"

      ```
       {
       	   "type" : "device",
           "message" : "This is from postman - test1",
           "deviceId" : ["apnstest001"],
           "templateId" : "1",
           "settings" : {
               "apns" : {
                   "payload" : {"sample":"message for APNS"}
               },
               "gcm" : {
                   "data" : {"sample":"message for APNS"}
               },
               "wns" : {
                   "payload" : {
               		"text1": "Hello",
               		"text2": "How are you?"
           		  }
               }
           }
       }

       - In the above JSOn body
           "type" : "device",  --> mandatory
           "message" : "This is from postman - test1", --> mandatory
           "deviceId" : ["apnstest001"], --> mandatory
           "templateId" : "1",  --> optional, if mentioned then it should be a valid ID else it will throw error
           "settings" : { } --> optional, But for wns it is a good practice to mention else template notification will be skipped
      ```


- Body request for type "bulk"

      ```
       {
       	   "type" : "bulk",
           "message" : "This is from postman - test1",
           "bulkOptions" : {
           	"apns" : false,
           	"gcm" : false
           },
           "templateId" : "1",
           "settings" : {
               "apns" : {
                   "payload" : {"sample":"message for APNS"}
               },
               "gcm" : {
                   "data" : {"sample":"message for APNS"}
               },
               "wns" : {
                   "payload" : {
               		"text1": "Hello",
               		"text2": "How are you?"
           		   }
               }
           }
       }

       - In the above JSOn body
           "type" : "bulk",  --> mandatory
           "message" : "This is from postman - test1", --> mandatory
           "bulkOptions" : {"apns" : false,"gcm" : false}, --> optional, by default notification will be sent to all providers. If any provider is false the notification will be skipped for that provider
           "templateId" : "1",  --> optional, if mentioned then it should be a valid ID else it will throw error
           "settings" : { } --> optional, But for wns it is a good practice to mention else template notification will be skipped
      ```



- Body request for type "channel"

      ```
       {
       	   "type" : "channel",
           "message" : "This is from postman - test1",
           "channelName" : "trekking",
           "templateId" : "1",
           "settings" : {
               "apns" : {
                   "payload" : {"sample":"message for APNS"}
               },
               "gcm" : {
                   "data" : {"sample":"message for APNS"}
               },
               "wns" : {
                   "payload" : {
               		"text1": "Hello",
               		"text2": "How are you?"
           		   }
               }
           }
       }

       - In the above JSOn body
           "type" : "channel",  --> mandatory
           "message" : "This is from postman - test1", --> mandatory
           "channelName" : "trekking"  --> mandatory.
           "templateId" : "1",  --> optional, if mentioned then it should be a valid ID else it will throw error
           "settings" : { } --> optional, But for wns it is a good practice to mention else template notification will be skipped
      ```


### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| body  | {"type":"channel","message":"This is from postman - test1","channelName":"trekking","templateId":"1","settings":{"apns":{"payload":{"sample":"message for APNS"}},"gcm":{"data":{"sample":"message for APNS"}},"wns":{"payload":{"text1":"Hello","text2":"How are you?"}}}}   |

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 202   | Accepted to send push notification. Notification will be sent if all settings are available  |
| 404   | If no devices found for sending push notifications  |


## PUT /apnssettings

- This api is used to upload update APNS settings
- p12 certificate of APNS  settings and passPhrase are required parameters as mutipart form data. key name for p12 file should be "certificate"
- apiKey required in headers


### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

| body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| passPhrase  | Password of the P12 certificate |
| certificate   | p12 certificate has to be uploaded as multipart data for APNS settings. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful upload  |
| 400   | During failure of upload operation  |

## PUT /gcmsettings

- apiKey required in headers
- The possible Body parameter to update  is gcmApiKey.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| body  | {"gcmApiKey":"John002"}  |

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful update of settings |
| 400   | During failure  |


## PUT /wnssettings

- apiKey required in headers
- The possible Body parameters are wnsClientId,wnsClientSecret ...

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| body  | {"wnsClientId":"John002","wnsClientSecret":"3625872435643"}  |

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful update of settings |
| 400   | During failure  |


## DELETE /apnssettings

- apiKey required in headers
- All the Settings parameters configured for the device will be deleted.

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful deletion of settings |
| 400   | During failure  |

## DELETE /gcmsettings

- apiKey required in headers
- All the Settings parameters configured for the device will be deleted.

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful deletion of settings |
| 400   | During failure  |


## DELETE /wnssettings

- apiKey required in headers
- All the Settings parameters configured for the device will be deleted.

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | During successful deletion of settings |
| 400   | During failure  |





## POST /createchannel

- This api is used to create a channel
- apiKey & channelName are required in headers
- channeldescription is optional in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |
| channelName  | name of the channel to create. |
| channeldescription  | description of the channel to create. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | Successfully created channel.  |
| 400   | If some error occurs while creating channel.  |


## PUT /updatechannel

- This api is used to update channel description
- apiKey & channeldescription are required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |
| channeldescription  | description of the channel to update. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | Successfully updated channel.  |
| 400   | If some error occurs while updating the channel.  |


## DELETE /deletechannel

- This api is used to delete channel
- apiKey & channelName are required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |
| channelName  | name of the channel to delete. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | Successfully deleted channel.  |
| 400   | If some error occurs while deleting the channel.  |


## PUT /subscribe

- This api is used to subscribe the device to a channel
- apiKey, channelName & deviceID are required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |
| channelName  | name of the channel to subcribe. |
| deviceID  | deviceID for channel subcription. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | Successfully subcribed to a channel.  |
| 400   | If some error occurs while subcribing to channel.  |


## DELETE /unsubscribe

- This api is used to unsubscribe the device from a channel
- apiKey, channelName & deviceID are required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |
| channelName  | name of the channel to unsubcribe. |
| deviceID  | deviceID to unsubscribe from a channel. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | Successfully unsubcribed from a channel.  |
| 400   | If some error occurs while unsubscribing from a channel.  |


## GET /getChannels

- This api is used to get channels info
- apiKey is required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | channels data  |
| 400   | If some error occurs while getting the channels data.  |


## GET /templates

- This api is used to get all registered templates information
- apiKey is required in headers

### Request

| Header  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  |  apiKey can be retrieved from VCAPS of developer bounded application. apiKey needs to be passed in header for all API requests. |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | all registered templates information  |
| 400   | If some error occurs while getting the templates data.  |



## POST /slack/channelcreate

- This api is used to create a channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| name         |	              Name of channel to create                                              |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"channel":{"id":"C1ZUML3UZ","name":"test","is_channel":true,"created":1470812986,"creator":"U1VJD01ND","is_archived":false,"is_general":false,"is_member":true,"last_read":"0000000000.000000","latest":null,"unread_count":0,"unread_count_display":0,"members":["U1VJD01ND"],"topic":{"value":"","creator":"","last_set":0},"purpose":{"value":"","creator":"","last_set":0}}} |
| 200   | {"ok":false,"error":"not_authed"}  |

## POST /slack/channelarchive

- This method archives a channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	             Channel Id to archive                                              |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok": true} |

## POST /slack/channelunarchive

- This method unarchives a channel. The calling user is added to the channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	             Channel Id to unarchive                                              |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok": true} |


## POST /slack/channelinvite

- This method unarchives a channel. The calling user is added to the channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	            Channel Id to invite user to.                                              |
|user|	User Id to invite to channel |

### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"channel":{"id":"C024BE91L","name":"fun","created":1360782804,"creator":"U024BE7LH","is_archived":false,"is_member":true,"is_general":false,"last_read":"1401383885.000061","latest":{…},"unread_count":0,"unread_count_display":0,"members":[…],"topic":{"value":"Fun times","creator":"U024BE7LV","last_set":1369677212},"purpose":{"value":"This channel is for fun","creator":"U024BE7LH","last_set":1360782804}}} |
| 200   | {"ok":false,"error":"channel_not_found"} |
| 200   | {"ok":false,"error":"no_user"} |


## POST /slack/channeljoin

- This method is used to join a channel. If the channel does not exist, it is created.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	             Name of channel to join                                            |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"channel":{"id":"C024BE91L","name":"fun","created":1360782804,"creator":"U024BE7LH","is_archived":false,"is_member":true,"is_general":false,"last_read":"1401383885.000061","latest":{…},"unread_count":0,"unread_count_display":0,"members":[…],"topic":{"value":"Fun times","creator":"U024BE7LV","last_set":1369677212},"purpose":{"value":"This channel is for fun","creator":"U024BE7LH","last_set":1360782804}}} |

- If you are already in the channel, the response is slightly different. already_in_channel will be true, and a limited channel object will be returned. This allows a client to see that the request to join GeNERaL is the same as the channel #general that the user is already in:

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   |{"ok":true,"already_in_channel":true,"channel":{"id":"C024BE91L","name":"fun","created":1360782804,"creator":"U024BE7LH","is_archived":false,"is_general":false}} |


## POST /slack/channelsleave

- This method is used to leave a channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	            Channel to leave                                          |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   |  {"ok": true} |

-This method will not return an error if the user was not in the channel before it was called. Instead the response will include a not_in_channel property:

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   |  {"ok":true,"not_in_channel":true} |


## POST /slack/channelslist

- This method returns a list of all channels in the team. This includes channels the caller is in, channels they are not currently in, and archived channels but does not include private channels. The number of (non-deactivated) members in each channel is also returned.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| exclude_archived         |	           	Don't return archived channels                                     |


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"channels":[{"id":"C024BE91L","name":"fun","created":1360782804,"creator":"U024BE7LH","is_archived":false,"is_member":false,"num_members":6,"topic":{"value":"Fun times","creator":"U024BE7LV","last_set":1369677212},"purpose":{"value":"This channel is for fun","creator":"U024BE7LH","last_set":1360782804}},....]} |

## POST /slack/postMessage

- This method posts a message to a public channel, private channel, or direct message/IM channel.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| channel         |	           Channel, private group, or IM channel to send message to. Can be an encoded ID, or a name  |
|text|Text of the message to send|


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"channel":"C1VFS40AH","ts":"1470814920.000002","message":{"text":"sfdfd","username":"Slack API Tester","bot_id":"B1VGWKYP5","type":"message","subtype":"bot_message","ts":"1470814920.000002"}} |



## POST /slack/searchMessage

- This method returns messages matching a search query.

### Request

| Body  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| token        | 	              Authentication token                                                   |
| query         |	          	Search query. May contains booleans, etc|


### Response

| HTTP status        |      Description                          |
|----------------|--------------------------------------|
| 200   | {"ok":true,"query":"test","messages":{"total":829,"paging":{"count":20,"total":829,"page":1,"pages":42},"matches":[{...},{...},{...}]}} |




