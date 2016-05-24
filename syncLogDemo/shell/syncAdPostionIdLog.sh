#!/bin/bash
date=`date -d -1hour +%Y%m%d%H`

if [ -z "$1" ]; then

    echo "1st argument is empty!"
else
   date="$1"
fi

cd /dmmiso/shell/syncAdPostionIdLog

sh scpFile.sh  /dmmiso/log/log/platformLogger.log.${date} /dmmiso/shell/syncAdPostionIdLog/data/platformLogger.log.${date}_145 10.46.228.145 22 Mima#1215*dm

cp /dmmiso/log/log/platformLogger.log.${date} /dmmiso/shell/syncAdPostionIdLog/data/platformLogger.log.${date}_144

cd /dmmiso/shell/syncAdPostionIdLog/data

cat platformLogger.log.${date}_144> gather/platformLogger.log.${date}

cat platformLogger.log.${date}_145>> gather/platformLogger.log.${date}

cd /dmmiso/shell/syncAdPostionIdLog

sh scpFile.sh  /dmmiso/log/log/platformReceiveLogger.log.${date} /dmmiso/shell/syncAdPostionIdLog/data/platformReceiveLogger.log.${date}_145 10.46.228.145 22 Mima#1215*dm

cp /dmmiso/log/log/platformReceiveLogger.log.${date} /dmmiso/shell/syncAdPostionIdLog/data/platformReceiveLogger.log.${date}_144

cd /dmmiso/shell/syncAdPostionIdLog/data

cat platformReceiveLogger.log.${date}_144> gather/platformReceiveLogger.log.${date}

cat platformReceiveLogger.log.${date}_145>> gather/platformReceiveLogger.log.${date}

cd /dmmiso/shell/syncAdPostionIdLog

java -jar syncAdPositionIdLog-0.0.1-SNAPSHOT.jar 1 N ${date}