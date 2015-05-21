#!/bin/bash

# Creates a postgresql dump of the database. Set SAVEPATH to the same path as READPATH in file_backup.sh.
# Otherwise the pstgresql dump will not be copied to the backup server. 

DATE=$(date +%x_%X_%Z)
DBUSER=
DBNAME=
DBPORT=
SAVEFILE=SqlBackup-$DATE.sql
SAVEPATH=

pg_dump -w -U $DBUSER -h localhost -p $DBPORT $DBNAME > tmp
echo pvt | sudo -S cp tmp $SAVEPATH$SAVEFILE

