package game;

import java.util.ArrayList;
import org.w3c.dom.*;
import utils.FileUtils;
import javafx.scene.transform.Affine;
import remotes.ShipRemote;
import javafx.geometry.Point2D;

public class Ship extends Actor {
	String hullName;
	
	double shieldDiam = 60;
	
	boolean inAtmo = true;
	double[] trailX = {3, 3};
	double[] trailY = {0, 32};
	
	Hull hull;
	Thruster[] thrusters;
	//Trail[] trails = new Trail[20];
	int nextTrail = 0;
	int numTrails = 0;
	
	double shotV = 0.9;
	int shootDelay = 20;
	int shootTimer = 0;
	
	private ShipRemote remote = null;

	ArrayList<Cargo> cargo = new ArrayList<Cargo>();
	ArrayList<InventoryItem> inventory = new ArrayList<InventoryItem>();
	ArrayList<InventoryItem> equippedItems = new ArrayList<InventoryItem>();
	
	public Ship(String config, double x, double y) {
		super(x, y, "ship");
		
		setDepth(100);
		
		config = "/resources/components/shipConfigurations/" + config + ".xml";
		
		configureShip(config);
	}
	
	@Override
	public void tick() {
		Point2D thrustVector;
		//AffineTransform trailXForm = new AffineTransform();
		
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			equippedItems.get(i).tick();
		}
		
		/*
	    trailXForm.rotate(this.getTheta());
		
		int prevInd = MathUtils.posMod(this.nextTrail - 2, this.trails.length);
		for (int i = 0; i < this.trailX.length; i ++) {
			double[] p2 = new double[2];
			double[] p1 = new double[2];
			double[] offset = {this.sprite.getWidth()/2 - this.trailX[i],
					this.sprite.getHeight()/2 - this.trailY[i]};		
			
			trailXForm.transform(offset, 0, offset, 0, 1);
			
			p1[0] = this.getX() - offset[0];
			p1[1] = this.getY() - offset[1];
			
			if (this.numTrails > 1) {
				p2[0] = this.trails[prevInd + i].x1;
				p2[1] = this.trails[prevInd + i].y1;
			} else {
				p2[0] = p1[0];
				p2[1] = p1[1];
			}

			this.trails[this.nextTrail + i] = new Trail(p1[0], p1[1], p2[0], p2[1]);
		}
		this.nextTrail = (this.nextTrail + this.trailX.length) 
				% this.trails.length;
		if (this.numTrails < this.trails.length) {
			this.numTrails += 2;
		}*/

		setThrustVector(remote.getDesiredThrustVector());
		
		/* Rotate */
		if (hull.getRotSpeed() < 0.0) {
			this.setDir(remote.getDesiredDirection());
		} else {
			double theta = remote.getDesiredTheta() - this.getTheta();
			double speed = Math.signum(theta)*hull.getRotSpeed();
			if (Math.abs(theta) > Math.abs(speed)) {
				this.rotate(speed);
			} else {
				this.rotate(theta);
			}
		}
		
		/* get actual thrust vector, and adjust speed */
		this.useFuel(this.getFuelUse());
		thrustVector = getThrustVector();
		
		if (thrustVector != null) {
			accelerate(thrustVector.multiply(1.0/getMass()));
		}
		
		/* Tick thrusters */
		if (this.thrusters != null) {
			for (int i = 0; i < this.thrusters.length; i ++) {
				this.thrusters[i].tick();
			}
		}
		
		if (remote.isShooting()) {
			this.shoot();
		}
		
		if (this.shootTimer > 0) {
			this.shootTimer --;
		}
		
