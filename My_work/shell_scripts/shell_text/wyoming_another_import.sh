#!/bin/bash

#--------爬虫-----------
cd /home/daizhaohui/Desktop/dzh/emdata/wyoming_another          #切换到项目位置
./yesterday.sh /home/daizhaohui/Desktop/dzh/download       #保存文件的位置

#--------导入数据库--------------
cd /home/daizhaohui/Desktop/dzh/download/$(date -d "1 day ago" +"%Y-%m-%d") #切换到csv文件所在目录
#filepath=$(cd "$(dirname "$0")"; pwd)
filepath=$(pwd)

export PGPASSWORD=123
for name in `ls`
do
	dir=${filepath}/${name}
 	if [ -s ${dir} ] ;then
		echo 'get it!'
		order="copy crawl.wyoming_another from '"${dir}"'"            #数据是放在crawl下的wyoming_another表中
		/opt/PostgreSQL/9.4/bin/psql -d em2 -U postgres -c "${order}" #执行copy语句
		mv ${dir} /home/daizhaohui/Desktop/dzh/done                   #把copy完毕的csv文件放到done文件下（done可以修改）
	else
		continue
	fi	
done
cd /home/daizhaohui/Desktop/dzh/download               
rm -rf /home/daizhaohui/Desktop/dzh/download/$(date -d "1 day ago" +"%Y-%m-%d")

