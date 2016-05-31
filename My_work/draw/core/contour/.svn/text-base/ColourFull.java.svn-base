package cn.ct.em.draw.core.contour;

public class ColourFull extends ColourBridge {

	@Override
	public String colour(double k, double n) {
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
