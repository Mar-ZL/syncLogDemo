#!/bin/bash
a=$1
b=$2
ip=$3
port=$4
passwd=$5
/bin/rpm -qa|/bin/grep -q expect
if [ $? -ne 0 ];then
        echo "please install expect"
        exit
fi
if [ $# -ne 5 ];then
        echo "must 5 parameter,1:source file,2:object file,3:object ip 4:object port,5:passwd"
        exit
fi
expect -c "
  spawn scp -P $port dmmiso@$ip:$a $b
  expect {
    \"*assword\" {set timeout 300; send \"$passwd\r\";}
    \"yes/no\" {send \"yes\r\"; exp_continue;}
  }
  expect eof"	
