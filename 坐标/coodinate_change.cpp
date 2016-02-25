//============================================================================
// Name        : aaa.cpp
// Author      : daizhaohui
// Version     :
// Copyright   : Your copyright notice
// Description : coordinates change
//============================================================================

#include <iostream>
#include <string>
#include <math.h>
using namespace std;
#define pi 3.1415926

//theta是维度，alfa是经度
//求面坐标转化为平面坐标
void transform_1(float theta, float alfa, float theta_0, float alfa_0)
{
	float n,F,r,r_0,the,theta_1,theta_2,x,y,R;
    //Radius of sphere
    R=1.0;
    //standard parallels
    theta_1=33*pi/180;
    theta_2=45*pi/180;
    theta=theta*pi/180;
    alfa=alfa*pi/180;
    theta_0=theta_0*pi/180;
    alfa_0=alfa_0*pi/180;
    //---------------------------------------------------//
    n=log(cos(theta_1)/cos(theta_2))/log(tan(pi/4+theta_2/2)/tan(pi/4+theta_1/2));
	F=(cos(theta_1))*(pow(tan(pi/4+theta_1/2),n))/n;
	r_0=R*F/(pow(tan(pi/4+theta_0/2),n));
	r=R*F/(pow(tan(pi/4+theta/2),n));
	the=n*(alfa-alfa_0)*180/pi;

	//--------------------------------------------------//
	//calculate x, y
	x=r*sin(the*pi/180);
    y=r_0-r*cos(the*pi/180);

    cout<<"x:"<<x<<endl;
    cout<<"y:"<<y<<endl;
}

//x，y是平面坐标
//平面坐标转化维球面坐标
void transform_2(float x, float y, float theta_0, float alfa_0)
{
	float n,F,r,r_0,the,theta_1,theta_2,R,theta,alfa;
	//Radius of sphere
	R=1.0;
	//standard parallels
	theta_1=33*pi/180;
	theta_2=45*pi/180;
	theta_0=theta_0*pi/180;
	alfa_0=alfa_0*pi/180;
	//--------------------------------------//
	n=log(cos(theta_1)/cos(theta_2))/log(tan(pi/4+theta_2/2)/tan(pi/4+theta_1/2));
	F=cos(theta_1)*(pow(tan(pi/4+theta_1/2),n))/n;
	r_0=R*F/(pow(tan(pi/4+theta_0/2),n));
	r=pow((pow(x,2)+pow((r_0-y),2)),0.5);
	the=atan(x/(r_0-y))*180/pi;
	//-----------------------------------------//
	//calculate theta,alfa
	theta=2*atan(pow((R*F/r),(1/n)))*180/pi-90;
	alfa=the/n+alfa_0*180/pi;

	cout<<"theta:"<<theta<<endl;
	cout<<"alfa:"<<alfa<<endl;
}

//比率：球面表面积/平面面积
void area_value(float x,float y,float theta_0, float alfa_0)
{
	float R,theta_1,theta_2,n,F,r_0,the,theta,r,a,b,c,d,ratio;
    //Radius of sphere
    R=1.0;
    //standard parallels
    theta_1=33*pi/180;
    theta_2=45*pi/180;
    theta_0=theta_0*pi/180;
    alfa_0=alfa_0*pi/180;
    //--------------------------------------//
    n=log(cos(theta_1)/cos(theta_2))/log(tan(pi/4+theta_2/2)/tan(pi/4+theta_1/2));
    F=cos(theta_1)*(pow(tan(pi/4+theta_1/2),n))/n;
    r_0=R*F/(pow(tan(pi/4+theta_0/2),n));
    //--------------------------------------//
    if (n>0)
    {
        r=pow((pow(x,2)+pow((r_0-y),2)),0.5);
    }
    else
    {
        r=-pow((pow(x,2)+pow((r_0-y),2)),0.5);
    }
    the=atan(x/(r_0-y))*180/pi;
    //---------------------------------------//
    a=(2*(pow(R*F,1/n))/(1+pow(R*F/r,2/n)))*(-pow((pow(x,2)+pow((r_0-y),2)),(-1/(2*n)-1))/(2*n))*2*x;
    b=(2*(pow(R*F,1/n))/(1+pow(R*F/r,2/n)))*(-pow((pow(x,2)+pow((r_0-y),2)),(-1/(2*n)-1))/(2*n))*(-2)*(r_0-y);
    c=(1/(n+n*(pow(x/(r_0-y),2))))*(1/(r_0-y));
    d=(1/(n+n*(pow(x/(r_0-y),2))))*(x/(pow(r_0-y,2)));
    //---------------------------------------//
    theta=2*atan(pow(R*F/r,1/n))*180/pi-90;
    ratio=(pow(R,2))*cos(theta)*(a*d-c*b);
    cout<<"ratio:"<<ratio<<endl;
}


int main()
{
	float theta , alfa, theta_0, alfa_0,x,y;
	theta=35.0;  //35 N.lat
	alfa=-75.0; //75 W.long
	//origin
	theta_0=23.0; //23 N.lat
	alfa_0=-96.0; //96 W.long
	transform_1(theta,alfa,theta_0,alfa_0);
	//values of x and y
	x=0.296678;
	y=0.246211;
	transform_2(x, y, theta_0, alfa_0);
}
