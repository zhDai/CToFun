//Biploar Oblique Conic Comformal Projection
public class BoccTrans {
	double pi = 3.14159265358979323846;
	double deg2ard = pi / 180.0;
	double R = 6370000.0;              // the radius of the global
//	double R  = 1;
	double niu_B = -19.993333*deg2ard; // !the longitude of B(negative is west long)
	double n = 0.63056;                // the cone constant for both conic projections
	double F_0 = 1.83376 * R;
	double k_0 = 1.03462;              // the scale factor by which the coordinates are multiplied to balance the errors
	double F = 1.89725 * R;            // a convenient constant
	double Az_AB = 46.78203*deg2ard;   // !the azimuth east of north of B from A
	double Az_BA = 104.42834*deg2ard;  // !the azimuth west of north of A from B
	double T = 1.27247;                // a convenient constant
	double p_c = 1.20709 * R;          // the radius of the center point of the axis from either pole
	double z_c = 52.03888*deg2ard;     // !the polar distance of the center point from either pole
	double fai_c = 17.274444*deg2ard;  // !the latitude of the center point, on the southern-cone side of the axis（维度）
	double niu_c = -73.0075*deg2ard;   // !the longitude of the center point, on the southern-cone side of the axis（经度）
	double Az_c = 45.81997*deg2ard;    // !the azimuth east of the north of the axis at the center point,relative to meridian niu_c on the southern-cone side of the axis 
	
	public double[] BoccXYToLonLat(double[] coor){
		double x = coor[0];
		double y = coor[1];
		double x_1 = -x*Math.cos(Az_c)+y*Math.sin(Az_c);
		double y_1 = -x*Math.sin(Az_c)-y*Math.cos(Az_c);
		double p_B1,Az_B1,p_B,z_B,alfa,Az_B;
		double p_A1,Az_A1,p_A,z_A,Az_A;
		double[] res = new double[2];
		if(x_1 < 0){
			//southern
			p_A1 = Math.pow(Math.pow(x_1, 2)+Math.pow(p_c-y_1, 2), 0.5);
			Az_A1 = Math.atan(x_1/(p_c-y_1));
			p_A = p_A1;
			z_A = 2*Math.atan(Math.pow(p_A/F,1/n));
			alfa = Math.acos((Math.pow(Math.tan(0.5*z_A), n)+Math.pow(0.5*(104*deg2ard-z_A), n))/T);
			while (Math.abs(Az_A1)<alfa){
				p_B = p_A1*Math.cos(alfa+Az_A1);
				z_B = 2*Math.atan(Math.pow(p_B/F, 1/n));
				alfa = Math.acos((Math.pow(Math.tan(0.5*z_B), n)+Math.pow(0.5*(104*deg2ard-z_B), n))/T);
			}
			Az_A = Az_AB-Az_A1/n;
			res[0] = Math.asin(Math.sin(-20*deg2ard)*Math.cos(z_A)+Math.cos(20*deg2ard)*Math.sin(z_A)*Math.cos(Az_A));
			res[1] = Math.atan(Math.sin(Az_A)/(Math.cos(-20*deg2ard)/Math.tan(z_A)-Math.sin(-20*deg2ard)*Math.cos(Az_A)));
			res[0] = res[0]/deg2ard;
			res[1] = res[1]/deg2ard-110;
		}else
		{
			p_B1 = Math.pow(Math.pow(x_1, 2)+Math.pow(p_c+y_1, 2), 0.5);
			Az_B1 = Math.atan(x_1/(p_c+y_1));
			p_B = p_B1;
			z_B = 2*Math.atan(Math.pow(p_B/F, 1/n));
			alfa = Math.acos((Math.pow(Math.tan(0.5*z_B), n)+Math.pow(0.5*(104*deg2ard-z_B), n))/T);
			while (Math.abs(Az_B1)<alfa){
				p_B = p_B1*Math.cos(alfa-Az_B1);
				z_B = 2*Math.atan(Math.pow(p_B/F, 1/n));
				alfa = Math.acos((Math.pow(Math.tan(0.5*z_B), n)+Math.pow(0.5*(104*deg2ard-z_B), n))/T);
			}
			Az_B = Az_BA-Az_B1/n;
			res[0] = Math.asin(Math.sin(pi/4)*Math.cos(z_B)+Math.cos(pi/4)*Math.sin(z_B)*Math.cos(Az_B));//fai
			res[1] = niu_B-Math.atan(Math.sin(Az_B)/(Math.cos(pi/4)/Math.tan(z_B)-Math.sin(pi/4)*Math.cos(Az_B))); //niu
			res[0] = res[0]/deg2ard;
			res[1] = res[1]/deg2ard;
		}		
		return res;
	}
	
