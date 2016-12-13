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
                      cat $path | tr -d ["\r"] > /home/zhdai/useless/n1.txt
                      cut -d ";" -f 1,2,3,4,5,6,7,8,9,10,11,12,13 /home/zhdai/useless/n1.txt > /home/zhdai/useless/n2.txt
                      file2=/home/zhdai/useless/n2.txt
                      /opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "\copy weather_noaa_gov.data from '"$file2"' WITH DELIMITER ';'"
                      rm -f /home/zhdai/useless/n1.txt /home/zhdai/useless/n2.txt

               fi

done
}
INIT_PATH=$1
ergodic $INIT_PATH
