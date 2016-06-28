package game;

import javafx.geometry.Point2D;

public class Bullet extends Actor {
	
	int life = 120;
	Ship firedFrom;
	
	public Bullet(Ship firedFrom, double x, double y, Point2D dir, double vX, double vY) {
		super(x, y, "bullet");
		
		this.setDepth(50);
		
		this.setDir(dir);
		
		this.setVX(vX);
		this.setVY(vY);
		
		this.firedFrom = firedFrom;
	}
	
	@Override
	public void tick() {
		this.life --;
		if (this.life <= 0) {
			this.destroy();
		}
		
		/*for (int i = 0; i < World.tickers.size(); i ++) {
			if (!(World.tickers.get(i) instanceof Actor)) {
				continue;
			}
			Actor obj = (Actor)World.tickers.get(i);
			if (this.isColliding(obj)) {
				if (obj instanceof Ship && obj != this.firedFrom) {
					Ship ship = (Ship)obj;
					ship.damage(1.0);
					this.destroy();
				}
			}
		}*/
		
		super.tick();
	}
}
