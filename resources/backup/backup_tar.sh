#!/bin/bash

DATE=$(date +%x)
READPATH=
SAVEPATH=
# Number of days to save backup-copy
DAYS=

find "$SAVEPATH" -maxdepth 1 -type f -mtime +$DAYS -print0 | xargs -0 rm
tar -zcf "$SAVEPATH/backup_${DATE}.tar.gz" "$READFOLDER"


