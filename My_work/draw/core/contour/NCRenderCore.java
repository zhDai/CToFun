package cn.ct.em.draw.core.contour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Variable;
import cn.ct.em.DBInerface.SQLParaCommit;
import cn.ct.em.calculate.NCConnection;
import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.NCcoordinate.NCCoorTrans;
import cn.ct.em.calculate.NCcoordinate.ToFactory;
import cn.ct.em.calculate.NCcoordinate.ToMercator;
import cn.ct.em.draw.core.RenderFunction;
import cn.ct.em.draw.model.PictureInfo;

public class NCRenderCore implements RenderFunction {

	public int divX = 1; //x切分成xDiv份
	public int divY = 1; //y切分成yDiv份
	public int numX = 0; //yNum 第y个，xNum 第x个， 从0开始
	public int numY = 0;
	public double[] level;
	public NcData ncData;
	public String species;
	public int time;
	public int layer;

	
	public int thread = 1;
//	public double MAXINTERVAL = 3000;
	
	public ColourBridge colourBridge;
	public ShapeBridge shapeBridge;
	public NCCoorTrans ncCoorTrans;

	public ToFactory toFactory;


//	public abstract String[] algorithem(double[][] data, double[] xPos, double[] yPos, double[] lev);
	public String[] algorithem(double[][] data, double[] xPos, double[] yPos, double[] lev){
		return shapeBridge.algorithem(data, xPos, yPos, lev);
	}

    //颜色渐变
	protected String colour(double k,double n){
		return colourBridge.colour(k, n);
    }
	

