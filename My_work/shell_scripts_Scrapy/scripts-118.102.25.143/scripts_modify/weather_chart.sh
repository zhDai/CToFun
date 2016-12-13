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
		      local name1=${path%/*} 
		      local name2=${name1%/*} 
                      local name3=${name2##*/}     
                      export PGPASSWORD=123456
                      /opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "insert into weather_chart.china(date,adress,picpath)values("${name:0-21:17}",'"$name3"','"$path"')"

               fi

done
}
INIT_PATH=$1
ergodic $INIT_PATH
