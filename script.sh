#!/bin/bash

ant jar
dateres=server_$(date +%d%m_%H%M).jar
mv server.jar $dateres

/usr/bin/expect <<EOF

spawn scp -P 2250 $dateres pvt@scratchy.cs.umu.se:
expect "password:"
sleep 1
send "pvt\r"
spawn ssh pvt@scratchy.cs.umu.se -p 2250
expect "password:"
sleep 1
send "pvt\r"
sleep 1
interact

EOF
