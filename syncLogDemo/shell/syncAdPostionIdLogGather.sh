#!/bin/bash
date=`date -d "yesterday" +"%Y%m%d%H"`
if [ -z "$1" ]; then

    echo "1st argument is empty!"
else
   date="$1"
fi

echo "${date}"
java -jar syncAdPositionIdLog-0.0.1-SNAPSHOT.jar 0 Y ${date}
