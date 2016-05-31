package cn.ct.em.draw.core.contour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.ct.em.calculate.LambertTransLonLatToXY;
import cn.ct.em.calculate.LambertTransXYToLonLat;
import cn.ct.em.calculate.MercatorTransLonLatToXY;
import cn.ct.em.calculate.MercatorTransXYToLonLat;
import cn.ct.em.calculate.NcData;

import com.vividsolutions.jts.geom.Coordinate;

public class Contour {

	protected double[] h = new double[5];
	protected int[] sh = new int[5];
	protected double[] xh = new double[5];
	protected double[] yh = new double[5];

//	protected NcData ncFile = null;
//
//	public NcData getNc() {
//		return ncFile;
//	}
//
//	public void setNc(NcData nc) {
//		this.ncFile = nc;
//	}

	// public String[][] Rander( String Species, int t, int l,int x0, int y0,
	// int NROWS,
	// int NCOLS, double nc, double zmax, double zmin,double width, double
	// height, double x11, double y11, double x22, double y22,int n){
	//
	//
	//
	// return null;
	// }

	// dat[y][x]
	// can make it multithread
	public Graph[] contour(double[][] data, double[] xPos, double[] yPos,
			double[] level) {

		// level,x,y,i
		int xLength = data.length - 1;
		int yLength = data[0].length - 1;
		int nc = level.length;

		Line[][][][] lineArr = new Line[nc][xLength][yLength][4];
		List[] lineList = new ArrayList[nc];
		// ListIterator[] itors = new ListIterator[nc];

		for (int i = 0; i < nc; i++) {
			lineList[i] = new ArrayList();
			// itors[i] = lineList[i].listIterator();
		}
		int m1;
		int m2;
		int m3;
		int case_value;
		double dmin;
		double dmax;
		int x, y, k, m;

		// The indexing of im and jm should be noted as it has to start from
		// zero
		// unlike the fortran counter part
		int[] xm = { 0, 1, 1, 0 };
		int[] ym = { 0, 0, 1, 1 };

		// Note that castab is arranged differently from the FORTRAN code
		// because
		// Fortran and C/C++ arrays are transposed of each other, in this case
		// it is more tricky as castab is in 3 dimension
		int[][][] castab = { { { 0, 0, 8 }, { 0, 2, 5 }, { 7, 6, 9 } },
				{ { 0, 3, 4 }, { 1, 3, 1 }, { 4, 3, 0 } },
				{ { 9, 6, 7 }, { 5, 2, 0 }, { 8, 0, 0 } } };

		for (y = (yLength - 1); y >= 0; y--) {
			for (x = 0; x <= xLength - 1; x++) {
				double temp1, temp2;
				temp1 = Math.min(data[x][y], data[x][y + 1]);
				temp2 = Math.min(data[x + 1][y], data[x + 1][y + 1]);
				dmin = Math.min(temp1, temp2);
				temp1 = Math.max(data[x][y], data[x][y + 1]);
				temp2 = Math.max(data[x + 1][y], data[x + 1][y + 1]);
				dmax = Math.max(temp1, temp2);

				if (dmax >= level[0] && dmin <= level[nc - 1]) {
					for (k = 0; k < nc; k++) {
						if (level[k] >= dmin && level[k] <= dmax) {

							for (m = 4; m >= 0; m--) {
								if (m > 0) {
									// The indexing of im and jm should be noted
									// as it has to
									// start from zero
									h[m] = data[x + xm[m - 1]][y + ym[m - 1]] - level[k];
									xh[m] = xPos[x + xm[m - 1]];
									yh[m] = yPos[y + ym[m - 1]];
									if (0 == h[m]) {// 避免等势面落在顶点上
										//h[m] = data[x + xm[m - 1]][y + ym[m - 1]]
										//		* 1.00000001 - level[k];
										h[m] = 0.00000000000001;
									}
								} else {
									h[0] = 0.25 * (h[1] + h[2] + h[3] + h[4]);
									xh[0] = 0.5 * (xPos[x] + xPos[x + 1]);
									yh[0] = 0.5 * (yPos[y] + yPos[y + 1]);
									if (0 == h[0]) {// 避免等势面落在顶点上
										//h[m] = data[x + xm[m - 1]][y + ym[m - 1]]
										//		* 1.00000001 - level[k];
										h[0] = 0.0000000000001;
									}
								}
								if (h[m] > 0.0) {
									sh[m] = 1;
								} else if (h[m] < 0.0) {
									sh[m] = -1;
								} else
									sh[m] = 0;
							}
							//
							// Note: at this stage the relative heights of the
							// corners and the
							// centre are in the h array, and the corresponding
							// coordinates are
							// in the xh and yh arrays. The centre of the box is
							// indexed by 0
							// and the 4 corners by 1 to 4 as shown below.
							// Each triangle is then indexed by the parameter m,
							// and the 3
							// vertices of each triangle are indexed by
							// parameters m1,m2,and
							// m3.
							// It is assumed that the centre of the box is
							// always vertex 2
							// though this is important only when all 3 vertices
							// lie exactly on
							// the same contour level, in which case only the
							// side of the box
							// is drawn.
							//
							//
							// vertex 4 +-------------------+ vertex 3
							// | \ / |
							// | \ m-3 / |
							// | \ / |
							// | \ / |
							// | m=4 X m=2 | the centre is vertex 0
							// | / \ |
							// | / \ |
							// | / m=1 \ |
							// | / \ |
							// vertex 1 +-------------------+ vertex 2
							//
							//
							//
							// Scan each triangle in the box
							//
							int nextGridX  = -1;
							int nextGridY  = -1;
							int nextGrid12 = 0; // 1，2边的临格
							int nextGrid23 = 0; // 2，3边的临格
							int nextGrid31 = 0; // 3，1边的临格

							for (m = 1; m <= 4; m++) {
								m1 = m;
								m2 = 0;
								if (m != 4) {
									m3 = m + 1;
								} else {
									m3 = 1;
								}

								case_value = castab[sh[m1] + 1][sh[m2] + 1][sh[m3] + 1];
								if (case_value != 0) {
									Line l = new Line();
									l.gridM = m;
									l.gridX = x;
									l.gridY = y;
									lineList[k].add(l);
//									l.idx = lineList[k].size() - 1;
									// ListIterator it =
									// lineList[k].listIterator();
									// it.next();
									// l.itor = it;
									lineArr[k][x][y][m - 1] = l;

									switch (m) {
									case 1:
										nextGridX = x;
										nextGridY = y - 1;
										nextGrid12 = 4;
										nextGrid23 = 2;
										nextGrid31 = 3;
										break;
									case 2:
										nextGridX = x + 1;
										nextGridY = y;
										nextGrid12 = 1;
										nextGrid23 = 3;
										nextGrid31 = 4;
										break;
									case 3:
										nextGridX = x;
										nextGridY = y + 1;
										nextGrid12 = 2;
										nextGrid23 = 4;
										nextGrid31 = 1;
										break;
									case 4:
										nextGridX = x - 1;
										nextGridY = y;
										nextGrid12 = 3;
										nextGrid23 = 1;
										nextGrid31 = 2;
										break;
									}

									switch (case_value) {
									// case 1: // Line between vertices 1 and 2
									// l.endNoVertex = false; //不清楚下一个格子
									// if(h[m3] > 0){//指向中心
									// // x1=xh[m1];
									// // y1=yh[m1];
									// // x2=xh[m2];
									// // y2=yh[m2];
									// l.startX = xh[m1];
									// l.startY = yh[m1];
									// l.endX = xh[m2];
									// l.endY = yh[m2];
									//
									// }else{//指向外
									// // x2=xh[m1];
									// // y2=yh[m1];
									// // x1=xh[m2];
									// // y1=yh[m2];
									// l.startX = xh[m2];
									// l.startY = yh[m2];
									// l.endX = xh[m1];
									// l.endY = yh[m1];
									// }
									// break;
									// case 2: // Line between vertices 2 and 3
									// l.endNoVertex = false; //不清楚下一个格子
									// if(h[m1] > 0){//
									// // x1=xh[m2];
									// // y1=yh[m2];
									// // x2=xh[m3];
									// // y2=yh[m3];
									// l.startX = xh[m2];
									// l.startY = yh[m2];
									// l.endX = xh[m3];
									// l.endY = yh[m3];
									// }else{
									// // x2=xh[m2];
									// // y2=yh[m2];
									// // x1=xh[m3];
									// // y1=yh[m3];
									// l.startX = xh[m3];
									// l.startY = yh[m3];
									// l.endX = xh[m2];
									// l.endY = yh[m2];
									// }
									// break;
									// case 3: // Line between vertices 3 and 1
									// l.endNoVertex = false; //不清楚下一个格子
									// if(h[m2] > 0){//
									// // x1=xh[m3];
									// // y1=yh[m3];
									// // x2=xh[m1];
									// // y2=yh[m1];
									// l.startX = xh[m3];
									// l.startY = yh[m3];
									// l.endX = xh[m1];
									// l.endY = yh[m1];
									//
									// }else{
									// // x2=xh[m3];
									// // y2=yh[m3];
									// // x1=xh[m1];
									// // y1=yh[m1];
									// l.startX = xh[m1];
									// l.startY = yh[m1];
									// l.endX = xh[m3];
									// l.endY = yh[m3];
									// }
									// break;
									// case 4: // Line between vertex 1 and side
									// 2-3
									// if(h[m3] > 0){//1指向2，3；内格
									// l.endNoVertex = true; //清楚下一个格子
									// // x1=xh[m1];
									// // y1=yh[m1];
									// // x2=xsect(m2,m3);
									// // y2=ysect(m2,m3);
									// l.startX = xh[m1];
									// l.startY = yh[m1];
									// l.endX = xsect(m2,m3);
									// l.endY = ysect(m2,m3);
									// l.nextGridX = x;
									// l.nextGridY = y;
									// l.nextGridM = nextGrid23;
									// }else{
									// l.endNoVertex = false; //不清楚下一个格子；2，3指向1
									// // x2=xh[m1];
									// // y2=yh[m1];
									// // x1=xsect(m2,m3);
									// // y1=ysect(m2,m3);
									// l.startX = xsect(m2,m3);
									// l.startY = ysect(m2,m3);
									// l.endX = xh[m1];
									// l.endY = yh[m1];
									//
									// }
									// break;
									// case 5: // Line between vertex 2 and side
									// 3-1
									// if(h[m1] > 0){//2指向3，1；外格
									// l.endNoVertex = true; //清楚下一个格子
									// // x1=xh[m2];
									// // y1=yh[m2];
									// // x2=xsect(m3,m1);
									// // y2=ysect(m3,m1);
									// l.startX = xh[m2];
									// l.startY = yh[m2];
									// l.endX = xsect(m3,m1);
									// l.endY = ysect(m3,m1);
									// l.nextGridX = nextGridX;
									// l.nextGridY = nextGridY;
									// l.nextGridM = nextGrid31;
									//
									// }else{
									// l.endNoVertex = false;
									// //不清楚下一个格子，1,3指向2；内
									// // x2=xh[m2];
									// // y2=yh[m2];
									// // x1=xsect(m3,m1);
									// // y1=ysect(m3,m1);
									// l.startX = xsect(m3,m1);
									// l.startY = ysect(m3,m1);
									// l.endX = xh[m2];
									// l.endY = yh[m2];
									// switch(m){
									// case 1:
									// if(y == 0){
									// l.startEdge = true;
									// }
									// break;
									// case 2:
									// if(x == xMax){
									// l.startEdge = true;
									// }
									// break;
									// case 3:
									// if(y == yMax){
									// l.startEdge = true;
									// }
									// break;
									// case 4:
									// if(x == 0){
									// l.startEdge = true;
									// }
									// break;
									//
									// }
									//
									// }
									// break;
									// case 6: // Line between vertex 3 and side
									// 1-2
									// if(h[m2] > 0){//3指向1,2；内
									// l.endNoVertex = true;
									// // x1=xh[m3];
									// // y1=yh[m3];
									// // x2=xsect(m1,m2);
									// // y2=ysect(m1,m2);
									// l.startX = xh[m3];
									// l.startY = yh[m3];
									// l.endX = xsect(m1,m2);
									// l.endY = ysect(m1,m2);
									// l.startX = x;
									// l.startY = y;
									// l.nextGridM = nextGrid12;
									// }else{//1,2指向3
									// l.endNoVertex = false;
									// // x2=xh[m3];
									// // y2=yh[m3];
									// // x1=xsect(m1,m2);
									// // y1=ysect(m1,m2);
									// l.startX = xsect(m1,m2);
									// l.startY = ysect(m1,m2);
									// l.endX = xh[m3];
									// l.endY = yh[m3];
									// }
									// break;
									case 7: // Line between sides 1-2 and 2-3
										l.knowNext = true; // 清楚下一个格子
										if (h[m2] > 0) {// 2，3指向1，2; 内
										// x2=xsect(m1,m2);
										// y2=ysect(m1,m2);
										// x1=xsect(m2,m3);
										// y1=ysect(m2,m3);

											// l.startX = xsect(m2,m3);
											// l.startY = ysect(m2,m3);
											l.endX = xsect(m1, m2);
											l.endY = ysect(m1, m2);
											l.nextGridX = x;
											l.nextGridY = y;
											l.nextGridM = nextGrid12;

										} else {// 1，2指向2，3；内
										// x1=xsect(m1,m2);
										// y1=ysect(m1,m2);
										// x2=xsect(m2,m3);
										// y2=ysect(m2,m3);

											// l.startX = xsect(m1,m2);
											// l.startY = ysect(m1,m2);
											l.endX = xsect(m2, m3);
											l.endY = ysect(m2, m3);
											l.nextGridX = x;
											l.nextGridY = y;
											l.nextGridM = nextGrid23;
										}
										break;
									case 8: // Line between sides 2-3 and 3-1
										l.knowNext = true; // 清楚下一个格子
										if (h[m3] > 0) {// 1，3指向2，3 ; 格内
										// x2=xsect(m2,m3);
										// y2=ysect(m2,m3);
										// x1=xsect(m3,m1);
										// y1=ysect(m3,m1);

											// l.startX = xsect(m3,m1);
											// l.startY = ysect(m3,m1);
											l.endX = xsect(m2, m3);
											l.endY = ysect(m2, m3);

											l.nextGridX = x;
											l.nextGridY = y;
											l.nextGridM = nextGrid23;
											switch (m) {
											case 1:
												if (y == 0) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);

												}
												break;
											case 2:
												if (x == xLength-1) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											case 3:
												if (y == yLength-1) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											case 4:
												if (x == 0) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											}

										} else {// 2，3指向1，3 ; 格外
										// x1=xsect(m2,m3);
										// y1=ysect(m2,m3);
										// x2=xsect(m3,m1);
										// y2=ysect(m3,m1);

											// l.startX = xsect(m2,m3);
											// l.startY = ysect(m2,m3);
											l.endX = xsect(m3, m1);
											l.endY = ysect(m3, m1);
											l.nextGridX = nextGridX;
											l.nextGridY = nextGridY;
											l.nextGridM = nextGrid31;

										}
										break;
									case 9: // Line between sides 3-1 and 1-2
										l.knowNext = true; // 清楚下一个格子
										if (h[m1] > 0) {// 1，2指向3，1；外
										// x2=xsect(m3,m1);
										// y2=ysect(m3,m1);
										// x1=xsect(m1,m2);
										// y1=ysect(m1,m2);

											// l.startX = xsect(m1,m2);
											// l.startY = ysect(m1,m2);
											l.endX = xsect(m3, m1);
											l.endY = ysect(m3, m1);

											l.nextGridX = nextGridX;
											l.nextGridY = nextGridY;
											l.nextGridM = nextGrid31;

										} else {// 3，1指向1，2；内
										// x1=xsect(m3,m1);
										// y1=ysect(m3,m1);
										// x2=xsect(m1,m2);
										// y2=ysect(m1,m2);

											// l.startX = xsect(m3,m1);
											// l.startY = ysect(m3,m1);
											l.endX = xsect(m1, m2);
											l.endY = ysect(m1, m2);
											l.nextGridX = x;
											l.nextGridY = y;
											l.nextGridM = nextGrid12;
											switch (m) {
											case 1:
												if (y == 0) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											case 2:
												if (x == xLength-1) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											case 3:
												if (y == yLength-1) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											case 4:
												if (x == 0) {
													l.startEdge = true;
													l.startX = xsect(m3, m1);
													l.startY = ysect(m3, m1);
												}
												break;
											}
										}
										break;
									default:
										break;
									}
									// Put your processing code here and comment
									// out the printf

								}
							}
						}
					}
				}
			}
		}

		Graph[] graph = new Graph[nc];
		for (int i = 0; i < nc; i++) {
			graph[i] = new Graph();
			graph[i].lineArr = lineArr[i];
			graph[i].lineList = lineList[i];
			graph[i].xLength = xLength;
			graph[i].yLength = yLength;
			// graph[i].level = nc;
			if (data[0][0] >= level[i]) {
				graph[i].vertex[0] = new Line();
				graph[i].vertex[0].startX = xPos[0];
				graph[i].vertex[0].startY = yPos[0];
				graph[i].vertex[0].endX = xPos[0];
				graph[i].vertex[0].endY = yPos[0];
				graph[i].vertex[0].gridX = 0;
				graph[i].vertex[0].gridY = 0;
			}
			if(data[0][yLength] >= level[i]){
				graph[i].vertex[1] = new Line();
				graph[i].vertex[1].startX = xPos[0];
				graph[i].vertex[1].startY = yPos[yLength];
				graph[i].vertex[1].endX = xPos[0];
				graph[i].vertex[1].endY = yPos[yLength];
				graph[i].vertex[1].gridX = 0;
				graph[i].vertex[1].gridY = yLength;
			}
			if(data[xLength][yLength] >= level[i]){
				graph[i].vertex[2] = new Line();
				graph[i].vertex[2].startX = xPos[xLength];
				graph[i].vertex[2].startY = yPos[yLength];
				graph[i].vertex[2].endX = xPos[xLength];
				graph[i].vertex[2].endY = yPos[yLength];
				graph[i].vertex[2].gridX = xLength;
				graph[i].vertex[2].gridY = yLength;
			}
			if(data[xLength][0] >= level[i]){
				graph[i].vertex[3] = new Line();
				graph[i].vertex[3].startX = xPos[xLength];
				graph[i].vertex[3].startY = yPos[0];
				graph[i].vertex[3].endX = xPos[xLength];
				graph[i].vertex[3].endY = yPos[0];
				graph[i].vertex[3].gridX = xLength;
				graph[i].vertex[3].gridY = yLength;
			}

		}
		return graph;
	}



