package game;

import javafx.geometry.Point2D;

public class Player extends ShipRemote {
	private boolean fore = false;
	private boolean aft = false;
	private boolean starboard = false;
	private boolean port = false;
	
	public Player(Ship host) {
		super(host);
		
		/**Get Inventory*/
		/* TODO: Load from save? */
	}
	
	@Override
	public Point2D getDesiredDirection() {
		return host.getDirectionToMouse();
	}
	
	@Override
	public Point2D getDesiredThrustVector() {
		/**Calculate states of each thruster to get as close to
		 * Desired thrust vector as possible
		 */
		Point2D v = new Point2D(0.0, 0.0);

		if (fore) {
			v = new Point2D(v.getX() + 1, v.getY());
		}
		if (aft) {
			v = new Point2D(v.getX() - 1, v.getY());
		}
		if (starboard) {
			v = new Point2D(v.getX(), v.getY() + 1);
		}
		if (port) {
			v = new Point2D(v.getX(), v.getY() - 1);
		}
		
		if (isBraking()) {
			v = getBrakingVector();
		}
		
		Game.screen.setCamPos(host.getPos());
		
		return v;
	}
	
	public boolean getFore() {
		return fore;
	}
	
	public boolean getAft() {
		return aft;
	}
	
	public boolean getStarboard() {
		return starboard;
	}
	
	public boolean getPort() {
		return port;
	}
	
	public void setFore(boolean state) {
		fore = state;
	}
	
	public void setAft(boolean state) {
		aft = state;
	}
	
	public void setStarboard(boolean state) {
		starboard = state;
	}
	
	public void setPort(boolean state) {
		port = state;
	}
	
}
