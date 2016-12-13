package DaraDeal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ct.em.DBInerface.PGConnection;
import cn.ct.em.calculate.Convention;

public class SQLParaCommit implements Runnable {
	// public List SQL = new ArrayList();
	public PGTransactionS pgt;
//	public String DBName;
//	public String schema;
	
	// public List reSet = new ArrayList();
	public void run() {

		Connection conn = PGConnection.getConnection();
		try {
			conn.setAutoCommit(false);
			while (true) {
				String[] sql = pgt.getOneSQL();
				if (sql == null) {
					break;
				}
//				if(null != schema){
//					Statement stmt = conn.createStatement();
//					stmt.execute("set search_path to " + schema);
//					stmt.close();
//				}
				for (int j = 0; j < sql.length; j++) {
					if (!("".equals(sql[j]) || sql[j] == null)) {
						Statement stmt = conn.createStatement();
						stmt.execute((String) sql[j]);
						stmt.close();
					}
				}
				conn.commit();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				PGConnection.close(conn);
			}
		}
		
		

	}

}
