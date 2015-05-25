#!/bin/bash

DATE=$(date +%x)
WORKDIR=/data
READFOLDER=server
SAVEPATH=/data/
# Number of days to save backup-copy
DAYS=1

find "$SAVEPATH" -maxdepth 1 -type f -mtime +$DAYS -print0 | xargs -0 rm
tar -C "$WORKDIR" -zcf "$SAVEPATH/backup_${DATE}.tar.gz" "$READFOLDER"


