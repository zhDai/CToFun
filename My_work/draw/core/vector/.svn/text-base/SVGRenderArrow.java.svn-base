package cn.ct.em.draw.core.vector;

import com.vividsolutions.jts.geom.Coordinate;

public class SVGRenderArrow extends StringRender {
	@Override
	public String ShapeToString(WindInfo[][] wind){
		StringBuffer svg1 = new StringBuffer();
		for(int c = 0; c < wind[0].length; c++)
        {
        	for(int r = 0; r <wind.length; r++)
            {
        		Coordinate f_1 = wind[r][c].Arrow_f1;
				Coordinate f_2 = wind[r][c].Arrow_f2;
				Coordinate f_3 = wind[r][c].Arrow_f3;
				Coordinate f_4 = wind[r][c].Arrow_f4;
				
				svg1.append("<line y2=\"-"+f_2.y+"\" x2=\""+f_2.x+"\" y1=\"-"+f_1.y+"\" x1=\""+f_1.x+"\" stroke=\"#000\" fill=\"none\"/>");
				svg1.append("<line y2=\"-"+f_3.y+"\" x2=\""+f_3.x+"\" y1=\"-"+f_2.y+"\" x1=\""+f_2.x+"\" stroke=\"#000\" fill=\"none\"/>");
				svg1.append("<line y2=\"-"+f_4.y+"\" x2=\""+f_4.x+"\" y1=\"-"+f_2.y+"\" x1=\""+f_2.x+"\" stroke=\"#000\" fill=\"none\"/>");
				
            }
        }

		return svg1.toString();
		
	}

	

}
