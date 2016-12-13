#coding:utf-8

from __future__ import absolute_import
from __future__ import print_function
import numpy as np
from PIL import Image
import os
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation, Flatten
from keras.layers.advanced_activations import PReLU
from keras.layers.convolutional import Convolution2D, MaxPooling2D
from keras.optimizers import SGD, Adadelta, Adagrad
from keras.utils import np_utils, generic_utils
from six.moves import range
import random


#读取文件夹mnist下的42000张图片，图片为灰度图，所以为1通道，
#如果是将彩色图作为输入,则将1替换为3,图像大小28*28
def load_data():
    data = np.empty((42000,1,28,28),dtype="float32")
    label = np.empty((42000,),dtype="uint8")

    imgs = os.listdir("/home/daizhaohui/Downloads/mnist")
    num = len(imgs)
    for i in range(num):
        img = Image.open("/home/daizhaohui/Downloads/mnist/"+imgs[i])
        arr = np.asarray(img,dtype="float32")
        data[i,:,:,:] = arr
        label[i] = int(imgs[i].split('.')[0])
    return data,label



if __name__ == "__main__":
	data1,label1 = load_data()

	#打乱数据
	index = [i for i in range(len(data1))]
	print(len(index))
	random.shuffle(index)
	pix = 0.8
	index_train = index[:int(len(index)*pix)]
	index_test = index[int(len(index)*pix):]
	data = data1[index_train]
	label = label1[index_train]
	data_test = data1[index_test]
	label_test = label1[index_test]

	label = np_utils.to_categorical(label, 10)
	label_test = np_utils.to_categorical(label_test, 10)
	model = Sequential()
	model.add(Convolution2D(4,5,5,border_mode='valid',input_shape=(1,28,28)))
	model.add(Activation('tanh'))
	model.add(Convolution2D(8,3,3,border_mode='valid'))
	model.add(Activation('tanh'))
	model.add(MaxPooling2D(pool_size=(2, 2)))
	model.add(Convolution2D(16,3,3,border_mode='valid'))
	model.add(Activation('tanh'))
	model.add(MaxPooling2D(pool_size=(2, 2)))
	model.add(Flatten())
	model.add(Dense(128, init='normal'))
	model.add(Activation('tanh'))

	#Softmax分类，输出是10类别
	model.add(Dense(10, init='normal'))
	model.add(Activation('softmax'))
	sgd = SGD(lr=0.05, decay=1e-6, momentum=0.9, nesterov=True)
	model.compile(loss='categorical_crossentropy', optimizer=sgd,metrics=["accuracy"])
	model.fit(data, label, batch_size=100,nb_epoch=10,shuffle=True,verbose=1,validation_split=0.2)
	model.evaluate(data_test, label_test, batch_size=50, verbose=1) 
	json_string = model.to_json()    
	open('savemodel8.20.json','w').write(json_string)    
	model.save_weights('savemodel_weights8.20.h5')  

