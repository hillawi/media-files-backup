#!/bin/bash

###################################################################
# Script Name	: launch_backup.sh
# Description	: Launch the backup process
# Args        : The device ID and the media type (IMG, VID, etc.)
# Author      : Ahmed Abdeen
###################################################################

# Error codes
ERROR_INPUT_PARAM_INVALID=1
ERROR_DEVICE_UNKNOWN=2
ERROR_MEDIA_TYPE_UNKNOWN=3
ERROR_INPUT_FOLDER_NOTFOUND=4
ERROR_OUTPUT_FOLDER_NOTFOUND=5

if [[ $# -ne 3 ]]; then
  echo "The source and target devices IDs (ex: 6t, 7t) and the media type (IMG or VID) are required"
  exit $ERROR_INPUT_PARAM_INVALID
fi

BIN_DIR=$MFB_BIN_DIR

declare -A INPUT_PATHS_PER_MEDIA_TYPE
declare -A OUTPUT_PATHS_PER_MEDIA_TYPE

SOURCE_DEVICE_ID=$1
TARGET_DEVICE_ID=$2

if [[ 1 -eq 1 ]]; then
  printf "Done"
  exit 0
fi

# TODO Use switch, extract the list of device IDs
if [[ "$SOURCE_DEVICE_ID" == "6t" ]]; then
  INPUT_PATHS_PER_MEDIA_TYPE[IMG]=$MFB_INPUT_ONEPLUS6T_IMG
  INPUT_PATHS_PER_MEDIA_TYPE[VID]=$MFB_INPUT_ONEPLUS6T_VID

  OUTPUT_PATHS_PER_MEDIA_TYPE[IMG]=$MFB_OUTPUT_ONEPLUS6T_IMG
  OUTPUT_PATHS_PER_MEDIA_TYPE[VID]=$MFB_OUTPUT_ONEPLUS6T_VID
elif [[ "$SOURCE_DEVICE_ID" == "7t" ]]; then
  INPUT_PATHS_PER_MEDIA_TYPE[IMG]=$MFB_INPUT_ONEPLUS7T_IMG
  INPUT_PATHS_PER_MEDIA_TYPE[VID]=$MFB_INPUT_ONEPLUS7T_VID

  OUTPUT_PATHS_PER_MEDIA_TYPE[IMG]=$MFB_OUTPUT_ONEPLUS7T_IMG
  OUTPUT_PATHS_PER_MEDIA_TYPE[VID]=$MFB_OUTPUT_ONEPLUS7T_VID
else
  echo "Unknown device ID. Supported device IDs are 6t and 7t."
  exit $ERROR_DEVICE_UNKNOWN
fi

MEDIA_TYPE=$3
FILE_SUFFIX=""

if [[ "$MEDIA_TYPE" == "IMG" ]]; then
  FILE_SUFFIX="IMG_*.jpg"
elif [[ "$MEDIA_TYPE" == "VID" ]]; then
  FILE_SUFFIX="VID_*.mp4"
else
  echo "Unknown media type $MEDIA_TYPE. Known types are IMG and VID."
  exit $ERROR_MEDIA_TYPE_UNKNOWN
fi

INPUT_FOLDER=${INPUT_PATHS_PER_MEDIA_TYPE["$MEDIA_TYPE"]}
DEST_FOLDER=${OUTPUT_PATHS_PER_MEDIA_TYPE["$MEDIA_TYPE"]}

[[ -d "$INPUT_FOLDER" ]] || {
  echo "Input folder ($INPUT_FOLDER) does not exist"
  exit $ERROR_INPUT_FOLDER_NOTFOUND
}
[[ -e "$DEST_FOLDER" ]] || {
  echo "Destination folder ($DEST_FOLDER) does not exist"
  exit $ERROR_OUTPUT_FOLDER_NOTFOUND
}

printf "\n\n"
echo "****************** MEDIA FILES BACKUP *******************"
echo "** DATE     : $(date +"%m-%d-%Y")"
echo "** DEVICE ID: $SOURCE_DEVICE_ID"
echo "** FROM     : $INPUT_FOLDER"
echo "** TO       : $DEST_FOLDER"
echo "*********************************************************"
printf "\n"

cd "$DEST_FOLDER" || {
  echo "Cannot change directory. Exiting."
  exit 6
}

numberOfCopiedFiles=0

bash "$BIN_DIR/do_backup.sh" "$INPUT_FOLDER" "$FILE_SUFFIX" "$MEDIA_TYPE" "$SOURCE_DEVICE_ID" &

childPid=$!

wait $childPid

numberOfCopiedFiles=$(cat "$childPid.mfb.pid")

rm "$childPid.mfb.pid" || echo "Cannot remove temporary files."

if [[ $numberOfCopiedFiles -gt 0 ]]; then
  printf '\nNumber of files copied: %s.\nDone. Goodbye :)\n' "$numberOfCopiedFiles"
else
  echo "All files are up to date. Nothing to do. Have a good day :)"
fi

printf "*********************************************************\n\n"
exit 0
