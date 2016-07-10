package solarSystem;

import java.util.ArrayList;

import game.MassiveObject;
import javafx.geometry.Point2D;

public class SpaceTime {
	private double c = 120.0;
	private ArrayList<MassiveObject> objs = new ArrayList<MassiveObject>();
	
	public double getC(double x, double y) {
		if (objs.size() == 0) {
			return this.c;
		}
		
		Point2D cPoint = new Point2D(x, y);
		double gravity = 1.0;
		for (int i = 0; i < objs.size(); i ++) {
			double width = objs.get(i).getWellWidth();
			double depth = objs.get(i).getWellDepth();
			Point2D objPos = objs.get(i).getPos();
			double dist = objPos.distance(cPoint);
			
			double pot = 1 - 1/((dist*dist)/(width*width) + 1);
			pot *= depth;
			pot += 1 - depth;
			gravity = Math.min(pot, gravity);
		}
			
		double c = this.c * gravity;
		
		return c;
	}
	
	public void add(MassiveObject obj) {
		if (this.objs.indexOf(obj) == -1) {
			this.objs.add(obj);
		}
	}
}
