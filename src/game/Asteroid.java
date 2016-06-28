package game;

import java.util.Random;
import javafx.geometry.Point2D;
import utils.MathUtils;

public class Asteroid extends Actor {
	
	double vTheta;
	int depth = 0;
	
	public Asteroid(double x, double y) {
		super(x, y, "asteroid1");
		
		Random rand = new Random();
		
		double xVMin = -5.0;
		double yVMin = -5.0;
		double xVMax = 5.0;
		double yVMax = 5.0;
		
		this.setVX(xVMin + (xVMax - xVMin) * rand.nextDouble());
		this.setVY(yVMin + (yVMax - yVMin) * rand.nextDouble());
		
		String spriteFilename = "asteroid";
		spriteFilename += rand.nextInt(3) + 1;
		
		sprite = new Sprite(this, spriteFilename);
		
		Point2D dir = new Point2D(rand.nextDouble() * 2 - 1, 
				rand.nextDouble() * 2 - 1);
		this.setDir(dir);
		
		this.vTheta = rand.nextDouble()/60;
	}
	
	public Asteroid() {
		super(0, 0, "asteroid1");
		
		Random rand = new Random();
		
		double xMin = 0.0;
		double yMin = 0.0;
		double xMax = 10.0;//World.camera.cameraSize[0];
		double yMax = 10.0;//World.camera.cameraSize[1];
		double xVMin = -5.0;
		double yVMin = -5.0;
		double xVMax = 5.0;
		double yVMax = 5.0;
		
		this.setX(MathUtils.randomDouble(xMin, xMax));
		this.setY(MathUtils.randomDouble(yMin, yMax));
		this.setVX(MathUtils.randomDouble(xVMin, xVMax));
		this.setVY(MathUtils.randomDouble(yVMin, yVMax));
		
		String spriteFilename = "asteroid";
		spriteFilename += rand.nextInt(3) + 1;
		
		this.sprite = new Sprite(this, spriteFilename);
		
		Point2D dir = new Point2D(rand.nextDouble() * 2 - 1, 
				rand.nextDouble() * 2 - 1);
		this.setDir(dir);
		
		this.vTheta = rand.nextDouble()/60;
	}
	
	@Override
	public void tick() {
		this.rotate(this.vTheta);
		
		//check for collision with bullet and ship
		/*if (this.alive) {
			for (int i = 0; i < World.tickers.size(); i ++) {
				if (!(World.tickers.get(i) instanceof Actor)) {
					continue;
				}
				Actor target = (Actor)World.tickers.get(i);
				if (target instanceof Bullet ||
					target instanceof Ship) {
					if (target.alive == true &&
						Math.abs(target.getX() - this.getY()) < this.sprite.getWidth()*this.scale &&
						Math.abs(target.getY() - this.getY()) < this.sprite.getHeight()*this.scale) {
						/*if (target instanceof Ship) {
							if ((((Ship)target).shielded)) {
								if (this.scale > 0.1) {
									((Ship)target).shielded = false;
								}
								this.destroy(0);
							} else {
								if (this.scale > 0.1) {
									target.alive = false;
								} else {
									this.destroy(0);
								}
							}
						} else {
							target.alive = false;
						}
						if (target instanceof Bullet) {
							Random rand = new Random();
							this.destroy(rand.nextInt(5));
						}
					}
				}
			}
		}*/
		
		super.tick();
	}
	
	@Override
	public int getDepth() {
		return depth;
	}
	
	public void destroy(int num) {
		//World.hud.asteroidsKilled ++;
		this.alive = false;
		
		if (num == 0) {
			return;
		}
		
		for (int i = 0; i < num; i ++) {
			new Asteroid(this.getX(), this.getY());
		}
	}
}
