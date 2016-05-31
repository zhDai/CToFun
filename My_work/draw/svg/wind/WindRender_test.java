package cn.ct.em.draw.svg.wind;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import ucar.ma2.InvalidRangeException;
import cn.ct.em.calculate.NcData;

public class WindRender_test {

	/**
	 * @param args
	 * @throws InvalidRangeException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InvalidRangeException {
		// TODO Auto-generated method stub
	    int t=1; //时间
	    int l=1; //层数
//	    WindRender dzh = new WindRender_Arrow();
	    WindRender dzh = new WindRender_Barb();
		NcData ncd = new NcData();
		String file = "/home/daizhaohui/Desktop/dzh/wind.nc";
	    String Species_1 = "UWIND";
	    String Species_2 = "VWIND";
	    double height = 4862938.890351994;
		ncd.openFile(file);
		dzh.ncData = ncd;
		String s = dzh.Wind_Render(Species_1, Species_2, t, l,height);
		System.out.println(s);
//		String s = null;
		
//		FileWriter fw = null;
//		File f = new File("/home/daizhaohui/Desktop/dzh/11.svg");
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
//		ncd.closeFile();
//		System.out.println("end");

	}

}
