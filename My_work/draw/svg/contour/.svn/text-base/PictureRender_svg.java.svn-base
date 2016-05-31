package cn.ct.em.draw.svg.contour;


//用svg画contour图



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ct.em.calculate.LambertTransLonLatToXY;
import cn.ct.em.calculate.LambertTransXYToLonLat;
import cn.ct.em.calculate.MercatorTransLonLatToXY;
import cn.ct.em.calculate.MercatorTransXYToLonLat;
import cn.ct.em.calculate.NCConnection;
import cn.ct.em.calculate.NcData;
import cn.ct.em.draw.svg.Trajectory_svg;
import cn.ct.em.draw.svg.wind.WindRender;
import cn.ct.em.draw.svg.wind.WindRender_Arrow;

import com.vividsolutions.jts.geom.Coordinate;









import ucar.ma2.InvalidRangeException;
//import ucar.ma2.ArrayFloat;
//import ucar.ma2.InvalidRangeException;
//import ucar.nc2.NetcdfFile;
//import ucar.nc2.Variable;
import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.multiarray.MultiArrayProxy;
import ucar.multiarray.SliceMap;
import ucar.netcdf.Dimension;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class PictureRender_svg 
implements Serializable{
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
	String fname;
	transient NetcdfFile ncfile;
	
	private double minX;  //左上X
	private double maxY;  //左上Y
	private double maxX;  //右下X
	private double minY;  //右下Y
	private String[][] svg;  //图片数据
	
	public MercatorTransXYToLonLat e3857To4326 = new MercatorTransXYToLonLat();
	public LambertTransLonLatToXY lonlat2XY = new LambertTransLonLatToXY();
	public LambertTransXYToLonLat f11 = new LambertTransXYToLonLat();
	public MercatorTransLonLatToXY f22 = new MercatorTransLonLatToXY();
	
	public void init(String file){
		ncfile = NCConnection.getConnection(file);
		fname = file;
//		reflon0 = ncfile.readAttributeDouble(null, "XCENT", 0.0);
//		reflat0 = ncfile.readAttributeDouble(null, "YCENT", 0.0);
//		reflat1 = ncfile.readAttributeDouble(null, "P_ALP", 0.0);
//		reflat2 = ncfile.readAttributeDouble(null, "P_BET", 0.0);
//		xorig = ncfile.readAttributeDouble(null, "XORIG", 0.0);
//		yorig = ncfile.readAttributeDouble(null, "YORIG", 0.0);
//		xcell = ncfile.readAttributeDouble(null, "XCELL", 0.0);
//		ycell = ncfile.readAttributeDouble(null, "YCELL", 0.0);
//		ncols = ncfile.readAttributeInteger(null, "NCOLS", -10);
//		nrows = ncfile.readAttributeInteger(null, "NROWS", -10);
//		nlays = ncfile.readAttributeInteger(null, "NLAYS", -10);
		reflon0 = (Double)ncfile.getAttribute("XCENT").getNumericValue();
		reflat0 = (Double)ncfile.getAttribute( "YCENT").getNumericValue();
		reflat1 = (Double)ncfile.getAttribute( "P_ALP").getNumericValue();
		reflat2 = (Double)ncfile.getAttribute( "P_BET").getNumericValue();
		xorig = (Double)ncfile.getAttribute( "XORIG").getNumericValue();
		yorig = (Double)ncfile.getAttribute( "YORIG").getNumericValue();
		xcell = (Double)ncfile.getAttribute( "XCELL").getNumericValue();
		ycell = (Double)ncfile.getAttribute( "YCELL").getNumericValue();
		ncols = (Integer)ncfile.getAttribute( "NCOLS").getNumericValue();
		nrows = (Integer)ncfile.getAttribute( "NROWS").getNumericValue();
		nlays = (Integer)ncfile.getAttribute( "NLAYS").getNumericValue();

	    f11.init(reflon0, reflat0, reflat1, reflat2);
	    
	    double minX2 = xorig;
	    double maxY2 = yorig + ycell * nrows;
	    double maxX2 = xorig + xcell * ncols;
	    double minY2 = yorig;
		Coordinate f1 = new Coordinate(minX2, minY2);
		Coordinate f2 = new Coordinate(maxX2, maxY2);          						
		Coordinate f_1 = f11.coordinateTrans(f1);
		Coordinate f_2 = f11.coordinateTrans(f2);
		Coordinate f_11 = f22.coordinateTrans(f_1);
		Coordinate f_22 = f22.coordinateTrans(f_2);
	    
		minX = f_11.x;
		maxX = f_22.x;
		minY = -f_22.y;
		maxY = -f_11.y;
		
		NCConnection.close(fname, ncfile);
	}
	

	
//	public String[][] PictureRender_svg_line( String Species,int t, int l,int x0, int y0,int NROWS,
//			  int NCOLS, double nc, double zmax, double zmin,double width, double height, double x11, double y11, double x22, double y22,int n) throws IOException{
//		
//		int nci = (int) nc;
//		double interval = (zmax-zmin)/nc;
//		int NROWS_1 = NROWS%n;
//    	int NCOLS_1 = NCOLS%n;
//		NROWS = NROWS/n;
//    	NCOLS = NCOLS/n;
//    	
//		double []x = new double[NCOLS+1];
//	    double []y = new double[NROWS+1];
//	    double []x_1 = new double[NCOLS+NCOLS_1];
//	    double []y_1 = new double[NROWS+NROWS_1];
//	    double []x_2 = new double[NCOLS+NCOLS_1];
//	    double []y_2 = new double[NROWS+1];
//	    double []x_3 = new double[NCOLS+1];
//	    double []y_3 = new double[NROWS+NROWS_1];
//  	    double []z = new double[(int) nc];
//	    
//  	    svg = new String[n][n]; 
//	    
////		NetcdfFile ncfile = NetcdfFile.open(file);
//		double[][] flat_samples = new double[NCOLS+1][NROWS+1];
//		double[][] flat_samples_1 = new double[NCOLS+NCOLS_1][NROWS+NROWS_1];
//		double[][] flat_samples_2 = new double[NCOLS+NCOLS_1][NROWS+1];
//		double[][] flat_samples_3 = new double[NCOLS+1][NROWS+NROWS_1];
//	    //nc文件打开，并且取数
////	    Variable dataVar = ncfile.findVariable(Species);
//		Variable dataVar = ncfile.get(Species);
//	    
//	    int x01;
//	    int y01;
//		int[] idxtmp = new int[4];
//	    for (int ih = 0; ih < n; ih++){
//	    	for (int jh = 0; jh < n; jh++){
//		    	if (ih != n-1 && jh !=n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//			    	int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+1,NCOLS+1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+1; r++)
//					      for(int c = 0; c < NCOLS+1; c++){
////					    	  flat_samples[c][r] = dataArray2.get(0,0,r,c); 
//								idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples[c][r] = dataArray2.getDouble(idxtmp);
//					      }
//					
//					int ii2 = 0;
//					int jj2 = 0;
//				    for (int i=y01;i<(jh+1)*NCOLS+1;i++){
//				    	x[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<(ih+1)*NROWS+1;j++){
//				    	y[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb = 0;
//				    int jub = NROWS;
//				    int jlb = 0; 
//				    int iub = NCOLS;		    
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//					svg[ih][jh] = contour(flat_samples,ilb,iub,jlb,jub,x,y,nci,z,width,height);
//		    	}
//		    	else if(ih != n-1 && jh == n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+1,NCOLS+NCOLS_1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+1; r++)
//					      for(int c = 0; c < NCOLS+NCOLS_1; c++){
////					    	  flat_samples_2[c][r] = dataArray2.get(0,0,r,c); 
//								idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_2[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<n*NCOLS+NCOLS_1;i++){
//				    	x_2[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<(ih+1)*NROWS+1;j++){
//				    	y_2[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb = 0;
//				    int jub = NROWS;
//				    int jlb = 0; 
//				    int iub = NCOLS+NCOLS_1-1; 
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//					svg[ih][jh] = contour(flat_samples_2,ilb,iub,jlb,jub,x_2,y_2,nci,z,width,height);
//		    	}
//		    	else if(ih == n-1 && jh != n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+NROWS_1,NCOLS+1};
//					
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+NROWS_1; r++)
//					      for(int c = 0; c < NCOLS+1; c++){
////					    	  flat_samples_3[c][r] = dataArray2.get(0,0,r,c); 
//								idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_3[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<(jh+1)*NCOLS+1;i++){
//				    	x_3[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<n*NROWS+NROWS_1;j++){
//				    	y_3[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb = 0;
//				    int jub = NROWS+NROWS_1-1;
//				    int jlb = 0; 
//				    int iub = NCOLS; 
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//					svg[ih][jh] = contour(flat_samples_3,ilb,iub,jlb,jub,x_3,y_3,nci,z,width,height);
//		    	}
//		    	else if(ih == n-1 && jh == n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+NROWS_1,NCOLS+NCOLS_1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+NROWS_1; r++)
//					      for(int c = 0; c < NCOLS+NCOLS_1; c++){
////					    	  flat_samples_1[c][r] = dataArray2.get(0,0,r,c); 
//								idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_1[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<n*NCOLS+NCOLS_1;i++){
//				    	x_1[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<n*NROWS+NROWS_1;j++){
//				    	y_1[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb = 0;
//				    int jub = NROWS+NROWS_1-1;
//				    int jlb = 0; 
//				    int iub = NCOLS+NCOLS_1-1; 
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//					svg[ih][jh] = contour(flat_samples_1,ilb,iub,jlb,jub,x_1,y_1,nci,z,width,height);
//		    	}
//	    	}
//	    }	       
//		
//		return svg;
//	}
	
