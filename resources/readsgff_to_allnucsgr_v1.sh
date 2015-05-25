#!/bin/expect

# We set our variables from argv
set path [lindex $argv 0]\r
set timeout -1

# We spawn the process to automate
spawn perl readsgff_to_allnucsgr_v1.pl

# We are going to respond to a few questions, remain calm.
expect {
    "Enter the path of gff files" { send -- $path ; exp_continue }
    eof
}

# We await process eof
#expect eof
