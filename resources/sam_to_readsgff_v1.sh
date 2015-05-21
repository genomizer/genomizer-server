#!/bin/expect

# We set our variables from argv
set path   [lindex $argv 0]\r
set timeout -1

# We spawn the process to automate
spawn perl sam_to_readsgff_v1.pl

# We are going to respond to a few questions, remain calm.
expect "Enter the path of sam files"
send -- $path

# We await process eof
expect eof