//	public String graphToSVGSurface(Graph g) {
//		List rings = g.getRing();
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < rings.size(); i++) {
//			List ring = (List) rings.get(i);
//			sb.append(coorToSVGSurface(ringToCoor(ring)));
//		}
//		return sb.toString();
//
//	}
	
	
	
//	public String graphToSVGLine(Graph g) {
//		List rings = g.getRing();
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < rings.size(); i++) {
//			List ring = (List) rings.get(i);
//			sb.append(coorToSVGSurface(ringToCoor(ring)));
//		}
//		return sb.toString();
//
//	}
	
	

//	public String coorToSVGSurface(List coor) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("M").append(((Coordinate) coor.get(0)).x).append(",")
//				.append(((Coordinate) coor.get(0)).y).append(" L ");
//		for (int i = 1; i < coor.size(); i++) {
//			sb.append(((Coordinate) coor.get(0)).x).append(",")
//					.append(-((Coordinate) coor.get(0)).y).append(" ");
//		}
//		sb.append("z ");
//		return sb.toString();
//	}

	
	
//	public String coorToSVGLine(List coor) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("M").append(((Coordinate) coor.get(0)).x).append(",")
//				.append(((Coordinate) coor.get(0)).y).append(" L ");
//		for (int i = 1; i < coor.size(); i++) {
//			sb.append(((Coordinate) coor.get(0)).x).append(",")
//					.append(-((Coordinate) coor.get(0)).y).append(" ");
//		}
//		return sb.toString();
//	}
	
//	// return coordinate lists
//	public List ringToCoor(List ring) {
//		List coor = new ArrayList();
//		for (int j = 0; j < ring.size(); j++) {
//			Line l = (Line) ring.get(j);
//			if (l.startEdge) {
//				Coordinate c = new Coordinate();
//				c.x = l.startX;
//				c.y = l.startY;
//				c = ncFile.e4326To3857.coordinateTrans(ncFile.XY2lonlat
//						.coordinateTrans(c));
//				coor.add(c);
//				Coordinate c2 = new Coordinate();
//				c2.x = l.endX;
//				c2.y = l.endY;
//				c2 = ncFile.e4326To3857.coordinateTrans(ncFile.XY2lonlat
//						.coordinateTrans(c2));
//				coor.add(c2);
//			} else {
//				Coordinate c = new Coordinate();
//				c.x = l.endX;
//				c.y = l.endY;
//				c = ncFile.e4326To3857.coordinateTrans(ncFile.XY2lonlat
//						.coordinateTrans(c));
//				coor.add(c);
//			}
//
//		}
//
//		return coor;
//	}

	protected double xsect(int p1, int p2) {
		return (h[p2] * xh[p1] - h[p1] * xh[p2]) / (h[p2] - h[p1]);
	}

	protected double ysect(int p1, int p2) {
		return (h[p2] * yh[p1] - h[p1] * yh[p2]) / (h[p2] - h[p1]);
	}

}
