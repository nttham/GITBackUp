# SendGrid
This module is used to send mails using sendgrid api 

## Main Function
    sendMail()
## Sample

```javascript

sendgrid.sendMail({
    "accountSID": "r8skU2912a",
    "authToken":"BPRV4rL9N7jM9272",
    "toRecipient": 'example@example.com',
    "fromMail": 'other@example.com',
    "subject": "Hello World"
    "text": "My first email ."
    }, function(err, json) {
    if (err) { return console.error(err); }
    console.log(json);
    });
```
User has to provide the AccountSID and the auth Token which is needed to connect with 
the send grid. 


Applications can enable the hooks in the developer application by downloading the source code template from here; 
inject the code in the application to call the send grid in an orderly fashion. It can be made as a prehook or posthook 
Prehooks will be called before an event, onhook will be called during the event and post hook will be called post 
the event. The sequence will be called on successful response from the previous event. If there are any errors,
the subsequent hooks will not be called.