//	public String[][] PictureRender_svg_surf(String Species,int t, int l,int x0, int y0,int NROWS,
//			  int NCOLS, double ncd, double zmax, double zmin,double width, double height, double x11, double y11, double x22, double y22,int n) throws IOException{
//		
//		int nc = (int)ncd;
//		
//		double interval = (zmax-zmin)/nc;
//		int NROWS_1 = NROWS%n;
//		int NCOLS_1 = NCOLS%n;
//		NROWS = NROWS/n;
//		NCOLS = NCOLS/n;
//	
//		double []x = new double[NCOLS+1];
//	    double []y = new double[NROWS+1];
//	    double []x_1 = new double[NCOLS+NCOLS_1];
//	    double []y_1 = new double[NROWS+NROWS_1];
//	    double []x_2 = new double[NCOLS+NCOLS_1];
//	    double []y_2 = new double[NROWS+1];
//	    double []x_3 = new double[NCOLS+1];
//	    double []y_3 = new double[NROWS+NROWS_1];
//	    double []z = new double[nc];
//	    svg = new String[n][n]; 
//	    
////		NetcdfFile ncfile = NetcdfFile.open(file);
//		double[][] flat_samples = new double[NCOLS+1][NROWS+1];
//		double[][] flat_samples_1 = new double[NCOLS+NCOLS_1][NROWS+NROWS_1];
//		double[][] flat_samples_2 = new double[NCOLS+NCOLS_1][NROWS+1];
//		double[][] flat_samples_3 = new double[NCOLS+1][NROWS+NROWS_1];
//	    //nc文件打开，并且取数
//	    Variable dataVar = ncfile.get(Species);
//	    
//	    int x01;
//	    int y01;
//	    int[] idxtmp = new int[4];
//	    for (int ih = 0; ih < n; ih++){
//	    	for (int jh = 0; jh <n; jh++){
//		    	if (ih != n-1 && jh !=n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//			    	int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+1,NCOLS+1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+1; r++)
//					      for(int c = 0; c < NCOLS+1; c++){
////					    	  flat_samples[c][r] = dataArray2.get(0,0,r,c);
//					    	  idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples[c][r] = dataArray2.getDouble(idxtmp);
//					      }
//					
//					int ii2 = 0;
//					int jj2 = 0;
//				    for (int i=y01;i<(jh+1)*NCOLS+1;i++){
//				    	x[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<(ih+1)*NROWS+1;j++){
//				    	y[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb1 = 0;
//				    int jub1 = NROWS;
//				    int jlb1 = 0; 
//				    int iub1 = NCOLS;	
//				    
//				    //画等势面需要用到
//				    int ilb = y0 + jh*NCOLS;
//				    int jub = x0 + (ih+1)*NROWS;
//				    int jlb = x0 + ih*NROWS; 
//				    int iub = y0 + (jh+1)*NCOLS;
//				    
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//				    
//				    Conrec_2 dzh = new Conrec_2();
//					svg[ih][jh] = dzh.contour(flat_samples,ilb1,iub1,jlb1,jub1,x,y,nc,z,ilb,iub,jlb,jub);
//		    	}
//		    	else if(ih != n-1 && jh == n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+1,NCOLS+NCOLS_1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+1; r++)
//					      for(int c = 0; c < NCOLS+NCOLS_1; c++){
////					    	  flat_samples_2[c][r] = dataArray2.get(0,0,r,c);
//					    	  idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_2[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<n*NCOLS+NCOLS_1;i++){
//				    	x_2[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<(ih+1)*NROWS+1;j++){
//				    	y_2[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb1 = 0;
//				    int jub1 = NROWS;
//				    int jlb1 = 0; 
//				    int iub1 = NCOLS+NCOLS_1-1; 
//				    
//				  //画等势面需要用到
//				    int ilb = y0 + jh*NCOLS;
//				    int jub = x0 + (ih+1)*NROWS;
//				    int jlb = x0 + ih*NROWS; 
//				    int iub = y0 + n*NCOLS+NCOLS_1-1;
//				    
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//				    Conrec_2 dzh = new Conrec_2();
//				    svg[ih][jh] = dzh.contour(flat_samples_2,ilb1,iub1,jlb1,jub1,x_2,y_2,nc,z,ilb,iub,jlb,jub);
//		    	}
//		    	else if(ih == n-1 && jh != n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+NROWS_1,NCOLS+1};
//					
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+NROWS_1; r++)
//					      for(int c = 0; c < NCOLS+1; c++){
////					    	  flat_samples_3[c][r] = dataArray2.get(0,0,r,c); 
//					    	  idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_3[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<(jh+1)*NCOLS+1;i++){
//				    	x_3[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<n*NROWS+NROWS_1;j++){
//				    	y_3[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb1 = 0;
//				    int jub1 = NROWS+NROWS_1-1;
//				    int jlb1 = 0; 
//				    int iub1 = NCOLS; 
//				    
//				  //画等势面需要用到
//				    int ilb = y0 + jh*NCOLS;
//				    int jub = x0 + n*NROWS+NROWS_1-1;
//				    int jlb = x0 + ih*NROWS; 
//				    int iub = y0 + (jh+1)*NCOLS;
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//				    Conrec_2 dzh = new Conrec_2();
//				    svg[ih][jh] = dzh.contour(flat_samples_3,ilb1,iub1,jlb1,jub1,x_3,y_3,nc,z,ilb,iub,jlb,jub);
//		    	}
//		    	else if(ih == n-1 && jh == n-1){
//		    		x01 = x0 + ih*NROWS;
//			    	y01 = y0 + jh*NCOLS;
//		    		int[] ori = {t,l-1,x01,y01};
//					int[] shp = {1,1,NROWS+NROWS_1,NCOLS+NCOLS_1};
////					ArrayFloat.D4 dataArray2 = (ArrayFloat.D4) dataVar.read(ori,shp);
//					MultiArray dataArray2 = dataVar.copyout(ori, shp);
//					for(int r = 0; r < NROWS+NROWS_1; r++)
//					      for(int c = 0; c < NCOLS+NCOLS_1; c++){
////					    	  flat_samples_1[c][r] = dataArray2.get(0,0,r,c); 
//					    	  idxtmp[0] = 0;
//								idxtmp[1] = 0;
//								idxtmp[2] = r;
//								idxtmp[3] = c;
//								flat_samples_1[c][r] = dataArray2.getDouble(idxtmp);
//					      }		
//					int ii2 = 0;
//					int jj2 = 0;	
//				    for (int i=y01;i<n*NCOLS+NCOLS_1;i++){
//				    	x_1[ii2] = xorig + i * xcell;
//				    	ii2++;
//				    }
//				    for (int j=x01;j<n*NROWS+NROWS_1;j++){
//				    	y_1[jj2] = yorig + j * ycell;
//				    	jj2++;
//				    }
//				    int ilb1 = 0;
//				    int jub1 = NROWS+NROWS_1-1;
//				    int jlb1 = 0; 
//				    int iub1 = NCOLS+NCOLS_1-1; 
//				    
//				  //画等势面需要用到
//				    int ilb = y0 + jh*NCOLS;
//				    int jub = x0 + n*NROWS+NROWS_1-1;
//				    int jlb = x0 + ih*NROWS; 
//				    int iub = y0 + n*NCOLS+NCOLS_1-1;
//				    for (int n1 = 0; n1 < nc; n1++){
//				    	z[n1] = zmin+(n1+1)*interval;
//				    }
//				    Conrec_2 dzh = new Conrec_2();
//				    svg[ih][jh] = dzh.contour(flat_samples_1,ilb1,iub1,jlb1,jub1,x_1,y_1,nc,z,ilb,iub,jlb,jub);
//		    	}
//	    	}
//	    }
//		return svg;
//	}
	
	public String[][] PictureRender_svg_surf2(String fileName, String Species,int t, int l, int xDiv, int yDiv, double zmax, double zmin, double interval ){
		
		svg = new String[xDiv][yDiv];
		
		NCRander rander = new NCRanderSurface();
		NcData ncdt = new NcData();
		ncdt.openFile(fileName);
		rander.ncData = ncdt;
		rander.xDiv = xDiv;
		rander.yDiv = yDiv;
		
		double v = (zmax - zmin)/interval;
		List<Double> levelList = new ArrayList<Double>();
		for(int i = 0; i < interval; i++){
			levelList.add(zmin + v * i);
		}
		
		double[] levelArray = new double[levelList.size()];
		for(int i = 0; i < levelList.size(); i++){
			levelArray[i] = levelList.get(i);
		}
		rander.level = levelArray;
		
//		rander.level = new double[]{20,30,40};
		
		for(int i = 0; i < xDiv; i++){
			for(int j = 0; j < yDiv; j++){
				String s = rander.rander(Species, t, l, i, j);  // 污染物种类，时间，层，x，y
				svg[i][j] = s;
			}
		}
		
		Coordinate merMin = ncdt.merMin;
		Coordinate merMax = ncdt.merMax;
		minX = merMin.x;
		maxX = merMax.x;
		minY = -merMax.y;
		maxY = -merMin.y;
		
		ncdt.closeFile();
		
		return svg;
	}
	
	public double getMinX() {
		return minX;
	}
	public double getMaxY() {
		return maxY;
	}
	public double getMaxX() {
		return maxX;
	}
	public double getMinY() {
		return minY;
	}
	public String[][] getSvg() {
		return svg;
	}

