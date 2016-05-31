package cn.ct.em.draw.svg.contour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Graph {
//	public int level;
	public int xLength;
	public int yLength;
	public Line[][][] lineArr;// x, y, m
	public List lineList;
	public Line head ;  //list head
	
//	static private Line empty = new Line();
	
	public int size(){
		int i = 0 ;
		Line p = head.listN;
		while(p != null){
			i++;
			p = p.listN;
		}
		return i;
	}
	
	protected void addFront(Line l){
		if(null != head.listN){
			head.listN.listP = l;
			l.listN = head.listN;
			l.listP = head;
			head.listN = l;
		}else{
			head.listN = l;
			l.listP = head;
		}
	}
//	public boolean hasEdge = false;
	
	//  1---2
	//  |   |
	//  0---3
	public Line[] vertex = new Line[4];
	
//	public Graph(){
//		for(int i = 0 ; i < 4; i++){
//			vertex[i] = new Line();
//		}
//	}

	public void connectEdgeVertex(){
		//顺时针循环
//		boolean end = false;
		
		//左
		List vs = new ArrayList();
		for(int y = 0 ;y < yLength; y++){
			//x=0
			if(null != lineArr[0][y][4-1]){
				Line l = lineArr[0][y][4-1];
				if(l.startEdge){
					vs.add(l);
				}else if(l.endEdge){
					vs.add(l);
				}else{
					//无
				}
			}
		}
		if(vertex[1] != null){//左上
			vs.add(vertex[1]);
			vs.add(vertex[1]);
//			head.listN.listP = vertex[1];
//			vertex[1].listN = head.listN;
//			vertex[1].listP = head;
//			head.listN = vertex[1];
			addFront(vertex[1]);
		}
		
		//上
		for(int x = 0; x < xLength; x++){
			//y=yMax
			if(null != lineArr[x][yLength-1][3-1]){
				Line l = lineArr[x][yLength-1][3-1];
				if(l.startEdge){
					vs.add(l);
				}else if(l.endEdge){
					vs.add(l);
				}else{
					//无
				}
			}
		}
		if(vertex[2] != null){//右上
			vs.add(vertex[2]);
			vs.add(vertex[2]);
//			head.listN.listP = vertex[2];
//			vertex[2].listN = head.listN;
//			vertex[2].listP = head;
//			head.listN = vertex[2];
			addFront( vertex[2]);
		}
		//右
		for(int y = yLength-1; y >= 0; y-- ){
			//x=xMax
			if(null != lineArr[xLength-1][y][2-1]){
				Line l = lineArr[xLength-1][y][2-1];
				if(l.startEdge){
					vs.add(l);
				}else if(l.endEdge){
					vs.add(l);
				}else{
					//无
				}
			}
		}
		if(vertex[3] != null){//右下
			vs.add(vertex[3]);
			vs.add(vertex[3]);
//			head.listN.listP = vertex[3];
//			vertex[3].listN = head.listN;
//			vertex[3].listP = head;
//			head.listN = vertex[3];
			addFront(vertex[3]);
		}
		
		//下
		for(int x = xLength-1; x >= 0; x-- ){
			//y=0
			if(null != lineArr[x][0][1-1]){
				Line l = lineArr[x][0][1-1];
				if(l.startEdge){
					vs.add(l);
				}else if(l.endEdge){
					vs.add(l);
				}else{
					//无
				}
			}
		}
		if(vertex[0] != null){
			vs.add(vertex[0]);
			vs.add(vertex[0]);
//			head.listN.listP = vertex[0];
//			vertex[0].listN = head.listN;
//			vertex[0].listP = head;
//			head.listN = vertex[0];
			addFront(vertex[0]);
		}
		
		
		int endEdge = 1;
		//find first endEdge
		for(int i = 0 ; i < vs.size(); i++){
			if(((Line)vs.get(i)).endEdge){
				endEdge = i;
				break;
			}
		}
		int j = endEdge;
		for(int i = 0; i < vs.size(); i = i + 2){
			((Line)vs.get(j%vs.size())).graphNext = (Line)vs.get( (j + 1)%vs.size() );
			j = j + 2;
		}
		
//		Line first = (Line)vs.get(0);
//		if(first.startEdge){
//			Line last = (Line)vs.get(vs.size()-1);
//			last.graphNext = first;
//			for(int i = 1; i < vs.size()-1; i = i + 2){
//				((Line)vs.get(i)).graphNext = (Line)vs.get(i+1);
//			}
//		}else{
//			for(int i = 0; i < vs.size(); i = i + 2){
//				((Line)vs.get(i)).graphNext = (Line)vs.get(i+1);
//			}
//		}

	}
	
	
	
	
	public void connectInner() {
		head = new Line();
		Line it = head;
		for(int i = 0 ; i < lineList.size(); i++){
			Line l = (Line)lineList.get(i);
			it.listN = l;
			l.listP = it;
			it = l;
//			addFront(l);
			if (l.knowNext) {
				int nx = l.nextGridX;
				int ny = l.nextGridY;
				int nm = l.nextGridM;
				if (nx > -1 && nx < xLength && ny > -1 && ny < yLength) {// 边界内
					l.graphNext = lineArr[nx][ny][nm - 1];
				} else {
					l.endEdge = true;
				}
			} else {
				// 无
			}
		}

	}

	
	
	public void connectRemove() {
		ListIterator it = lineList.listIterator();
		while (it.hasNext()) {
			Line l = (Line)it.next();
			l.graphNext = null;
		}
		head = null;
	}
	
	
	//can make it multithread
	public List getRing(){
		
//		Line n = head.listN;
//		while(n != null){
//			System.out.println("---");
//			System.out.println(" X=" + n.gridX + " Y=" + n.gridY + " M=" + n.gridM );
//			if(n.graphNext != null){
//				System.out.println(" X=" + n.graphNext.gridX + " Y=" + n.graphNext.gridY + " M=" + n.graphNext.gridM );
//			}
//			n = n.listN;
//		}
		
		
		List res = new ArrayList();
//		System.out.println(size());
		while(head.listN != null){
			Line first = (Line)head.listN;
			//remove first
//			System.out.println("begin");
//			System.out.println(size());
//			System.out.println("X="+first.gridX+" Y="+first.gridY+" M="+first.gridM);
			first.listP.listN = first.listN;
			if(first.listN != null){
				first.listN.listP = first.listP;
			}
			first.listN = null;
			first.listP = null;
			List ring = new ArrayList();
			ring.add(first);
//			System.out.println("X=" + first.endX + " Y=" + first.endY);
			Line it2 = first.graphNext;
			if(it2 != null){
				ring.add(it2);
			}
			while(first != it2 && it2 != null){
				//remove it2
//				System.out.println(size());
//				System.out.println("X="+it2.gridX+" Y="+it2.gridY+" M="+it2.gridM);
				
				
				
				it2.listP.listN = it2.listN;
				if(it2.listN != null){
					it2.listN.listP = it2.listP;
				}
				it2.listN = null;
				it2.listP = null;
				it2 = it2.graphNext;

				if(it2 != null){
					ring.add(it2);
				}
				
//				System.out.println("X=" + it2.endX + " Y=" + it2.endY);
			}
			res.add(ring);
		}
		return res;
	}
	
	
}
