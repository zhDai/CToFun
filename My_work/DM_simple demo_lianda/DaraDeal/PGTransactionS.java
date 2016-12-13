package DaraDeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cn.ct.em.pointLineSurfaceManage.model.EBaseGrid;


public class PGTransactionS {
	private List SQL = new ArrayList();
	private List reSet = new ArrayList();
	private int count = 0;
	
	public synchronized String[] getOneSQL() {
		if (SQL.size() > count) {
			String[] sql = (String[]) SQL.get(count);
			count++;
			return sql;
		}
		return null;
	}
	
	public void insert(EBaseGrid obj) {
		SQLWapper.insertSQL(obj, reSet, SQL);
	}
	
	public void commitParallel(int threadNum) throws InterruptedException {
	
		if (threadNum > SQL.size()) {
			threadNum = SQL.size();
		}
		Thread[] threads = new Thread[threadNum];
		// int step = SQL.size() / threadNum;

		for (int i = 0; i < threadNum; i++) {
			SQLParaCommit sQLParaCommit = new SQLParaCommit();
			sQLParaCommit.pgt = this;
			threads[i] = new Thread(sQLParaCommit);
		}
		count = 0;
		for (int i = 0; i < threads.length; i++) {
			if (null != threads[i]) {
				threads[i].start();
			}
		}
		for (int i = 0; i < threads.length; i++) {
			if (null != threads[i]) {
				threads[i].join();
			}
		}
		for (int i = 0; i < reSet.size(); i++) {
			Map[] m = (Map[]) reSet.get(i);
			for (int j = 0; j < m.length; j++) {
				if (null != m[j]) {
					m[j].clear();
				}
			}
		}

	}

}
