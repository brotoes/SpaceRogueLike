package solarSystem;

import org.w3c.dom.*;
import utils.FileUtils;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.ArrayList;

public class SolarSystem {
	ArrayList<Planet> planets;
	SpaceTime spacetime = new SpaceTime();
	
	/**
	 * Generate a random Solar system
	 */
	public SolarSystem() {
		Random rand = new Random();
		/* Choose number of each type of planet */
		int numStars = rand.nextInt(3) + 4;
		int numSmall = rand.nextInt(4) + 2;
		int numGiant = rand.nextInt(4) + 2;
		double curX = 0;
		
		planets = new ArrayList<Planet>();
		
		/* Generate Stars */
		for (int i = 0; i < numStars; i ++) {
			Point2D pos;
			Rotate rotate;
			double theta;
			Planet planet = new Planet(this);
			
			planets.add(planet);
			
			pos = new Point2D(curX, 0.0);
			theta = rand.nextDouble() * 360.0;
			
			rotate = new Rotate(theta);
			pos = rotate.transform(pos);
			planet.setPos(pos);
			
			curX += planet.getRadius();
		}
	}
	
	/**
	 * Read premade solar system from XML
	 * @param config
	 */
	public SolarSystem(String config) {
		Document systemXML;
		NodeList bodies;
		double curX = 0;
		
		/* Read planets from file and generate */
		config = "/resources/components/solarSystems/" + config + ".xml";
		systemXML = FileUtils.openXML(config);
		
		bodies = systemXML.getElementsByTagName("bodies");
		if (bodies.getLength() == 0) {
			return;
		}
		bodies = bodies.item(0).getChildNodes();
		planets = new ArrayList<Planet>();
		for (int i = 0; i < bodies.getLength(); i ++) {
			///AffineTransform xform = new AffineTransform();
			Node body = bodies.item(i);
			NodeList bodyAttrs = body.getChildNodes();
			String name = null;
			String type = null;
			//String sprite = null;
			Color color = Color.BLACK;
			Color color2 = Color.BLACK;
			double radius = 0;
			Point2D pos;
			Rotate rotate;
			double theta;
			Random rand = new Random();

			type = body.getNodeName().toLowerCase().trim();
			
			for (int j = 0; j < bodyAttrs.getLength(); j ++) {
				switch (bodyAttrs.item(j).getNodeName()) {
				case "name":
					name = bodyAttrs.item(j).getTextContent();
					break;
				case "color":
					try {
						color = Color.valueOf(bodyAttrs.item(j).getTextContent().trim());
					} catch (Exception e) {
					    color = Color.BLACK;
					}
					break;
				case "secondaryColor":
					try {
					    color2 = Color.valueOf(bodyAttrs.item(j).getTextContent().trim());
					} catch (Exception e) {
					    color2 = Color.BLACK;
					}
					break;
				case "size":
					radius = Double.parseDouble(bodyAttrs.item(j).getTextContent());
					break;
				case "sprite":
					//sprite = bodyAttrs.item(j).getTextContent().trim();
					break;
				}
			}
			
			if (i != 0) {
				curX += radius;
			}
			
			pos = new Point2D(curX, 0.0);
			theta = rand.nextDouble() * 360.0;
			
			rotate = new Rotate(theta);
			pos = rotate.transform(pos);
			
			Planet nextPlanet = null;
			
			if (type == null || type == "planet") {
				nextPlanet = new Planet(this, name, 
						pos.getX(), pos.getY(), radius, color, color2);
			} else if (type == "star") {
				nextPlanet = new Star(this, name, 
						pos.getX(), pos.getY(), radius, color, color2);
			}
			if (nextPlanet != null) {
				planets.add(nextPlanet);
			}
			curX += radius + radius * 0.1;
		}
	}
	
	public Planet getPlanet(int index) {
		return planets.get(index);
	}
	
	public int numPlanets() {
		return planets.size();
	}
	
	public void tick() {
		for (int i = 0; i < planets.size(); i ++) {
			planets.get(i).tick();
		}
	}
}
