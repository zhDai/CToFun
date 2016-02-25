#_*_ coding:utf-8 _*_

import sys,os,getpass

#######################################################################
def start_screen():
    print ('''
*****************************************************************************
If you have one account,then choose "登录(L)";if none, please choose "注册(R)"
                                 So L or R?
*****************************************************************************
''')
    comein = raw_input("Your choice:")
    return comein

######################对raw_input输入字符类型判断并转化#####################
def input_handle(s):
    if str.isdigit(s):                            ###对输入是否是数字进行判断###
        s = int(s)                                ###如果输出的是个数字，则转化为整数类型###
    return s


####################框架函数###################################
def framework(name='',balance=''):
    os.system('cls')                             ###清屏###
    balance = int(balance)
    print('''
******************************************************************
*                                                                *
*                          欢迎来到sky购物平台                    *
*                                                                *
******************************************************************                                                                *
                                       会员：%s   当前余额：%d
''' % (name,balance))

########################商品列表展示函数#######################
def shop_show(shop_dict):

    res_dict = {}
    ############对商品列表进行遍历并加上数字编号###############
    i = 1
    print('商品列表 ：')
    print('================================================================')
    print('%-5s \t %-20s \t %-10s \t %-10s' % ('编号','商品名称','商品价格(元)','商品数量(个)'))
    for k in shop_dict:
        v = shop_dict[k]
        if type(v) is dict:
            print('%-5d \t %-20s \t %-10s \t %-10s' % (i,k,v['price'],v['num']))
            res_dict[i] = [k,v['price'],v['num']]
        i += 1
    print('================================================================')
    print('q : Exit')
    return res_dict

#########################购物车函数#############################
def shopping_cart(shop_cart):

    ############对商品列表进行遍历并加上数字编号###############
    print('###################欢迎来到您的购物车##################')
    print('%-20s \t %-10s \t %-10s \t %-10s' % ('商品名称','商品价格(元)','购买数量(个)','购买总金额（元）'))
    for k in shop_cart:
        v = shop_cart[k]
        if type(v) is list:
            print('%-20s \t %-10d \t %-10d \t %-10d' % (k,v[0],v[1],v[2]))

    print('###################请确认您购买商品####################')

########################################################################
def main_():
    i = 0
    while i < 3:                                                       #只要用户登录异常不超过3次就不断循环
        name = raw_input('请输入用户名：')                             #输入用户名
        passwd = raw_input('请输入密码：')                             #输入隐藏密码
        user_file = open('info.txt','r')                              #打开帐号文件
        user_list = user_file.readlines()                               
        for user_line in user_list:                                     #对帐号文件进行遍历
            (user,password,balance) = user_line.strip('\n').split()     #分别获取帐号、密码信息和当前余额
            balance = int(balance)
            if name == user and passwd == password:                     #如用户名和密码正常匹配
                my_shop_cart = {}
                first_flag = 1
                while first_flag:
                    framework(user,balance)
                    new_dict = shop_show(shopping_dict)
                    shop_index = raw_input('请输入商品编号 | 退出(q): ')
                    ###############如果输入非空，对输入进行判断并转化类型###########
                    if len(shop_index) != 0:
                        shop_index = input_handle(shop_index)

                    if shop_index == 'q':                            ###如果输入为q,则退出程序###
                        sys.exit(0)
                    elif shop_index in new_dict:
                        (shop_name,shop_price,shop_num) = (new_dict[shop_index][0], new_dict[shop_index][1], new_dict[shop_index][2])
                        print('商品信息 【 名称：%-15s \t 价格：%-5d（元） \t 数量：%-5d（个）】' % (shop_name,shop_price,shop_num))

                        second_flag = 1
                        while second_flag:
                            shop_num = raw_input('请输入购买商品个数 | 返回(b) | 退出(q): ')
                            if len(shop_num) != 0:
                                shop_num = input_handle(shop_num)
                            if shop_num == 'q':                            ###如果输入为q,则退出程序###
                                sys.exit(0)
                            elif shop_num == 'b':
                                break
                            elif shop_num > 0 and shop_num <= new_dict[shop_index][2]:
                                shop_sum = shop_price * shop_num
                                if shop_sum <= balance:
                                    print('购买商品 %s 总价格为 : %d' % (shop_name,shop_sum))
                                    add_flag = raw_input('请确认是否加入购物车（y | n）：')
                                    if add_flag == 'y':
                                        my_shop_cart[shop_name] = [shop_price,shop_num,shop_sum]

                                        balance -= shop_sum
                                        shopping_dict[shop_name]['num'] -= shop_num
                                        second_flag = 0
                                    else:
                                        break
                                else:
                                    print('您的余额不足，请充值或重新选择，谢谢')
                            else:
                                pass
                        shopping_cart(my_shop_cart)
                    else:
                        pass
	else:
	    if i != 2:
		print('用户或密码错误，请重新输入，还有 %d 次机会' % (2 - i))
	i += 1
    else: 
        sys.exit('用户或密码输入错误超过三次，退出系统,欢迎下次光临')              #用户输入三次错误后，异常退出
    user_file.close()                                                     #关闭帐号文件

################################主程序开始################################

shopping_dict = {
             'iphone6': {'price':6000,'num':10},
             'ipad': {'price':3000,'num':20},
             'mi4': {'price':2000,'num':43},
             'huawei6_plus': {'price':1999,'num':8},
}

start = start_screen()
if start == "L":
    main_()
elif start == "R":
    judge = 1
    while judge:
        Rname = raw_input("注册的用户名：")
        Rpassword = raw_input("设置的密码:")
	f = open('info.txt','r')
        #with open('info.txt','wr') as f:
        for line in f.readlines():
            (user,password,balance)=line.strip().split()
            if Rname == user:
	        judge = 1
	        print ('''
        *********************************************
                  改用户名已被注册，请重新输入！
        *********************************************
        ''')
		break
	    else:
		judge = 0
	f.close()
	if judge == 0:
	    f = open('info.txt','a')
            f.write(Rname+" "+Rpassword+" "+"0\n")
	    f.close()
	    print "祝贺你注册成功，返回登录(R)|退出(Q)"
	    chosen = raw_input("您的选择:")
	    if chosen == "R":
		main_()
	    elif chosen == "Q":
		sys.exit(0)
else:
    sys.exit(0)   
	    
	                                                         

                                             
