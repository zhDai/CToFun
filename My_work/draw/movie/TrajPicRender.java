package cn.ct.em.draw.movie;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucar.ma2.InvalidRangeException;
import net.sf.ehcache.Cache;
import cn.ct.em.DBInerface.SQLWapper;
import cn.ct.em.DBInerface.SQLHelperSelect;
import cn.ct.em.calculate.Convention;
import cn.ct.em.calculate.NcData;
import cn.ct.em.core.util.EhcacheUtil;
import cn.ct.em.dbService.util.TransparentPictureUtil;
import cn.ct.em.draw.svg.contour.NCRander;
import cn.ct.em.draw.svg.contour.NCRanderSurface;
import cn.ct.em.draw.svg.contour.PictureRender_svg;

public class TrajPicRender  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String picId;
	public Map<String,String> picTrack;
	public String taskId;
	public Map<String,String> task;
	public List<Long> time;
	public Map<Long,String> svg;
	public Long timeStart;
	public Long timeEnd;
	public String timeUnit;
	public int timeInv;
	public int t;
	public String type;
	public Map<Long,Map<String,String>> timeIndex;
	
	public TrajPicRender(){};
	
	public TrajPicRender(String picId){
		this.picId = picId;
	};
	
	public void frPicTrack(){
		Map<String,String> con = new HashMap<String,String>();
		con.put("id", this.picId);
		List<Map<String,String>> picTracks = SQLWapper.selectPicTra(con);
		if(null != picTracks && picTracks.size() > 0){
			this.picTrack = picTracks.get(0);
			this.taskId = picTrack.get("task_id");
			this.type = picTrack.get("track");
		}
	}
	
	public void frTask(){
		if(null != taskId){
			Map<String,String> con = new HashMap<String,String>();
			con.put("id", this.taskId);
			List<Map<String,String>> tasks = SQLWapper.selectTask(con);
			if(null != tasks && tasks.size() > 0){
				task = tasks.get(0);
			}
		}
	}
	
	public void frTime(){
		timeUnit = picTrack.get("time");
		timeStart = Long.parseLong((String)picTrack.get("time_start")) * 1000;
		timeEnd = Long.parseLong((String)picTrack.get("time_end")) * 1000;
		
		if("h".equals(timeUnit)){
			t = 60 * 60 * 1000;
		}else if("d".equals(timeUnit)){
			t = 60 * 60 * 24 * 1000;
		}else if("m".equals(timeUnit)){
			t = 60 * 60 * 24 * 30 * 1000;
		}
		timeInv = (int)(timeEnd - timeStart)/t;
		time = new ArrayList<Long>();
		
		//<--排序
		timeIndex = new HashMap<Long,Map<String,String>>();
		//-->
		
		for(int i = 0; i < timeInv; i++){
			Long tt = timeStart + i *t;
			
			//<--排序
			Map<String,String> pp = new HashMap<String,String>();
			pp.put("id", picId);
			pp.put("type", type);
			pp.put("index", tt + "");
			timeIndex.put(tt, pp);
			//-->
			
			time.add(tt);
		}
	}
	//时变线图
	public TrajPicRender svg_redener_fstr() throws IOException, InvalidRangeException{
		String key = this.picId;
		Cache cache = EhcacheUtil.getCache("trajBack-cache");
		if (EhcacheUtil.contains(cache,key))
		{
			Object pic = EhcacheUtil.getObject(cache, key);
			return (TrajPicRender)pic;
		}
		else 
		{
			frPicTrack();
			frTask();
			frTime();
			
			SQLHelperSelect helper = new SQLHelperSelect();
			helper.value = task;
			List<String> names = helper.getListString("ncfilename");
			String filePath = names.get(5);
			
			svg = new HashMap<Long,String>();
			PictureRender_svg dzh = new PictureRender_svg();
		    dzh.PictureRender_svg_trajectory(filePath);
		    String[][] svgs = dzh.getSvg();
		    
			for(int i = 0; i < time.size(); i++)
			{
				if(i < svgs[0].length)
				{
					String s = svgs[0][i];
					svg.put(time.get(i),s);
				}
				else
				{
					svg.put(time.get(i),"");
				}
			}
		    
			EhcacheUtil.insertObject(cache,key,this);
			return this;
		}
	}
	//同源线图
	public TrajPicRender svg_redener_ttr() throws IOException, InvalidRangeException{
		String key = this.picId;
		Cache cache = EhcacheUtil.getCache("trajBack-cache");
		if (EhcacheUtil.contains(cache,key))
		{
			Object pic = EhcacheUtil.getObject(cache, key);
			return (TrajPicRender)pic;
		}
		else 
		{
			frPicTrack();
			frTask();
			frTime();
			
			SQLHelperSelect helper = new SQLHelperSelect();
			helper.value = task;
			List<String> names = helper.getListString("ncfilename");
			String filePath = names.get(5);
			
			svg = new HashMap<Long,String>();
			PictureRender_svg dzh = new PictureRender_svg();
		    dzh.PictureRender_svg_trajectory_1(filePath);

			String[][] svgs = dzh.getSvg();
		    
			for(int i = 0; i < time.size(); i++)
			{
				if(i < svgs[0].length)
				{
					String s = svgs[0][i];
					svg.put(time.get(i),s);
				}
				else
				{
					svg.put(time.get(i),"");
				}
			}
		    
			EhcacheUtil.insertObject(cache,key,this);
			return this;
		}
	}
	
	//同源面图
	public TrajPicRender svg_redener_dtr() throws IOException, InvalidRangeException{
		String key = this.picId;
		Cache cache = EhcacheUtil.getCache("trajBack-cache");
		if (EhcacheUtil.contains(cache,key))
		{
			Object pic = EhcacheUtil.getObject(cache, key);
			return (TrajPicRender)pic;
		}
		else 
		{
			frPicTrack();
			frTask();
			frTime();
			
			SQLHelperSelect helper = new SQLHelperSelect();
			helper.value = task;
			List<String> names = helper.getListString("ncfilename");
			String filePath = names.get(5);
			
			String species = (String)picTrack.get("species");
			float zmax = Float.parseFloat((String)(String)picTrack.get("max_val"));
			float zmin = Float.parseFloat((String)(String)picTrack.get("min_val"));
			float interval = Float.parseFloat((String)(String)picTrack.get("step_val"));
			
			NCRander rander = new NCRanderSurface();
			NcData ncdt = new NcData();
			ncdt.openFile(filePath);
			rander.ncData = ncdt;
			rander.xDiv = 1;
			rander.yDiv = 1;
			
			double v = (zmax - zmin)/interval;
			List<Double> levelList = new ArrayList<Double>();
			for(int i = 0; i < interval; i++){
				levelList.add(zmin + v * i);
			}
			
			double[] levelArray = new double[levelList.size()];
			for(int i = 0; i < levelList.size(); i++){
				levelArray[i] = levelList.get(i);
			}
			rander.level = levelArray;
			
			svg = new HashMap<Long,String>();
			for(int i = 0; i < time.size(); i++){
				
				String s = null;
				try
				{
					s = rander.rander(species, i, 0, 0, 0);  // 污染物种类，时间，层，x，y
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				if(null == s)
				{
					s = "";
				}
				
				svg.put(time.get(i), s);
			}
			
			ncdt.closeFile();
			
			EhcacheUtil.insertObject(cache,key,this);
			return this;
		}
	}
}
