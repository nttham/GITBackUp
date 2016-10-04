# Node JS template application for Authentication

This is a sample application where the developer can start building the application using the Authentication microservice. This application provides a sample user interface, implementation of the login and the data hooks configured in the UI. The developer can modify the UI and add the required business functionality and can push and restage the application into target PaaS platform.  The Authentication microservice expose API end points methods where the developer can login, logout, send and validate the OTP using Twilio or Sendgrid

### Template application files

File | Description
:-- | :-- 
src/app.js | This file does all the bootstrapping by require-ing all the controllers, models and middlewares. Configures the port and domain, starts the application on the available or given port.
views/ | Contains the jade view files for different scenarios.
package.json | All npm packages contain a file, this file holds various metadata relevant to the project.


### Application configuration

- Find the required credentials of the AuthService from the VCAP_SERVICES. The credentials will contain the following details

      ```
       serviceUrl: Base URL for authentication service.
       *provider*_callback: This URL is the provider callback URL. Configure this URL as redirect URI in the provider app configuration. Keys will be in the form of facebook_callback, google_callback, linkedin_callback, twitter_callback
       apiKey: This Secret key is required for consuming the authentication services APIs. Do not share this key, keep it confidential
      ```

### Application execution

- app.js provides the sample routes calling the services of Authentication service.

      ```
       1. '/login' to show the provider options to login.
       2. '/authenticate' Initiates the call to Authentication service based on a selected provider login.
       3. '/callback/:token' Authentication service redirects back to the application with accessToken as path parameter. Later it is rendered to show some profile information.
       4. '/ladp/authenticate' Authenticates the user via LDAP

      ```

- After all configuration is done, start the application and try 'YOUR_DOMAIN_URL' or 'YOUR_DOMAIN_URL'/login in the browser. For eg: http://localhost:3000/login or http://localhost:3000/


## OAuth Authentication Service API

## POST /auth/facebook, /auth/google, /auth/twitter, /auth/linkedin, /auth/custom
- These APIs will initiate the authentication to providers. This is the first API to be consumed for authentication.
- This API requires apiKey as headers. In return if apiKey is valid then a token will be sent along with next call details.
- If any prehooks are available then response json will have details of nextCall, token and channelProvider with statusCode as 303
- If no prehooks are available then response json will have the details of nextCall with status code as 302

### Request

| Headers  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  | apiKey which will be obtained from the VCAPS of the bounded developer application    |

### Response
- With prehooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/generateOtp","channelprovider":"sendgrid","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |


- Without prehooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |302 |
|body   | {"nextCall":"/facebook/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |



## GET /{provider}/:token?callbackUrl={DEV_REDIRECTION_URL}

### APIs - These are redirection API calls
- /facebook/:token?callbackUrl={DEV_REDIRECTION_URL}
- /google/:token?callbackUrl={DEV_REDIRECTION_URL}
- /twitter/:token?callbackUrl={DEV_REDIRECTION_URL}
- /linkedin/:token?callbackUrl={DEV_REDIRECTION_URL}
- /custom/:token?callbackUrl={DEV_REDIRECTION_URL}

### Description
- Authenticates the user against requested provider and redirects back to application with a path parameter as token.
- Developer application has to redirect to any of the above APIs with token as path parameter and query parameter as callbackUrl(to where microservice has to redirect back)
- Token information needs to be retrieved from prior call (POST /auth/facebook, /auth/google, /auth/twitter, /auth/linkedin,/auth/custom)

- Developer application should follow the below steps to implement the callback route

      ```
       1. While calling API callbackUrl should have only the route without path parameter. But while defining the route in the developer application it should have path parameter as a token.

       eg. Passing a callbackUrl as http://localhost:3000/callback should have route as '/callback/:token'
      ```

- The token which is coming in the path parameter should be retained and given to the next call that is /auth/complete

### Request

| Query Param  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| callbackUrl  | callbackUrl to where Authentication service needs to redirect after authentication    |

### Response

| HTTP           |      Value                           |
|----------------|--------------------------------------|
| path parameter   | eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}   |



