# social-composite-service
composite service to connect to social media

## Linkedin Flow:

When a request is made to the social-composite-service to get LinkedIn data.

* **The LinkedInAuthorizationFilter** intercepts the request and checks if there is still a valid primary connection*.
If no PrimaryConnection is present or it can’t be refreshed, a message is send back to the front-end together with an 401 Authorization needed.

* The front-end then sends a request to /connect/linkedin.
The request gets picked up by the **LinkedInConnectionFilter** which is used to get the original url from the request. (It is set as an session attribute so it can be used later on)

* The Spring **LinkedInConnectController** is then send to LinkedIn to establish a Primary Connection where the user can fill in his personal data.
LinkedIn then returns a primaryConnection which can be used to get the profile data.

* The Spring **LinkedInConnectController**  uses the primaryConnection to return a LinkedIn Object.
Then a connection is made with the Employee-Core-Service to get the correct employee and update it’s information with the profile information from the LinkedIn Object.

* The Spring **LinkedInConnectController**  get’s overwritten by the CustomConnectController and sends a message to the front-end using the original URL that the connection was successful.


*A primaryConnection is a token linked to the user that contains a client ID and secret ID given by LinkedIn. This Token is only valid for a certain time, afterwards it needs to be refreshed, or if more time has past a new primaryConnection has to be established.

**LinkedIn also requires a token for an application. An application has to be registered with LinkedIn before it gets it’s credentials.

