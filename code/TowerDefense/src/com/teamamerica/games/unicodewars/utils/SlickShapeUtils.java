package com.teamamerica.games.unicodewars.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class SlickShapeUtils {
    private static Logger logger = Logger.getLogger( SlickShapeUtils.class );

	public static Color colorFromXML(Element e) { 
		float r = Float.parseFloat(e.attributeValue("r"));
		float g = Float.parseFloat(e.attributeValue("g"));
		float b = Float.parseFloat(e.attributeValue("b"));
		float a = Float.parseFloat(e.attributeValue("a"));
		
		return new Color(r,g,b,a);
	}
	
	public static Shape shapeFromXML(Element e) { 
		String type = e.attributeValue("type");
		return Shapes.valueOf(type).fromXML(e);
	}
	
}

enum Shapes { 
	circle {
	    Shape fromXML(Element e) { 
		    Logger logger = Logger.getLogger( SlickShapeUtils.class );
			logger.debug("Creating a circle");
			Element center = e.element("center");
			float x = Float.parseFloat(center.attributeValue("x"));
			float y = Float.parseFloat(center.attributeValue("y"));
			float radius = Float.parseFloat(e.element("radius").attributeValue("value"));
			return new Circle(x,y,radius);
		}
	},
	rectangle {
		Shape fromXML(Element e) { 
			float x = Float.parseFloat(e.element("x").attributeValue("value"));
			float y = Float.parseFloat(e.element("y").attributeValue("value"));
			
			float width = Float.parseFloat(e.element("width").attributeValue("value"));
			float height = Float.parseFloat(e.element("height").attributeValue("value"));
			
			return new Rectangle(x,y,width,height);
		}
	},
	polygon { 
		Shape fromXML(Element e) { 
			Polygon p = new Polygon();
			List points = e.elements("vertex");
			for (int i = 0; i < points.size(); ++i) { 
				Element v = (Element) points.get(i);
				float x = Float.parseFloat(v.attributeValue("x"));
				float y = Float.parseFloat(v.attributeValue("y"));
				p.addPoint(x, y);
			}
			return p;
		}
	};
	
	
	abstract Shape fromXML(Element e);
}