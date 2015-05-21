#!/bin/bash

# Makes a full copy of the server to the backup server. All files are copied 
# to a folder named with the date and time of the backup execution. No files are 
# removed from the backup server.

DATE=$(date +%x_%X_%Z)
PORT=
USER=
IP=
READPATH=
SAVEPATH=/path/to/folder/backup_$DATE
rsync -abvzW --delete -e 'ssh -p '$PORT $READPATH $USER@$IP:$SAVEPATH
