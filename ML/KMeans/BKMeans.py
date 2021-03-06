# coding:utf-8
#二分KMeans方法
#依赖于KMeans里面的一些方法
import numpy as np
from KMeans import *
from pylab import *

#进行多次（n次），寻找到近似最小SSE
def choose_min_SSE(n,data_1):
    min_sse = np.inf
    i = 0
    while i<n :
        data_2=choose_initil_point(2, data_1)
        [outcomes,poly_point]=k_means(data_1, data_2)
        sse = 0
        sse1 = 0
        sse2 = 0
        for j in xrange(len(data_1)):
            if outcomes[j][0]==0:
                sse1 += pow(data_1[j][0]-data_2[0][0],2)+pow(data_1[j][1]-data_2[0][1],2)
            elif outcomes[j][0]==1:
                sse2 += pow(data_1[j][0]-data_2[1][0],2)+pow(data_1[j][1]-data_2[1][1],2)
            else:
                pass
        sse = sse1+sse2
        if sse < min_sse:
            min_sse = sse
            min_sse1 = sse1
            min_sse2 = sse2
            data1 = []
            data2 = []
            for j in xrange(len(data_1)):
                if outcomes[j][0]==0:
                    data1.append(data_1[j])
                elif outcomes[j][0]==1:
                    data2.append(data_1[j])
            poly_point2 = poly_point
            
            
        i+=1
    return min_sse1,min_sse2,data1,data2,poly_point2

#这个函数是为了寻找字典里面的最大的sse，以便继续划分
def choose_max_SSE(rad):
    maxvalue = 0
    for key in rad.keys():
        if maxvalue<key:
            maxvalue = key
    return maxvalue 

#二分Kmeans方法
def Bk_means(k,dataname):
    data_1=openfile(dataname)
    i = 2
    n = 50 #尝试次数
    rad = {}
    [min_sse1,min_sse2,data1,data2,poly_point2] = choose_min_SSE(n, data_1)
    rad[min_sse1] = data1,poly_point2[0]
    rad[min_sse2] = data2,poly_point2[1]
    while i<k:      
        svalue = choose_max_SSE(rad) 
        data_1 = rad[svalue][0]
        del rad[svalue]
        [min_sse1,min_sse2,data1,data2,poly_point2] = choose_min_SSE(n, data_1)
        rad[min_sse1] = data1,poly_point2[0]
        rad[min_sse2] = data2,poly_point2[1]       
        i+=1
        
    return rad

    
if __name__ == "__main__":
    rad = Bk_means(4, 'Kmean_test.txt')
    data_1=openfile('Kmean_test.txt')
    aa = []
    for ivalue in rad.itervalues():
        aa.append(ivalue[1])
    bb = []
    for ikey in rad.iterkeys():
        bb.append(ikey)
    print sum(bb)
    hua_tu(data_1, aa)
    
