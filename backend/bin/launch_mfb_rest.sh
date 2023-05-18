#!/bin/bash

java -jar -Dspring.profiles.active=local /opt/mfb/bin/mfb-rest.jar > /opt/mfb/log/mfb-rest.log 2>&1 &

echo $! > /opt/mfb/pid/mfb-rest.pid
