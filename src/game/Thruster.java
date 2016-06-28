package game;

import javafx.geometry.Point2D;
import javafx.scene.transform.Translate;
import javafx.scene.transform.Rotate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.FileUtils;

public class Thruster {
	String spriteLocation = "/resources/sprites/jet.png";
	Sprite sprite = null;
	
	String name;
	double mass;
	double maxThrust = 1.0f;
	double thrustState = 0.0;
	
	private double destThrustState = 0.0;
	private double thrustChangeRate = 0.1;
	private double efficiency = 0.0;
	private Ship host;
	private ThrusterSlot slot;
	
	public Thruster(ThrusterSlot slot, String thrusterFName) {
		/*this.vector[0] = x;
		this.vector[1] = y;
		this.maxThrust = thrust;*/
		Document thrusterXML;
		NodeList thrusterAttrs;
		
		this.slot = slot;
		this.host = slot.host;
		
		thrusterFName = "/resources/components/thrusters/" + thrusterFName + ".xml";
		thrusterXML = FileUtils.openXML(thrusterFName);
		thrusterAttrs = thrusterXML.getElementsByTagName("info");
		if (thrusterAttrs != null) {
			thrusterAttrs = thrusterAttrs.item(0).getChildNodes();
			for (int i = 0; i < thrusterAttrs.getLength(); i ++) {
				Node thrusterAttr = thrusterAttrs.item(i);
				switch (thrusterAttr.getNodeName()) {
				case "name":
					this.name = thrusterAttr.getTextContent();
					break;
				case "mass":
					this.mass = Double.parseDouble(thrusterAttr.getTextContent());
					break;
				case "thrust":
					maxThrust = Double.parseDouble(thrusterAttr.getTextContent());
					break;
				case "sprite":
					this.spriteLocation = "/resources/sprites/" + 
							thrusterAttr.getTextContent() + ".png";
					break;
				case "efficiency":
					this.efficiency = Double.parseDouble(thrusterAttr.getTextContent().trim());
				}
			}
		}
		
		//this.sprite = Sprite.createFromMeta(this, "animJet");
	}
	
	public void setThrust(double thrust) {
		this.destThrustState = thrust;
		if (thrust > 1.0) {
			this.destThrustState = 1.0;
		}

		if (this.host.getFuelAvailable() < this.thrustState) {
			this.thrustState = 0.0;
		}
	}
	
	public double getFuelUse() {
		return this.thrustState * this.efficiency;
	}
	
	public Point2D getVector() {
		return this.slot.getVector();
	}
	
	public Point2D getThrustVector() {
		double x = this.getVector().getX() * this.maxThrust * this.thrustState;
		double y = this.getVector().getY() * this.maxThrust * this.thrustState;
		Point2D tv = new Point2D(x, y);
		
		return tv;
	}
	
	public double getX() {
		return getPos().getX();
	}
	
	public double getY() {
		return getPos().getY();
	}
	
	public Point2D getPos() {
		Point2D result;
		Rotate rotate = new Rotate(Math.toDegrees(host.getTheta()));
		Translate trans = new Translate(host.getX(), host.getY());
		
		result = slot.getOffset();
		result = rotate.transform(result);
		result = trans.transform(result);
		
		return result;
	}
	
	public double getTheta() {
		double finalTheta = host.getTheta() + slot.getTheta();
		return finalTheta;
	}
	
	public void tick() {
		if (sprite != null) {
			sprite.tick();
		}
		if (this.thrustState < this.destThrustState) {
			this.thrustState += this.thrustChangeRate;
			if (this.thrustState > this.destThrustState) {
				this.thrustState = this.destThrustState;
			}
		} else if (this.thrustState > this.destThrustState) {
			this.thrustState = this.destThrustState;
		}
	}
}