/*
 * GaussionRandom.cpp
 *
 *  Created on: Jan 22, 2016
 *      Author: daizhaohui
 */
#include <stdlib.h>
#include <math.h>
#include<iostream>

using namespace std;

double gaussrand()
{
    static double V1, V2, S;
    static int phase = 0;
    double X;

    if ( phase == 0 ) {
        do {
        	double U1 = (double)rand() / RAND_MAX;
        	double U2 = (double)rand() / RAND_MAX;

            V1 = 2 * U1 - 1;
            V2 = 2 * U2 - 1;
            S = V1 * V1 + V2 * V2;
        } while(S >= 1 || S == 0);

        X = V1 * sqrt(-2 * log(S) / S);
    } else
        X = V2 * sqrt(-2 * log(S) / S);

    phase = 1 - phase;

    return X;
}

double gaussrand(double exp,double var)
{
    static double V1, V2, S;
    static int phase = 0;
    double X;

    if ( phase == 0 ) {
        do {
        	double U1 = (double)rand() / RAND_MAX;
        	double U2 = (double)rand() / RAND_MAX;

            V1 = 2 * U1 - 1;
            V2 = 2 * U2 - 1;
            S = V1 * V1 + V2 * V2;
        } while(S >= 1 || S == 0);

        X = V1 * sqrt(-2 * log(S) / S);
    } else
        X = V2 * sqrt(-2 * log(S) / S);

    phase = 1 - phase;

    return (X*var+exp);
}



int main(){
	int i = 0;
	while(i<10){
		double aa = gaussrand(0,1);
		cout<<aa<<endl;
		i++;
	}
}


