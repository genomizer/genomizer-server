#!/bin/bash

# Synchronizes the server and the remote backup. The backup will be an image
# of the current state of the server.

PORT=
USER=
IP=
READPATH=
SAVEPATH=
rsync --ignore-existing --delete --update -avze 'ssh -p '$PORT $READPATH $USER@$IP:$SAVEPATH
