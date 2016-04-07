# social-composite-service
composite service to connect to social media

## Linkedin Flow:

#### When a request is made to /api/linkedin?username={username} to get LinkedIn data.
**The LinkedInAuthorizationFilter** intercepts the request and checks if there is still a valid Primary Connection*.
If no Primary Connection is present or it can’t be refreshed, a message is send back to the front-end together with an 401 Authorization needed.
Otherwise the profile is synced with LinkedIn using the stored connection.

*A primaryConnection is a token linked to the user that contains a client ID and secret ID given by LinkedIn. This Token is only valid for a certain time, afterwards it needs to be refreshed, or if more time has past a new Primary Connection has to be established.


#### When no connection is available the front-end then sends a request to /connect/linkedin?username{username}.
* The request gets picked up by the **LinkedInConnectionFilter** which is used to get the original url from the request. The original url is set as an session attribute so it can be used later on.

* The Spring **LinkedInConnectController** then redirects to LinkedIn to establish a Primary Connection where the user can fill in his personal data.

* The Spring **LinkedInConnectController**  stores the new Primary Connection returned by LinkedIn so it can be used to fetch the LinkedIn data.

* The Spring **LinkedInConnectController**  is overwritten by the **CustomConnectController** which will then redirect to the stored original URL on the session.**

**This will not perform the sync but only store the new Primary Connection, to execute the sync a call needs to be made to /api/linkedin?username={username}

