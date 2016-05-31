package cn.ct.em.draw.time;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.ct.em.DBInerface.SQLWapper;
import cn.ct.em.DBInerface.SQLHelperSelect;
import cn.ct.em.calculate.Convention;

/*
 * @author: Shaola
 */

public class FrameGen {
	
	private static int t = 0;
	static {
		try{
			Properties properties = new Properties();
			String path = System.getProperty("EM_CONF") + "em.properties";
			InputStream input = new FileInputStream(path);
			properties.load(input);
			t = Integer.parseInt(properties.getProperty("nc_t"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private String id;  //e_pic id
	private List epicContent;
	private String type; //区分是e_task还是e_nc_emit
	
	private FrameInf[] frameInfoArray;
	
	private String model;  //0:WRF  1:CMAQ  2:CAMX  3:WRF-CHEM  4:flexpart
	private long movieStartR = 0L;
	private long movieEndR = 0L;
	
	private long movieStart = 0L;
	private long movieEnd = 0L;
	
	private Map<String, String> taskIdAndNcfilename;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FrameGen(String type,String id) {
		this.id = id;
		this.type = type;
		Map<String, String> cond = new HashMap<String, String>();
		cond.put("id", id);
		this.epicContent = SQLWapper.selectPic(cond);
		if(!"pul_lis".equals(type)){
			getStartEndFromEPic();
		}
		taskIdAndNcfilename = new HashMap<String, String>();
		frameInfoArray = getFrameInfoList();
	}
	
	private void getStartEndFromEPic() {
		String startName = "time_start";
		String endName = "time_end";
		if (epicContent.size() == 1) {
			movieStartR = Long.parseLong(((Map<String, String>) epicContent.get(0)).get(startName));
			movieEndR = Long.parseLong(((Map<String, String>) epicContent.get(0)).get(endName));
		}
	}
	
	public long getMovieStart() {
		return movieStart;
	}
	
	public long getMovieEnd() {
		return movieEnd;
	}
	
	public FrameInf[] getFrameInfoArray() {
		return frameInfoArray;
	}
	
	public Comparator<TimeInterval> cmp = new Comparator<TimeInterval>() {
		@Override
		public int compare(TimeInterval i1, TimeInterval i2) {
			return (int)(i1.getStart() - i2.getStart());
		}
	};
	
	private String[] getTaskIdListFromEPic() {
		String [] list = null;
		String col = "id";
		if (epicContent.size() == 1) {
			model = ((Map<String, String>) epicContent.get(0)).get("model");
			String result = ((Map<String, String>) epicContent.get(0)).get("task_id");
			list = result.substring(1, result.length() - 1).split(",");
		}

		return list;
	}
	
	public String[] generatePartFrameKey() {
		String[] key = new String[6];
		String col = "id";
		if (epicContent.size() == 1) {
			Map<String, String> tmp = (Map<String, String>) epicContent.get(0);
			key[0] = tmp.get("model_data"); 
			key[1] = tmp.get("height");
			key[2] = tmp.get("max_val"); 
			key[3] = tmp.get("min_val"); 
			key[4] = tmp.get("step_val"); 
			key[5] = tmp.get("colour");
		}
		return key;
	}
	
//	private int modelToint(String model){
//		
//	}
	
	private List<TimeInterval> getIntervalListFromETask() {
		String[] taskIdList = getTaskIdListFromEPic();
		if(null == taskIdList)
		{
			return null;
		}
		List<TimeInterval> list = new ArrayList<TimeInterval>();
//		String startName = "time_start";
//		String endName = "time_end";
		for (String e : taskIdList) {
			Map<String, String> cond = new HashMap<String, String>();
			cond.put("id", e);
			
			List<Map> curList = SQLWapper.selectTask(cond);
			if (curList.size() == 1) {
				Map<String, String> resultMap = (Map<String, String>) curList.get(0);
				SQLHelperSelect helper = new SQLHelperSelect();
				helper.value = resultMap;
				List names = helper.getListString("ncfilename");
				
//				taskIdAndNcfilename.put(e, resultMap.get("ncfilename") + "." + model);
				taskIdAndNcfilename.put(e, (String)names.get(Convention.modelToint(model)));
				long start = Long.parseLong(resultMap.get("time_start"));
				long end = Long.parseLong(resultMap.get("time_end"));
				TimeInterval curInterval = new TimeInterval(start, end, e);
				list.add(curInterval);
			}
		}
		Collections.sort(list, cmp);
		return list;
	}
	
	// the given list is sorted
	// remove the duplicates of intervals with the same start
	// the rule is like this: keep the longest length interval
	private List<TimeInterval> removeDuplicateStart() {
		List<TimeInterval> list = getIntervalListFromETask();
		if(null == list){
			return null;
		}
		List<TimeInterval> res = new ArrayList<TimeInterval>();
		int sz = list.size();
		int i = 0;
		while (i < sz) {
			int j = i + 1;
			TimeInterval longestInterval = list.get(i);
			int maxDuration = (int)(longestInterval.getEnd() - longestInterval.getStart());
			while (j < sz && list.get(j).getStart() == list.get(i).getStart()) {
				int curDuration = (int)(list.get(j).getEnd() - list.get(j).getStart());
				if (curDuration > maxDuration) {
					maxDuration = curDuration;
					longestInterval = list.get(j);
				}
				j++;
			}
			res.add(longestInterval);
			i = j;
		}
		return res;
	}
	
	private TimeInterval[] getIntervalArray() {
		List<TimeInterval> list = removeDuplicateStart();
		if(null == list || list.size() < 1)
		{
			return null;
		}
		
		long start = list.get(0).getStart();
		long end = list.get(list.size() - 1).getEnd();
		int n = (int) (end - start) / 3600 + 1;
		TimeInterval[] res = new TimeInterval[n];
		int i = 0;
		while (i < list.size()) {
			long curS = list.get(i).getStart();
			long curE = list.get(i).getEnd();
			String curId = list.get(i).getId();
			int curP = (int) (curS - start) / 3600;
			for (long j = curS; j < curE; j += 3600) {
				if(curP >= n){
					break;
				}
				res[curP++] = new TimeInterval(j, j + 3600, curId, curS);
			}
			i++;
		}
		
		int markStart = movieStartR > start ? (int) (movieStartR - start) / 3600 : 0;
		int markEnd = movieEndR < end ? (int) (movieEndR - start) / 3600 + 1 : n;
		
		if (markStart > markEnd) {
		    markStart = markEnd;
		}
		
		while (markEnd > 0 && res[markEnd - 1] == null) {
			markEnd--;
		}
		
		res = Arrays.copyOfRange(res, markStart, markEnd);
		
		movieStart = res[0].getStart();
		movieEnd = res[res.length - 1].getEnd();
		
		return res;
	}
	
	private FrameInf[] getFrameInfoList() {
		if(null != type && type.equals("pul_lis")){
			Map<String,String> cond = new HashMap<String,String>();
			String id = ((Map<String, String>) epicContent.get(0)).get("task_id");
			cond.put("id", id.substring(1, id.length() - 1));
			List<Map> curList = SQLWapper.selectNcEmit(cond);
			if (null != curList && curList.size() == 1) {
				this.movieStart = 0 - 8 * 60 * 60;
				this.movieEnd = (FrameGen.t - 8) * 60 * 60;
				Map<String, String> resultMap = (Map<String, String>) curList.get(0);
				FrameInf[] res = new FrameInf[FrameGen.t];
				for(int i = 0; i < FrameGen.t; i++){
					String fName = resultMap.get("ncfilename").substring(1, resultMap.get("ncfilename").length() - 1);
					res[i] = new FrameInf(fName, i);
				}
				return res;
			}
			return null;
		}else{
			TimeInterval[] intervals = getIntervalArray();
			if(null == intervals)
			{
				return null;
			}
			int n = intervals.length;
			FrameInf[] res = new FrameInf[n];
			int i = 0;
			while (i < n) {
				if (intervals[i] != null) {
					String ncfilename = taskIdAndNcfilename.get(intervals[i].getId());
					int time = (int) (intervals[i].getEnd() - intervals[i].getOriginalStart()) / 3600 - 1;
					res[i] = new FrameInf(ncfilename, time);
				}
				i++;
			}
			return res;
		}
	}
}
