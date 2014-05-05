Genomizer Server
===================
This is the main repository for the server side of the Genomizer system. 

##Development
All development is done in either of four branches, *communication*, *process*, *transfer* or *database*. When some feature
is done in either of these branches it should be merged into the *development* branch.
A guide for doing this can be found [here](https://github.com/genomizer/genomizer-server/wiki/Merging-in-Git). The *development* 
branch is used for ongoing
features which haven't been fully tested yet. This is used to connect different branches which are working on the same feature.
Only when a feature is proven to work in the *development* branch should it be merged into *master*. This way
the *master* branch stays clean and can potentially be released at any time, with features that are mostly bug-free.

##Overview
The server uses HTTP with a RESTful interface
for client-server communication. Check out the full API [here](http://docs.genomizer.apiary.io/).
Since HTTP is based around non-persistent connections a token is generated for users when logging in, which must
then be passed with all following requests. The authorization token should be stored in the header of all requests
except login requests.

###Example: logging in
The client sends username and password in a JSON object and receives an authorization token from the server.
The HTTP response code is used to notify the client if the operation was successful. 200 means OK.

Request

    POST /login HTTP/1.1
    Content-Length: XXXX
    
    {"username":"yuri","password":"epicgenie"}

Response

    HTTP/1.1 200 OK
    Content-Length: XXXX

    {"token":"abca44db68cd2ffg2293"}

###Example:  fetching annotation list
Using the authorization token from the login request, the client can then make other requests, such as fetching a list
of all annotation types in the database.

Request

    GET /annotation HTTP/1.1
    Authorization: abca44db68cd2ffg2293

Response

    HTTP/1.1 200 OK
    Content-Length: XXXX

    [
     {
      "id": 1, 
      "name": "pubmedId",
      "value": "freetext",
      "forced": true
     }, 
     {
      "id": 2,
      "name": "type",
      "value": "freetext",
      "forced": true
     },
     {
      "id": 3,
      "name": "specie",
      "value": ["fly", "human", "rat"],
      "forced": true
     }
    ]

##Links
[Client-Server API](http://docs.genomizer.apiary.io/)

[Git merge instructions](https://github.com/genomizer/genomizer-server/wiki/Merging-in-Git)
