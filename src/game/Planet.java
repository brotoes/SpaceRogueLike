package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import java.util.Random;
import nameGenerator.NameGenerator;

public class Planet extends Actor implements MassiveObject {

	private Sphere sphere;
	private double radius;
	private Color color;
	private Color secondaryColor;
	private String name;
	
	public Planet(SolarSystem host) {
		super(0, 0);
		Random rand = new Random();
		NameGenerator nameGen = new NameGenerator("RomanCity");
		name = nameGen.nextName();
		
		radius = rand.nextDouble()/2.0 + 0.5;
		radius *= 500;
		color = new Color(rand.nextDouble(), rand.nextDouble(), 
				rand.nextDouble(), 1.0);
		secondaryColor = new Color(rand.nextDouble(), rand.nextDouble(), 
				rand.nextDouble(), 1.0);
		host.spacetime.add(this);
	}
	
	public Planet(SolarSystem host, String name, double x, double y, 
			double radius, Color color, Color color2) {
		super(x, y);
		this.name = name;
		this.radius = radius;
		this.color = color;
		this.secondaryColor = color2;
		
		generatePlanet();
		
		host.spacetime.add(this);
		
		sphere = new Sphere(radius);
		sphere.setMaterial(getMaterial());
		Game.screen.getPlanetPane().getChildren().add(sphere);
	}
	
	@Override
	public void tick() {
		sphere.setLayoutX(getX());
		sphere.setLayoutY(getY());
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		Game.screen.getPlanetPane().getChildren().remove(sphere);
	}
	
	private PhongMaterial getMaterial() {
		PhongMaterial mat = new PhongMaterial(color);
		
		mat.setSpecularColor(Color.WHITE);
		
		return mat;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	@Override
	public double getWellWidth() {
		return this.getRadius()*1.3;
	}
	
	@Override
	public double getWellDepth() {
		return Math.min(1.0, (this.radius)/15000 + 0.5);
	}
	
	protected void generatePlanet() {
		//TODO: Generate a pretty planet
	}
}