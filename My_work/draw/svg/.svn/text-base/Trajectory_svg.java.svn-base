package cn.ct.em.draw.svg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import cn.ct.em.calculate.LambertTransXYToLonLat;
import cn.ct.em.calculate.MercatorTransLonLatToXY;
import com.vividsolutions.jts.geom.Coordinate;

public class Trajectory_svg {
	public LambertTransXYToLonLat f11 = new LambertTransXYToLonLat();
	public MercatorTransLonLatToXY f22 = new MercatorTransLonLatToXY();
	
	int count = 0;
	
	//路径在规定时间内的轨迹变化情况
	public String Trajectory(String filename,String color, int k) {		
		StringBuffer svg = new StringBuffer();
		StringBuffer circle = new StringBuffer();
		k = k+6;
		svg.append("<polyline points=\"");
        try {        	
            Scanner in = new Scanner(new File(filename));
            int i = 0;
            while (in.hasNextLine()) {
            	String str = in.nextLine();
            	if(i > 5){
			        String[] abc = str.trim().split("[\\p{Space}]+");		
            		if (i == k){
				        double a = Double.valueOf(abc[2]);    
				        double b = Double.valueOf(abc[3]); 
				        Coordinate f = new Coordinate(a,b);
				        Coordinate f_11 = f22.coordinateTrans(f);     
				        svg.append(f_11.x+","+(-f_11.y));
				        svg.append(" ");
				        circle.append("<circle cx=\""+f_11.x+"\" cy=\""+(-f_11.y)+"\" r=\"30000\" fill=\""+color+"\"/>");
            		}else{
				        double a = Double.valueOf(abc[2]);    
				        double b = Double.valueOf(abc[3]); 
				        Coordinate f = new Coordinate(a,b);
				        Coordinate f_11 = f22.coordinateTrans(f);     
				        svg.append(f_11.x+","+(-f_11.y));
				        svg.append(" ");         			
            		}
            	}
            	i++;
            }
          svg.append("\" style=\"fill:none;stroke-dasharray:50000;stroke-linecap:round;stroke:"+color+"\"/>"); 
          svg.append(circle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		return svg.toString();
    }
	
	//从txt读取数据
	public String Trajectory(String filename,String color) {		
		StringBuffer svg = new StringBuffer();
		StringBuffer circle = new StringBuffer();
		svg.append("<polyline points=\"");
        try {        	
            Scanner in = new Scanner(new File(filename));
            int i = 0;
            while (in.hasNextLine()) {
            	String str = in.nextLine();
            	if(i > 5){
			        String[] abc = str.trim().split("[\\p{Space}]+");		
				    if (Double.valueOf(abc[0]) == 1){
	            		if (i == 6){
					        double a = Double.valueOf(abc[2]);    
					        double b = Double.valueOf(abc[3]); 
					        Coordinate f = new Coordinate(a,b);
					        Coordinate f_11 = f22.coordinateTrans(f);     
					        svg.append(f_11.x+","+(-f_11.y));
					        svg.append(" ");
					        circle.append("<circle cx=\""+f_11.x+"\" cy=\""+(-f_11.y)+"\" r=\"30000\" fill=\""+color+"\"/>");
	            		}else{
					        double a = Double.valueOf(abc[2]);    
					        double b = Double.valueOf(abc[3]); 
					        Coordinate f = new Coordinate(a,b);
					        Coordinate f_11 = f22.coordinateTrans(f);     
					        svg.append(f_11.x+","+(-f_11.y));
					        svg.append(" ");         			
	            		}
            		}
            	}
            	i++;
            }
          svg.append("\" style=\"fill:none;stroke:"+color+"\"/>");
//          svg.append("\" style=\"fill:none;stroke-dasharray:50000;stroke-linecap:round;stroke:"+color+"\"/>");
          svg.append(circle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		return svg.toString();
    }
		
	//文件夹读取txt
	public String Trajectory_1(List<File> f){
		StringBuffer svg = new StringBuffer();
		 if (f.size() != 0){
			 for (int k = 0; k < f.size(); k++) {
				 String filePath = f.get(k).toString();
				 String clr = color(k+1,f.size());
				 String bb = Trajectory(filePath,clr);
				 svg.append(bb);
			 } 
		 }
		 return svg.toString();
	}
	
	//另外一种文件夹读取txt
	public String Trajectory_1(String f, String[] filelist,int i){
		StringBuffer svg = new StringBuffer();
		for (int k = 0; k < filelist.length; k++){
			String filePath = f+"/"+filelist[k];
			String clr = color(k+1,filelist.length);
			String bb = Trajectory(filePath,clr,i);
			svg.append(bb);
		}
		return svg.toString();
	}
	
	//按文件夹名称排序
	public List<File> orderByName(String fliePath) {
		  List<File> files = Arrays.asList(new File(fliePath).listFiles());
		  Collections.sort(files, new Comparator< File>() {
		   public int compare(File o1, File o2) {
			if (o1.isDirectory() && o2.isFile())
		          return -1;
			if (o1.isFile() && o2.isDirectory())
		          return 1;
			return o1.getName().compareTo(o2.getName());
		   }
		  });
		  return files;
	}
	
	//判定行数
	public int get_Rows(String filename){
		try {
			Scanner in = new Scanner(new File(filename));
			int i = 0;
			while (in.hasNextLine()) {
				String str = in.nextLine();
            	if(i > 5){
            		String[] abc = str.trim().split("[\\p{Space}]+");
					if (Double.valueOf(abc[0]) == 1){
						count++;
					}	
            	}
            	i++;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return count;
	}
	//颜色选取
	protected String color(double k,double n){
    	int r,g,b;
    	double cx =  k/n; 
    	if (cx <= 0.25){
    		r = 0;
    		g = (int) (4 * cx *255);
    		b = 255;
    	}
    	else if (cx > 0.25 && cx <= 0.5){
    		r = 0;
    		g = 255;
    		b = (int) ((1 - 4 * (cx - 0.25)) *255);
    	}
    	else if (cx > 0.5 && cx <= 0.75){
    		r = (int) (4 * (cx - 0.5) *255);
    		g = 255;
    		b = 0;
    	}
    	else {
    		r = 255;
    		g = (int) ((1-4*(cx-0.75))*255);
    		b = 0;
    	}
		String aa = "rgb("+ r +","+ g +","+ b +")";
    	return aa;
    }
}
