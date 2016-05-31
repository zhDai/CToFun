package cn.ct.em.draw.movie;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import ucar.ma2.InvalidRangeException;
import cn.ct.em.DBInerface.SQLWapper;
import cn.ct.em.core.util.EhcacheUtil;
import cn.ct.em.draw.svg.contour.PictureRender_svg;

public class EmitPicRender implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String picId;
	public Map<String,String> emitPic;
	public String taskId;
	public Map<String,String> emitTask;
	public List<Long> time;
	public Map<Long,String> svg;
	public Long timeStart;
	public Long timeEnd;
	public String timeUnit;
	public int timeInv;
	public int t;
	public String type;
	public Map<Long,Map<String,String>> timeIndex;
	
	public EmitPicRender(String picId){
		this.picId = picId;
	};
	
	public void frEmitPic(){
		Map<String,String> con = new HashMap<String,String>();
		con.put("id", this.picId);
		List<Map<String,String>> emitPics = SQLWapper.selectPic(con);
		if(null != emitPics && emitPics.size() > 0){
			this.emitPic = emitPics.get(0);
			this.taskId = emitPic.get("task_id").substring(1, emitPic.get("task_id").length() - 1);
			this.type = emitPic.get("type");
		}
	}
	
	public void frEmitTask(){
		if(null != taskId){
			Map<String,String> con = new HashMap<String,String>();
			con.put("id", this.taskId);
			List<Map<String,String>> tasks = SQLWapper.selectNcEmit(con);
			if(null != tasks && tasks.size() > 0){
				emitTask = tasks.get(0);
			}
		}
	}
	
	public void frTime(){
		timeUnit = emitPic.get("time");
		timeStart = 0L;
		timeEnd = 24 * 60 * 60 * 1000L;
		
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
	
	public EmitPicRender svg_redener_emit() throws IOException, InvalidRangeException{
		String key = "picId:" + this.picId;
		Cache cache = EhcacheUtil.getCache("emit-cache");
		if (EhcacheUtil.contains(cache,key)){
			Object pic = EhcacheUtil.getObject(cache, key);
			return (EmitPicRender)pic;
		}else{
			frEmitPic();
			frEmitTask();
			frTime();
			String fileName = emitTask.get("ncfilename");
			fileName = fileName.substring(1, fileName.length() -1);
			String species = emitPic.get("model_data");
			Integer l = Integer.parseInt(emitPic.get("height"));
			Double max = Double.parseDouble(emitPic.get("max_val"));
			Double min = Double.parseDouble(emitPic.get("min_val"));
			Double interval = Double.parseDouble(emitPic.get("step_val"));
			svg = new HashMap<Long,String>();
			for(int i = 0; i < timeInv; i++){
				Long tt = timeStart + i * t;
				
				PictureRender_svg dzh = new PictureRender_svg();
				String[][] emitSvg = dzh.PictureRender_svg_surf2(fileName,species, i, l - 1, 1, 1,max,min,interval);
				svg.put(tt, emitSvg[0][0]);
			}
			if(null != svg){
				EhcacheUtil.insertObject(cache,key,this);
			}
			return this;
		}
	}
}
