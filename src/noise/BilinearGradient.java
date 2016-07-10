package noise;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.ImageUtils;

public class BilinearGradient {
	private ArrayList<ColorPoint> points = new ArrayList<ColorPoint>();
	
	public BilinearGradient() {
		/* Corners */
		add(new ColorPoint(0, 0, Color.DARKBLUE));
		add(new ColorPoint(0, 1, Color.DARKBLUE));
		add(new ColorPoint(1, 1, Color.WHITE));
		add(new ColorPoint(1, 0, Color.DARKGREEN));
		
		
		add(new ColorPoint(1, 0.8, Color.DARKGREEN));
		add(new ColorPoint(0.4, 0.5, Color.LIGHTBLUE));
	}
	
	public void add(ColorPoint point) {
		points.add(point);
	}
	
	public Color getColor(double x, double y) {
		return getColor(new Point2D(x, y));
	}
	
	public Color getColor(Point2D pos) {
		ColorPoint tr = null;
		ColorPoint tl = null;
		ColorPoint bl = null;
		ColorPoint br = null;
		
		/* Get top right point */
		double lastDist = 0.0;
		for (int i = 0; i < points.size(); i ++) {
			ColorPoint point = points.get(i);
			
			if (point.getX() >= pos.getX() && point.getY() <= pos.getY()) {
				double dist = point.getPos().distance(pos);
				if (dist < lastDist || tr == null) {
					tr = point;
					lastDist = dist;
				}
			}
		}
		/* Get top left point */
		for (int i = 0; i < points.size(); i ++) {
			ColorPoint point = points.get(i);
			if (point.getX() <= pos.getX() && point.getY() <= pos.getY()) {
				double dist = point.getPos().distance(pos);
				if (dist < lastDist || tl == null) {
					tl = point;
					lastDist = dist;
				}
			}
		}
		/* Get bottom left point */
		for (int i = 0; i < points.size(); i ++) {
			ColorPoint point = points.get(i);
			if (point.getX() <= pos.getX() && point.getY() >= pos.getY()) {
				double dist = point.getPos().distance(pos);
				if (dist < lastDist || bl == null) {
					bl = point;
					lastDist = dist;
				}
			}
		}
		/* Get bottom right point */
		for (int i = 0; i < points.size(); i ++) {
			ColorPoint point = points.get(i);
			if (point.getX() >= pos.getX() && point.getY() >= pos.getY()) {
				double dist = point.getPos().distance(pos);
				if (dist < lastDist || br == null) {
					br = point;
					lastDist = dist;
				}
			}
		}
		
		if (tr == null || tl == null || bl == null || br == null) {
			return Color.WHITE;
		}
		
		double tt = (pos.getX() - tl.getX())/(tr.getX() - tl.getX());
		Color top = ImageUtils.lerpColor(tl.getColor(), tr.getColor(), tt);
		double bt = (pos.getX() - bl.getX())/(br.getX() - bl.getX());
		Color btm = ImageUtils.lerpColor(bl.getColor(), br.getColor(), bt);
		
		double t = (pos.getY() - tr.getY())/(br.getY() - tr.getY());
		return ImageUtils.lerpColor(top, btm, t);
	}
}
