package cn.ct.em.draw.time;

/*
 * @author: Shaola
 */

public class FrameInf {
	private String ncfilename;
	private int time;
	
	FrameInf(String nc, int t) {
		ncfilename = nc;
		time = t;
	}

	public void setNcfilename(String ncfilename) {
		this.ncfilename = ncfilename;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public String getNcfilename() {
		return ncfilename;
	}
	
	public int getTime() {
		return time;
	}
}
