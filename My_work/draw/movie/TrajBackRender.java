package cn.ct.em.draw.movie;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.vividsolutions.jts.geom.Coordinate;
//
//import ucar.ma2.InvalidRangeException;
//import net.sf.ehcache.Cache;
//import cn.ct.em.calculate.NcData;
//import cn.ct.em.calculate.PGORM;
//import cn.ct.em.core.util.EhcacheUtil;
//import cn.ct.em.draw.svg.contour.NCRander;
//import cn.ct.em.draw.svg.contour.NCRanderSurface;
//import cn.ct.em.draw.svg.contour.PictureRender_svg;
//import cn.ct.em.draw.svg.contour.Picturer;
//
//public class TrajBackRender {
//	public String picId;
//	public Map<String,String> picTrack;
//	public String taskId;
//	public Map<String,String> task;
//	public List<Integer> time;
//	public String[][] svg;
//	
//	public TrajBackRender(){};
//	
//	public TrajBackRender(String picId){
//		this.picId = picId;
//	};
//	
//	public void frPicTrack(){
//		Map<String,String> con = new HashMap<String,String>();
//		con.put("id", this.picId);
//		List<Map<String,String>> picTracks = PGORM.selectPicTra(con);
//		if(null != picTracks && picTracks.size() > 0){
//			this.picTrack = picTracks.get(0);
//			this.taskId = picTrack.get("task_id");
//		}
//	}
//	
//	public void frTask(){
//		if(null != taskId){
//			Map<String,String> con = new HashMap<String,String>();
//			con.put("id", this.taskId);
//			List<Map<String,String>> tasks = PGORM.selectTask(con);
//			if(null != tasks && tasks.size() > 0){
//				task = tasks.get(0);
//			}
//		}
//	}
//	
//	public void svg_redener_fstr() throws IOException, InvalidRangeException{
//		String filePath = task.get("ncfilename");
//		String key = this.picId + filePath;
//		Cache cache = EhcacheUtil.getCache("trajBack-cache");
//		if (EhcacheUtil.contains(cache,key))
//		{
//			Object pic = EhcacheUtil.getObject(cache, key);
//			svg = (String[][])pic;
//		}
//		else 
//		{
//			Picturer picturer = new Picturer(filePath);
//			PictureRender_svg  svgs = picturer.createTrajectory();
//			svg = svgs.getSvg();
//			EhcacheUtil.insertObject(cache,key,svg);
//		}
//		if(null != svg){
//			time = new ArrayList<Integer>();
//			for(int i = 0; i < svg[0].length; i++){
//				time.add(i);
//			}
//		}
//	}
//	
//	public void svg_redener_ttr() throws IOException, InvalidRangeException{
//		String filePath = task.get("ncfilename");
//		String key = this.picId + filePath;
//		Cache cache = EhcacheUtil.getCache("trajBack-cache");
//		if (EhcacheUtil.contains(cache,key))
//		{
//			Object pic = EhcacheUtil.getObject(cache, key);
//			svg = (String[][])pic;
//		}
//		else 
//		{
//			Picturer picturer = new Picturer(filePath);
//			PictureRender_svg  svgs = picturer.createTrajectory1();
//			svg = svgs.getSvg();
//			EhcacheUtil.insertObject(cache,key,svg);
//		}
//		if(null != svg){
//			time = new ArrayList<Integer>();
//			for(int i = 0; i < svg[0].length; i++){
//				time.add(i);
//			}
//		}
//	}
//	
//	public void svg_redener_dtr() throws IOException, InvalidRangeException{
//		String filePath = task.get("ncfilename");
//		String key = this.picId + filePath;
//		Cache cache = EhcacheUtil.getCache("trajBack-cache");
//		if (EhcacheUtil.contains(cache,key))
//		{
//			Object pic = EhcacheUtil.getObject(cache, key);
//			svg = (String[][])pic;
//		}
//		else 
//		{
//			long startTime = Long.parseLong((String)picTrack.get("time_start"));
//			long endTime = Long.parseLong((String)picTrack.get("time_end"));
//			String species = (String)picTrack.get("species");
//			float zmax = Float.parseFloat((String)(String)picTrack.get("max_val"));
//			float zmin = Float.parseFloat((String)(String)picTrack.get("min_val"));
//			float interval = Float.parseFloat((String)(String)picTrack.get("step_val"));
//			
//			
//			long time = endTime - startTime;
//			long t = time / (60 * 60 );
//			
//			svg = new String[1][(int)t];
//			 
//			NCRander rander = new NCRanderSurface();
//			NcData ncdt = new NcData();
//			ncdt.openFile(filePath);
//			rander.ncData = ncdt;
//			rander.xDiv = 1;
//			rander.yDiv = 1;
//			
//			double v = (zmax - zmin)/interval;
//			List<Double> levelList = new ArrayList<Double>();
//			for(int i = 0; i < interval; i++){
//				levelList.add(zmin + v * i);
//			}
//			
//			double[] levelArray = new double[levelList.size()];
//			for(int i = 0; i < levelList.size(); i++){
//				levelArray[i] = levelList.get(i);
//			}
//			rander.level = levelArray;
//			
////			rander.level = new double[]{20,30,40};
//			
//			for(int i = 0; i < t; i++){
//				String s = rander.rander(species, i, 0, 0, 0);  // 污染物种类，时间，层，x，y
//				svg[0][i] = s;
//			}
//			
//			ncdt.closeFile();
//			
//			EhcacheUtil.insertObject(cache,key,svg);
//		}
//		if(null != svg){
//			time = new ArrayList<Integer>();
//			for(int i = 0; i < svg[0].length; i++){
//				time.add(i);
//			}
//		}
//	}
//}
