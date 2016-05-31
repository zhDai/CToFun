package cn.ct.em.draw.svg.contour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import ucar.multiarray.MultiArray;
import ucar.netcdf.Variable;
import cn.ct.em.DBInerface.SQLParaCommit;
import cn.ct.em.calculate.Convention;
import cn.ct.em.calculate.NCConnection;
import cn.ct.em.calculate.NcData;

public abstract class NCRander {

	public int xDiv = 1; //x切分成xDiv份
	public int yDiv = 1; //y切分成yDiv份
	public double[] level;
	public NcData ncData = null;
	public int thread = 1;
//	public double MAXINTERVAL = 3000;
	
	public abstract String[] algorithem(double[][] data, double[] xPos, double[] yPos, double[] lev);
	
    //颜色渐变
	protected String colour(double k,double n){
    	int r,g,b;
    	double cx =  k/n; 
    	if (cx <= 0.25){
    		r = 0;
    		g = (int) (4 * cx *255);
    		b = 255;
    	}
    	else if (cx > 0.25 && cx <= 0.5){
    		r = 0;
    		g = 255;
    		b = (int) ((1 - 4 * (cx - 0.25)) *255);
    	}
    	else if (cx > 0.5 && cx <= 0.75){
    		r = (int) (4 * (cx - 0.5) *255);
    		g = 255;
    		b = 0;
    	}
    	else {
    		r = 255;
    		g = (int) ((1-4*(cx-0.75))*255);
    		b = 0;
    	}
		String aa = "rgb("+ r +","+ g +","+ b +")";
    	return aa;
    }
	
	
//	protected String colour(int level){
//		return "rgb(0,34,255)";
//	}

	
	protected String coorToSVG(List coor) {
		return null;
	}
	
	
	protected String graphToSVG(Graph g) {
		List rings = g.getRing();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rings.size(); i++) {
			List ring = (List) rings.get(i);
			sb.append(coorToSVG(coorTrans(ringToCoor(ring))));
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
				if(ncData.projection == Convention.LAMBERT){
					coor2.add(ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(tmp))) ;
				}else if (ncData.projection == Convention.LONLAT){
					coor2.add(ncData.lonlat2mer.coordinateTrans(tmp)) ;
				}
			}
			last = c;
			if(ncData.projection == Convention.LAMBERT){
				coor2.add(ncData.lonlat2mer.coordinateTrans(ncData.lam2lonlat.coordinateTrans(c))) ;
			}else if (ncData.projection == Convention.LONLAT){
				coor2.add(ncData.lonlat2mer.coordinateTrans(c)) ;
			}
		}
		
		
		
		
		return coor2;
	}
	
	// return coordinate lists
	protected List ringToCoor(List ring) {
		List coor = new ArrayList();
//		Coordinate last = new Coordinate();
//		last.x = ((Line) ring.get(0)).startX;
//		last.y = ((Line) ring.get(0)).startY;
		for (int j = 0; j < ring.size(); j++) {
			Line l = (Line) ring.get(j);
			//for testing
//			System.out.println("x=" + l.gridX + " y=" + l.gridY + " m=" + l.gridM);
//			if(l.gridY == 0 ){
//				System.out.println("X=" + l.gridX + " Y=" + l.gridY + " M=" + l.gridM);
//			}
			if (l.startEdge) {
//				System.out.println("start x="+l.gridX+" y="+l.gridY);
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
	
	
//	public String rander( String species, int time, int layer){
//		int xStep = ncData.ncols/xDiv;
//		int yStep = ncData.nrows/yDiv;
//		int xRes = ncData.ncols%xDiv;
//		int yRes = ncData.nrows%yDiv;
//		int yLength = ncData.nrows;
//		int xLength =  ncData.ncols;
//		int xStart = 0;
//		int yStart = 0;
//		
//		Variable dataVar = ncData.ncfile.get(species);
//
//		int[] ori = new int[4];
//		ori[0] = time;
//		ori[1] = layer;
//		ori[2] = yStart;
//		ori[3] = xStart;
//
//		int[] shp = new int[4];
//		shp[0] = 1;
//		shp[1] = 1;
//		shp[2] = yLength;
//		shp[3] = xLength;
//		
//		MultiArray dataArray  = null;
//		try {
//			dataArray = dataVar.copyout(ori, shp);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//		double[][] data = new double[xLength][yLength];
//		
//		int[] idx = new int[4];
//		for(int x = 0 ; x < data.length; x++){
//			for(int y = 0; y < data[0].length; y++){
//				idx[0] = 0;
//				idx[1] = 0;
//				idx[2] = y;
//				idx[3] = x;
//				try {
//					data[x][y] = dataArray.getDouble(idx);//x，y对换
//				} catch (IOException e) {
//
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		double[] xPos = new double[xLength];
//		for(int x = 0; x <  xLength; x++){
//			xPos[x] = ncData.xorig + ncData.xcell * (x + xStart);
//		}
//		double[] yPos = new double[yLength];
//		for(int y = 0; y <  yLength; y++){
//			yPos[y] = ncData.yorig + ncData.ycell * (y + yStart);
//		}
//		
//		return rander( data, xPos, yPos, level);
//	
//	}
	
	
	//yNum 第y个，xNum 第x个， 从0开始
	public String rander( String species, int time, int layer, int xNum, int yNum){

		int xStep = ncData.ncols/xDiv;
		int yStep = ncData.nrows/yDiv;
		int xRes = ncData.ncols%xDiv;
		int yRes = ncData.nrows%yDiv;
		int yLength = 0;
		if(yStep*(yNum+1) >= ncData.nrows-yRes){
			yLength = yStep + yRes;
		}else{
//			if(yDiv != 1){
//				yLength = yStep + 1;
//			}else{
//				yLength = yStep;
//			}
			yLength = yStep + 1;
		} 
		int xLength = 0;
		if(xStep*(xNum+1) >= ncData.ncols-xRes){
			xLength = xStep + xRes;	
		}else{
//			if(xDiv != 1){
//				xLength = xStep + 1;
//			}else{
//				xLength = xStep;
//			}
			xLength = xStep + 1;
		}
		int xStart = xStep * xNum;
		int yStart = yStep * yNum;
		
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
		
//		return rander( data, xPos, yPos, level);
		
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
//		List strList = new ArrayList();
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
		
		
//		for(int i = 0 ; i < levelList.size(); i++){
//			String[] s = algrithem( data, xPos, yPos, (double[])levelList.get(i));	
//			strList.add(s);
//		}
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		
		
		NCRander rander = new NCRanderSurface();
		NcData ncd = new NcData();
		ncd.openFile("/home/xschen/Downloads/t24p6.nc");
		rander.ncData = ncd;
		rander.xDiv = 2;
		rander.yDiv = 1;
		rander.level = new double[]{0,10,20,30,50,100,500}; 
		String s = rander.rander("SO2",0, 0, 1, 0);
//		String s = rander.rander("SO2",0, 0);
		System.out.println(s);
		ncd.closeFile();
		
	}
}
