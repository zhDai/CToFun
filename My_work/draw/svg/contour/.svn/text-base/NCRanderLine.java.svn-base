package cn.ct.em.draw.svg.contour;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class NCRanderLine extends NCRander {

	protected String coorToSVG(List coor) {
		StringBuffer sb = new StringBuffer();
		sb.append("M ").append(((Coordinate) coor.get(0)).x).append(",")
				.append(-((Coordinate) coor.get(0)).y).append(" L ");
		for (int i = 1; i < coor.size(); i++) {
			sb.append(((Coordinate) coor.get(i)).x).append(",")
					.append(-((Coordinate) coor.get(i)).y).append(" ");
		}
		return sb.toString();
	}

//	protected String graphToSVG(Graph g) {
//		List rings = g.getRing();
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < rings.size(); i++) {
//			List ring = (List) rings.get(i);
//			sb.append(coorToSVG(ringToCoor(ring)));
//		}
//		return sb.toString();
//
//	}

	public String[] algorithem(double[][] data, double[] xPos, double[] yPos,
			double[] lev) {
		Contour contour = new Contour();
		Graph[] graphs = contour.contour(data, xPos, yPos, lev);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < graphs.length; i++) {
			graphs[i].connectInner();
			// graphs[i].connectEdge();
			sb.append("<path d=\"");
			sb.append(graphToSVG(graphs[i]));
			sb.append("\" style=\"stoke:rgb:" + colour(i+1,lev.length+1) + "\"");
//			sb.append("fill-rule=\"evenodd\"/>");
		}
//		return sb.toString();
		return null;
	}

}
