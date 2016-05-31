#! /bin/bash

function ergodic(){
for file in ` ls $1`
do
                if [ -d $1"/"$file ] 
                then
                      ergodic $1"/"$file
                else
                      local path=$1"/"$file 
                      local name=$file       
                      export PGPASSWORD=123456
                      /opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "\copy ftp_wmo_int.VolA_New from '"$path"' with encoding 'windows-1251';"
               fi

done
}
INIT_PATH=$1
ergodic $INIT_PATH
