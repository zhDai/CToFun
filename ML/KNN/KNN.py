'''
Created on Apr 5, 2016

@author: daizhaohui
'''
# coding:utf-8
#This part is ready for KNN algorithm

import numpy as np
from math import sqrt


def CreateDataSet():
    dataset = np.array([[1.0, 1.1], [1.0, 1.0], [5., 2.], [5.0, 0.1]])
    labels = ['A', 'A', 'B', 'B'] 
    return dataset, labels

#inputdata is a input dataset which is unknown 
#dataset is a already known dataset
#labels match dataset
#k means match 
def KNN(inputdata, dataset, labels, k):
    aa = {}
    for i in range(len(dataset)):
        dis = sqrt(pow(inputdata[0]-dataset[i][0],2)+pow(inputdata[1]-dataset[i][1], 2))
        bb = [dis,labels[i]]
        aa[i] = bb
    
    
    print aa
    cc = sorted(aa.items(), key = lambda t: t[1][0])
    
    Anum = 0
    Bnum = 0
    for j in range(k):
        if cc[j][1][1] == "A":
            Anum += 1
        elif cc[j][1][1] == "B":
            Bnum += 1
        else:
            print "Wrong! Don't match anything!"
    
    if Anum > Bnum:
        print "This set match 'A'!"
    else:
        print "This set match 'B'!"


if __name__ == '__main__':
    dataset, labels = CreateDataSet()
    inputdata = [0,0]
    k = 3
    KNN(inputdata, dataset, labels, k)