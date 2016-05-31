package cn.ct.em.draw.core.vector_old;

import java.io.IOException;

import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.NCCoorTrans;
import cn.ct.em.draw.core.contour.NCRenderCore;
import ucar.multiarray.MultiArray;

public abstract class WindBridge extends WindRenderCore{

	//抽样
	public int sample(double height){
		return (int) (height/1300000*12);
	}
	
	//放大
	public int amplicate(double height){
		return (int) (height/1300000*10000);
	}

	public WindRenderCore windRenderCore;
	public abstract WindInfo[][] Wind_Render(MultiArray dataArray_1, MultiArray dataArray_2, int t, int l, double height,NcData ncData) throws IOException;
	public abstract String Wind2Style(WindInfo[][] windinfo,NcData ncData, NCCoorTrans ncCoorTrans);
}
