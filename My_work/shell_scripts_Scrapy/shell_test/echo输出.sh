#!/bin/bash
# filename is while.sh
count=0
while(($count < 5 ));do
echo -n " count=" $count
let count=count+1
done
