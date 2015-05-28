#!/bin/bash

# Synchronizes the server and the backup. The backup will be an image
# of the current state of the server.

READPATH=
SAVEPATH=
rsync --delete --update -avz $READPATH $SAVEPATH
