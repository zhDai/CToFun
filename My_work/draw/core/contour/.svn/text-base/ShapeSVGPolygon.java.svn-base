package cn.ct.em.draw.core.contour;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class ShapeSVGPolygon extends ShapeBridge {

	@Override
	public String coorToString(List coor) {
		StringBuffer sb = new StringBuffer();
		sb.append(" M ").append(((Coordinate) coor.get(0)).x).append(",")
				.append(-((Coordinate) coor.get(0)).y).append(" L ");
		for (int i = 1; i < coor.size(); i++) {
			sb.append(((Coordinate) coor.get(i)).x).append(",")
					.append(-((Coordinate) coor.get(i)).y).append(" ");
		}
		sb.append(" z ");
		return sb.toString();
	}

	@Override
	public String[] algorithem(double[][] data, double[] xPos, double[] yPos,
			double[] lev) {
		Contour contour = new Contour();
		Graph[] graphs = contour.contour(data, xPos, yPos, lev);

		String[] res = new String[lev.length];
		for (int i = 0; i < lev.length; i++) {
			StringBuffer sb = new StringBuffer();
			graphs[i].connectInner();
			graphs[i].connectEdgeVertex();
			sb.append(" <path d=\"");
			sb.append(nCRanderCore.graphToString(graphs[i]));

			sb.append("\" style=\"fill:" + nCRanderCore.colour(lev[i] - nCRanderCore.level[0], nCRanderCore.level[nCRanderCore.level.length-1] - nCRanderCore.level[0]) + "\"");
			
			sb.append(" fill-rule=\"evenodd\"/> ");
			sb.append("\n");
			
//			System.out.println(sb2.toString());
			res[i] = sb.toString();
		}
//		System.out.println(sb.toString());
		return res;
	}

}
