package noise;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class ColorPoint {
	private Point2D pos;
	private Color color;
	
	public ColorPoint(Point2D pos, Color color) {
		this.pos = pos;
		this.color = color;
	}
	
	public ColorPoint(double x, double y, Color color) {
		this.pos = new Point2D(x, y);
		this.color = color;
	}
	
	public Point2D getPos() {
		return pos;
	}
	
	public double getX() {
		return pos.getX();
	}
	
	public double getY() {
		return pos.getY();
	}
	
	public Color getColor() {
		return color;
	}
	
	public String toString() {
		return "[" + color.toString() + ", " + pos.toString() + "]";
	}
}
