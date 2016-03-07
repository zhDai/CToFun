#encoding:utf-8
#爬国家最新县及县以上行政区划代码

from bs4 import BeautifulSoup

import urllib2 
response = urllib2.urlopen('http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201504/t20150415_712722.html') 
html = response.read()
soup=BeautifulSoup(html)
aa=soup.find_all('p') 
f=open('a1.txt','w+')
for i in range(len(aa)): 
    if len(aa[i].find_all('span'))==3:
        for j in [0,2]:
            f.write(aa[i].find_all('span')[j].text.strip().encode('utf-8'))
            f.write(' ')
    elif len(aa[i].find_all('span'))==2:
        for j in [0]:
            f.write(aa[i].find_all('span')[j].text.strip().encode('utf-8'))
            f.write(' ')       
    f.write('\n')
f.close()
        
    

    
