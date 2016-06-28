package game;

import org.w3c.dom.Node;

public class FuelTank extends InventoryItem {
	
	double fuelCapacity;
	double fuel;
	
	public FuelTank(String xmlFName) {
		super("fuelTanks/" + xmlFName);
		this.updateBlockSprite("fuelBlock");
	}
	
	@Override
	protected void parseAttribute(Node attr) {
		switch(attr.getNodeName()) {
		case "capacity":
			this.fuelCapacity = Double.parseDouble(attr.getTextContent().trim());
			this.fuel = this.fuelCapacity;
			break;
		default:
			super.parseAttribute(attr);
		}
	}
}
