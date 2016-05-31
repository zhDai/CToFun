#!/bin/bash

initpath=$1
if [ -d $initpath ];then
	cd $initpath
	for name in `ls`
	do
		file1=${initpath}/${name}
		if [ -d ${file1} ];then
			cd $file1
			export PGPASSWORD=123456
			for name1 in `ls`
			do
				cat $name1 | tr -d ["\r"] > /home/zhdai/useless/n1.txt
				cut -d ";" -f 1,2,3,4,5,6,7,8,9,10,11,12,13 /home/zhdai/useless/n1.txt > /home/zhdai/useless/n2.txt
				file2=/home/zhdai/useless/n2.txt
				/opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "\copy weather_noaa_gov.data from '"$file2"' WITH DELIMITER ';'"
				rm -f /home/zhdai/useless/n1.txt /home/zhdai/useless/n2.txt
			done
		fi
	done
fi


