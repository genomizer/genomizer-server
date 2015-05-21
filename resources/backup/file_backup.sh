#!/bin/bash

# Transfers the difference between the server and backup to the 
# backup server. This script doesn't remove any files on the 
# backup server to protect from any deletion mistakes made on the server.
# To sync the backup with the server use file_backup_sync.sh.

PORT=
USER=
IP=
READPATH=
SAVEPATH=
rsync -avze 'ssh -p '$PORT $READPATH $USER@$IP:$SAVEPATH
