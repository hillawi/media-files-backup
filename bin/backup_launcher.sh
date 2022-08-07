#!/bin/bash

###################################################################
# Script Name	: backup_launcher.sh
# Description	: Launch the backup process
# Args        : The source device ID, the target device ID and the
#               media type (IMG, VID, etc.)
# Author      : Ahmed Abdeen
# Last update : 2022/07/22
###################################################################

# Debug option, comment out if not needed
#set -x

# Error codes
ERROR_INPUT_PARAM_INVALID=1
ERROR_MEDIA_TYPE_UNKNOWN=2
ERROR_INPUT_FOLDER_NOTFOUND=3
ERROR_OUTPUT_FOLDER_NOTFOUND=4
ERROR_BAD_DEVICE_CONF=5
ERROR_BACKUP_ERROR=6

# Log file
LOG_FILE="$MFB_HOME/log/backup_launcher-$(date +"%Y-%m-%d")"

if [[ $# -ne 3 ]]; then
  printf "The source device ID, the target device ID and the media type (IMG or VID) are required\n"
  exit $ERROR_INPUT_PARAM_INVALID
fi

BIN_DIR=$MFB_BIN_DIR
DEVICES_CONF_FILE="$MFB_CONF_DIR/devices.json"

declare -A INPUT_PATHS_PER_MEDIA_TYPE
declare -A OUTPUT_PATHS_PER_MEDIA_TYPE

SOURCE_DEVICE_ID=$1
TARGET_DEVICE_ID=$2
MEDIA_TYPE=$3
FILE_SUFFIX=""

# Read the devices list from the configuration file
devices=$(cat "$DEVICES_CONF_FILE");
sourceDeviceType=$(echo "$devices" | jq -r --arg SOURCE_DEVICE_ID "$SOURCE_DEVICE_ID" '.[] | select(.id==$SOURCE_DEVICE_ID) | .type');
targetDeviceType=$(echo "$devices" | jq -r --arg TARGET_DEVICE_ID "$TARGET_DEVICE_ID" '.[] | select(.id==$TARGET_DEVICE_ID) | .type');

# Devices validation
if [[ "$sourceDeviceType" != "source" ]]; then
  echo "The source device is not of type source"
  exit $ERROR_BAD_DEVICE_CONF
fi

if [[ "$targetDeviceType" != "target" ]]; then
  echo "The target device is not of type target"
  exit $ERROR_BAD_DEVICE_CONF
fi

sourceDeviceMountPath=""
# TODO add the target device mount path to the source devices subPaths

if [[ "$MEDIA_TYPE" == "IMG" ]]; then
  FILE_SUFFIX="IMG_*.jpg"
  sourceDeviceMountPath=$(echo "$devices" | jq -r --arg SOURCE_DEVICE_ID "$SOURCE_DEVICE_ID" '.[] | select(.id==$SOURCE_DEVICE_ID) | .mountPath.img');
  sourceDeviceTargetSubPath=$(echo "$devices" | jq -r --arg SOURCE_DEVICE_ID "$SOURCE_DEVICE_ID" '.[] | select(.id==$SOURCE_DEVICE_ID) | .targetSubPath.img');
elif [[ "$MEDIA_TYPE" == "VID" ]]; then
  FILE_SUFFIX="VID_*.mp4"
  sourceDeviceMountPath=$(echo "$devices" | jq -r --arg SOURCE_DEVICE_ID "$SOURCE_DEVICE_ID" '.[] | select(.id==$SOURCE_DEVICE_ID) | .mountPath.vid');
  sourceDeviceTargetSubPath=$(echo "$devices" | jq -r --arg SOURCE_DEVICE_ID "$SOURCE_DEVICE_ID" '.[] | select(.id==$SOURCE_DEVICE_ID) | .targetSubPath.vid');
else
  echo "Unknown media type $MEDIA_TYPE. Known types are IMG and VID."
  exit $ERROR_MEDIA_TYPE_UNKNOWN
fi

targetDeviceMountPath=$(echo "$devices" | jq -r --arg TARGET_DEVICE_ID "$TARGET_DEVICE_ID" '.[] | select(.id==$TARGET_DEVICE_ID) | .mountPath')"/$sourceDeviceTargetSubPath"

[[ -d "$sourceDeviceMountPath" ]] || {
  echo "Input folder ($sourceDeviceMountPath) does not exist"
  exit $ERROR_INPUT_FOLDER_NOTFOUND
}
[[ -e "$targetDeviceMountPath" ]] || {
  echo "Destination folder ($targetDeviceMountPath) does not exist"
  exit $ERROR_OUTPUT_FOLDER_NOTFOUND
}

printf "\n\n"
echo "****************** MEDIA FILES BACKUP *******************"
echo "** DATE     : $(date +"%m-%d-%Y")"
echo "** DEVICE ID: $SOURCE_DEVICE_ID"
echo "** FROM     : $sourceDeviceMountPath"
echo "** TO       : $targetDeviceMountPath"
echo "*********************************************************"
printf "\n"

cd "$targetDeviceMountPath" || {
  echo "Cannot change directory. Exiting."
  exit 6
}

numberOfCopiedFiles=0

bash "$BIN_DIR/do_backup.sh" "$sourceDeviceMountPath" "$FILE_SUFFIX" "$MEDIA_TYPE" "$SOURCE_DEVICE_ID" &

childPid=$!

wait $childPid

exitCode=$?

[[ $exitCode -eq 0 ]] || {
  echo "Backup failed"
  exit $ERROR_BACKUP_ERROR
}

numberOfCopiedFiles=$(cat "$childPid.mfb.pid")

rm "$childPid.mfb.pid" || echo "Cannot remove temporary files."

if [[ $numberOfCopiedFiles -gt 0 ]]; then
  printf 'Number of files copied: %s.\nDone. Goodbye :)\n' "$numberOfCopiedFiles"
else
  echo "All files are up to date. Nothing to do. Have a good day :)"
fi

printf "*********************************************************\n\n"
exit 0