//	private double max(double a, double b){
//		if (a > b){
//			return a;
//		}
//		else{
//			return b;
//		}
//	}
//	private double min(double a ,double b){
//		if (a < b){
//			return a;
//		}
//		else{
//			return b;
//		}
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void PictureRender_svg_wind(String file, int t,int l, double height,double y22) throws IOException, InvalidRangeException {
	    WindRender dzh = new WindRender_Arrow();
		NcData ncd = new NcData();
	    String Species_1 = "UWIND";
	    String Species_2 = "VWIND";
		ncd.openFile(file);
		dzh.ncData = ncd;
		svg = new String[1][1];
		svg[0][0] = dzh.Wind_Render(Species_1, Species_2, t, l,height);
	}
	
	public void PictureRender_svg_trajectory(String filePath){
//		String filePath = "/home/daizhaohui/Desktop/dzh1/trajectory/trajectories";
        Trajectory_svg aa = new Trajectory_svg();
        List<File> files = aa.orderByName(filePath);
        svg = new String[1][files.size()];
        int i = 0 ;
        for (File f : files){
        	List<File> f1 = aa.orderByName(f.getPath());
        	String bb = aa.Trajectory_1(f1);
        	svg[0][i] = bb;
        	i++;
        }
	}
	
	public void PictureRender_svg_trajectory_1(String filePath)throws IOException, InvalidRangeException {
		Trajectory_svg dzh = new Trajectory_svg();
		File file = new File(filePath);
		String[] filelist = file.list();
		int rows = dzh.get_Rows(filePath+'/'+filelist[0]);
		svg = new String[1][rows];
		for (int i = 0; i < rows; i++){
			svg[0][i] = dzh.Trajectory_1(filePath,filelist,i);
		}
	}

}
