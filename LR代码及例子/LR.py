#LR 
from numpy import *
from pylab import *

def openfile(file):
    file_1=open(file)
    data_1=[]
    data_2=[]
    for eachline in file_1.readlines():
        eachline_1=eachline.strip().split('\t')
        data_1.append([1.0,float(eachline_1[0]),float(eachline_1[1])])
        data_2.append(map(float,eachline_1[2]))
    return array(data_1),array(data_2)

def sigmoid(inX):  
    return 1.0 / (1 + exp(-inX))

def LR(data_1,data_2,opt):
    data_matrix=mat(data_1)
    alfa=opt['alfa']
    Max_iter=opt['Max_iter']
    weight=opt['weight']
    for k in range(Max_iter):
        h=sigmoid(data_matrix*weight)
        err=data_2-h
        weight=weight+alfa*(data_matrix.transpose())*err
    return weight
    
def hua_tu(amout,amout_1,weight):  
    x1=[min(amout[:,1]),max(amout[:,1])]
    y1=[]
    for x2 in x1:
        y=(-weight[0]-weight[1]*x2)/weight[2]
        y1.append(float(y))
    plot(x1,y1,'g-')
    hold('on')
    x1=[];y1=[];x2=[];y2=[]
    for i in range(len(amout_1)):
        m=amout_1[i,0]
        if m==1:
            x1.append(amout[i][1])
            y1.append(amout[i][2])
        else:
            x2.append(amout[i][1])
            y2.append(amout[i][2])
    plot(x1,y1,'b.',x2,y2,'r.')      
    show()

data_1,data_2=openfile('test1.txt')
m,n=shape(data_1)
opt={'alfa': 0.01, 'Max_iter':500, 'weight':ones((n,1))}   
weight=LR(data_1,data_2,opt)
#print shape(weight)##(3*1)
hua_tu(data_1,data_2,weight)
