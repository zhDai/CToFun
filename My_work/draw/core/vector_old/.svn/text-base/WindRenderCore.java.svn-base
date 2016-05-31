package cn.ct.em.draw.core.vector_old;

import java.io.IOException;

import com.vividsolutions.jts.geom.Coordinate;

import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.NCCoorTrans;
import cn.ct.em.calculate.NCcoordinate.ToFactory;
import cn.ct.em.calculate.NCcoordinate.ToMercator;
import cn.ct.em.draw.core.RenderFunction;
import cn.ct.em.draw.core.contour.ColourBridge;
import cn.ct.em.draw.model.PictureInfo;
import ucar.ma2.InvalidRangeException;
import ucar.multiarray.MultiArray;


public class WindRenderCore implements RenderFunction{

	String out;
	WindInfo c;
	public NcData ncData = null;
	
	public WindBridge windbridge;
	public NCCoorTrans ncCoorTrans;
	public ColourBridge colourBridge;
	
	public ToFactory toFactory;
	
	public WindInfo[][] Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l, double height,NcData ncData) throws IOException{
		return windbridge.Wind_Render(dataArray_1, dataArray_2, t, l, height ,ncData);
	};

	public String Wind2Style(WindInfo[][] windinfo,NcData ncData, NCCoorTrans ncCoorTrans){
		return windbridge.Wind2Style(windinfo,ncData,ncCoorTrans);
	};
	
	public String Wind_Render(String Species_1, String Species_2,int t,int l, double height,NcData ncData) throws IOException, InvalidRangeException{
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
		
		WindInfo[][] windinfo =  Wind_Render(dataArray_1,dataArray_2,t,l,height, ncData);
		return Wind2Style(windinfo , ncData, ncCoorTrans);
	}	
	
	public String render(PictureInfo picInf){
		
		ncData = new NcData();
		ncData.openFile(picInf.fileName);
		toFactory = new ToMercator();
		toFactory.from = ncData.fromFactory;
		ncCoorTrans = toFactory.to();
		try {
			out =  Wind_Render(picInf.Wind_U, picInf.Wind_V, picInf.time, picInf.layer, picInf.mapH,ncData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ncData.closeFile();
		return out;
	}

	@Override
	public PictureInfo[] subPictureInfo(PictureInfo picInf) {
		// TODO Auto-generated method stub
		return null;
	}
}
