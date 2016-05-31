package cn.ct.em.draw.core.vector;

import com.vividsolutions.jts.geom.Coordinate;
import cn.ct.em.draw.core.vector.WindInfo;

public class SVGRenderBarb extends StringRender{
	
	@Override
	public String ShapeToString(WindInfo[][] wind) {
		StringBuffer svg1 = new StringBuffer();
		for(int c = 0; c < wind[0].length; c++)
        {
        	for(int r = 0; r <wind.length; r++)
            {
        		WindInfo a = wind[r][c];
        		double wind_speed = a.wind_speed;
        		if (wind_speed >= 2.5 && wind_speed < 400.){
        			double wsp_25 = a.wsp25;
        			double nbarb_50 = a.nbarb50;
        			double nbarb_10 = a.nbarb10;
        			double nbarb_5 = a.nbarb5;
        			if(nbarb_5 == 1) {
        				Coordinate f_1 = a.Barb_nbarb5_1_f1;
        				Coordinate f_2 = a.Barb_nbarb5_1_f2;
        				svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");
        			}
        			if(wsp_25 >= 5. && wsp_25 < 10.) {
        				Coordinate f_0 = a.Barb_wsp25_5_10_f1;
        				Coordinate f_1 = a.Barb_wsp25_5_10_f2;
        				svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
        			}
        			for(int j=0;j<nbarb_10;j++) {
        				Coordinate f_1 = a.Barb_nbarb10_f1;
        				Coordinate f_2 = a.Barb_nbarb10_f2;
        				svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"black\"/>");     
        			}
        			Coordinate f_0 = a.Barb_f1;
        			Coordinate f_1 = a.Barb_f2;
                    svg1.append("<line y2=\"-"+f_1.y+"\" x2=\""+f_1.x+"\" y1=\"-"+f_0.y+"\" x1=\""+f_0.x+"\" stroke=\"black\"/>");
                    if(nbarb_50 > 0) {
                    	Coordinate f_00 = a.Barb_nbarb50_0_f1;
                    	Coordinate f_11 = a.Barb_nbarb50_0_f2;
		                svg1.append("<line y2=\"-"+f_11.y+"\" x2=\""+f_11.x+"\" y1=\"-"+f_00.y+"\" x1=\""+f_00.x+"\" stroke=\"black\"/>");    
                    }
                    for(int j=0;j<nbarb_50;j++) {
                    	Coordinate f_11 = a.Barb_nbarb50_f1;
                    	Coordinate f_2 = a.Barb_nbarb50_f2;
                    	Coordinate f_3 = a.Barb_nbarb50_f3;
                        svg1.append("<polygon points=\""+f_11.x+",-"+f_11.y+" "+f_2.x+",-"+f_2.y+" "+f_3.x+",-"+f_3.y+"\" style=\"fill:black;stroke:black\"/>");                 
                    }
        		}else if(wind_speed < 2.5)
        		{
        			Coordinate f_0 = a.Barb_wind_speed_f1;
                    svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+(a.k1)*1+"\" fill=\"black\"/>");
                	svg1.append("<circle cx=\""+f_0.x+"\" cy=\"-"+f_0.y+"\" r=\""+(a.k1)*0.5+"\" fill=\"white\"/>");                
        		}
            }
        }
		return svg1.toString();
	}}