## POST /auth/complete
- This API should be called after authentication to a provider and it is redirected back to developer application.
- The token which is retained from the prior call should be passed as 'token' and  also 'apiKey' in headers
- In response if any post hooks are available then response json will have details of nextCall, token and channelProvider with statusCode as 303
- If no prehooks are available then response json will have the details of the loggedin user with status code as 200. This response will have accessToken, id, displayName and provider details. For twitter refreshToken will be available.

### Request

| Headers  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  | apiKey which will be obtained from the VCAPS of the bounded developer application    |
| token  | The token which is retained from the prior call    |

### Response

- With posthooks available

| HTTP           |Value|
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/generateOtp","channelprovider":"sendgrid","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |


- Without posthooks available

| HTTP           |Value|
|----------------|--------------------------------------|
|   status code |200 |
| Body       | {"accessToken":"Rcp4xZC06HnmcFMaW866wS9KSK4bCjxBQB2UTPfVbOJH","id":"607861336036","displayName":"John Doe","provider":"facebook"}|


*Note: refreshToken will be available  for /twitter  or /custom call. Not applicable for other providers


## OTP related APIs

## POST /generateOtp
- This API generates an OTP with the given combination(numeric,alpha or both) and sends the OTP to desired receipient based on the given channel and receipient details.
- If the channelProvider is sendgrid then this API requires request body parameters as {"toRecipient":"someemail@gmail.com","fromMail": "someemail@gmail.com"}
- If the channelProvider is twilio then this API requires request body parameters as {"toRecipient":"+765XXXX","fromNo": "+91XXXXXX"}

- The response json will have details of nextCall and token with statusCode as 303

### Request

- channelProvider as Sendgrid

| HTTP       | Value |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| token      | The token which is retained from the prior call should be passed as header |
| Body       | {"toRecipient":"someemail@gmail.com","fromMail": "someemail@gmail.com"} |


- channelProvider as Twilio

| HTTP       | Value|
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| token      | The token which is retained from the prior call should be passed as header |
| Body       | {"toRecipient":"+765XXXX","fromNo": "+91XXXXXX"} |

### Response

