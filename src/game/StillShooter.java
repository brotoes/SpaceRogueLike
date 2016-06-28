package game;

import javafx.geometry.Point2D;

public class StillShooter extends ShipRemote {
	//double[] desiredThrustVector = {1.0, 0.0};
	
	public StillShooter(Ship host) {
		super(host);
		
		setShooting(true);
	}
	
	@Override
	public Point2D getDesiredDirection() {
		return host.getDirectionTo(World.player);
	}
}