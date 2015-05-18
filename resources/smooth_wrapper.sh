#!/bin/expect

# The smoothing script is terrible. We make it _slighty_
# easier to deal with.  This script wraps itself around
# ratio_calculator_v2.pl, by answering the four questions asked by the
# script, instead taking them as commandline arguments.

# We set our variables from argv
set path          [lindex $argv 0]\r
set window_size   [lindex $argv 1]\r
set median        [lindex $argv 2]\r
set min_step      [lindex $argv 3]\r
set calc_tot      [lindex $argv 4]\r
set print_pos     [lindex $argv 5]\r

# We spawn the process to automate
spawn perl smooth_v4.pl

# We are going to respond to a few questions, remain calm.
expect "Enter the path of sgr files"
send -- $path

expect "Enter window size"
send -- $window_size

expect "Enter 1 or 0, to smooth with median (1) or trimmed mean (0)"
send -- $median

expect "Enter minimum step pos to smooth"
send -- $min_step

expect "Do you want to calculate total mean (1) or no (0) and print on terminal"
send -- $calc_tot

expect "Do you want to print pos with no reads in smoothed file, (1) or no (0)"
send -- $print_pos

# We await process eof
expect eof
