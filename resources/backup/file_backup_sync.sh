#!/bin/bash

# Synchronizes the server and backup. The backup will be an image
# of the current state of the server.
# Note: Use this script when you are sure no files are missing on the server.
# Any files existing on the backup but not on the server will be deleted.

PORT=
USER=
IP=
READPATH=
SAVEPATH=
rsync --ignore-existing --delete --update -avze 'ssh -p '$PORT $READPATH $USER@$IP:$SAVEPATH