	protected String coorToString(List coor) {
//		return null;
		return shapeBridge.coorToString(coor);
	}
	
	
	protected String graphToString(Graph g) {
		List rings = g.getRing();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rings.size(); i++) {
			List ring = (List) rings.get(i);
			sb.append(coorToString(coorTrans(ringToCoor(ring))));
		}
		return sb.toString();

	}

	
	
	protected List coorTrans(List coor){
		List coor2 = new ArrayList();
		Coordinate last = (Coordinate)coor.get(0);
		
		for(int i = 0; i < coor.size(); i++){
			Coordinate c = (Coordinate)coor.get(i);
			int n = (int)( (Math.abs(last.x - c.x) + Math.abs(last.y - c.y)) / (ncData.xcell + ncData.ycell) );
			for(int j = 1 ; j < n ; j++){
				Coordinate tmp = new Coordinate();
				tmp.x = last.x + j * ( c.x - last.x ) / n;
				tmp.y = last.y + j * ( c.y - last.y ) / n;
				coor2.add(ncCoorTrans.trans(tmp));
			}
			last = c;

			coor2.add(ncCoorTrans.trans(c));
		}		
		return coor2;

	}
	
	// return coordinate lists
	protected List ringToCoor(List ring) {
		List coor = new ArrayList();
		for (int j = 0; j < ring.size(); j++) {
			Line l = (Line) ring.get(j);
			if (l.startEdge) {
				Coordinate c = new Coordinate();
				c.x = l.startX;
				c.y = l.startY;
				coor.add(c);
				Coordinate c2 = new Coordinate();
				c2.x = l.endX;
				c2.y = l.endY;
				coor.add(c2);
			
			}else{
				Coordinate c = new Coordinate();
				c.x = l.endX;
				c.y = l.endY;
				coor.add(c);
			}

		}

		return coor;
	}
	

	/* (non-Javadoc)
	 * @see cn.ct.em.draw.core.contour.RanderInterface#rander(cn.ct.em.draw.model.PictureInfo)
	 */
	@Override
	public String render( PictureInfo picInf){
		ncData = new NcData();
		ncData.openFile(picInf.fileName);
//		ToFactory to = new ToMercator();
		toFactory.from = ncData.fromFactory;
//		to.from = ncData.fromFactory;
		ncCoorTrans = toFactory.to();
		species = picInf.species;
		time = picInf.time;
		layer = picInf.layer;
		
		divX = picInf.divX;
		divY = picInf.divY;
		numX = picInf.numX;
		numY = picInf.numY;
		
		double intelval = (picInf.maxV - picInf.minV) / picInf.stepV;
		level = new double[picInf.stepV];
		for(int i = 0 ; i < picInf.stepV; i++){
			level[i] = picInf.minV + i * intelval;
		}
		String res = render();
		ncData.closeFile();
		return res;
	}
	
	
	

	protected String render( ){
		int xStep = ncData.ncols/divX;
		int yStep = ncData.nrows/divY;
		int xRes = ncData.ncols%divX;
		int yRes = ncData.nrows%divY;
		int yLength = 0;
		if(yStep*(numY+1) >= ncData.nrows-yRes){
			yLength = yStep + yRes;
		}else{
			yLength = yStep + 1;
		} 
		int xLength = 0;
		if(xStep*(numX+1) >= ncData.ncols-xRes){
			xLength = xStep + xRes;	
		}else{
			xLength = xStep + 1;
		}
		int xStart = xStep * numX;
		int yStart = yStep * numY;
		
		Variable dataVar = ncData.ncfile.get(species);

		int[] ori = new int[4];
		ori[0] = time;
		ori[1] = layer;
		ori[2] = yStart;
		ori[3] = xStart;

		int[] shp = new int[4];
		shp[0] = 1;
		shp[1] = 1;
		shp[2] = yLength;
		shp[3] = xLength;
		
		MultiArray dataArray  = null;
		try {
			dataArray = dataVar.copyout(ori, shp);
		} catch (IOException e) {

			e.printStackTrace();
		}
		double[][] data = new double[xLength][yLength];
		
		int[] idx = new int[4];
		for(int x = 0 ; x < data.length; x++){
			for(int y = 0; y < data[0].length; y++){
				idx[0] = 0;
				idx[1] = 0;
				idx[2] = y;
				idx[3] = x;
				try {
					data[x][y] = dataArray.getDouble(idx);//x，y对换
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		
		double[] xPos = new double[xLength];
		for(int x = 0; x < xLength; x++){
			xPos[x] = ncData.xorig + ncData.xcell * (x + xStart);
		}
		double[] yPos = new double[yLength];
		for(int y = 0; y < yLength; y++){
			yPos[y] = ncData.yorig + ncData.ycell * (y + yStart);
		}
		return partition( data, xPos, yPos);
		
	}
		

	protected String partition(double[][] data, double[] xPos, double[] yPos){
		List levelList = new ArrayList();
		//按level交错分组
		for(int i = 0 ; i < thread; i++){
			List levelSub = new ArrayList();
			for(int j = i ; j < level.length; j = j + thread){
				levelSub.add(level[j]);
			}
			double[] tmp = new double[levelSub.size()];
			for(int k = 0; k < tmp.length; k++){
				tmp[k] = (double)levelSub.get(k);
			}
			levelList.add(tmp);
		}
		
		//多线程渲染
		AlgorithemWapper[] aw = new AlgorithemWapper[thread];
		for(int i = 0; i < thread; i++){
			aw[i] = new AlgorithemWapper();
			aw[i].data = data;
			aw[i].xPos = xPos;
			aw[i].yPos = yPos;
			aw[i].lev =  (double[])levelList.get(i);
			aw[i].ncRander = this;
		}
		
		Thread[] threads = new Thread[thread];
		for (int i = 0; i < thread; i++) {
			threads[i] = new Thread(aw[i]);
		}
		for (int i = 0; i < thread; i++) {
				threads[i].start();
		}
		for (int i = 0; i < thread; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}		
		
		//合并结果
		StringBuffer sb = new StringBuffer();
		for(int j = 0; j < 1+level.length/thread; j++){
			for(int i = 0; i < thread ; i++){
				String[] s = aw[i].res;
				if(s.length > j){
					sb.append(s[j]);
				}
			}
		}
		return sb.toString();
	}
	
	
	@Override
	public PictureInfo[] subPictureInfo(PictureInfo picInf) {
		//简单实现
		PictureInfo[] subPicInfs = new PictureInfo[1];
		try {
			subPicInfs[0] = (PictureInfo) picInf.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		subPicInfs[0].divX = 1;
		subPicInfs[0].divY = 1;
		subPicInfs[0].numX = 0;
		subPicInfs[0].numY = 0;
		
		return subPicInfs;
	}
	
	
	
	
	public static void main(String[] args){
		
		
		NCRenderCore render = NCRenderFactory.getMercatorSVGPolygonFull();
		
		PictureInfo picInf = new PictureInfo();
		picInf.fileName = "/home/xschen/Downloads/cmaq20151116_00.nc.CMAQ";
		picInf.species = "SO2";
		picInf.layer = 0;
		picInf.time = 0;
		picInf.maxV = 5;
		picInf.minV = 0;
		picInf.stepV = 5;
		picInf.divX = 2;
		picInf.divY = 2;
		picInf.numX = 0;
		picInf.numY = 0;
		String s = render.render(picInf);
//		rander.level = new double[]{0,1,2,3,4}; 

//		s += rander.rander("SO2",0, 0, 0, 0);
//		s += rander.rander("SO2",0, 0, 0, 1);
//		s += rander.rander("SO2",0, 0, 1, 0);
//		s += rander.rander("SO2",0, 0, 1, 1);
		System.out.println(s);
		render.ncData.closeFile();
		
	}



	
}
