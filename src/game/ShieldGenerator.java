package game;

import org.w3c.dom.Node;

public class ShieldGenerator extends InventoryItem {
	
	double rate;
	double strength;
	double charge = 0.0;
	double chargeCost; //TODO: fix bug: if I set this here, parseAttr can't
	
	public ShieldGenerator(String xmlFName) {
		super("shieldGenerators/" + xmlFName);
		this.updateBlockSprite("shieldBlock");
	}
	
	@Override
	protected void parseAttribute(Node attr) {
		switch(attr.getNodeName()) {
		case "strength":
			this.strength = Double.parseDouble(attr.getTextContent().trim());
			break;
		case "rate":
			this.rate = Double.parseDouble(attr.getTextContent().trim());
			break;
		case "chargeCost":
			this.chargeCost = Double.parseDouble(attr.getTextContent().trim());
			break;
		default:
			super.parseAttribute(attr);
		}
	}
	
	@Override
	public void tick() {
		/* charge shield at designated rate
		 * If shield is charged, consume half power 
		 */
		//TODO Fix Bug: if generator is added before reactor, will not recharge
		if (this.charge < 1.0 && 
				this.host.useCharge(this.chargeCost*this.rate)) {
			this.addCharge(this.rate);
		} else if (!this.host.useCharge(this.chargeCost*this.rate/2)) {
			this.depleteCharge(0.1);
		}
	}
	
	public void addCharge(double charge) {
		this.charge += charge;
		if (this.charge > 1.0) {
			this.charge = 1.0;
		}
	}
	
	public void depleteCharge(double charge) {
		this.charge -= charge;
		if (this.charge < 0.0) {
			this.charge = 0.0;
		}
	}
	
	public double getCharge() {
		return this.charge;
	}
	
	public double damage(double damage) {
		double absorbed = Math.min(this.strength*this.charge, damage);
		absorbed *= this.charge;
		this.depleteCharge(absorbed/this.strength);
		
		return damage - absorbed;
	}
	
	/*public void draw() {
		double shieldDiam = host.sprite.getWidth() * 2;
		Graphics2D g = World.canvas.graphics;

		Color fillColor = new Color(0, 255, 255, (int)(255*this.charge/3));
		Color edgeColor = new Color(0, 255, 255, (int)(255*this.charge));
		g.setColor(fillColor);
		g.fillOval(
				(int)(host.getX() - shieldDiam/2 - World.camera.cameraPos[0]),
				(int)(host.getY() - shieldDiam/2 - World.camera.cameraPos[1]),
				(int)shieldDiam,
				(int)shieldDiam);
		g.setColor(edgeColor);
		g.drawOval(
				(int)(host.getX() - shieldDiam/2 - World.camera.cameraPos[0]),
				(int)(host.getY() - shieldDiam/2 - World.camera.cameraPos[1]),
				(int)shieldDiam,
				(int)shieldDiam);
	}*/
}
