#!/bin/bash

username=$1
password=$2

export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

ant jar
filename=server_$(date +%d%m_%H%M).jar
mv server.jar $filename

/usr/bin/expect <<EOF

spawn scp -P 2200 $filename $username@scratchy.cs.umu.se:
expect "password:"
sleep 1
send "$password\r"
spawn ssh $username@scratchy.cs.umu.se -p 2200
expect "password:"
sleep 1
send "$password\r"
sleep 1
send "yes | cp $filename server.jar\r"

interact

EOF
