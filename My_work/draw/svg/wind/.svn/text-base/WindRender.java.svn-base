package cn.ct.em.draw.svg.wind;
//daizhaohui
//风矢量的图片渲染
import java.io.IOException;
import cn.ct.em.calculate.NcData;
import ucar.ma2.InvalidRangeException;
import ucar.multiarray.MultiArray;


public abstract class WindRender {
	
	double reflon0;
	double reflat0;
	double reflat1;
	double reflat2;
	double xorig;
	double yorig;
	double xcell;
	double ycell;
	int ncols;
	int nrows;
	int nlays;
	double alph;
	double y22;
	double height;
	
	public NcData ncData = null;
	
	protected abstract String Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2,
			int t, int l, double height2) throws IOException, InvalidRangeException;
	
	//抽样
	public int sample(double height){
		return (int) (height/1300000*12);
//		return (int) (kk/1000);
	}
	
	//放大
	public int amplicate(double height){
		return (int) (height/1300000*10000);
	}
	
	public String Wind_Render(String Species_1, String Species_2,int t,int l, double height) throws IOException, InvalidRangeException{
		ucar.netcdf.Variable dataVar_1 = ncData.ncfile.get(Species_1);
		ucar.netcdf.Variable dataVar_2 = ncData.ncfile.get(Species_2);
		int[] ori = new int[4];
		ori[0] = t;
		ori[1] = l-1;
		ori[2] = 0;
		ori[3] = 0;

		int[] shp = new int[4];
		shp[0] = 1;
		shp[1] = 1;
		shp[2] = ncData.nrows;
		shp[3] = ncData.ncols;
		
		MultiArray dataArray_1  = null;
		MultiArray dataArray_2  = null;
		
		dataArray_1 = dataVar_1.copyout(ori, shp);
		dataArray_2 = dataVar_2.copyout(ori, shp);
		
		return Wind_Render(dataArray_1,dataArray_2,t,l,height);
	}
	
}
