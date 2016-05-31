/*
 * ParticleTracking.cpp
 *
 *  Created on: Jan 14, 2016
 *      Author: daizhaohui
 */

#include<iostream>
#include<cmath>
#include<vector>

using namespace std;

struct data{
	double length;
	double wide;
	double height;
	double windx;
	double windy;
	double windz;
	double gridx;
	double gridy;
	double gridz;
	double deltax;
	double deltay;
	double deltaz;
};

struct coordinate{
	double x;
	double y;
	double z;
};

float choose(float tx, float ty, float tz){
	float t,t1;
	if (tx > ty){
		t = ty;
	}else{
		t = tx;
	}
	if(t>tz){
		t1 = tz;
	}else{
		t1 = t;
	}
	return t1;
}
char match(float tx, float ty, float tz){
	char t_value,t_value1;
	float t;
	if (tx > ty){
		t = ty;
		t_value = 'y';
	}else{
		t = tx;
		t_value = 'x';
	}
	if(t>tz){
		t_value1 = 'z';
	}else{
		t_value1 = t_value;
	}
	return t_value1;
}

char match(float tx, float ty){
	char t_value;
	if (tx > ty){
		t_value = 'y';
	}else{
		t_value = 'x';
	}
	return t_value;
}
/*
 * move值的规则：
 * 0-> 不移动
 * 1-> +x轴方向移动
 * 2-> +y轴方向移动
 * 3-> +z轴方向移动
 * 4-> -x轴方向移动
 * 5-> -y轴方向移动
 * 6-> -z轴方向移动
 */
