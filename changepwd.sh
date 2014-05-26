#!/bin/bash

printf $1 > temp
printf "genomizer" >> temp
ARG=`sha256sum temp`
ARG=`echo $ARG` | `awk '{print $1}'`
echo $ARG | awk '{print $1}' > client_password.txt
rm temp
