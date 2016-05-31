package cn.ct.em.draw.core.vector;



public class WindReanderFactory {
	public static WindRenderCore getMercatorSVGBarb(){

		WindRenderCore obj = new WindRenderCore();
		obj.shape = new WindBarRender();
		obj.svg = new SVGRenderBarb();
		return obj;

	}
	
	public static WindRenderCore getMercatorSVGArrow(){

		WindRenderCore obj = new WindRenderCore();
		obj.shape = new WindArrowRender();
		obj.svg = new SVGRenderArrow();
		return obj;

	}
}
