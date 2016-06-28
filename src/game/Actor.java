package game;

import javafx.geometry.Point2D;

public abstract class Actor {

	protected Sprite sprite;
	
	private int depth = 0;
	
	private Point2D v = new Point2D(0, 0);
	private Point2D pos;
	private Point2D dir;
	public Point2D origin;

	boolean alive = true;
	
	public Actor (double x, double y, String spriteLocation) {
		this(x, y);
		
		sprite = new Sprite(this, spriteLocation);
	}
	
	public Actor(double x, double y) {
		pos = new Point2D(x, y);
		dir = new Point2D(1.0, 0.0);
		
		World.addActor(this);

		origin = pos;
		
		return;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getDepth() {
		return depth;
	}
	
	/**
	 * Changes the actors depth and TODO: Moves the sprite and actor in world
	 * and Screen accordingly
	 * @param depth
	 */
	public int setDepth(int depth) {
		this.depth = depth;
		
		if (sprite != null) {
			sprite.place();
		}
		
		return World.addActor(this);
	}
	
	public void tick() {
		if (!alive || World.isPaused()) {
			return;
		}
		
		if (sprite != null) {
			sprite.tick();
		}
		
		if (v.magnitude() == 0.0) {
			return;
		}
		
		double c = getC();
		Point2D delta = v.multiply(c).multiply(Game.getFrameLen());
		pos = pos.add(delta);
	}
	
	public void destroy() {
		World.removeActor(this);
		if (sprite != null) {
			sprite.destroy();
		}
		alive = false;
	}
	
	public void updateSprite(String name) {
		Sprite newSprite = new Sprite(this, name);
		sprite.destroy();
		sprite = newSprite;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public void setAlive(boolean state) {
		alive = state;
	}
	
	protected double getC() {
		//double c = World.solarSystem.spacetime.getC(this.x, this.y);
		//return c;
		return 3000;
	}
	
	public double getTheta() {
		return Math.atan2(dir.getY(),  dir.getX());
	}
	
	public Point2D getDir() {
		return this.dir;
	}
	
	public void setDir(Point2D newDir) {

		dir = newDir;
	}
	
	public void rotate(double theta) {
		double newTheta = this.getTheta() + theta;
		Point2D newDir = new Point2D(Math.cos(newTheta), Math.sin(newTheta));
		dir = newDir;
	}
	
	public double getX() {
		return pos.getX();
	}
	
	public double getY() {
		return pos.getY();
	}
	
	public Point2D getPos() {
		return pos;
	}
	
	public Point2D getOrigin() {
		return origin;
	}
	
	public void setPos(Point2D newPos) {
		pos = newPos;
	}
	
	public void setX(double x) {
		pos = new Point2D(x, pos.getY());
	}
	
	public void setY(double y) {
		pos = new Point2D(pos.getX(), y);
	}
	
	public void setVX(double vx) {
		v = new Point2D(vx, v.getY());
	}
	
	public void setVY(double vy) {
		v = new Point2D(v.getX(), vy);
	}
	
	public double getVX() {
		return v.getX();
	}
	
	public double getVY() {
		return v.getY();
	}
	
	public Point2D getV() {
		return v;
	}
	
	protected void setV(Point2D newV) {
		v = newV;
	}
	
	public double getVelocity() {
		return v.magnitude();
	}
	
	public double getWidth() {
		if (this.sprite != null) {
			return this.sprite.getWidth();
		}
		return 0;
	}
	
	public double getHeight() {
		if (this.sprite != null) {
			return this.sprite.getHeight();
		}
		return 0;
	}
	
	public double getSize() {
		return Math.max(this.getWidth(), this.getHeight());
	}
	
	public boolean isColliding(Actor actor) {
		//TODO this should be much better
		return this.distanceFrom(actor) < this.getSize() + actor.getSize();
	}
	
	protected void accelerate(Point2D delta) {
		if (Math.signum(delta.getX()) == Math.signum(this.getVX())) {
			setVX(getVX() + delta.getX()*(1.0 - this.getVelocity()));
		} else {
			setVX(getVX() + delta.getX()*(1.0 + this.getVelocity()));
		}
		
		if (Math.signum(delta.getY()) == Math.signum(this.getVY())) {
			setVY(getVY() + delta.getY()*(1.0 - this.getVelocity()));
		} else {
			setVY(getVY() + delta.getY()*(1.0 + this.getVelocity()));
		}
	}
	
	protected void face(Actor actor) {
		this.facePoint(actor.getPos());
	}
	
	protected void facePoint(Point2D point) {
		Point2D newDir;
		
		newDir = getDirectionToPoint(point);
		if (newDir != null) {
			this.dir = newDir;
		}
	}
	
	public Point2D getDirectionTo(Actor actor) {
		return getDirectionToPoint(actor.getPos());
	}
	
	protected void faceCursor() {
		Point2D newDir = this.getDirectionToMouse();
		if (newDir != null) {
			this.dir = newDir;
		}
	}
	
	protected Point2D getDirectionToMouse() {	
		return getDirectionToPoint(Game.screen.getCamMousePos());
	}
	
	protected Point2D getDirectionToPoint(Point2D point) {
		return point.subtract(pos).normalize();
	}
	
	public double distanceFrom(Point2D point) {
		return point.subtract(pos).magnitude();
	}
	
	public double distanceFrom(Actor actor) {
		return distanceFrom(actor.getPos());
	}
}
