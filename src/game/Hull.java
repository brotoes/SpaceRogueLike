package game;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.FileUtils;
import javafx.geometry.Point2D;

public class Hull {
	
	String name;
	String sprite;
	
	double mass;
	double HP;
	double maxHP;
	double scale = 1.0;
	double rotSpeed = -1.0;
	double[] trailX = {0.0, 0.0};
	double[] trailY = {0.0, 0.0};
	
	ThrusterSlot[] thrusterSlots;
	public boolean[][] shape;
	public Ship host;
	
	public Hull(Ship host, String hullName) {
		Document hullXML;
		NodeList hullInfo;
		String hullFName = "/resources/components/hulls/" + hullName + ".xml";
		this.host = host;
		
		hullXML = FileUtils.openXML(hullFName);

		/*Get general hull info */
		hullInfo = hullXML.getElementsByTagName("info");
		if (hullInfo == null) {
			return;
		}
		hullInfo = hullInfo.item(0).getChildNodes();
		String[] nums;
		for (int i = 0; i < hullInfo.getLength(); i ++) {
			switch (hullInfo.item(i).getNodeName()) {
			case "name":
				this.name = hullInfo.item(i).getTextContent().trim();
				break;
			case "mass":
				this.mass = Double.parseDouble(hullInfo.item(i).getTextContent().trim());
				break;
			case "sprite":
				this.sprite = hullInfo.item(i).getTextContent().trim();
				break;
			case "hp":
				this.maxHP = Double.parseDouble(hullInfo.item(i).getTextContent().trim());
				this.HP = maxHP;
				break;
			case "scale":
				this.scale = Double.parseDouble(hullInfo.item(i).getTextContent().trim());
				break;
			case "rotSpeed":
				this.rotSpeed = Double.parseDouble(hullInfo.item(i).getTextContent().trim());
				break;
			case "trailX":
				nums = hullInfo.item(i).getTextContent().split(",");
				this.trailX = new double[nums.length];
				for (int j = 0; j < nums.length; j ++) {
					this.trailX[j] = Double.parseDouble(nums[j].trim());
				}
				break;
			case "trailY":
				nums = hullInfo.item(i).getTextContent().split(",");
				this.trailY = new double[nums.length];
				for (int j = 0; j < nums.length; j ++) {
					this.trailY[j] = Double.parseDouble(nums[j].trim());
				}
				break;
			}
		}
		
		/* Get list of thruster slots */
		NodeList thrusterSlotNodes = hullXML.getElementsByTagName("thruster");
		if (thrusterSlotNodes == null) {
			return;
		}
		this.thrusterSlots = new ThrusterSlot[thrusterSlotNodes.getLength()];
		
		for (int i = 0; i < thrusterSlotNodes.getLength(); i ++) {
			String type = "main";
			Point2D vector = new Point2D(1.0, 0.0);
			Point2D offset = new Point2D(0.0, 0.0);
			Node thrusterSlotNode = thrusterSlotNodes.item(i);
			NodeList slotAttrs = thrusterSlotNode.getChildNodes();
			
			/* Get Slot Info */
			double x;
			double y;
			for (int j = 0; j < slotAttrs.getLength(); j ++) {
				Node slotAttr = slotAttrs.item(j);
				switch (slotAttr.getNodeName()) {
				case "type":
					type = slotAttr.getTextContent();
					break;
				case "vector":
					x = Double.parseDouble(slotAttr.getTextContent().split(",")[0].trim());
					y = Double.parseDouble(slotAttr.getTextContent().split(",")[1].trim());
					vector = new Point2D(x, y);
					break;
				case "offset":
					x = Double.parseDouble(slotAttr.getTextContent().split(",")[0].trim());
					y = Double.parseDouble(slotAttr.getTextContent().split(",")[1].trim());
					offset = new Point2D(x, y);
				}
			}
			this.thrusterSlots[i] = new ThrusterSlot(this.host, type, vector, offset);
			
			/* configure equipment screen */
			NodeList rows = hullXML.getElementsByTagName("row");
			if (rows == null) {
				return;
			}
			shape = new boolean[rows.getLength()][];
			for (int row = 0; row < rows.getLength(); row ++) {
				char[] rowChars = rows.item(row).getTextContent().trim().toCharArray();
				shape[row] = new boolean[rowChars.length];
				for (int c = 0; c < rowChars.length; c ++) {
					switch(rowChars[c]) {
					case '0':
						shape[row][c] = false;
						break;
					case '1':
						shape[row][c] = true;
						break;
					}
				}
			}
		}
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public double getRotSpeed() {
		return this.rotSpeed;
	}
}
