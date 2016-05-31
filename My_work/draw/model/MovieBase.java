package cn.ct.em.draw.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;

import cn.ct.em.core.util.EhcacheUtil;
import cn.ct.em.draw.core.RenderFunction;
//import cn.ct.em.draw.randerInterface.RanderCache;

public class MovieBase {
	public long movieStart;
	public long movieEnd;
	public RenderFunction rander;
	public PictureInfo[] picInfs;


	
	
	public String[] getMovie(double refX, double refY, double refH){
		String[] res = new String[picInfs.length];
		for (int i = 0; i < picInfs.length; i++) {
			res[i] = getPicture(i, refX, refY, refH);
		}
		return res;
	}

	
	//t is frame in movie
	public String getPicture(int t, double mapX, double mapY, double mapH){
		PictureInfo picInf = null;
		try {
			picInf = (PictureInfo) picInfs[t].clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		picInf.mapX = mapX;
		picInf.mapY = mapY;
		picInf.mapH = mapH;
		PictureInfo[] subPicInfs = rander.subPictureInfo(picInf);	
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ;i < subPicInfs.length ; i++){
			sb.append(getPicture(subPicInfs[i]));
		}
		return sb.toString();
	}
	
	protected Object getPicture(PictureInfo picinf){
		Cache cache = EhcacheUtil.getCache("pic-cache");
		Object pic = null;
		if (EhcacheUtil.contains(cache, picinf.getKey())) {
			pic = EhcacheUtil.getObject(cache, picinf.getKey());
		} else {
			pic = rander.render(picinf);
			EhcacheUtil.insertObject(cache, picinf.getKey(), pic);
		}
		return pic;
	}
	
	
}