		super.tick();
	}
	
	
	public ShipRemote getRemote() {
		return remote;
	}
	
	public void setRemote(ShipRemote remote) {
		this.remote = remote;
	}
	
	public void shoot() {
		if (this.alive && this.shootTimer == 0 && this.useCharge(100.0)) {
			Point2D bulletV = getV().add(getDir().multiply(this.shotV));
			/*if (bulletV.magnitude() > 1.0) {
				//bulletV = bulletV.normalize();
			}*/
			new Bullet(this, this.getX(), this.getY(), this.getDir(), bulletV.getX(), bulletV.getY());
			this.shootTimer = this.shootDelay;
		}
	}
	
	public void damage(double damage) {
		/* loop over equipped items, damaging shields first */
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof ShieldGenerator) {
				ShieldGenerator gen = (ShieldGenerator)item;
				damage = gen.damage(damage);
			}
		}
		
		/* apply remaining damage to hull */
		this.hull.HP -= damage;
		if (this.hull.HP <= 0) {
			//this.destroy();
		}
	}
	
	protected void configureShip(String ConfigFName) {
		Document shipXML;
		NodeList thrusters;

		shipXML = FileUtils.openXML(ConfigFName);
		
		/* Configure Hull */
		NodeList hullNode = shipXML.getElementsByTagName("hull");
		String hullName = hullNode.item(0).getTextContent().trim();
		hull = new Hull(this, hullName);
		updateSprite(hull.sprite);
		trailX = hull.trailX;
		trailY = hull.trailY;

		/* Configure Thrusters */
		thrusters = shipXML.getElementsByTagName("thruster");
		
		this.thrusters = new Thruster[this.hull.thrusterSlots.length];

		for (int i = 0; i < this.hull.thrusterSlots.length; i ++) {
			Node thruster = thrusters.item(i);
			String thrusterFName;
			
			/* Get Thruster Info */
			thrusterFName = thruster.getTextContent();
			this.thrusters[i] = new Thruster(hull.thrusterSlots[i], thrusterFName);
		}
		
		/* Configure Equipment */
		InventoryItem item = null;
		NodeList equipNodes = shipXML.getElementsByTagName("equipment");
		equipNodes = equipNodes.item(0).getChildNodes();
		for (int i = 0; i < equipNodes.getLength(); i ++) {
			Node node = equipNodes.item(i);
			NodeList attrs = node.getChildNodes();
			String name = null;
			String[] posList;
			int[] pos = null;
			/* get item configurations */
			for (int j = 0; j < attrs.getLength(); j ++) {
				Node attr = attrs.item(j);
				switch(attr.getNodeName()) {
				case "name":
					name = attr.getTextContent().trim();
					break;
				case "pos":
					posList = attr.getTextContent().trim().split(",");
					pos = new int[posList.length];
					for (int k = 0; k < posList.length; k ++) {
						pos[k] = Integer.parseInt(posList[k]);
					}
					break;
				}
			}
			
			if (name == null || pos == null) {
				continue;
			}
			
			/* Instantiate Item */
			switch (node.getNodeName()) {
			case "fuelTank":
				item = (new FuelTank(name));
				break;
			case "reactor":
				item = (new Reactor(name));
				break;
			case "shieldGenerator":
				item = (new ShieldGenerator(name));
				break;
			default:
				System.err.println("Error: Unrecognized Equipment Type");
				break;
			}
			
			if (item == null) {
				continue;
			}
			
			/* equip item */
			this.equip(item);
			
			/* make item show in equip screen */
			/* TODO */
		}
	}
	
	public double getMass() {
		double mass = 0.0;
		if (this.thrusters != null) {
			for (int i = 0; i < this.thrusters.length; i ++) {
				mass += this.thrusters[i].mass;
			}
		}
		if (this.hull != null) {
			mass += this.hull.mass;
		}
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			mass += this.equippedItems.get(i).mass;
		}
		return mass;
	}
	
	public void setThrustVector(Point2D desiredThrustVector) {
		if (this.thrusters == null) {
			return;
		}
		
		/*
		mags = LinAlg.linearCombination(this.desiredThrustVector, thrusterVectors, 2, this.thrusters.length, false);
		for (int i = 0; i < this.thrusters.length; i ++) {
			this.thrusters[i].setThrust(mags[i]);
		}
		*/
		for (int i = 0; i < thrusters.length; i ++) {
			Point2D v = thrusters[i].getVector();
			if ((Math.signum(v.getX()) == Math.signum(desiredThrustVector.getX())
					&& Math.signum(v.getX()) != 0) ||
					Math.signum(v.getY()) == Math.signum(desiredThrustVector.getY())
					&& Math.signum(v.getY()) != 0) {
				thrusters[i].setThrust(1.0);
			} else {
				thrusters[i].setThrust(0.0);
			}

		}
	}
	
	/* Returns the thrust vector of the ship, accounting for rotation */
	public Point2D getThrustVector() {
		Affine affine;
		Point2D thrustVector = new Point2D(0.0, 0.0);
		
		if (this.thrusters == null) {
			return null;
		}
		
		for (int i = 0; i < this.thrusters.length; i ++) {
			thrustVector = thrustVector.add(thrusters[i].getThrustVector());
		}

		affine = new Affine();
		affine.appendRotation(Math.toDegrees(getTheta()));
		return affine.transform(thrustVector);
	}
	
	public void addToInventory(InventoryItem item) {
		this.inventory.add(item);
	}
	
	public void addToCargo(Cargo cargo) {
		this.cargo.add(cargo);
		/*TODO Add to cargo screen?*/
	}
	
	public void equip(InventoryItem item) {
		if (this.equippedItems.indexOf(item) == -1) {
			this.equippedItems.add(item);
			item.host = this;
		}
	}
	
	public void unequip(InventoryItem item) {
		int index = this.equippedItems.indexOf(item);
		if (index != -1) {
			this.equippedItems.remove(index);
		}
	}
	
	public boolean useFuel(double fuel) {
		if (this.getFuelAvailable() < fuel) {
			return false;
		}
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof FuelTank) {
				FuelTank tank = (FuelTank)item;
				double used = Math.min(fuel, tank.fuel);
				tank.fuel -= used;
				fuel -= used; 
			}
		}
		return true;
	}
	
	public double getFuelAvailable() {
		double fuel = 0.0;
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof FuelTank) {
				FuelTank tank = (FuelTank)item;
				fuel += tank.fuel;
			}
		}
		return fuel;
	}
	
	public void addFuel(double fuel) {
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof FuelTank) {
				FuelTank tank = (FuelTank)item;
				double space = tank.fuelCapacity - tank.fuel;
				double amountFilled = Math.min(space, fuel);
				tank.fuel += amountFilled;
				fuel -= amountFilled;
			}
		}
	}
	
	public double getFuelCapacity() {
		double cap = 0.0;
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof FuelTank) {
				FuelTank tank = (FuelTank)item;
				cap += tank.fuelCapacity;
			}
		}
		return cap;
	}
	
	public double getFuelUse() {
		double use = 0.0;
		
		if (this.thrusters == null) {
			return use;
		}
		
		for (int i = 0; i < this.thrusters.length; i ++) {
			use += this.thrusters[i].getFuelUse();
		}
		
		return use;
	}
	
	public double getShieldCharge() {
		double charge = 0;
		int nGens = 0;
		
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof ShieldGenerator) {
				ShieldGenerator gen = (ShieldGenerator)item;
				charge += gen.charge*gen.strength;
				nGens += gen.strength;
			}
		}
		
		return charge/nGens;
	}
	
	public double getChargeAvailable() {
		double charge = 0;
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof Reactor) {
				Reactor reactor = (Reactor)item;
				charge += reactor.charge;
			}
		}
		return charge;
	}
	
	public boolean useCharge(double charge) {
		if (this.getChargeAvailable() < charge) {
			return false;
		}
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof Reactor) {
				Reactor reactor = (Reactor)item;
				double used = Math.min(charge, reactor.charge);
				charge -= used;
				reactor.charge -= used;
			}
		}
		return true;
	}
	
	public double getChargeCapacity() {
		double capacity = 0;
		for (int i = 0; i < this.equippedItems.size(); i ++) {
			InventoryItem item = this.equippedItems.get(i);
			if (item instanceof Reactor) {
				Reactor reactor = (Reactor)item;
				capacity += reactor.capacity;
			}
		}
		return capacity;
	}
}
