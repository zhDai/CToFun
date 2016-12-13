package DataNC;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.ct.em.calculate.NCConnection;
import cn.ct.em.calculate.NcData;
import cn.ct.em.calculate.PollutionDataImport;

import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;
import ucar.netcdf.Netcdf;

class DStruct{

	public int sort_value;
	public float number;
	
	public DStruct(int sort_value, float number){
			this.sort_value = sort_value;
			this.number= number;
	 }
};

class DataStruct{

	public int t;
	public int l;
	public int x;
	public int y;
	public double v;
		
	public DataStruct(int t, int l, int x, int y, double v){
			this.t = t;
			this.l = l;
			this.x = x;
			this.y = y;
			this.v = v;
	 }
};

public class ConsultToNc {
	
	private static String url = "jdbc:postgresql://127.0.0.1/em_master";
	private static String user = "postgres";
	private static String passwd = "123123";

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Connection conn = DriverManager.getConnection(url, user, passwd);			
			conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");
			Statement stmt = conn.createStatement();
//			String sql = "select * from public.ddlexec('create table userTable( id serial primary key,  userName varchar(250),  userAge smallint,  userAlias varchar(250))');";
//			stmt.executeUpdate(sql);
//			String sql1 = "select * from public.ddlexec('insert into mobile0.userTable(userName, userAge) values(''laser'', 30)');";
//			String sql2 = "select * from public.ddlexec('insert into mobile0.userTable(userName, userAge) values(''henry'', 20)');";
//			stmt.executeUpdate(sql1);		
//			stmt.executeUpdate(sql2);
//			String sql = "select st_astext(the_geom),sum(countn) as numbers from public.dquery('select t1.id , t1.the_geom, count( * ) from base_grid as t1, (select * from mobile0.testgeo limit 100) as t2 where st_intersects(t1.the_geom, t2.the_geom) group by t1.id') as (id character varying(48), the_geom geometry(Polygon,4326),countn bigint) group by the_geom";
			String sql = "select sort,sum(countn) as numbers from public.dquery('select t1.sort, count( * ) from base_grid as t1, (select * from mobile0.testgeo limit 100) as t2 where st_intersects(t1.the_geom, t2.the_geom) group by t1.sort') as (sort integer,countn bigint) group by sort";
			ResultSet a = stmt.executeQuery(sql);
			//ResultSet转化为List
			List ls = resultSetToList(a);   
			
			NcData nc = new NcData();
//			nc.fname = "/home/daizhaohui/dzh.nc";
//			nc.ncfile = NCConnection.getConnection("/home/daizhaohui/dzh.nc");
			nc.fname = "/home/daizhaohui/wed.nc";
			nc.ncfile = NCConnection.getConnection("/home/daizhaohui/wed.nc");
			
			ConsultToNc aa = new ConsultToNc();
			aa.write(nc, "pop", ls);
			
			stmt.close();
			conn.commit();	
			conn.close();
			
		}catch (Exception e) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
		}
		
		System.out.println("Records created successfully");
		
	}
	
	//ResultSet转化为List
	public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
        List list = new ArrayList();   
        Map rowData = new HashMap();   
        while (rs.next()) {   
         rowData = new HashMap(columnCount);   
         for (int i = 1; i <= columnCount; i++) {   
                 rowData.put(md.getColumnName(i), rs.getObject(i));   
         }   
         list.add(rowData);   
        }   
        return list;   
	} 
	
	public void write(NcData nc , String varname, List data){
		
//		int[] ori = new int[4];
//		ori[0] = 0;
//		ori[1] = 0;
//		ori[2] = 0;
//		ori[3] = 0;
//		int[] shp = new int[4];
//		shp[0] = 1;
//		shp[1] = 1;
//		shp[2] = 50;
//		shp[3] = 50;
//		int[] ori = new int[2];
//		ori[0] = 0;
//		ori[1] = 0;
//		int[] shp = new int[2];
//		shp[0] = 50;
//		shp[1] = 50;
//		Variable dataVar = nc.ncfile.findVariable(varname);//得到变量
////		Array dataArray = null;
//
//		ArrayInt.D2 dataArray = null;
//		try { 
////			dataArray = dataVar.read(ori, shp);
//			dataArray = (ArrayInt.D2)dataVar.read(ori, shp);
//		} catch (IOException | InvalidRangeException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		int[] idxtmp = new int[4];
//		for(int i = 0; i < data.size(); i++){
//		for(int i = 0; i < 1; i++){
//			Map aa = (Map)data.get(i);
//			int sort = (int) aa.get("sort");
//			Object numb = aa.get("numbers");
//			dataArray.setObject(sort, numb);
//			dataArray.setObject(2000, 2000);
//			dataArray.set(45, 45, 999);
//			System.out.println(dataArray.getObject(1500));

//		}
		try {
			NetcdfFileWriter ncw = NetcdfFileWriter.openExisting(nc.fname);
			Variable v = ncw.findVariable("pop");
			int[] shape = v.getShape();
			ArrayDouble A = new ArrayDouble.D2(shape[0], shape[1]);
		    int i, j;
		    Index ima = A.getIndex();
		    for(int k = 0; k < data.size(); k++){
		    	Map aa = (Map)data.get(k);
		    	int sort = (int) aa.get("sort");
		    	Object numb = aa.get("numbers");
		    	j = (sort-1)%100;
		    	i = (sort-1)/100;
		    	A.setObject(ima.set(i, j), numb);
		    }
		    int[] origin = new int[2];
		    try {
		    	ncw.write(v, origin, A);
		    } catch (IOException e) {
		    	System.err.println("ERROR writing file");
		    } catch (InvalidRangeException e) {
		    	e.printStackTrace();
		    }
//			ncw.write(dataVar, ori, dataArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(dataArray.getObject(8053));
	}

}
