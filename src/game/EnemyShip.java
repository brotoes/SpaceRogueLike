package game;

import javafx.geometry.Point2D;

public class EnemyShip extends StillShooter {
	
	public EnemyShip(Ship host) {
		super(host);
		
		setShooting(true);
	}
	
	@Override
	public Point2D getDesiredThrustVector() {
		return new Point2D(1.0, 0.0);
	}
}