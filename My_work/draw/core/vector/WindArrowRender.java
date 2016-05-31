package cn.ct.em.draw.core.vector;

import java.io.IOException;
import ucar.multiarray.MultiArray;
import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.NCCoorTrans;
import cn.ct.em.draw.core.vector.WindInfo;
import com.vividsolutions.jts.geom.Coordinate;

public class WindArrowRender extends ShapeRender{
	double alph;
	@Override
	public WindInfo[][] Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l,double height,NcData ncData,NCCoorTrans ncCoorTrans) throws IOException {
	    
	    //画线条及箭头
	    int[] idx = new int[4];
	    WindRenderCore ask = new WindRenderCore();
	    int n1 = ask.sample(height);
		int k1 = ask.amplicate(height);
	    WindInfo[][] WindInfo = new WindInfo[ncData.nrows/n1][ncData.ncols/n1];
	    
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
                
				double x1 = ncData.yorig + idx[3] * ncData.ycell;
				double y1 = ncData.xorig + idx[2] * ncData.xcell;           
				
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
				
				
				Coordinate f_1 = ncCoorTrans.trans(f1);
				Coordinate f_2 = ncCoorTrans.trans(f2);
				Coordinate f_3 = ncCoorTrans.trans(f3);
				Coordinate f_4 = ncCoorTrans.trans(f4);
				
				WindInfo in = new WindInfo();
				
				in.Arrow_f1 = f_1;
				in.Arrow_f2 = f_2;
				in.Arrow_f3 = f_3;
				in.Arrow_f4 = f_4;
				
				WindInfo[r][c] = in;
            }
        }
        return WindInfo;
	}

	

}
