#!/bin/expect

# The smoothing script is terrible. We make it _slighty_
# easier to deal with.  This script wraps itself around
# ratio_calculator_v2.pl, by answering the four questions asked by the
# script, instead taking them as commandline arguments.
#
# The scripts takes the following parameters:
# argv 0 - path 	- the path the .sgr files are located at.
# argv 1 - window_size 	- the window size to send to script.
# argv 2 - median 	- the median to send to script.
# argv 3 - min_step 	- the min_step to send to script.
# argv 4 - calc_tot 	- the calc_tot to send to script.
# argv 5 - print_pos 	- the print_pos to send to script.
# argv 6 - location 	- the location to send to script. (output path)

# We set our variables from argv
set path          [lindex $argv 0]\r
set path_intern   [lindex $argv 0]
set window_size   [lindex $argv 1]\r
set median        [lindex $argv 2]\r
set min_step      [lindex $argv 3]\r
set calc_tot      [lindex $argv 4]\r
set print_pos     [lindex $argv 5]\r
set location      [lindex $argv 6]

# Make temporary smoothed dir
system mkdir -p $path_intern/smoothed

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

# We move the output to the correct location
system mkdir -p $location
system mv $path_intern/smoothed/* $location
system rm -rf $path_intern/smoothed