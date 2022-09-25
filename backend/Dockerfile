FROM node:18
LABEL maintainer="abdeenahmed2002@hotmail.com"

ENV NODE_ENV=production
ENV MFB_HOME=/opt/mfb
ENV MFB_BACKEND=$MFB_HOME/backend
ENV MFB_BIN_DIR=$MFB_BACKEND/system
ENV MFB_CONF_DIR=$MFB_BACKEND/conf
ENV MFB_DEVICE_DIR=$MFB_HOME/device
ENV MFB_SCRIPT_NAME=backup_launcher.sh
ENV MFB_DEVICES_CONF_PATH=$MFB_CONF_DIR/devices.json

#RUN apt-get update && apt-get install -y inotify-tools

RUN mkdir $MFB_HOME
RUN mkdir $MFB_HOME/backend
RUN mkdir $MFB_HOME/udev
RUN mkdir $MFB_HOME/work

WORKDIR $MFB_BACKEND
COPY . ./
WORKDIR $MFB_BACKEND/server/
RUN npm install --omit=dev

EXPOSE 8387
CMD [ "node", "server.js" ]
