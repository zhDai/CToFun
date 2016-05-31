package cn.ct.em.draw.svg.wind;

import java.io.IOException;
import com.vividsolutions.jts.geom.Coordinate;
import ucar.ma2.InvalidRangeException;
import ucar.multiarray.MultiArray;

public class WindRender_Barb extends WindRender{
	int n1;
	int k1;
	double wsp25,slant,barb,d;
	double x,y,x1,y1,x2,y2,x3,y3,nbarb50,nbarb10,nbarb5;
	//长度
	double Barb_length = 6;
//	double Barb_length = 100000;
	
	protected String Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l,double height) 
			throws IOException, InvalidRangeException{
		
		StringBuffer svg1 = new StringBuffer();
//		svg1.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1632\" height=\"1577\" viewBox=\"9879930.198944887,-5826249.116775121,4091724.920077987,3822666.5805142396\" style=\"stroke-width:9783.939620499965\">");
//		svg1.append("<?xml version=\"1.0\"?>");
//		svg1.append("<svg width=\"580\" height=\"400\" xmlns=\"http://www.w3.org/2000/svg\">");
//		svg1.append("<g>");

	    //抽样
//	    int n1 = (int) (y22/1000000);
//	    int k1 = (int) (y22/height);
		n1 = sample(height);
		k1 = amplicate(height);
//		n1 = 17;
//		int m = 10;
		
	    //画线条及箭头
	    int[] idx = new int[4];
        for(int c = 0; c < ncData.ncols/n1; c++)
        {
        	for(int r = 0; r <ncData.nrows/n1; r++)
            {
        		idx[0] = 0;
				idx[1] = 0;
				idx[2] = (n1+1)/2-1+n1*r;
				idx[3] = (n1+1)/2-1+n1*c;
				double u1 = dataArray_1.getDouble(idx); 
                double v1 = dataArray_2.getDouble(idx); 
                double uv = Math.atan(Math.abs(u1/v1));
                double wind_speed = Math.sqrt(Math.pow(u1,2)+Math.pow(v1,2));
                double wind_length = k1*Barb_length;
//                System.out.println(wind_speed);
                
                if(u1>0 && v1>0){
                	alph = 0.5*Math.PI-uv;
                }else if(u1>0 && v1<0){
                	alph = -0.5*Math.PI+uv;
                }else if(u1<0 && v1>0){
                	alph = 0.5*Math.PI+uv;
                }else if(u1<0 && v1<0){
                	alph = 1.5*Math.PI-uv;
                }
                               
//				int x = (idx[3]+1)*m;
//				int y = (idx[2]+1)*m;
				x = ncData.yorig + idx[3] * ncData.ycell;
				y = ncData.xorig + idx[2] * ncData.xcell;
				
                if (wind_speed >= 2.5 && wind_speed < 400.){
                	wsp25 = Math.max(wind_speed+2.5,5.);
                    slant = 0.2*wind_length;
                    barb = 0.4*wind_length;
                    double x0 = -Math.sin(alph);
                    double y0 = -Math.cos(alph);

                    //plot the flag pole
                    d = barb;
                    x1 = x +y0*d;
                    y1 = y +x0*d;
                    
                  //determine number of wind barbs needed for 10 and 50 kt winds
                    nbarb50 = (int)(wsp25/50.f);
                    nbarb10 = (int)((wsp25 - (nbarb50 * 50.f))/10.f);
                    nbarb5 =  (int)((wsp25 - (nbarb50 * 50.f) - (nbarb10 * 10.f))/5.f);
                    
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
                        Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
                        Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
                        
                        svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");
//                        g.drawLine(x1,y1,x2,y2);
                       }
                  //add a little more pole
                    if(wsp25 >= 5. && wsp25 < 10.) {
		                 d = d+.125*wind_length;
		                 x1=(int)(x+y0*d);
		                 y1=(int)(y+x0*d);
		                 
		                 Coordinate f0 = new Coordinate(y, x); 
	                     Coordinate f1 = new Coordinate(y1, x1); 
                         Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
                         Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
		                 
		                 svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
//		                 g.drawLine(x,y,x1,y1);
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
                         Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
                         Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
		                 
		                 svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");
//		                 g.drawLine(x1,y1,x2,y2);
                    }
                    Coordinate f0 = new Coordinate(y, x); 
                    Coordinate f1 = new Coordinate(y1, x1); 
                    Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
                    Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
                    
                    svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
//                    g.drawLine(x,y,x1,y1);
                  //lengthn the pole to accomodate the 50 not barbs
                    if(nbarb50 > 0) {
		                 d = d +0.125*wind_length;
		                 x1=(int)(x + y0*d);
		                 y1=(int)(y + x0*d);
		                 
		                 Coordinate f00 = new Coordinate(y, x); 
	                     Coordinate f11 = new Coordinate(y1, x1); 
                         Coordinate f_00 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f00));
                         Coordinate f_11 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f11));
		                 
		                 svg1.append("<line y2=\"-"+f_11.y+"\" x2=\""+f_11.x+"\" y1=\"-"+f_00.y+"\" x1=\""+f_00.x+"\" stroke=\"black\"/>");
//		                 g.drawLine(x,y,x1,y1);
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
                         Coordinate f_11 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f11));
                         Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
                         Coordinate f_3 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f3));
		                 //填充
		                 svg1.append("<polygon points=\""+f_11.x+",-"+f_11.y+" "+f_2.x+",-"+f_2.y+" "+f_3.x+",-"+f_3.y+"\" style=\"fill:black;stroke:black\"/>");
//		                 svg1.append("<line y2=\""+y1+"\" x2=\""+x1+"\" y1=\""+y+"\" x1=\""+x+"\" stroke=\"#000\"/>");
		                 //start location for the next barb	                 
		                 x1=f3.x;
		                 y1=f3.y;
                    }
                }else if(wind_speed < 2.5){
                	Coordinate f0 = new Coordinate(y, x); 
                    Coordinate f_0 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f0));
                	svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+k1*1+"\" fill=\"black\"/>");
                	svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+k1*0.5+"\" fill=\"white\"/>");
                }				
           }
        }
//        svg1.append("</g></svg>");
//        System.out.println(svg1.toString());
        return svg1.toString();
	}

}
