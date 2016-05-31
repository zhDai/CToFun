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
						if [ -f $file3 ];then
							/opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "insert into nmc_others.nmc_others(date,optionA,optionB,picpath)values("${name2:0-21:17}",'"$name1"','"$name1"','"$file3"')"
						else
							cd $file3
							export PGPASSWORD=123456
							for name3 in `ls`
							do
								file4=$file3/$name3
								/opt/PostgreSQL/9.5/bin/psql -U zhdai -d zbase -c "insert into nmc_others.nmc_others(date,optionA,optionB,picpath)values("${name3:0-21:17}",'"$name1"','"$name2"','"$file4"')"
							done
						fi

					done
				fi
			done
		fi
	done
fi


