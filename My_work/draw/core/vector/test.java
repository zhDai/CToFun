package cn.ct.em.draw.core.vector;

import cn.ct.em.draw.core.vector.WindRenderCore;
import cn.ct.em.draw.model.PictureInfo;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PictureInfo picInf = new PictureInfo();;
	    picInf.fileName = "/home/daizhaohui/Desktop/dzh/wind.nc";
	    picInf.Wind_U = "UWIND";
	    picInf.Wind_V = "VWIND";
	    picInf.time = 1;
	    picInf.layer = 1;
	    picInf.mapH= 4862938.890351994;
//	    WindRenderCore obj = WindReanderFactory.getMercatorSVGArrow();
	    WindRenderCore obj = WindReanderFactory.getMercatorSVGBarb();
	    String s = obj.render(picInf);	    
	    System.out.println(s);

	}

}
