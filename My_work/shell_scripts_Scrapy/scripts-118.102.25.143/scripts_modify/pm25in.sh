#!/bin/bash

function ergodic(){
for file in ` ls $1`
do
                if [ -d $1"/"$file ] #如果 file存在且是一个目录则为真
                then
                      ergodic $1"/"$file
                else
                      local path=$1"/"$file #得到文件的完整的目录
                      local name=$file       #得到文件的名字
		      export PGPASSWORD=123456
                      /opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "\copy pm25in.pm25in from '"$path"'" 
               fi

done
}
INIT_PATH=$1
ergodic $INIT_PATH 
