package game;

import javafx.geometry.Point2D;

public class ThrusterSlot {
	Ship host;
	String type;
	private Point2D vector = new Point2D(0.0, 0.0);
	private Point2D offset = new Point2D(0.0, 0.0);
	
	public ThrusterSlot(Ship host, String type, Point2D vector, Point2D offset) {
		this.type = type;
		this.vector = vector;
		this.offset = offset;
		this.host = host;
	}
	
	public double getTheta() {
		return Math.atan2(vector.getY(), vector.getX());
	}
	
	public Point2D getVector() {
		return vector;
	}
	
	public Point2D getOffset() {
		return offset;
	}
}
