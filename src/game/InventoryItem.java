package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.FileUtils;

public class InventoryItem {
	
	BufferedImage sprite = null;
	public BufferedImage blockSprite = null;
	String spriteLocation;
	String blockSpriteLocation;
	String name;
	
	boolean[][] shape;
	
	Ship host = null;
	
	double mass;
	
	public InventoryItem(String xmlFName) {
		xmlFName = "/resources/components/" + xmlFName + ".xml";
		Document itemXML;
		NodeList itemAttrs;
		
		/* Read in info attributes */
		itemXML = FileUtils.openXML(xmlFName);
		itemAttrs = itemXML.getElementsByTagName("info");
		if (itemAttrs != null) {
			itemAttrs = itemAttrs.item(0).getChildNodes();
			for (int i = 0; i < itemAttrs.getLength(); i ++) {
				parseAttribute(itemAttrs.item(i));
			}
		}
		
		/* Load Sprite */
		
		/* Load Inventory Shape */
		NodeList rows = itemXML.getElementsByTagName("row");
		if (rows != null) {
			this.shape = new boolean[rows.getLength()][];
			for (int row = 0; row < rows.getLength(); row ++) {
				char[] chars = rows.item(row).getTextContent().trim().toCharArray();
				this.shape[row] = new boolean[chars.length];
				for (int c = 0; c < chars.length; c ++) {
					if (chars[c] == '0') {
						this.shape[row][c] = false;
					} else {
						this.shape[row][c] = true;
					}
				}
			}
		}
	}
	
	protected void updateBlockSprite(String fname) {
		this.spriteLocation = "/resources/sprites/" + fname + ".png";
		//load sprite
		try {
			InputStream is = this.getClass().getResourceAsStream(this.spriteLocation);
			this.blockSprite = ImageIO.read(is);
		} catch (IOException e) {
			System.err.print("Error Reading Sprite File: ");
			System.err.println(e.getMessage());
		}
	}
	
	public void tick() {}
	public void draw() {}
	/* called when this item is equipped */
	public void equip() {}
	public void unequip() {}
	
	protected void parseAttribute(Node attr) {
		switch(attr.getNodeName()) {
		case "name":
			this.name = attr.getTextContent().trim();
			break;
		case "mass":
			this.mass = Double.parseDouble(attr.getTextContent().trim());
			break;
		case "sprite":
			this.spriteLocation = "/resources/sprites/" +
					attr.getTextContent().trim() + ".png";
			break;
		}
	}
}
