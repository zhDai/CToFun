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
import cn.ct.em.DBInerface.SQLHelperSelect;
import cn.ct.em.calculate.Convention;
import cn.ct.em.calculate.NcData;
import cn.ct.em.core.util.EhcacheUtil;
import cn.ct.em.draw.svg.contour.PictureRender_svg;
import cn.ct.em.draw.svg.wind.WindRender;
import cn.ct.em.draw.svg.wind.WindRender_Barb;

public class WindPicRender implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String picId;
	public Map<String,String> windPic;
	public String taskId;
	public Map<String,String> windTask;
	public List<Long> time;
	public Map<Long,String> svg;
	public Long timeStart;
	public Long timeEnd;
	public String timeUnit;
	public int timeInv;
	public int t;
	public String type;
	public Map<Long,Map<String,String>> timeIndex;
	
	public WindPicRender(String picId){
		this.picId = picId;
	};
	
	public void frWindPic(){
		Map<String,String> con = new HashMap<String,String>();
		con.put("id", this.picId);
		List<Map<String,String>> picTracks = SQLWapper.selectPic(con);
		if(null != picTracks && picTracks.size() > 0){
			this.windPic = picTracks.get(0);
			this.taskId = windPic.get("task_id").substring(1, windPic.get("task_id").length() - 1);
			this.type = windPic.get("type");
		}
	}
	
	public void frWindTask(){
		if(null != taskId){
			Map<String,String> con = new HashMap<String,String>();
			con.put("id", this.taskId);
			List<Map<String,String>> tasks = SQLWapper.selectTask(con);
			if(null != tasks && tasks.size() > 0){
				windTask = tasks.get(0);
			}
		}
	}
	
	public void frTime(){
		timeUnit = windPic.get("time");
		timeStart = Long.parseLong((String)windPic.get("time_start")) * 1000;
		timeEnd = Long.parseLong((String)windPic.get("time_end")) * 1000;
		
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
	
	public WindPicRender svg_redener_wind(double height) throws IOException, InvalidRangeException{
		String key = "picId:" + this.picId + ";height:" + height;
		Cache cache = EhcacheUtil.getCache("wind-cache");
		if (EhcacheUtil.contains(cache,key)){
			Object wind = EhcacheUtil.getObject(cache, key);
			return (WindPicRender)wind;
		}else{
			frWindPic();
			frWindTask();
			frTime();
			SQLHelperSelect helper = new SQLHelperSelect();
			helper.value = windTask;
			List<String> names = helper.getListString("ncfilename");
			String model = windPic.get("model");
			String fileNmae = names.get(Convention.modelToint(model));
			String l = windPic.get("height");
			svg = new HashMap<Long,String>();
			if("met_sim_wind".equals(type)){
				for(int i = 0; i < timeInv; i++){
					Long tt = time.get(i);
					WindRender dzh = new WindRender_Barb();
					NcData ncd = new NcData();
					ncd.openFile(fileNmae);
					dzh.ncData = ncd;
					String s = dzh.Wind_Render("UWIND", "VWIND", i, Integer.parseInt(l),height);
					svg.put(tt, s);
				}
			}else{
				String species = windPic.get("model_data");
				Integer layerNum = Integer.parseInt(l) - 1;
				Double max_val = Double.parseDouble(windPic.get("max_val"));
				Double min_val = Double.parseDouble(windPic.get("min_val"));
				Double step_val = Double.parseDouble(windPic.get("step_val"));
				for(int i = 0; i < timeInv; i++){
					Long tt = time.get(i);
					PictureRender_svg dzh = new PictureRender_svg();
					String[][] picSvg = dzh.PictureRender_svg_surf2(fileNmae,species,i,layerNum,1, 1, max_val, min_val, step_val);
					svg.put(tt, picSvg[0][0]);
				}
			}
			if(null != svg){
				EhcacheUtil.insertObject(cache,key,this);
			}
			return this;
		}
	}
}
