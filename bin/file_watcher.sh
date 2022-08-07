#!/bin/bash

###################################################################
# Script Name	: file_watcher.sh
# Description	: Watch files created by the udev rule and call
#               the automatic launcher (requires inotify-tools)
# Author      : Ahmed Abdeen
# Last update : 2022/08/07
###################################################################

# Log file
LOG_FILE="$MFB_HOME/log/file_watcher-$(date +"%Y-%m-%d")"

# Work directory
WORK_DIR="$MFB_HOME/udev/"

[[ -f $LOG_FILE ]] || touch "$LOG_FILE"

inotifywait -mq --format '%f' -e create "$WORK_DIR/" | while read -r file; do
  printf "Detected file %s\n" "$file" >> "$LOG_FILE"
  bash "$MFB_BIN_DIR/automatic_backup_launcher.sh ${file:0:3} ${file:4:6}" &
done