#!/bin/bash

# This script will change the path where sra-files are stored.
# sra-toolkit uses the home directory as default path to store
# sra-files. By using this script all sra-files is stored in 
# the path specified in settings.cfg and is removed when not 
# needed. 
# NOTE: The SRA download feature will still work without using
# this script but unnecessary files will be stored in the home
# directory and will not be removed by genomizer.

SETTINGS="settings.cfg"
VDB_CONFIG="resources/sra-toolkit/vdb-config"

line=`cat $SETTINGS | grep "fileLocation"`

read -a arr <<< "$line"

PATH=${arr[2]}

`$VDB_CONFIG --set repository/user/main/public/root=$PATH`
`$VDB_CONFIG --set repository/user/default-path=$PATH`
if [ $? != 0 ]; then
	echo "Could not configure SRA"
	exit -1
fi

echo "Success!"
echo "SRA files are now stored at $PATH"



