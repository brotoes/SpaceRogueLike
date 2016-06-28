package game;

import org.w3c.dom.Node;

public class Reactor extends InventoryItem {
	
	double rate;
	double capacity;
	double charge = 0.0;
	
	public Reactor(String xmlFName) {
		super("reactors/" + xmlFName);
		this.updateBlockSprite("reactorBlock");
	}
	
	@Override
	protected void parseAttribute(Node attr) {
		switch(attr.getNodeName()) {
		case "capacity":
			this.capacity = Double.parseDouble(attr.getTextContent().trim());
			break;
		case "rate":
			this.rate = Double.parseDouble(attr.getTextContent().trim());
			
		default:
			super.parseAttribute(attr);
		}
	}
	
	@Override
	public void tick() {
		this.addCharge(this.rate);
	}
	
	public void addCharge(double charge) {
		this.charge += charge;
		if (this.charge > this.capacity) {
			this.charge = this.capacity;
		}
	}
	
	public double getCharge() {
		return this.charge;
	}
}
