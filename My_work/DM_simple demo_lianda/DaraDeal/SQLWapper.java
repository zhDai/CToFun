package DaraDeal;

import java.util.List;
import cn.ct.em.pointLineSurfaceManage.model.EBaseGrid;

public class SQLWapper {
	
	public static void insertSQL(EBaseGrid obj, List reSet, List SQL) {
		SQLHelperString sqlhelper = new SQLHelperString("ct_insertbasegridwkt_base");
		sqlhelper.put(obj.getGeom().toText()).put(obj.getEpsg()).put(obj.getSort());
		String[] SQLs = new String[1];
		SQLs[0] = sqlhelper.getSQL();
		SQL.add(SQLs);
	}

}
