package cn.ct.em.draw.core.contour;

import cn.ct.em.calculate.NCcoordinate.ToLonLat;
import cn.ct.em.calculate.NCcoordinate.ToMercator;

public class NCRenderFactory {

	static public NCRenderCore getMercatorSVGPolygonFull(){
		NCRenderCore obj = new NCRenderCore();
		obj.shapeBridge = new ShapeSVGPolygon();
		obj.colourBridge = new ColourFull();
		obj.colourBridge.nCRanderCore = obj;
		obj.shapeBridge.nCRanderCore = obj;
		obj.toFactory = new ToMercator();

		return obj;
	}
	
	static public NCRenderCore getLonLatSVGPolygonFull(){
		NCRenderCore obj = new NCRenderCore();
		obj.shapeBridge = new ShapeSVGPolygon();
		obj.colourBridge = new ColourFull();
		obj.colourBridge.nCRanderCore = obj;
		obj.shapeBridge.nCRanderCore = obj;
		obj.toFactory = new ToLonLat();

		return obj;
	}
	
	
	static public NCRenderCore getMercatorSVGPolylineFull(){
		NCRenderCore obj = new NCRenderCore();
		obj.shapeBridge = new ShapeSVGPolyline();
		obj.colourBridge = new ColourFull();
		obj.colourBridge.nCRanderCore = obj;
		obj.shapeBridge.nCRanderCore = obj;
		obj.toFactory = new ToMercator();

		return obj;
	}
	
	
	static public NCRenderCore getLonLatJsonPolygonFull(){
		NCRenderCore obj = new NCRenderCore();
		obj.shapeBridge = new ShapeJsonPolygon();
		obj.colourBridge = new ColourFull();
		obj.colourBridge.nCRanderCore = obj;
		obj.shapeBridge.nCRanderCore = obj;
		obj.toFactory = new ToLonLat();
		return obj;
	}


}
