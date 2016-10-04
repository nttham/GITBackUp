# AuthService Service Broker
A  Cloud Foundry Service Broker, complying to version 2.4 of the interface specification. This will serve all the request 
for AuthService services which is coming from the cloud controller. This service broker  is responsible for:
- Implementing a REST server to interface with the Cloud Controller
- Authenticating requests using HTTP basic auth
- Providing an interface to the data service itself for all provision/unprovision & bind/unbind events
- Maintaining a catalog of all available services and associated service plans

## Config
The catalog of services and plans is defined in the file config/auth-service-broker.json.
Hopefully, the configurations are fairly self explanatory. Most importantly, don't forget to change "authUser" and "authPassword" .
If you don't provide the authUser and authPassword , there will not be any security for the broker; and it would be accesible to anyone.

##  Creating AuthService Broker App:

  1.	Clone the Auth-Service Service broker 
  
  ```
    cd  Microservices/ServiceBrokers/AuthServiceBroker/
  ```
  
  ```  
    cf push AuthServiceBroker
  ```    
  An App “AuthServiceBroker” will be started based on the package.json  file. You can view the app using following command “cf apps”.
	   
  
  
##  Register the AuthServiceBroker app as service broker in cloud foundry

  1.	To create service broker in CF
  
  ```
    cf create-service-broker “<brokername>” <username> <pwd> <brokerapp url>	
  ```
  This username and password is the same as that which you have given in the config.json 
  
  2.	Get the service name from below command
  
  ```
    cf service-access
  ```
  3.	To get the service in marketplace
  
  ```
 	  cf enable-service-access “<servicename>”
 	  cf marketplace
 ```
 
##	Now to create AuthService instance

  To create single instance,

  ```
    cf create-service AuthService free AuthInstance -c '{"endpoint":"https://api.54.208.194.189.xip.io","appname":"testservice1","space_guid":"b169a527-a10a-4a84-a45a-2909fee6b1d9","domain_guid":"56f6da1f-eed3-42fb-a629-b28101069137","host":"54.208.194.189.xip.io","environment_json":{"facebook_clientID":"478519535677977","facebook_clientSecret":"a9bc36abea045066cd4be131e278ff80","google_clientID":"625227390094-m47bnlnuaguvq3phn5t5kmp503fsiagd.apps.googleusercontent.com","google_clientSecret":"k0vpP0Tp5dP2oqXmcF9v10G8","twitter_clientID":"6F5cvS3hPsh4QIVfS6PsJ10uz","twitter_clientSecret":"DHDIojA6kdzi77nH3eLXmh3fvHN68AAL0zSxC11yh0N2huBFr1","linkedin_clientID":"75shq40xwna1yl","linkedin_clientSecret":"kUomrXzdex8PfY3e"},"token":{"access_token":"eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJkZDRlNGY3ODY5ZDA0MDFkYjAxZDhiNzE1MWI2Mzc5NCIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NjE1NTc0NCwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY2MTU1NzQ0LCJleHAiOjE0NjYxNTYzNDQsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.q3gxL0FPXvLn-ummG2a5-uKXsykB8zE8BzU4FelhoNzdR9tWcKlBYA-LEDBjHxVCd_E-MD9nuTHaWUPFiejsLqMyCizFsYM7DlTL8qMfXACs9p4SP-9FqzvQMaTZAoJcAMfcFqMdGf6WaJfbEF5vOKFbrxfsVZ9aIDw1wUCz_1g","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJkZDRlNGY3ODY5ZDA0MDFkYjAxZDhiNzE1MWI2Mzc5NC1yIiwic3ViIjoiYzc0NDhkZmYtZGZmOS00YmZkLTkyNjEtN2IyYjZiMTY4ZWUzIiwic2NvcGUiOlsib3BlbmlkIiwic2NpbS5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5hZG1pbiIsInVhYS51c2VyIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzLnJlYWQiLCJjbG91ZF9jb250cm9sbGVyLnJlYWQiLCJwYXNzd29yZC53cml0ZSIsImNsb3VkX2NvbnRyb2xsZXIud3JpdGUiLCJkb3BwbGVyLmZpcmVob3NlIiwic2NpbS53cml0ZSJdLCJpYXQiOjE0NjYxNTU3NDQsImV4cCI6MTQ2ODc0Nzc0NCwiY2lkIjoiY2YiLCJjbGllbnRfaWQiOiJjZiIsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfbmFtZSI6ImFkbWluIiwib3JpZ2luIjoidWFhIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInJldl9zaWciOiJlZWYwMzQ4YyIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.Xc1YoB8g6o0nMft8zmvormPP9lipTRqqulio4oYYAyCEnuMcHJ0wzZnQLXvV-VFTJwpkUVBNuQEHQqQSpeKfXtRfQAaMRtW1AjmFW_EjXHebA6Uw8lSTj0oytMjqDHtQPyYEIX_Liz1l0sTPuG9M_vsfqF1esEoOBcvQahdG8UM","expires_in":599,"scope":"openid scim.read cloud_controller.admin uaa.user routing.router_groups.read cloud_controller.read password.write cloud_controller.write doppler.firehose scim.write","jti":"dd4e4f7869d0401db01d8b7151b63794"}}'
  ```

  
##	To bind the service to users app

  ```
    cf bind-service AuthTemplateApp AuthInstance 
  ```
  
##    To un-bind the service from users app

 ```
    cf unbind-service AuthTemplateApp AuthInstance 
 ```
 
  
## To delete the service instance

  ```
    cf delete-service AuthInstance
  ```


