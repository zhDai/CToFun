# coding:utf-8

import jieba
import sys
reload(sys)  
sys.setdefaultencoding('utf8')


jieba.load_userdict("/home/daizhaohui/Documents/all/document/deep_learn/jieba/test.txt")
a = "戴兆辉喜欢吃鱼香肉丝!"

b = jieba.cut(a)

print "Default Mode:", "/ ".join(b)
