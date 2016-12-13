package DataNC;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayFloat.D4;
import ucar.ma2.ArrayInt;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

//import cn.ct.em.calculate.NCConnection;
//import cn.ct.em.calculate.NcData;

public class test {
	
	private static String url = "jdbc:postgresql://127.0.0.1/em_master";
	private static String user = "postgres";
	private static String passwd = "123123";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Connection conn = DriverManager.getConnection(url, user, passwd);			
			conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");
			Statement stmt = conn.createStatement();
//			String sql = "select st_astext(the_geom),sum(countn) as numbers from public.dquery('select t1.id , t1.the_geom, count( * ) from base_grid as t1, (select * from mobile0.testgeo limit 100) as t2 where st_intersects(t1.the_geom, t2.the_geom) group by t1.id') as (id character varying(48), the_geom geometry(Polygon,4326),countn bigint) group by the_geom";
			String sql = "select sort,sum(countn) as numbers from public.dquery('select t1.sort, count( * ) from base_grid as t1, (select * from mobile0.testgeo limit 100) as t2 where st_intersects(t1.the_geom, t2.the_geom) group by t1.sort') as (sort integer,countn bigint) group by sort";
			ResultSet a = stmt.executeQuery(sql);
			//ResultSet转化为List
			List ls = resultSetToList(a);  
			/*
			//生成nc文件
			NetcdfFileWriter writer1 = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, "/home/daizhaohui/dzh.nc", null);
			
			Dimension latDim = writer1.addDimension(null, "nrow", 200);
		    Dimension lonDim = writer1.addDimension(null, "ncol", 200);
		    
		    List<Dimension> dims = new ArrayList<Dimension>();
		    dims.add(latDim);
		    dims.add(lonDim);
		    
		    Variable t = writer1.addVariable(null, "pop", DataType.DOUBLE, dims);
		    t.addAttribute(new Attribute("units", "K"));
//		    Array data = Array.factory(int.class, new int[]{3}, new int[]{1, 2, 3});
//		    t.addAttribute(new Attribute("scale", data));
		    
		    try {
		    	writer1.create();
    	    } catch (IOException e) {
    	      System.err.printf("ERROR creating file %s%n%s", "/home/daizhaohui/dzh.nc", e.getMessage());
    	    }
		    writer1.close();
			*/
			
			//写入nc文件
			NetcdfFileWriter writer = NetcdfFileWriter.openExisting("/home/daizhaohui/dzh.nc");
			Variable v = writer.findVariable("pop");
			int[] shape = v.getShape();
			ArrayDouble A = new ArrayDouble.D2(shape[0], shape[1]);
		    int i, j;
		    Index ima = A.getIndex();
		    for (i = 0; i < shape[0]; i++) {
		      for (j = 0; j < shape[1]; j++) {
		        A.setDouble(ima.set(i, j), (double) (i * 1000000 + j * 1000));
		      }
		    }
		    int[] origin = new int[2];
		    try {
		    	writer.write(v, origin, A);
		    } catch (IOException e) {
		    	System.err.println("ERROR writing file");
		    } catch (InvalidRangeException e) {
		    	e.printStackTrace();
		    }
		    
			
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
//         System.out.println("list:" + list.toString());   
        }   
        return list;   
	} 

}
