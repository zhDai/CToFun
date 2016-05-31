
public class PolarStereographic {
	public double pi = 3.14159265358979323846;
	public double k0 = 1;
	public double deg2ard = pi / 180.0;
	public double R  = 1;
	
	public double[] PolarStereLanLonToXY(double[] coor, double[] origin){
		double[] res = new double[2];
		double fai = coor[0]*deg2ard;
		double niu = coor[1]*deg2ard;
		double fai_0 = origin[0]*deg2ard;
		double niu_0 = origin[1]*deg2ard;
		double x = 0,y = 0,k,p,theta;	
		if(fai_0 == pi/2){
			//north polar stereographic
			x = 2*R*k0*Math.tan(pi/4-fai/2)*Math.sin(niu-niu_0);
			y = -2*R*k0*Math.tan(pi/4-fai/2)*Math.cos(niu-niu_0);
			k = 2*k0/(1+Math.sin(fai));
			p = 2*R*k0*Math.tan(pi/4-fai/2);
			theta = niu-niu_0;
		}else if(fai_0 == -pi/2){
			//south polar stereographic
			x = 2*R*k0*Math.tan(pi/4+fai/2)*Math.sin(niu-niu_0);
			y = 2*R*k0*Math.tan(pi/4+fai/2)*Math.cos(niu-niu_0);
			k = 2*k0/(1-Math.sin(fai));
			p = 2*R*k0*Math.tan(pi/4+fai/2);
			theta = pi-niu+niu_0;
		}else if(fai_0 == 0){
			//equatorial aspect
			k = 2*k0/(1+Math.cos(fai)*Math.cos(niu-niu_0));
			x = R*k*Math.cos(fai)*Math.sin(niu-niu_0);
			y = R*k*Math.sin(fai);
		}else{
			System.out.println("pass!");
		}
		res[0] = x;
		res[1] = y;
		return res;
	}
	
	public double[] PolarStereXYToLanLon(double[] coor, double[] origin){
		double[] res = new double[2];
		
		double fai_0 = origin[0]*deg2ard;
		double niu_0 = origin[1]*deg2ard;
		double x = coor[0];
		double y = coor[1];
		double p = Math.pow((Math.pow(x, 2)+Math.pow(y, 2)), 0.5);
		double c = 2*Math.atan(p/(2*R*k0));
		double fai,niu;
		double eps = 1e-5;
		if (p == 0){
			fai = fai_0;
			niu = niu_0;
		}else{
			double aa = Math.cos(c)*Math.sin(fai_0)+y*Math.sin(c)*Math.cos(fai_0)/p;
//			System.out.println(aa);
			fai = Math.asin(Math.cos(c)*Math.sin(fai_0)+y*Math.sin(c)*Math.cos(fai_0)/p);	
			if(Math.abs(fai - pi/2)<eps){
				niu = niu_0 + Math.atan(x/(-y));
			}else if(Math.abs(fai + pi/2)<eps){
				niu = niu_0 + Math.atan(x/y);
			}else{
				niu = niu_0+Math.atan(x*Math.sin(c)/(p*Math.cos(fai_0)*Math.cos(c)-y*Math.sin(fai_0)*Math.sin(c)));
			}
		}
		res[0] = fai/deg2ard;
		res[1] = niu/deg2ard;
		
		return res;
	}
	//测试
/*
	public static void main(String args[]){
		double[] origin = new double[2];
		origin[0] = 0;
		origin[1] = 0;
		double[] coor = new double[2];
		coor[0] = 0;
		coor[1] = 1.67820;
		PolarStereographic a = new PolarStereographic();
		double[] outcome = new double[2];
//		outcome = a.PolarStereLanLonToXY(coor, origin);
		outcome = a.PolarStereXYToLanLon(coor, origin);
		System.out.println(outcome[0]);
		System.out.println(outcome[1]);
		
	}
*/
}
