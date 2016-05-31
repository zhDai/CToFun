package cn.ct.em.draw.core.vector_old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ucar.ma2.InvalidRangeException;
import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.ToFactory;
import cn.ct.em.draw.core.contour.ColourFull;
import cn.ct.em.draw.core.contour.NCRenderCore;

import cn.ct.em.draw.model.PictureInfo;

public class WindRender_test {

	/**
	 * @param args
	 * @throws InvalidRangeException 
	 * @throws IOException 
	 */
//	PictureInfo picInf = null;
	
	public static void main(String[] args) throws IOException, InvalidRangeException {
		// TODO Auto-generated method stub
//	    int t=1; //时间
//	    int l=1; //层数
//	    WindRenderBase dzh = new WindRenderSVGArrow();
	    PictureInfo picInf = new PictureInfo();;
	    picInf.fileName = "/home/daizhaohui/Desktop/dzh/wind.nc";
	    picInf.Wind_U = "UWIND";
	    picInf.Wind_V = "VWIND";
	    picInf.time = 1;
	    picInf.layer = 1;
	    picInf.mapH= 4862938.890351994;
//	    WindRenderCore a = WindRenderFactory.getMercatorSVGArrow();
	    WindRenderCore a = WindRenderFactory.getMercatorSVGBarb();
	    String s = a.render(picInf);	    
	    System.out.println(s);
//		FileWriter fw = null;
//		File f = new File("/home/daizhaohui/Desktop/dzh/dzh.svg");
//		try {
//			if(!f.exists()){
//				f.createNewFile();
//			}
//			fw = new FileWriter(f);
//			BufferedWriter out = new BufferedWriter(fw);
//			out.write(s, 0, s.length());
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		ncd.closeFile();
//		System.out.println("end");

	}

}
