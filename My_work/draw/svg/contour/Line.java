package cn.ct.em.draw.svg.contour;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Line {
	public double startX;
	public double startY;
	public double endX;
	public double endY;
	public int gridX ;
	public int gridY ;
	public int gridM = -1; //-1 means vertex
	public boolean knowNext = true; //知道下一个格子
	public int nextGridX;
	public int nextGridY;
	public int nextGridM;
//	public int previousGridX;
//	public int previousGridY;
//	public int previousGridM;
	
	public double level;
//	public int idx;
//	public Iterator itor;
	public Line graphNext = null;
	public boolean startEdge = false;
	public boolean endEdge = false;
	
	
	public Line listP = null;
	public Line listN =  null;
	
}
