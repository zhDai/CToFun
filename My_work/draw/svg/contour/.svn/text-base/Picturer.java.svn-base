package cn.ct.em.draw.svg.contour;



import java.io.IOException;

import ucar.ma2.InvalidRangeException;

public class Picturer
{
	private String file; //打开文件路径
	private String Species;  //物种
	private int t;       //时间
	private int l;       //层数
	private int x0;
	private int y0;
	private int NCOLS = 126;
	private int NROWS = 104;  
	private double interval;     //分成的层数
	private double zmin;
    private double zmax;
    private double width;
    private double height;

    private int xDiv;
    private int yDiv;
	
    public Picturer(){
    }
    public Picturer(String filePath){
    	this.file = filePath;
    }
	public Picturer(String file, String species, int t, int l, int x0, int y0,
			int nCOLS, int nROWS, double interval, double zmin, double zmax,
			double width, double height,int xDiv, int yDiv) {
		super();
		this.file = file;
		Species = species;
		this.t = t;
		this.l = l;
		this.x0 = x0;
		this.y0 = y0;
		NCOLS = nCOLS;
		NROWS = nROWS;
		this.interval = interval;
		this.zmin = zmin;
		this.zmax = zmax;
		this.width = width;
		this.height = height;
		this.xDiv = xDiv;
		this.yDiv = yDiv;
	}
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getZmin() {
		return zmin;
	}
	public void setZmin(double zmin) {
		this.zmin = zmin;
	}
	public double getZmax() {
		return zmax;
	}
	public void setZmax(double zmax) {
		this.zmax = zmax;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSpecies() {
		return Species;
	}
	public void setSpecies(String species) {
		Species = species;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getX0() {
		return x0;
	}
	public void setX0(int x0) {
		this.x0 = x0;
	}
	public int getY0() {
		return y0;
	}
	public void setY0(int y0) {
		this.y0 = y0;
	}
	public int getNCOLS() {
		return NCOLS;
	}
	public void setNCOLS(int nCOLS) {
		NCOLS = nCOLS;
	}
	public int getNROWS() {
		return NROWS;
	}
	public void setNROWS(int nROWS) {
		NROWS = nROWS;
	}
    public double getInterval() {
		return interval;
	}
	public void setInterval(double interval) {
		this.interval = interval;
	}
//	public PictureRender_svg createLinePic() throws IOException, InvalidRangeException {
//    	PictureRender_svg pic = new PictureRender_svg();
//    	pic.init(this.file);
// 	    pic.PictureRender_svg_line(this.Species, this.t, this.l, this.x0, this.y0, this.NROWS,this.NCOLS, this.interval, this.zmax, this.zmin,
// 	    		this.width, this.height, this.x11, this.y11, this.x22, this.y22,this.n );
// 	    return pic;
//    }
	
//	public PictureRender_svg createSurfPic() throws IOException, InvalidRangeException {
//    	PictureRender_svg pic = new PictureRender_svg();
//    	pic.init(this.file);
// 	    pic.PictureRender_svg_surf(this.Species, this.t, this.l, this.x0, this.y0, this.NROWS,this.NCOLS, this.interval, this.zmax, this.zmin,
// 	    		this.width, this.height, this.x11, this.y11, this.x22, this.y22,this.n );
// 	    return pic;
//    }
	
	public PictureRender_svg createSurfPic2() throws IOException, InvalidRangeException {
    	PictureRender_svg pic = new PictureRender_svg();
    	//pic.init(this.file);
 	    pic.PictureRender_svg_surf2(this.file,this.Species, this.t, this.l,this.xDiv, this.yDiv, this.zmax, this.zmin, this.interval);
 	    return pic;
    }
	
	public PictureRender_svg createWindPic() throws IOException, InvalidRangeException {
    	PictureRender_svg pic = new PictureRender_svg();
    	//pic.init(this.file);
 	    pic.PictureRender_svg_wind(file,this.t, this.l,this.height, this.height);
 	    return pic;
    }
	public PictureRender_svg createEmitPic() {
		PictureRender_svg pic = new PictureRender_svg();
		pic.PictureRender_svg_surf2(this.file,this.Species, this.t, this.l,this.xDiv, this.yDiv, this.zmax, this.zmin, this.interval);
		return pic;
	}
	
	public PictureRender_svg createTrajectory() throws IOException, InvalidRangeException {
	     PictureRender_svg pic = new PictureRender_svg();
	     pic.PictureRender_svg_trajectory(this.file);
	     return pic;
	}
	
	public PictureRender_svg createTrajectory1() throws IOException, InvalidRangeException {
	     PictureRender_svg pic = new PictureRender_svg();
	     pic.PictureRender_svg_trajectory_1(this.file);
	     return pic;
	}
}
