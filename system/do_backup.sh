#!/bin/bash

###################################################################
# Script Name	: do_backup.sh
# Description	: Copy the files (called by launch_backup.sh)
# Args        : The input and output folders, the files suffix,
#               the media type and the device ID
# Author      : Ahmed Abdeen
###################################################################

INPUT_FOLDER=$1
FILE_SUFFIX=$2
MEDIA_TYPE=$3
DEVICE_ID=$4

CONF_DIR=$MFB_CONF_DIR

subFolders=()

numberOfCopiedFiles=0
lastBackupDateFileName="${CONF_DIR}/latest_${DEVICE_ID}_${MEDIA_TYPE}"

# If the latest file contains nothing, put a very old date on it
lastBackupDate=$(cat "$lastBackupDateFileName" 2>/dev/null || echo "1970-01-01")

while read -r tmp; do
  [[ "$(echo "$tmp" | awk '{print length}')" -gt 0 ]] || {
    echo "No files to backup. Have a good day :)"
    break
  }

  p=$(printf '%s\n' "$tmp")
  file=$(basename "$p")

  subFolder="${file:4:6}"
  if [[ ! " ${subFolders[@]} " =~ " ${subFolder} " ]]; then
    if [[ ! -d "$subFolder" ]]; then
      mkdir "$subFolder"
    fi
    subFolders+=("$subFolder")
  fi

  echo -ne "Processing file: $file"\\r

  cp -p "$p" "$subFolder" 2>/dev/null

  lastProcessedFile=$file
  ((numberOfCopiedFiles++))
done <<<"$(find "$INPUT_FOLDER/" -type f -name "$FILE_SUFFIX" -newermt "$lastBackupDate" | sort)"

echo "$numberOfCopiedFiles" > "$$.mfb.pid"

# Update the latest backed up file
if [[ "$(echo "$lastProcessedFile" | awk '{print length}')" -gt 0 ]]; then
  backup_date=$(date -d "$((${lastProcessedFile:4:8}))" +'%Y-%m-%d')
  echo "$backup_date" > "$lastBackupDateFileName"
fi
