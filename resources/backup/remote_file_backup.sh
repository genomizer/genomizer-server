#!/bin/bash

# Synchronizes the server and the remote backup over ssh. The backup will be an image
# of the current state of the server.

PORT=
USER=
IP=
READPATH=
SAVEPATH=
rsync --delete --update -avze 'ssh -p '$PORT $READPATH $USER@$IP:$SAVEPATH
