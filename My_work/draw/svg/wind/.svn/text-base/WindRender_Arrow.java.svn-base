package cn.ct.em.draw.svg.wind;

import java.io.IOException;

import com.vividsolutions.jts.geom.Coordinate;

import ucar.ma2.InvalidRangeException;
import ucar.multiarray.MultiArray;

public class WindRender_Arrow extends WindRender{
	int n1;
	int k1;
	
	protected String Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l,double height) 
			throws IOException, InvalidRangeException{
		
				StringBuffer svg1 = new StringBuffer();
//				svg1.append("<svg width=\"1800\" height=\"1400\" xmlns=\"http://www.w3.org/2000/svg\">");
//				svg1.append("<g>");

			    //抽样
//			    int n1 = (int) (y22/1000000);
//			    int k1 = (int) (y22/height);
//				n1 = 17;
				n1 = sample(height);
				k1 = amplicate(height);
//				k1 = 10000;
//				n1 = 1;
//				k1 = 3;
//			    int m = 10;
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
		                double wind_length = k1*Math.sqrt(Math.pow(u1,2)+Math.pow(v1,2));

		                if(u1>0 && v1>0){
		                	alph = 0.5*Math.PI-uv;
		                }else if(u1>0 && v1<0){
		                	alph = -0.5*Math.PI+uv;
		                }else if(u1<0 && v1>0){
		                	alph = 0.5*Math.PI+uv;
		                }else if(u1<0 && v1<0){
		                	alph = 1.5*Math.PI-uv;
		                }
//		                Coordinate f1 = new Coordinate(u1, v1);    						
//						Coordinate f_1 = ncData.XY2lonlat.coordinateTrans(f1);
//						Coordinate f_11 = ncData.e4326To3857.coordinateTrans(f_1);
//		                double wind_length = k1*Math.sqrt(Math.pow(f_11.x,2)+Math.pow(f_11.y,2));
		                
//						int x1 = (idx[3]+1)*m;
//						int y1 = (idx[2]+1)*m;
						double x1 = ncData.yorig + idx[3] * ncData.ycell;
						double y1 = ncData.xorig + idx[2] * ncData.xcell;           
						
//						int x1 = idx[3];
//						int y1 = idx[2];
						double x2 = x1+wind_length*(Math.cos(alph));
						double y2 = y1+wind_length*(Math.sin(alph));
						double a = x1 - x2;
						double b = y1 - y2;
						double x2_1 = x2 + 0.25*(Math.sqrt(3))*a+0.25*b;
						double y2_1 = y2 + 0.25*(Math.sqrt(3))*b-0.25*a;
						double x2_2 = x2 + 0.25*(Math.sqrt(3))*a-0.25*b;
						double y2_2 = y2 + 0.25*(Math.sqrt(3))*b+0.25*a;
						
						Coordinate f1 = new Coordinate(y1, x1);  
						Coordinate f2 = new Coordinate(y2, x2);
						Coordinate f3 = new Coordinate(y2_1, x2_1);
						Coordinate f4 = new Coordinate(y2_2, x2_2);
						Coordinate f_1 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f1));
						Coordinate f_2 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f2));
						Coordinate f_3 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f3));
						Coordinate f_4 = ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(f4));
						
						svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"#000\" fill=\"none\"/>");
						svg1.append("<line y2=\"-"+f_3.y+"\" x2=\""+f_3.x+"\" y1=\"-"+f_2.y+"\" x1=\""+f_2.x+"\" stroke=\"#000\" fill=\"none\"/>");
						svg1.append("<line y2=\"-"+f_4.y+"\" x2=\""+f_4.x+"\" y1=\"-"+f_2.y+"\" x1=\""+f_2.x+"\" stroke=\"#000\" fill=\"none\"/>");
		            }
		        }
//		        svg1.append("</g></svg>");
		        return svg1.toString();
	}

}
