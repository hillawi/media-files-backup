#!/bin/bash

###################################################################
# Script Name	: automatic_backup_launcher.sh
# Description	: Automatically launch of the backup process
# Args        : The connection type (USB, OTP,..) and the device
#               UUID (for USB) or PRODUCT ID (for OTP)
# Author      : Ahmed Abdeen
# Last update : 2022/07/30
###################################################################

# Debug option, comment out if not needed
#set -x

# Log file
LOG_FILE="$MFB_HOME/log/automatic_backup_launcher-$(date +"%Y-%m-%d")"

# Work directory
WORK_DIR="$MFB_HOME/work"

[[ -f $LOG_FILE ]] || touch "$LOG_FILE"

# Error codes
ERROR_INPUT_PARAM_INVALID=1
ERROR_UNKNOWN_DEVICE_ID=2
ERROR_DIR_NOT_FOUND=3
ERROR_BACKUP_PROCESS_ONGOING=4
ERROR_BACKUP_ERROR=5

if [[ $# -ne 2 ]]; then
  printf "The connection type and the device UUID/PRODUCT ID are required\n" >>"$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

[[ -d "$WORK_DIR" ]] || {
  printf "Work directory not found. Trying to create it\n" >>"$LOG_FILE"
  mkdir "$WORK_DIR" || exit $ERROR_DIR_NOT_FOUND
}

[[ -f "$WORK_DIR/mfb_automatic_backup_launcher.pid" ]] && {
  printf "Another backup process in progress. Exiting\n" >>"$LOG_FILE"
  exit $ERROR_BACKUP_PROCESS_ONGOING
}

DEVICE_CONNECTION_TYPE=$1
DEVICE_UUID=$2

BIN_DIR=$MFB_BIN_DIR
DEVICES_CONF_FILE="$MFB_CONF_DIR/devices.json"

# Read the devices list from the configuration file
devices=$(cat "$DEVICES_CONF_FILE")

if [[ "$DEVICE_CONNECTION_TYPE" == "USB" ]]; then
  deviceId=$(echo "$devices" | jq -r --arg DEVICE_UUID "$DEVICE_UUID" '.[] | select(.uuid.fs==$DEVICE_UUID) | .id')
elif [[ "$DEVICE_CONNECTION_TYPE" == "OTP" ]]; then
  deviceId=$(echo "$devices" | jq -r --arg DEVICE_UUID "$DEVICE_UUID" '.[] | select(.uuid.idProduct==$DEVICE_UUID) | .id')
else
  printf "Unknown device connection type %s. Known types are USB and OTP\n" "$DEVICE_CONNECTION_TYPE" >>"$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

[[ "$(echo "$deviceId" | awk '{print length}')" -gt 0 ]] || {
  printf "Cannot find device with UUID %s\n" "$DEVICE_UUID" >>"$LOG_FILE"
  exit $ERROR_UNKNOWN_DEVICE_ID
}

deviceType=$(echo "$devices" | jq -r --arg DEVICE_ID "$deviceId" '.[] | select(.id==$DEVICE_ID) | .type')

# Wait for the devices to be ready (properly mounted)
printf "Waiting for the devices to be ready\n" >>"$LOG_FILE"
sleep 3

printf "device id is %s\n" "$deviceId" >>"$LOG_FILE"

if [[ "$deviceType" == "source" ]]; then
  printf "Detected a source device\n" >>"$LOG_FILE"

  while read -r tmp; do
    [[ "$(echo "$tmp" | awk '{print length}')" -gt 0 ]] || {
      printf "No target devices available\n" >>"$LOG_FILE"
      echo "${deviceId}" >"${WORK_DIR}/${deviceType}.device"
      break
    }

    printf "%s" "$$" >>"$WORK_DIR/mfb_automatic_backup_launcher.pid"

    p=$(printf '%s\n' "$tmp")
    printf "%s\n" "$p" >>"$LOG_FILE"

    targetDeviceId=$(cat "$p")

    # Backing up images
    bash "$BIN_DIR/$MFB_SCRIPT_NAME" "$deviceId" "$targetDeviceId" IMG &
    childPid=$!
    wait $childPid
    exitCode=$?
    [[ $exitCode -eq 0 ]] || {
      printf "Backup failed\n" >>"$LOG_FILE"
      rm "$p" 2>/dev/null
      rm "$WORK_DIR/mfb_automatic_backup_launcher.pid" 2>/dev/null
      exit $ERROR_BACKUP_ERROR
    }

    rm "$p" 2>/dev/null
  done <<<"$(find "$WORK_DIR/" -type f -name "target.device" | sort)"
elif [[ "$deviceType" == "target" ]]; then
  printf "Detected a target device\n" >>"$LOG_FILE"

  while read -r tmp; do
    [[ "$(echo "$tmp" | awk '{print length}')" -gt 0 ]] || {
      printf "No source devices available\n" >>"$LOG_FILE"
      echo "${deviceId}" >"${WORK_DIR}/${deviceType}.device"
      break
    }

    printf "%s" "$$" >>"$WORK_DIR/mfb_automatic_backup_launcher.pid"

    p=$(printf '%s\n' "$tmp")
    printf "%s\n" "$p" >>"$LOG_FILE"

    targetDeviceId=$(cat "$p")

    # Backing up images
    bash "$BIN_DIR/$MFB_SCRIPT_NAME" "$targetDeviceId" "$deviceId" IMG &
    childPid=$!
    wait $childPid
    exitCode=$?
    [[ $exitCode -eq 0 ]] || {
      printf "Backup failed\n" >>"$LOG_FILE"
      rm "$p" 2>/dev/null
      rm "$WORK_DIR/mfb_automatic_backup_launcher.pid" 2>/dev/null
      exit $ERROR_BACKUP_ERROR
    }

    rm "$p" 2>/dev/null
  done <<<"$(find "$WORK_DIR/" -type f -name "source.device" | sort)"
else
  printf "Cannot find the device type. Please verify the configuration\n" >>"$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

rm "$WORK_DIR/mfb_automatic_backup_launcher.pid" 2>/dev/null
