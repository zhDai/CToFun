package cn.ct.em.draw.model;

import cn.ct.em.draw.time.FrameGen;
import cn.ct.em.draw.time.FrameInf;


public  class MovieFactory {

	//multi task  污染模型图SO2、PM25等 	气象模型图风、温、湿、压
	static public MovieBase getMovieMT(String picId) {
		//从数据库读取数据
		//type == "pul_lis" 或 “”
		FrameGen frameGenerator = new FrameGen("", picId);
		FrameInf[] frameInfoArray = frameGenerator.getFrameInfoArray();
		String[] keyElements = frameGenerator.generatePartFrameKey();
		PictureInfo[] picInfs = new PictureInfo[frameInfoArray.length];
		for (int i = 0; i < frameInfoArray.length; i++){			
			FrameInf fi = frameInfoArray[i];
			PictureInfo in = new PictureInfo();
			in.fileName = fi.getNcfilename();
			in.time = fi.getTime();
			in.species = keyElements[0];
			in.layer = Integer.parseInt(keyElements[1]);
			in.maxV = Float.parseFloat(keyElements[2]);
			in.minV = Float.parseFloat(keyElements[3]);
			in.stepV = (int) Float.parseFloat(keyElements[4]);
			in.colour = keyElements[5];
			picInfs[i] = in;
		}
		MovieBase movie = new MovieBase();
		movie.picInfs = picInfs;
		movie.movieStart = frameGenerator.getMovieStart();
		movie.movieEnd = frameGenerator.getMovieEnd();
		return movie;	
	}
	
	
	//one task  污染清单图，后向轨迹
	static public MovieBase getMovieOT(String picId) {
		
		//type == "pul_lis" 或 “”
		FrameGen frameGenerator = new FrameGen("pul_lis", picId);
		FrameInf[] frameInfoArray = frameGenerator.getFrameInfoArray();
		String[] keyElements = frameGenerator.generatePartFrameKey();
		PictureInfo[] picInfs = new PictureInfo[frameInfoArray.length];
		for (int i = 0; i < frameInfoArray.length; i++){			
			FrameInf fi = frameInfoArray[i];
			PictureInfo in = new PictureInfo();
			in.fileName = fi.getNcfilename();
			in.time = fi.getTime();
			in.species = keyElements[0];
			in.layer = Integer.parseInt(keyElements[1]);
			in.maxV = Float.parseFloat(keyElements[2]);
			in.minV = Float.parseFloat(keyElements[3]);
			in.stepV = (int) Float.parseFloat(keyElements[4]);
			in.colour = keyElements[5];
			picInfs[i] = in;
		}
		MovieBase movie = new MovieBase();
		movie.picInfs = picInfs;
		movie.movieStart = frameGenerator.getMovieStart();
		movie.movieEnd = frameGenerator.getMovieEnd();
		return movie;	
		
	}
}