	public double[] BoccLonLatToXY(double[] coor){
		double fai = coor[0]*deg2ard;
		double niu = coor[1]*deg2ard;
		double z_B = Math.acos(Math.sin(pi/4)*Math.sin(fai)+Math.cos(pi/4)*Math.cos(fai)*Math.cos(niu_B-niu));
		double Az_B = Math.atan(Math.sin(niu_B-niu)/(Math.cos(pi/4)*Math.tan(fai)-Math.sin(pi/4)*Math.cos(niu_B-niu)));
		if (Az_B < 0){
			Az_B = Az_B + pi;
		}
		double[] res = new double[2];
		double k,p_B1,p_A1,alfa,x_1,y_1;
		if(Az_B > Az_BA){
			double z_A = Math.acos(Math.sin(-20*deg2ard)*Math.sin(fai)+Math.cos(-20*deg2ard)*Math.cos(fai)*Math.cos(niu+110*deg2ard));
			double Az_A = Math.atan(Math.sin(niu+110*deg2ard)/(Math.cos(-20*deg2ard)*Math.tan(fai)-Math.sin(-20*deg2ard)*Math.cos(niu+110*deg2ard)));
			double p_A = F*Math.pow(Math.tan(0.5*z_A),n);
			k = Math.sin(z_A)*p_A*n/R;
			alfa = Math.acos((Math.pow(Math.tan(0.5*z_A), n)+Math.pow(0.5*(104*deg2ard-z_A), n))/T);
			if(Math.abs(n*(Az_AB-Az_A))<alfa){
				p_A1 = p_A/(Math.cos(alfa+n*(Az_AB-Az_A)));
			}else
			{
				p_A1 = p_A;
			}
			x_1 = p_A1*Math.sin(n*(Az_AB-Az_A));
			y_1 = -p_A1*Math.cos(n*(Az_AB-Az_A))+p_c;
			res[0] = -x_1*Math.cos(Az_c)-y_1*Math.sin(Az_c); //x
			res[1] = -y_1*Math.cos(Az_c)+x_1*Math.sin(Az_c); //y
		}else
		{
			double p_B = F*Math.pow(Math.tan(0.5*z_B), n);
			k = p_B*n/(R*Math.sin(z_B));
			alfa = Math.acos((Math.pow(Math.tan(0.5*z_B), n)+Math.pow(0.5*(104*deg2ard-z_B), n))/T);
			if(Math.abs(n*(Az_BA-Az_B))<alfa){
				p_B1 = p_B/(Math.cos(alfa-n*(Az_BA-Az_B)));
			}else
			{
				p_B1 = p_B;
			}
			x_1 = p_B1*Math.sin(n*(Az_BA-Az_B));
			y_1 = p_B1*Math.cos(n*(Az_BA-Az_B))-p_c;
			res[0] = -x_1*Math.cos(Az_c)-y_1*Math.sin(Az_c); //x
			res[1] = -y_1*Math.cos(Az_c)+x_1*Math.sin(Az_c); //y
		}		
		return res;
	}
//测试
/*	
	public static void main(String args[]){
		double[] coor = new double[2];
		double[] coor1 = new double[2];
//		coor[0] = -90;
//		coor[1] = -90;
//		coor[0] = -0.09518897621483613;//R=1
//		coor[1] = 0.3866046456891057;
//		coor[0] = -606353.7784885059;  //R=6370000.0
//		coor[1] = 2462671.593039604;
		coor[0] = -0.15254;
		coor[1] = 1.07330;
		BoccTrans a = new BoccTrans();
//		coor1 = a.BoccLonLatToXY(coor);
		coor1 = a.BoccXYToLonLat(coor);
		System.out.println(coor1[0]);
		System.out.println(coor1[1]);
	}
*/
}
