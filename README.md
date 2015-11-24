Genomizer Server [![Build Status](https://secure.travis-ci.org/genomizer/genomizer-server.svg?branch=master)](http://travis-ci.org/genomizer/genomizer-server)
===================

This is the main repository for the server side of the Genomizer system.

##Downloads

Auto-generated binary snapshots are uploaded by the Travis build bot to the
[`genomizer-downloads`](https://github.com/genomizer/genomizer-downloads) repository after each successful build.

The following downloads are available:

 * **master branch:**
  * [`genomizer-server.jar`](https://github.com/genomizer/genomizer-downloads/raw/genomizer-server-master/genomizer-server.jar)
  * [`genomizer-server-tester.jar`](https://github.com/genomizer/genomizer-downloads/raw/genomizer-server-master/genomizer-server-tester.jar)
 * **develop branch:**
  * [`genomizer-server.jar`](https://github.com/genomizer/genomizer-downloads/raw/genomizer-server-develop/genomizer-server.jar)
  * [`genomizer-server-tester.jar`](https://github.com/genomizer/genomizer-downloads/raw/genomizer-server-develop/genomizer-server-tester.jar)
 * **Resources (common for both branches):**
  * [`genomizer-server-resources.tar.xz`](https://github.com/genomizer/genomizer-downloads/releases/download/genomizer-server-resources/genomizer-server-resources.tar.xz)

**NB:** The `genomizer-server-resources.tar.xz` archive is updated manually due to
its large size. GitHub Releases feature is used to host it.

##Building

To build the `server.jar` file manually, run `ant jar` from the console. To just compile
the sources, use `ant build` or `ant compile`. To run tests, use `ant test`. To
run individual tests or test groups, use an IDE (e.g. in Eclipse you can import
the project with 'File > New > Project > Java > Java Project from Existing Ant
Buildfile'). To produce a HTML report with test results, run `ant junitreport`.
The report will be put under `junit/index.html`. To build and run the server in
one step (useful during development), use `ant run`.

##Usage

The server requires Java 1.7 to run. This is the default on lab
computers, so you can just use

    java -jar server.jar

to run the Genomizer server. The full path to the Java 7 executable is
`/usr/lib/jvm/java-7-openjdk-amd64/bin/java`.

The program can be run with the following options:

    -p [NUMBER]    The listening port for the server, default is 7000

    -f [FILE]      Use a custom config file instead of settings.cfg.

    -nri       "No remove inactive", don't remove inactive users that are
               logged in.

    -debug         Print out various debug information.

If no options are used the server will listen on port 7000 and look
for a file named "settings.cfg" in the current folder to read settings
from.

##Development

The main development branch is called *`develop`*. New feature
development is done on separate feature branches. When a feature is
completed, the feature branch is merged into *`develop`* after a code
review. The *`develop`* branch is itself regularly merged into
*`master`* (usually before a delivery).

More on this branching model can be found
[here](http://nvie.com/posts/a-successful-git-branching-model/). A
guide for merging in Git can be found
[here](https://github.com/genomizer/genomizer-server/wiki/Merging-in-Git).

##Overview

The server uses HTTP with a RESTful interface for client-server
communication. Full documentation for the API can be found [on
Apiary](http://docs.genomizer.apiary.io/). Since HTTP is based around
non-persistent connections a token is generated for users when logging
in, which must then be passed with all following requests. The
authorization token should be passed in in the `Authorization` headr
(or, alternatively, using a `?token=` URL parameter) of all requests
except login requests.

###Example: logging in

The client sends username and password in a JSON object and receives
an authorization token from the server.  The HTTP response code is
used to notify the client if the operation was successful. 200 means
OK.

Request

    POST /login HTTP/1.1
    Content-Length: XXXX

    {"username":"yuri","password":"epicgenie"}

Response

    HTTP/1.1 200 OK
    Content-Length: XXXX

    {"token":"abca44db68cd2ffg2293"}

###Example:  fetching annotation list

Using the authorization token from the login request, the client can
then make other requests, such as fetching a list of all annotation
types in the database.

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
[Client-Server API](http://docs.genomizerexperimental.apiary.io/)

[Git merge instructions](https://github.com/genomizer/genomizer-server/wiki/Merging-in-Git)
