package cn.ct.em.draw.core.vector_old;

import java.io.IOException;

import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.NCCoorTrans;

import com.vividsolutions.jts.geom.Coordinate;

import ucar.multiarray.MultiArray;

public class WindRenderSVGBarb extends WindBridge{
	int n1;
	int k1;
	double wsp25,slant,barb,d;
	double x,y,x1,y1,x2,y2,x3,y3,nbarb50,nbarb10,nbarb5;
	double alph;

	double Barb_length = 6;
	
	public WindInfo[][] Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l,double height,NcData ncData) throws IOException{
		
		n1 = sample(height);
		k1 = amplicate(height);
		WindInfo[][] WindInfo = new WindInfo[ncData.nrows/n1][ncData.ncols/n1];
	    //画线条及箭头
	    int[] idx = new int[4];
        for(int c = 0; c < ncData.ncols/n1; c++)
        {
        	for(int r = 0; r <ncData.nrows/n1; r++)
            {
        		WindInfo in = new WindInfo();
        		
        		in.k1 = k1;
        		
        		idx[0] = 0;
				idx[1] = 0;
				idx[2] = (n1+1)/2-1+n1*r;
				idx[3] = (n1+1)/2-1+n1*c;
				double u1 = dataArray_1.getDouble(idx); 
                double v1 = dataArray_2.getDouble(idx); 
                double uv = Math.atan(Math.abs(u1/v1));
                double wind_speed = Math.sqrt(Math.pow(u1,2)+Math.pow(v1,2));
                double wind_length = k1*Barb_length;
                
                in.wind_speed = wind_speed;
                
                if(u1>0 && v1>0){
                	alph = 0.5*Math.PI-uv;
                }else if(u1>0 && v1<0){
                	alph = -0.5*Math.PI+uv;
                }else if(u1<0 && v1>0){
                	alph = 0.5*Math.PI+uv;
                }else if(u1<0 && v1<0){
                	alph = 1.5*Math.PI-uv;
                }
                               
				x = ncData.yorig + idx[3] * ncData.ycell;
				y = ncData.xorig + idx[2] * ncData.xcell;
				
				
                if (wind_speed >= 2.5 && wind_speed < 400.){
                	wsp25 = Math.max(wind_speed+2.5,5.);
                    slant = 0.2*wind_length;
                    barb = 0.4*wind_length;
                    double x0 = -Math.sin(alph);
                    double y0 = -Math.cos(alph);
                    
                    in.wsp25 = wsp25;

                    //plot the flag pole
                    d = barb;
                    x1 = x +y0*d;
                    y1 = y +x0*d;
                    
                  //determine number of wind barbs needed for 10 and 50 kt winds
                    nbarb50 = (int)(wsp25/50.f);
                    nbarb10 = (int)((wsp25 - (nbarb50 * 50.f))/10.f);
                    nbarb5 =  (int)((wsp25 - (nbarb50 * 50.f) - (nbarb10 * 10.f))/5.f);
                    
                    in.nbarb50 = nbarb50;
                    in.nbarb10 = nbarb10;
                    in.nbarb5 = nbarb5;
                    
                  //2.5 to 7.5 kt winds are plotted with the barb part way done the pole
                    if(nbarb5 == 1) {
                        barb = barb*.4;
                        double shortslant = slant*.4;
                        x1 = (int)(x + y0*d);
                        y1 = (int)(y + x0*d);

                        x2 = (int)(x + y0*(d+shortslant) - x0*barb);
                        y2 = (int)(y + x0*(d+shortslant) + y0*barb);

                        Coordinate f1 = new Coordinate(y1, x1); 
                        Coordinate f2 = new Coordinate(y2, x2); 
//                        Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
//                        Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
                       
                        in.Barb_nbarb5_1_f1 = f1;
                        in.Barb_nbarb5_1_f2 = f2;
//                        svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");

                       }
                  //add a little more pole
                    if(wsp25 >= 5. && wsp25 < 10.) {
		                 d = d+.125*wind_length;
		                 x1=(int)(x+y0*d);
		                 y1=(int)(y+x0*d);
		                 
		                 Coordinate f0 = new Coordinate(y, x); 
	                     Coordinate f1 = new Coordinate(y1, x1); 
//                         Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
//                         Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
		                 
                         in.Barb_wsp25_5_10_f1 = f0;
                         in.Barb_wsp25_5_10_f2 = f1;
//		                 svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");

                    }
                  //now plot any 10 kt wind barbs
                    barb=0.4*wind_length;
                    for(int j=0;j<nbarb10;j++) {
		                 d = d +0.125*wind_length;
		                 x1=(int)(x + y0*d);
		                 y1=(int)(y + x0*d);
		                 x2 = (int)(x + y0*(d+slant) - x0*barb);
		                 y2 = (int)(y + x0*(d+slant) + y0*barb);
		                 
		                 Coordinate f1 = new Coordinate(y1, x1); 
	                     Coordinate f2 = new Coordinate(y2, x2); 
//                         Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
//                         Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
		                 
                         in.Barb_nbarb10_f1 = f1;
                         in.Barb_nbarb10_f2 = f2;
//		                 svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");
                    }
                    Coordinate f0 = new Coordinate(y, x); 
                    Coordinate f1 = new Coordinate(y1, x1); 
//                    Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
//                    Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
                    
                    in.Barb_f1 = f0;
                    in.Barb_f2 = f1;
//                    svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");

                  //lengthn the pole to accomodate the 50 not barbs
                    if(nbarb50 > 0) {
		                 d = d +0.125*wind_length;
		                 x1=(int)(x + y0*d);
		                 y1=(int)(y + x0*d);
		                 
		                 Coordinate f00 = new Coordinate(y, x); 
	                     Coordinate f11 = new Coordinate(y1, x1); 
//                         Coordinate f_00 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f00));
//                         Coordinate f_11 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f11));
		                 
                         in.Barb_nbarb50_0_f1 = f00;
                         in.Barb_nbarb50_0_f2 = f11;
//		                 svg1.append("<line y2=\"-"+f_11.y+"\" x2=\""+f_11.x+"\" y1=\"-"+f_00.y+"\" x1=\""+f_00.x+"\" stroke=\"black\"/>");
                    }
                    //plot the 50 kt wind barbs
                    double s195 = Math.sin(195/57.3);
                    double c195 = Math.cos(195/57.3);
                    barb=0.4*wind_length;
                    for(int j=0;j<nbarb50;j++) {
		                 x1=(int)(x + y0*d);
		                 y1=(int)(y + x0*d);
		                 d = d+0.3*wind_length;
		                 x3=(int)(x+y0*d);
		                 y3=(int)(y+x0*d);
		                 x2=(int)(x3+barb*(y0*s195+x0*c195));
		                 y2=(int)(y3-barb*(y0*c195-x0*s195));
		                 
		                 Coordinate f11 = new Coordinate(y1, x1); 
	                     Coordinate f2 = new Coordinate(y2, x2); 
	                     Coordinate f3 = new Coordinate(y3,x3);
//                         Coordinate f_11 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f11));
//                         Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
//                         Coordinate f_3 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f3));
		                 //填充
		                 in.Barb_nbarb50_f1 = f11;
		                 in.Barb_nbarb50_f2 = f2;
		                 in.Barb_nbarb50_f3 = f3;
                         //svg1.append("<polygon points=\""+f_11.x+",-"+f_11.y+" "+f_2.x+",-"+f_2.y+" "+f_3.x+",-"+f_3.y+"\" style=\"fill:black;stroke:black\"/>");
		                 //start location for the next barb	                 
		                 x1=f3.x;
		                 y1=f3.y;
                    }
                }else if(wind_speed < 2.5){
                	Coordinate f0 = new Coordinate(y, x); 
//                    Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
                	
                    in.Barb_wind_speed_f1 = f0;
//                  svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+k1*1+"\" fill=\"black\"/>");
//                	svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+k1*0.5+"\" fill=\"white\"/>");
                }	
                WindInfo[r][c] = in;
           }
        }
        return WindInfo;
	}
	
	public String Wind2Style(WindInfo[][] WindInfo,NcData ncData, NCCoorTrans ncCoorTrans){
		
		StringBuffer svg1 = new StringBuffer();
		for(int c = 0; c < WindInfo[0].length; c++)
        {
        	for(int r = 0; r <WindInfo.length; r++)
            {
        		WindInfo a = WindInfo[r][c];
        		double wind_speed = a.wind_speed;
        		if (wind_speed >= 2.5 && wind_speed < 400.){
        			double wsp_25 = a.wsp25;
        			double nbarb_50 = a.nbarb50;
        			double nbarb_10 = a.nbarb10;
        			double nbarb_5 = a.nbarb5;
        			if(nbarb_5 == 1) {
        				Coordinate f_1 = ncCoorTrans.trans(a.Barb_nbarb5_1_f1);
        				Coordinate f_2 = ncCoorTrans.trans(a.Barb_nbarb5_1_f2);
        				svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");
        			}
        			if(wsp_25 >= 5. && wsp_25 < 10.) {
        				Coordinate f_0 = ncCoorTrans.trans(a.Barb_wsp25_5_10_f1);
        				Coordinate f_1 = ncCoorTrans.trans(a.Barb_wsp25_5_10_f2);
        				svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
        			}
        			for(int j=0;j<nbarb_10;j++) {
        				Coordinate f_1 = ncCoorTrans.trans(a.Barb_nbarb10_f1);
        				Coordinate f_2 = ncCoorTrans.trans(a.Barb_nbarb10_f2);
        				svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");     
        			}
        			Coordinate f_0 = a.Barb_f1;
        			Coordinate f_1 = a.Barb_f2;
                    svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
                    if(nbarb_50 > 0) {
                    	Coordinate f_00 = ncCoorTrans.trans(a.Barb_nbarb50_0_f1);
                    	Coordinate f_11 = ncCoorTrans.trans(a.Barb_nbarb50_0_f2);
		                svg1.append("<line y2=\"-"+f_11.y+"\" x2=\""+f_11.x+"\" y1=\"-"+f_00.y+"\" x1=\""+f_00.x+"\" stroke=\"black\"/>");    
                    }
                    for(int j=0;j<nbarb_50;j++) {
                    	Coordinate f_11 = ncCoorTrans.trans(a.Barb_nbarb50_f1);
                    	Coordinate f_2 = ncCoorTrans.trans(a.Barb_nbarb50_f2);
                    	Coordinate f_3 = ncCoorTrans.trans(a.Barb_nbarb50_f3);
                        svg1.append("<polygon points=\""+f_11.x+",-"+f_11.y+" "+f_2.x+",-"+f_2.y+" "+f_3.x+",-"+f_3.y+"\" style=\"fill:black;stroke:black\"/>");                 
                    }
        		}else if(wind_speed < 2.5)
        		{
        			Coordinate f_0 = ncCoorTrans.trans(a.Barb_wind_speed_f1);
                    svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+(a.k1)*1+"\" fill=\"black\"/>");
                	svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+(a.k1)*0.5+"\" fill=\"white\"/>");                
        		}
            }
        }
		return svg1.toString();
	}


	

}
