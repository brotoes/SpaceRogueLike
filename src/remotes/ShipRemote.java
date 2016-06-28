package remotes;

import game.Ship;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public abstract class ShipRemote {
	private boolean braking = false;
	private boolean shooting = false;
	
	Ship host;

	public ShipRemote(Ship host) {
		this.host = host;
	}
	
	public boolean isShooting() {
		return this.shooting;
	}
	
	public void setShooting(boolean state) {
		shooting = state;
	}
	
	public boolean isBraking() {
		return braking;
	}
	
	public void setBraking(boolean state) {
		braking = state;
	}
	
	public double getDesiredTheta() {
		Point2D dir = this.getDesiredDirection();
		return Math.atan2(dir.getY(), dir.getX());
	}
	
	public Point2D getDesiredDirection() {
		return new Point2D(1.0, 0.0);
	}

	public Point2D getDesiredThrustVector() {
		Point2D v = new Point2D(0.0, 0.0);
		
		/* Check if braking */
		if (this.braking) {
			v = this.getBrakingVector();
		}
		
		return v;
	}
	
	public Point2D getBrakingVector() {
		Point2D v = host.getV().multiply(-1);
		Rotate rotate = new Rotate(Math.toDegrees(-host.getTheta()));
		v = rotate.transform(v);
		
		if (Math.abs(host.getVX()) <= 0.005 && Math.abs(host.getVY()) <= 0.005) {
			v = new Point2D(0.0, 0.0);
			host.setVX(0);
			host.setVY(0);
		}
		
		return v;
	}
}
