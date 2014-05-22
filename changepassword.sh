#!bin/bash


if [ $# -lt 1 ]
then
    echo "Useage: $0 [new password]"
    exit 1
fi

newpass=$1
salted="$1genomizer"
echo -n $salted | md5sum | cut -d ' ' -f 1 > client_password.txt
echo "Password changed."