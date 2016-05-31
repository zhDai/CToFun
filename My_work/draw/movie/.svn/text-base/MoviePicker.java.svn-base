package cn.ct.em.draw.movie;

import java.awt.AWTException;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ct.em.core.util.EhcacheUtil;
import cn.ct.em.draw.svg.contour.PictureRender_svg;
import cn.ct.em.draw.svg.contour.Picturer;
import net.sf.ehcache.Cache;
import ucar.ma2.InvalidRangeException;
import visad.VisADException;

/*
 * author: Shaola
 */

public class MoviePicker {
	private String[] keyElements;
	private String partKey;
	private FrameInfo[] frameInfoArray;
	private long startTime;
	private long endTime;
	private Cache cache;
	private String type; //区分是e_task还是e_nc_emit
	
	private List<String> extent;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public MoviePicker(String id) {
		FrameGenerator frameGenerator = new FrameGenerator(type,id);
		keyElements = frameGenerator.generatePartFrameKey();
		startTime = frameGenerator.getMovieStart();
		endTime = frameGenerator.getMovieEnd();
		StringBuilder sb = new StringBuilder();
		for (String el : keyElements) {
			sb.append(el);
		}
		partKey = sb.toString();
		frameInfoArray = frameGenerator.getFrameInfoArray();
		extent = new ArrayList<String>();
		cache = EhcacheUtil.getCache("pic-cache");
	}
	
	public MoviePicker(String id,String type) {
		FrameGenerator frameGenerator = new FrameGenerator(type,id);
		this.type = type;
		keyElements = frameGenerator.generatePartFrameKey();
		startTime = frameGenerator.getMovieStart();
		endTime = frameGenerator.getMovieEnd();
		StringBuilder sb = new StringBuilder();
		for (String el : keyElements) {
			sb.append(el);
		}
		partKey = sb.toString();
		frameInfoArray = frameGenerator.getFrameInfoArray();
		extent = new ArrayList<String>();
		cache = EhcacheUtil.getCache("pic-cache");
	}
	
	public long getStartTime(){
		return this.startTime;
	}
	public long getEndTime(){
		return this.endTime;
	}
	public List<String> getExtent(){
		return this.extent;
	}
	
	public List<PictureRender_svg> svgMovieById() throws VisADException, IOException, InvalidRangeException, InterruptedException, AWTException, ClassNotFoundException {
		if(null == frameInfoArray){
			return null;
		}
		List<PictureRender_svg> movie = new ArrayList<PictureRender_svg>();

		for (FrameInfo fi : frameInfoArray) {
			if (fi != null) {
				String ncfilename = fi.getNcfilename();
				String species = keyElements[0];
				float max_val = Float.parseFloat(keyElements[2]);
				float min_val = Float.parseFloat(keyElements[3]);
				float step_val = Float.parseFloat(keyElements[4]);
				int layerNum = Integer.parseInt(keyElements[1]);
				int frameNum = fi.getTime();
				Color color = convertStringToColor(keyElements[5]);
				
				String key = partKey + fi.getNcfilename() + fi.getTime();
				
				if (EhcacheUtil.contains(cache,key)) {
					Object pic = EhcacheUtil.getObject(cache, key);
					movie.add((PictureRender_svg)pic);
				}else {
					PictureRender_svg dzh = new PictureRender_svg();
			 	    dzh.PictureRender_svg_surf2(ncfilename,species,frameNum,layerNum,4, 4, max_val, min_val, step_val);
					EhcacheUtil.insertObject(cache,key,dzh);
					movie.add(dzh);
				}
			}else{
				movie.add(null);
			}
		}
		return movie;
	}
	
	public List<PictureRender_svg> svgMovieById(String x,String y) throws VisADException, IOException, InvalidRangeException, InterruptedException, AWTException, ClassNotFoundException {
		if(null == frameInfoArray){
			return null;
		}
		List<PictureRender_svg> movie = new ArrayList<PictureRender_svg>();

		for (FrameInfo fi : frameInfoArray) {
			if (fi != null) {
				String ncfilename = fi.getNcfilename();
				String species = keyElements[0];
				float max_val = Float.parseFloat(keyElements[2]);
				float min_val = Float.parseFloat(keyElements[3]);
				float step_val = Float.parseFloat(keyElements[4]);
				int layerNum = Integer.parseInt(keyElements[1]);
				int frameNum = fi.getTime();
				Color color = convertStringToColor(keyElements[5]);
				
				String key = partKey + fi.getNcfilename() + fi.getTime();
				if("met_sim".equals(type) || "met_obs".equals(type)){
					key = "x:" + x + ",y:" + y + key;
				}
				
				if (EhcacheUtil.contains(cache,key)) {
					Object pic = EhcacheUtil.getObject(cache, key);
					movie.add((PictureRender_svg)pic);
					
				}else {
					Picturer svg = new Picturer(ncfilename, species, frameNum, layerNum, 0, 0,
							53, 57, step_val, min_val, max_val,
							0,Double.parseDouble(y),4,4);
//					PictureRender_svg pic = svg.createLinePic();
//					PictureRender_svg pic = svg.createSurfPic();
					PictureRender_svg pic = null;
					if("pul_sim".equals(type) || "pul_obs".equals(type)){
						pic = svg.createSurfPic2();
					} else if("met_sim".equals(type) || "met_obs".equals(type)){
						pic = svg.createWindPic();
					} else if("pul_lis".equals(type)){
						pic = svg.createEmitPic();
					}
					
					EhcacheUtil.insertObject(cache,key,pic);
					movie.add(pic);
				}
			}else{
				movie.add(null);
				extent.add(null);
			}
		}
		return movie;
	}
	
	// not implemented, don't know the color format stored in database
	private Color convertStringToColor(String c) {
		Color color = null;
		return color;
	}
}
