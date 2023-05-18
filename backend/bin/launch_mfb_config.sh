#!/bin/bash

java -jar -Dspring.profiles.active=basic,offline /opt/mfb/bin/mfb-config.jar > /opt/mfb/log/mfb-config.log 2>&1 &

echo $! > /opt/mfb/pid/mfb-config.pid
