#!/bin/bash

SALT=$(grep -i passwordsalt settings.cfg | tr -d '[:blank:]' | cut -d '=' -f 2-)
printf $1 > temp
printf $SALT >> temp
ARG=`sha256sum temp`
ARG=`echo $ARG` | `awk '{print $1}'`
PWD=$(echo $ARG | awk '{print $1}')
grep -iv passwordhash settings.cfg > temp
echo "passwordHash     = $PWD" >> temp
cp temp settings.cfg
rm temp
