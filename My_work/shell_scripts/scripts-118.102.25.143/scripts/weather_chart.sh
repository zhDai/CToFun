#!/bin/bash

initpath=$1
if [ -d $initpath ];then
	cd $initpath
	for name in `ls`
	do
		file1=${initpath}/${name}
		if [ -d ${file1} ];then
			cd $file1
			for name1 in `ls`
			do
				file2=$file1/$name1
				if [ -d ${file2} ];then
					cd $file2
					for name2 in `ls`
					do
						file3=$file2/$name2
						cd $file3
						#export PGPASSWORD=123456
						for name3 in `ls`
						do
							file4=$file3/$name3
							cd $file4
							export PGPASSWORD=123456
							for name4 in `ls`
							do 
								file5=$file4/$name4								
								/opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "insert into weather_chart.china(date,adress,picpath)values("${name4:0-21:17}",'"$name2"','"$file5"')"
								
							done
						done

					done
				fi
			done
		fi
	done
fi


