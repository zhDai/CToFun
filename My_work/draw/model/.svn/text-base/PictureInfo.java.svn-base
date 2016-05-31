package cn.ct.em.draw.model;

public class PictureInfo implements Cloneable{
	//buffer
//	private String key;
	
	//other infos;
	public String fileName;
	public int time; // frame in nc
	public String species;
	public int layer;
	public double maxV;
	public double minV;
	public int stepV;
	public String colour;
	
//	public double height;
	
	public String Wind_U;
	public String Wind_V;

	public double mapH;	//地图高
	public double mapX; //地图水平参考
	public double mapY; //地图水平参考
	
	public int divX;	//总分块
	public int divY;
	public int numX;	//第几分块
	public int numY;
	public int zoomLevel;
	
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(fileName)
		.append("#").append(time)
		.append("#").append(species)
		.append("#").append(layer)
		.append("#").append(maxV)
		.append("#").append(minV)
		.append("#").append(stepV)
		.append("#").append(colour)
		.append("#").append(divX)
		.append("#").append(divY)	
		.append("#").append(numX)
		.append("#").append(numY)
		.append("#").append(zoomLevel);
		return sb.toString();
	}
//	public void setKey(String key) {
//		this.key = key;
//	}
	
	
}
