#!/bin/expect

# The ratio calculation script is terrible. We make it _slighty_
# easier to deal with.  This script wraps itself around
# ratio_calculator_v2.pl, by answering the four questions asked by the
# script, instead taking them as commandline arguments.
#
# Usage example(s):
# expect ratio_calc_wrapper.sh "Argument1" "Split arg" "Argument3" "Argument4"
# expect ratio_calc_wrapper.sh test1 test2 test3 test4

# We set our variables from argv
set path   [lindex $argv 0]\r
set mean   [lindex $argv 1]\r
set cutoff [lindex $argv 2]\r
set chr    [lindex $argv 3]\r

# We spawn the process to automate
spawn perl ratio_calculator_v2.pl

# We are going to respond to a few questions, remain calm.
expect "Enter the path of IP and Input SGR files"
send -- $path

expect "Do you want double mean (double) or single mean (single) cutoff from Input"
send -- $mean

expect "Give Input reads count cutoff"
send -- $cutoff

expect "Do you want to specify chr's (type like chr1,chr2) or no (0)"
send -- $chr

# We await process eof
expect eof