| HTTP       |  Value|
|------------|--------------------------------------------------------------------------------------------|
|body   | {"nextCall":"/validateOtp","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |

* Note : Default values for otpLength, otpType and otpExpiryTime are 4, numeric and 15(mins) respectively. otpType can be of numeric or alpha or alphanumeric



## POST /validateOtp
- This API validates the OTP
- In the request you have to pass otpCode in body with the token in the header parameter
- If any further hooks are available then response json will have details of nextCall, token and channelProvider with statusCode as 303

- For OAuth related if there are no further prehooks then response json will have the details of nextCall with status code as 302 normally it will be the redirect url to the providers.
- For OAuth related if no further posthooks are available then response json will have the details of the loggedin user with status code as 200. This response will have accessToken, id, displayName and provider details. For twitter refreshToken will be available.

- For LDAP related if there are no further prehooks available then response json will have details of nextCall as /ldap with status 303
- For LDAP related if there are no further posthooks available then response json will have the message and authFlag(boolean) with status code as 200

### Request

| HTTP       |Value   |
|------------|--------------------------------------------------------------------------------------------|
| Body       | {"otpCode":"79653"}        |
| token      | The token which is retained from the prior call should be passed as header |


### Response
- If further prehooks or posthooks are available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/generateOtp","channelprovider":"sendgrid","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |


- If no further posthooks are available

- For OAuth related with no further posthooks

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |200 |
| Body       | {"accessToken":"Rcp4xZC06HnmcFMaW866wS9KSK4bCjxBQB2UTPfVbOJH","id":"607861336036","displayName":"John Doe","provider":"facebook"}|

- For LDAP related with no further posthooks

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |200 |
|body   | {"message":"authentication successful","authFlag":true} |


- Without further prehooks available

- For OAuth related with no further prehooks

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |302 |
|body   | {"nextCall":"/facebook/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |


- For LDAP related with no further prehooks

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/ldap","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |



## LDAP related APIs

## POST /auth/ldap
- This API will initiate the authentication to configured LDAP server. This is the first API to be consumed for LDAP authentication.
- This API requires apiKey as headers. In return if apiKey is valid then a token will be sent along with next call details.
- If any prehooks are available then response json will have details of nextCall, token and channelProvider with statusCode as 303
- If no prehooks are available then response json will have the details of nextCall and token with status code as 303

- token received in response should be used in headers for nextCall

### Request

| Headers  |                  Description                                                          |
|--------------|---------------------------------------------------------------------------------------|
| apiKey  | apiKey which will be obtained from the VCAPS of the bounded developer application    |


### Response
- With prehooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/generateOtp","channelprovider":"sendgrid","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |

- Without prehooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/ldap","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |


## POST /ldap
- Authenticates the user against configured LDAP server.
- Request headers for this API are token(received in prior call /auth/ldap API), dn and password
- If any posthooks are available then response json will have details of nextCall, token and channelProvider with statusCode as 303
- If no posthooks are available then response json will have the message and authFlag(boolean) with status code as 200

### Request

| Headers  |                  Description                  |
|--------------|-------------------------------------------|
| token  | token received in prior call /auth/ldap API   |
| dn | LDAP distinguished name for user |
| password | password of the user |

      ```
       - Examples for dn
             1. cn=foo, ou=people, o=example (passing the whole)
             2. foo (passing just username)

       - dn differs from LDAP to LDAP configurations. Sometimes it requires just username/alias and sometimes it requires whole dn.
      ```

### Response
- With posthooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |303 |
|body   | {"nextCall":"/generateOtp","channelprovider":"sendgrid","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjoiNjI2NmJmYTJmYWFmNTkyYTc4YzFlMDFmZjFhZjI3YzgwNGQ0ZWJjNjU4ZmNiNWY0NzEyZmRlZDU3ZjhhMjBiMjA5YjMwYzE5ZGQ3YjA3Y2FmMTI0NGVlNTcyYWY4NmRjNmJlYmY2MTYxZWYwNWUyZWQ0MDNjN2ZhNmZiMWRhOWRjZmFlMTQzNDgxNzIyYzM4MzdjODc4MWJiZTJlMTg4ZmNjMzZjNjUyNDE5YTk4MTZlNmQyYThjZDVhZjNlOGYxM2IwMTk3Njc4ZTI1N2QwYTBhNmI0MjExMGM1ZjZmOTExYzkzNmEzMzNhZjFhNGY0YzBhZTcxMDJlZDQ1ODY4M2M0ZmM5OWRlOGJmYzE0NTQ3MjEzYTRhMGIwOTMzZGVjNjQ4NmY5NzFlYzY1NGEyMmY4M2RjMzY5M2IwNmQ5YmRjZDc5NjU3OWZhNzFjY2I0ZjFkZjA3ZmQzYWEwOTUyZGYzY2QxNzdhNGM5NTg3ODI3ODdhMmY1ZDI2OTk0NzJlYzE0YmVkZDViYmFkMGVlNzJkYTNhZjQ1NzQ3MGY2YWQ4M2UxNmIzYmFjODY0Y2RhM2YxYTM1OWY5ZWVlZGE1NWY5NmFmNjI4ODg4YTNmZTEyZWRlZGFkMDZhZGJkZjdmIiwiaWF0IjoxNDcwMzA1MzE5fQ.cvNZ2OWO78zTTItlUfl8V-XujosiP4BTzW_Ug-wQF3g"}  |

- Without posthooks available

| HTTP           |      Value                           |
|----------------|--------------------------------------|
|   status code |200 |
|body   | {"message":"authentication successful","authFlag":true} |
