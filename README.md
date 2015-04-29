Genomizer Server [![Build Status](https://secure.travis-ci.org/genomizer/genomizer-server.svg?branch=master)](http://travis-ci.org/genomizer/genomizer-server)
===================

**WARNING, contains outdated information. For more recent information read the full documentation from https://github.com/genomizer/genomizer-documentation**

This is the main repository for the server side of the Genomizer system. It is currently found
at http://scratchy.cs.umu.se:7000.

##Building

To build the `server.jar` file, run `ant jar` from the console. To just compile
the sources, use `ant build` or `ant compile`. To run tests, use `ant test`. To
run individual tests or test groups, use an IDE (e.g. in Eclipse you can import
the project with 'File > New > Project > Java > Java Project from Existing Ant
Buildfile'). To produce a HTML report with test results, run `ant junitreport`.
The report will be put under `junit/index.html`. To build and run the server in
one step (useful during development), use `ant run`.

##Download instructions
A jar of the current version of the server is available on scratchy.cs.umu.se at all times. To download it execute the following in
a terminal:

    scp -P 2222 pvt@scratchy.cs.umu.se:server.jar .

After entering the password this will download server.jar to your current directory. An alternative is to connect to
pvt@scratchy.cs.umu.se on port 2222
using Filezilla or similar and choosing server.jar for download. There will also be other build versions there, with date and timestamp
from when they where created.

##Usage
**Warning! If the server is run on your local computer, uploading and downloading files probably wont work.**

The server requires Java 1.7 to run. In the lab computers this is not the default, but it is installed and can be found at
/usr/lib/jvm/java-7-openjdk-amd64. Either make a symbolic link to /usr/lib/jvm/java-7-openjdk-amd64/bin/java or enter the full path.

Symbolic link

    ln -s /usr/lib/jvm/java-7-openjdk-amd64/bin/java java7
    ./java7 -jar server.jar
Full path

    /usr/lib/jvm/java-7-openjdk-amd64/bin/java -jar server.jar

The program can be run with the following options:

    -p [NUMBER]    The listening port for the server, default is 7000

    -d [DB]        DB can be either "test" or "global". Chooses which database to use, test is located at ITS and can only be reached
                   using lab computers. Global is in MC333 and requires the server application to be started locally on that computer.

    -f [FILE]      Reads the database information from a file. The file should contain a single line with 4 words, each separated by a space.
                   It should be written in the following format: USERNAME PASSWORD DATABASE HOST

If no options are used the server will listen on port 7000 and look for a file named "dbconfig" in the current folder
to read database settings from. If the file doesn't exist, the server will use the database called "test". This is the same as running

    /usr/lib/jvm/java-7-openjdk-amd64/bin/java -jar server.jar -p 7000 -d test

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

[Server information](http://scratchy.cs.umu.se:8000/admin/server.html)
