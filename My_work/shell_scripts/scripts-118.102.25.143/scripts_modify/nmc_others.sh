#!/bin/bash

function ergodic(){
for file in ` ls $1`
do
                if [ -d $1"/"$file ] 
                then
                      ergodic $1"/"$file
                else
                      local path=$1"/"$file 
                      local name=$file
		      local name1=${path%/*}
                      local name11=${name1##*/}
                      local name2=${name1%/*}
                      local name21=${name2##*/}       
                      export PGPASSWORD=123456
                      /opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "insert into nmc_others.nmc_others(date,optionA,optionB,picpath)values("${name:0-21:17}",'"$name21"','"$name11"','"$path"')"
                fi

done
}
INIT_PATH=$1
ergodic $INIT_PATH
