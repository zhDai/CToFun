package DaraDeal;
import cn.ct.em.core.util.MyUtils;
import cn.ct.em.pointLineSurfaceManage.model.EBaseGrid;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


public class GridCreate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("begin");
		try {
			GridCreate gridManager = new GridCreate();
			gridManager.createBaseGrids(115, 39, 0.03, 0.02, 100, 100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("finished");

	}
	
	public void createBaseGrids(double left, double bottom, double xlength, double ylength, int xSteps, int ySteps) throws InterruptedException{
		GeometryFactory factory = new GeometryFactory();
		int sort = 0;
		PGTransactionS pgt = new PGTransactionS();
		for(int x = 0; x < xSteps; x++){
			for(int y = 0; y < ySteps; y++){
				Coordinate[] coordinates = new Coordinate[5];
				for(int i = 0 ;i < coordinates.length; i++){
					coordinates[i] = new Coordinate();
				}
				double l = left + x * xlength;
				double b = bottom + y * ylength;
				coordinates[0].x = l;
				coordinates[0].y = b;
				coordinates[1].x = l + xlength;
				coordinates[1].y = b;
				coordinates[2].x = l + xlength;
				coordinates[2].y = b + ylength;
				coordinates[3].x = l;
				coordinates[3].y = b + ylength;
				coordinates[4] = coordinates[0];
				sort++;
				Geometry grid = factory.createPolygon(coordinates);
				grid.setSRID(4326);
				EBaseGrid ebgrid = new EBaseGrid();
				ebgrid.setId(MyUtils.getUUID());
				ebgrid.setSort(sort);
				ebgrid.setGeom(grid);
				ebgrid.setEpsg(4326);
				pgt.insert(ebgrid);
			}
		}

		pgt.commitParallel(16);
		
		
	}

}
