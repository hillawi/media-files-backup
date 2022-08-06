#!/bin/bash

###################################################################
# Script Name	: automatic_launcher.sh
# Description	: Automatically launch of the backup process
# Args        : The connection type (USB, OTP,..) and the device
#               UUID (for USB) or PRODUCT ID (for OTP)
# Author      : Ahmed Abdeen
# Last update : 2022/07/30
###################################################################

# Debug option, comment out if not needed
#set -x

# Log file
LOG_FILE="$MFB_HOME/log/automatic_launcher-$(date +"%Y-%m-%d")"

[[ -f $LOG_FILE ]] || touch "$LOG_FILE"

# Error codes
ERROR_INPUT_PARAM_INVALID=1
ERROR_UNKNOWN_DEVICE_ID=2

if [[ $# -ne 2 ]]; then
  printf "The connection type and the device UUID/PRODUCT ID are required\n" >> "$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

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
  printf "Unknown device connection type %s. Known types are USB and OTP\n" "$DEVICE_CONNECTION_TYPE" >> "$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

[[ "$(echo "$deviceId" | awk '{print length}')" -gt 0 ]] || {
  printf "Cannot find device with UUID %s\n" "$DEVICE_UUID" >> "$LOG_FILE"
  exit $ERROR_UNKNOWN_DEVICE_ID
}

deviceType=$(echo "$devices" | jq -r --arg DEVICE_ID "$deviceId" '.[] | select(.id==$DEVICE_ID) | .type')

if [[ "$deviceType" == "source" ]]; then
  printf "Detected a source device\n" >> "$LOG_FILE"
  # Check for target devices
elif [[ "$deviceType" == "target" ]]; then
  printf "Detected a target device\n" >> "$LOG_FILE"
  # Check for source devices
else
  printf "Cannot find the device type. Please verify the configuration\n" >> "$LOG_FILE"
  exit $ERROR_INPUT_PARAM_INVALID
fi

printf "device id is %s\n" "$deviceId" >> "$LOG_FILE"