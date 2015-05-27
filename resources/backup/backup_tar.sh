#!/bin/bash

DATE=$(date +%x)
WORKDIR=
READFOLDER=
SAVEPATH=
# Number of days to save backup-copy
DAYS=

mkdir -p $SAVEPATH

find "$SAVEPATH" -maxdepth 1 -type f -mtime +$DAYS -print0 | xargs -0 rm -f
tar -C $WORKDIR -zcf "$SAVEPATH/backup_${DATE}.tar.gz" "$READFOLDER"



