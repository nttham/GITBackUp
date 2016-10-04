# Slack API Wrapper.

### A node app for sending and receiving messages with Slack via webhooks.

    Slack is a messaging platform that is easy to integrate with. This module should be useful for creating various integrations with Slack, such as channel creation!

## Slack Service API

## GET /apitest
Checks API calling code..


### Response
| HTTP           |      Value                           |
|----------------|--------------------------------------|
| body           | {"ok":true}                          |

* Example API call

      ```
       var url = "http://slackservice.54.208.194.189.xip.io"+"/apitest";
      ```

## POST /authtest
This API checks authentication and tells you who you are.

### Request
| HTTP       |                             Value                                           |
|------------|-----------------------------------------------------------------------------|
| Body       | {"token":"xoxp-61766724487-61777461362-6..."}                               |

### Response
| HTTP       |  Value                                                               |
|------------|----------------------------------------------------------------------|
| Body       | {"ok":true,"url":"https:\/\/microservices-hq.slack.com\/","team":"Microservices","user":"suryakala","team_id":"T1TNJMAEB","user_id":"U1TNVDKAN"}|


## POST /channelcreate
- This method is used to create a channel.

### Request
| HTTP       |                             Value                                               |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Body       | {"token": "xoxp-61766724487-61777461362-6...", "name": "testchannel"}           |

### Response
| HTTP       |  Value                                                                                     |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"ok":true,"channel":{"id":"C1VF9UBMF","name":"testchannel","is_channel":true,"created":1469594674,"creator":"U1TNVDKAN","is_archived":false,"is_general":false,"is_member":true,"last_read":"0000000000.000000","latest":null,"unread_count":0,"unread_count_display":0,"members":["U1TNVDKAN"],"topic":{"value":"","creator":"","last_set":0},"purpose":{"value":"","creator":"","last_set":0}}} |


## POST /channelarchive
- This method archives a channel.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token": "xoxp-61766724487-61777461362-6...", "channel": "C1VF9UBMF"}                     |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true}                                 |


## POST /channelunarchive
- This method unarchives a channel. The calling user is added to the channel.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token": "xoxp-61766724487-61777461362-6...", "channel": "C1VF9UBMF"}                     |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true}                                 |

## POST /channelinvite
- This method is used to invite a user to a channel. The calling user must be a member of the channel.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token": "xoxp-61766724487-61777461362-6...", "channel": "C1VF9UBMF", "user":"U1TNVDKAN"} |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true,"channel":{"id":"C1VF9UBMF","name":"fun","created":1360782804,"creator":"U1TNVDKAN","is_archived":false,"is_member":true,"is_general":false,"last_read":"1401383885.000061","latest":{…},"unread_count":0,"unread_count_display":0,"members":[…],"topic":{"value":"Fun times","creator":"U024BE7LV","last_set":1369677212},"purpose":{"value":"This channel is for fun","creator":"U024BE7LH","last_set":1360782804}}}|

## POST /channeljoin
- This method is used to join a channel. If the channel does not exist, it is created.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token": "xoxp-61766724487-61777461362-6...", "name": "C1VF9UBMF"} |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true,"channel":{"id":"C024BE91L","name":"fun","created":1360782804,"creator":"U024BE7LH","is_archived":false,"is_member":true,"is_general":false,"last_read":"1401383885.000061","latest":{…},"unread_count":0,"unread_count_display":0,"members":[…],"topic":{"value":"Fun times","creator":"U024BE7LV","last_set":1369677212},"purpose":{"value":"This channel is for fun","creator":"U024BE7LH","last_set":1360782804}}} |

## POST /postMessage
- This method posts a message to a public channel, private channel, or direct message/IM channel.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token":"token","channel":"channel","text":"text","parse":"parse","link_names":"link_names","attachments":"attachments","unfurl_links":"unfurl_links","unfurl_media":"unfurl_media","username":"username","as_user":"as_user","icon_url":"icon_url","icon_emoji":"icon_emoji"}                               |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true,"channel":"C1V62TPBK","ts":"1469595616.304858","message":{"text":"suryatestmsg","username":"Slack API Tester","bot_id":"B1TNTUMPY","type":"message","subtype":"bot_message","ts":"1469595616.304858"}} |

## POST /searchMessage
- This method returns messages matching a search query.

### Request
| HTTP       |                             Value                                                          |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"token":"token","query":"query","sort":"sort","sort_dir":"sort_dir","highlight":"highlight","count":"count","page":"page"} |

### Response
| HTTP       |  Value                                      |
|------------|---------------------------------------------|
| Body       | {"ok":true,"query":"surya","messages":{"total":1,"pagination":{"total_count":1,"page":1,"per_page":20,"page_count":1,"first":1,"last":1},"paging":{"count":20,"total":1,"page":1,"pages":1},"matches":[{"type":"message","user":"","username":"slack api tester","ts":"1469085906.000003","team":"T1TNJMAEB","channel":{"id":"C1TMG214K","name":"notification","is_shared":false,"is_org_shared":false},"permalink":"https:\/\/microservices-hq.slack.com\/archives\/notification\/p1469085906000003","text":"hi surya"}]}}                                            |