//x，y，z是网格长，宽，高，windx和windy是风xy方向的矢量（有正负号），pointx、pointy和pointz是点的坐标位置，gridx、gridy和gridz是左下角的坐标
//Time是剩余时间（比如在某个时间段下）
double* ParticleTracking(double x ,double y, double z,double windx, double windy, double windz, double pointx, double pointy, double pointz,double gridx, double gridy,double gridz, float Time){

	double* varr = new double[9];
	double lenx,leny,lenz,move;
	float tx,ty,tz,t;
	double pnextx,pnexty,pnextz;
	char f;
	bool judge = false; //是否终止
	//需要判断风的xy方向（正负号）
	//NO.1
	if(windx>0 && windy>0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(gridy+y-pointy);
		lenx = abs(gridx+x-pointx);
		tz = lenz/windz;
		ty = leny/windy;
		tx = lenx/windx;
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if(f == 'x'){
			move = 1;
		}else if( f == 'y'){
			move = 2;
		}else if( f == 'z'){
			move = 3;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time - t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
		    pnexty = pointy+Time*windy;
		    pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.2
	}else if(windx>0 && windy>0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(gridy+y-pointy);
		lenx = abs(gridx+x-pointx);
		tz = lenz/abs(windz);
		ty = leny/windy;
		tx = lenx/windx;
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if(f == 'x'){
			move = 1;
		}else if( f == 'y'){
			move = 2;
		}else if( f == 'z'){
			move = 6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time - t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.3
	}else if(windx>0 && windy>0 && windz==0){
		leny = abs(gridy+y-pointy);
		lenx = abs(gridx+x-pointx);
		ty = leny/windy;
		tx = lenx/windx;
		t = min(tx,ty);
		f = match(tx,ty);
		if (f == 'x'){
			move = 1;
		}else if( f == 'y'){
			move = 2;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time - t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.4
	}else if(windx>0 && windy<0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(pointy-gridy);
		lenx = abs(gridx+x-pointx);
		tz = lenz/windz;
		ty = leny/abs(windy);
		tx = lenx/windx;
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 1;
		}else if(f == 'y'){
			move = 5;
		}else if(f == 'z'){
			move = 3;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.5
	}else if(windx>0 && windy<0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(pointy-gridy);
		lenx = abs(gridx+x-pointx);
		tz = lenz/abs(windz);
		ty = leny/abs(windy);
		tx = lenx/windx;
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 1;
		}else if(f == 'y'){
			move = 5;
		}else if(f == 'z'){
			move =6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.6
	}else if(windx>0 && windy<0 && windz==0){
		leny = abs(pointy-gridy);
		lenx = abs(gridx+x-pointx);
		ty = leny/abs(windy);
		tx = lenx/windx;
		t = min(tx,ty);
		f = match(tx,ty);
		if (f == 'x'){
			move = 1;
		}else if(f == 'y'){
			move = 5;
		}else{
			cout<<"wrong value"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.7
	}else if(windx<0 && windy>0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(gridy+y-pointy);
		lenx = abs(pointx-gridx);
		tz = lenz/windz;
		ty = leny/windy;
		tx = lenx/abs(windx);
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 2;
		}else if(f == 'z'){
			move = 3;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.8
	}else if(windx<0 && windy>0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(gridy+y-pointy);
		lenx = abs(pointx-gridx);
		tz = lenz/abs(windz);
		ty = leny/windy;
		tx = lenx/abs(windx);
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 2;
		}else if(f == 'z'){
			move =6;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.9
	}else if(windx<0 && windy>0 && windz==0){
		leny = abs(gridy+y-pointy);
		lenx = abs(pointx-gridx);
		ty = leny/windy;
		tx = lenx/abs(windx);
		t = min(tx,ty);
		f = match(tx,ty);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 2;
		}else{
			cout<<"wrong value"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.10
	}else if(windx<0 && windy<0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(pointy-gridy);
		lenx = abs(pointx-gridx);
		tz = lenz/windz;
		ty = leny/abs(windy);
		tx = lenx/abs(windx);
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 5;
		}else if(f == 'z'){
			move = 3;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.11
	}else if(windx<0 && windy<0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(pointy-gridy);
		lenx = abs(pointx-gridx);
		tz = lenz/abs(windz);
		ty = leny/abs(windy);
		tx = lenx/abs(windx);
		t = choose(tx,ty,tz);
		f = match(tx,ty,tz);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 5;
		}else if(f == 'z'){
			move = 6;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.12
	}else if(windx<0 && windy<0 && windz==0){
		leny = abs(pointy-gridy);
		lenx = abs(pointx-gridx);
		ty = leny/abs(windy);
		tx = lenx/abs(windx);
		t = min(tx,ty);
		f = match(tx,ty);
		if (f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 5;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.13
	}else if(windx == 0 && windy > 0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(gridy+y-pointy);
		tz = lenz/windz;
		ty = leny/windy;
		t = min(ty,tz);
		f = match(ty,tz);
		if (f == 'x'){
			move = 2;
		}else if(f == 'y'){
			move = 3;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.14
	}else if(windx == 0 && windy > 0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(gridy+y-pointy);
		tz = lenz/abs(windz);
		ty = leny/windy;
		t = min(ty,tz);
		f = match(ty,tz);
		if (f == 'x'){
			move = 2;
		}else if(f == 'y'){
			move = 6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.15
	}else if(windx == 0 && windy > 0 && windz==0){
		leny = abs(gridy+y-pointy);
		t = leny/windy;
		move = 2;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.16
	}else if(windx == 0 && windy < 0 && windz>0){
		lenz = abs(gridz+z-pointz);
		leny = abs(pointy-gridy);
		tz = lenz/windz;
		ty = leny/abs(windy);
		t = min(ty,tz);
		f = match(ty,tz);
		if (f == 'x'){
			move = 5;
		}else if(f == 'y'){
			move = 3;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.17
	}else if(windx == 0 && windy < 0 && windz<0){
		lenz = abs(pointz-gridz);
		leny = abs(pointy-gridy);
		tz = lenz/abs(windz);
		ty = leny/abs(windy);
		t = min(ty,tz);
		f = match(ty,tz);
		if (f == 'x'){
			move = 5;
		}else if(f == 'y'){
			move = 6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.18
	}else if(windx == 0 && windy < 0 && windz == 0){
		leny = abs(pointy-gridy);
		t = leny/abs(windy);
		move = 5;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy+t*windy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy+Time*windy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.19
	}else if(windx>0 && windy == 0 && windz>0){
		lenz = abs(gridz+z-pointz);
		lenx = abs(gridx+x-pointx);
		tz = lenz/windz;
		tx = lenx/windx;
		t = min(tx,tz);
		f = match(tx,tz);
		if (f == 'x'){
			move = 1;
		}else if(f == 'y'){
			move = 3;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.20
	}else if(windx>0 && windy == 0 && windz<0){
		lenz = abs(pointz-gridz);
		lenx = abs(gridx+x-pointx);
		tz = lenz/abs(windz);
		tx = lenx/windx;
		t = min(tx,tz);
		f = match(tx,tz);
		if(f == 'x'){
			move = 1;
		}else if(f == 'y'){
			move = 6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.21
	}else if(windx>0 && windy == 0 && windz == 0){
		lenx = abs(gridx+x-pointx);
		t = lenx/windx;
		move = 1;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.22
	}else if(windx<0 && windy ==0 && windz >0){
		lenz = abs(gridz+z-pointz);
		lenx = abs(pointx-gridx);
		tz = lenz/windz;
		tx = lenx/abs(windx);
		t = min(tx,tz);
		f = match(tx,tz);
		if(f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 3;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.23
	}else if(windx<0 && windy ==0 && windz <0){
		lenz = abs(pointz-gridz);
		lenx = abs(pointx-gridx);
		tz = lenz/abs(windz);
		tx = lenx/abs(windx);
		t = min(tx,tz);
		f = match(tx,tz);
		if(f == 'x'){
			move = 4;
		}else if(f == 'y'){
			move = 6;
		}else{
			cout<<"wrong value!"<<endl;
		}
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.24
	}else if(windx<0 && windy ==0 && windz == 0){
		lenx = abs(pointx-gridx);
		t = lenx/abs(windx);
		move = 4;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx+t*windx;
			pnexty = pointy;
			pnextz = pointz;
			judge = false;
		}else{
			pnextx = pointx+Time*windx;
			pnexty = pointy;
			pnextz = pointz;
			judge = true;
			Time = 0;
		}
		//NO.25
	}else if(windx == 0 && windz == 0 && windz >0){
		lenz = abs(gridz+z-pointz);
		t = lenz/windz;
		move = 3;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.26
	}else if(windx == 0 && windz == 0 && windz <0){
		lenz = abs(pointz-gridz);
		t = lenz/abs(windz);
		move = 6;
		if (t < Time){
			Time = Time -t;
			pnextx = pointx;
			pnexty = pointy;
			pnextz = pointz+t*windz;
			judge = false;
		}else{
			pnextx = pointx;
			pnexty = pointy;
			pnextz = pointz+Time*windz;
			judge = true;
			Time = 0;
		}
		//NO.27
	}else{
		move = 0;
		Time = 0;
		judge = true;
		pnextx = pointx;
		pnexty = pointy;
		pnextz = pointz;
	}
	varr[0]=pointx;
	varr[1]=pointy;
	varr[2]=pointz;
	varr[3]=pnextx;
	varr[4]=pnexty;
	varr[5]=pnextz;
	varr[6]=Time;
	varr[7]=judge;
	varr[8]=move;
	return varr;
}

vector<coordinate> Get_Path(data* dalton,int numx, int numy, int numz, int gnumx, int gnumy, int gnumz, double px, double py, double pz, float Time){
	int n,nx,ny,nz;
	bool judge = false;  //是否终止
	float t = Time;
	nx = gnumx-1;
	ny = gnumy-1;
	nz = gnumz-1;
	//新建一个动态数组，储存点坐标
	vector<coordinate> array;
	coordinate coo;
	coo.x = px;
	coo.y = py;
	coo.z = pz;
	array.push_back(coo);
	while(not judge){
		n = nx + ny*numx+nz*numx*numy;
		double* b;
		b = ParticleTracking(dalton[n].length ,dalton[n].wide, dalton[n].height ,dalton[n].windx, dalton[n].windy, dalton[n].windz, px,
				py, pz,dalton[n].gridx, dalton[n].gridy, dalton[n].gridz,t);
		if (b[8] == 0){
			nx = nx+0;
			ny = ny+0;
			nz = nz+0;
		}else if(b[8] == 1){
			nx = nx+1;
			ny = ny+0;
			ny = ny+0;
		}else if(b[8] == 2){
			nx = nx+0;
			ny = ny+1;
			nz = nz+0;
		}else if(b[8] == 3){
			nx = nx+0;
			ny = ny+0;
			nz = nz+1;
		}else if(b[8] == 4){
			nx = nx-1;
			ny = ny+0;
			nz = nz+0;
		}else if(b[8] == 5){
			nx = nx+0;
			ny = ny-1;
			nz = nz+0;
		}else if(b[8] == 6){
			nx = nx+0;
			ny = ny+0;
			nz = nz-1;
		}else{
			cout<<"something wrong!"<<endl;
		}
//		cout<<b[8]<<endl;
//		for(int kk = 0; kk < 6; kk++){
//			cout<<b[kk]<<" ";
//		}
//		cout<<endl;
//		cout<<nx<<" "<<ny<<" "<<nz<<endl;
//		cout<<b[6]<<endl;
		coo.x = b[3];
		coo.y = b[4];
		coo.z = b[5];
		array.push_back(coo);
		px = b[3];
		py = b[4];
		pz = b[5];
		t = b[6];
		judge = b[7];
		//边界问题
		if(nx<0 || nx>numx || ny<0 || ny>numy || nz<0 || nz>numz){
//			cout<<"边界问题"<<endl;
			break;
		}
	}
	return array;
}

//int main(){
//	data a[64];
//	for(int i = 0; i <4; i++){
//		for(int j = 0; j <4; j++){
//			for(int k = 0; k <4; k++){
//				data da;
//				da.gridx=k*5;
//				da.gridy=j*5;
//				da.gridz=i*5;
//				da.windx=1;
//				da.windy=1;
//				da.windz=1;
//				da.length=5;
//				da.wide=5;
//				da.height=5;
//				a[k+j*4+i*16]=da;
//			}
//		}
//	}
////	(data* dalton,int numx, int numy, int numz, int
////	gnumx, int gnumy, int gnumz, double px, double py, double pz, float Time)
//	vector<coordinate> test;
//	test = Get_Path(a, 4, 4, 4, 1, 1, 1, 1, 1, 1, 7);
////	Get_Path(a, 3, 3, 3, 0, 0, 0, 1, 1, 1, 13);
//	for (unsigned int j = 0; j< test.size(); j++)
//	{
//		cout<<test[j].x<<" "<<test[j].y<<" "<<test[j].z<<endl;
//	}
//
//	return 0;
//}
