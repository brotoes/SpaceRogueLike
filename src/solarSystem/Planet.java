package solarSystem;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.image.WritableImage;
import javafx.geometry.Point3D;
import javafx.geometry.Point2D;
import java.util.Random;
import game.Actor;
import game.Game;
import game.MassiveObject;
import nameGenerator.NameGenerator;
import noise.SimplexNoise;
import noise.PerlinNoise;
import noise.Gradient;
import utils.ImageUtils;

/**
 * TODO:
 * Tiling Textures
 * Faster noise
 * Higher Resolutions
 * better clouds
 * better city lights
 * Generate normal map
 * 
 * @author brock
 */

public class Planet extends Actor implements MassiveObject {

	private final double atmoSize = 10;
	private Sphere sphere;
	private Sphere atmoSphere;
	private Group group;
	private double radius;
	private Color color;
	private Color secondaryColor;
	private Color lightColor = new Color(1.0, 0.9, 0.6, 0.8);
	private String name;
	private boolean inhabited = false;
	private boolean atmosphere = true;
	private Point3D rotAxis = new Point3D(0.0, 1.0, 0.0);
	private double cloudRotSpeed = 0.05;
	private double rotSpeed = 0.005;
	private int texWidth = 1024;
	private int texHeight = 512;
	private Gradient gradient;
	
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
		
		Game.screen.getPlanetPane().getChildren().add(group);
	}
	
	@Override
	public void tick() {
		if (atmosphere) {
			atmoSphere.setRotate(atmoSphere.getRotate() + cloudRotSpeed);
		}
		sphere.setRotate(sphere.getRotate() + rotSpeed);
		
		group.setLayoutX(getX());
		group.setLayoutY(getY());
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		Game.screen.getPlanetPane().getChildren().remove(group);
	}
	
	protected PhongMaterial getAtmoMaterial() {
		PhongMaterial mat = new PhongMaterial();
		
		SimplexNoise noise = new SimplexNoise(2048, 0.4);
		WritableImage texture = noise.getSphereMap(texWidth, texHeight, 2.0);
		
		texture = ImageUtils.filterPixels(texture, 
			new ImageUtils.ColorMap() {
				@Override
				public Color map(Color a, double x, double y) {
					double density = 0.7;
					double sharpness = 20;
					double alpha = a.getBrightness();

					alpha = 1 - Math.exp(-(alpha - density)*sharpness);
					alpha = Math.max(0.0, alpha);
					
					alpha *= 0.85;
					
					return new Color(1.0, 1.0, 1.0, alpha);
				}
			}
		);
		mat.setDiffuseMap(texture);
		
		return mat;
	}
	
	protected PhongMaterial getMaterial() {
		PhongMaterial mat = new PhongMaterial();

		/* Generate base planet */ 
		SimplexNoise noise = new SimplexNoise(2048, 0.6);
		WritableImage orig = noise.getSphereMap(1024, 512, 2.0);
		/* Generate Lighting map for cities */
		if (inhabited) {
			PerlinNoise pNoise = new PerlinNoise(texWidth, texHeight);
			WritableImage illum = pNoise.generate(texWidth, texHeight, 256);
			illum = ImageUtils.getHighContrastColoring(illum, 
					lightColor, Color.TRANSPARENT, 0.55);
			WritableImage illumMask = ImageUtils.getHighContrastColoring(
					orig, Color.BLACK, Color.TRANSPARENT);
			illum = ImageUtils.maskImage(illum, illumMask);
			
			mat.setSelfIlluminationMap(illum);
		}

		/* Generate Land and water diffuse map */
		double density = 0.50;
		double sharpness = 1;
		WritableImage texture = ImageUtils.filterPixels(orig, 
			new ImageUtils.ColorMap() {
				@Override
				public Color map(Color a, double x, double y) {
					double v = y/texHeight;
					v = Math.abs(v*2 - 1);
					return gradient.getColor(new Point2D(a.getBrightness(), v));
				}
			});
		/* Generate water Specular Map */
		WritableImage specMap = ImageUtils.filterPixels(orig,
			new ImageUtils.ColorMap() {
				@Override
				public Color map(Color a, double x, double y) {
					double map = a.getBrightness();
					map = 1 - Math.exp(-(map - density)*sharpness);
					
					if (map < 0.0) {
						return new Color(1.0, 1.0, 1.0, 0.7);
					} else {
						return Color.TRANSPARENT;
					}
				}
			});
		
		mat.setDiffuseMap(texture);
		mat.setSpecularMap(specMap);
		
		return mat;
	}
	
	protected void generatePlanet() {
		group = new Group();
		
		gradient = new Gradient();
		
		sphere = new Sphere(radius);
		sphere.setMaterial(getMaterial());
		sphere.setRotationAxis(rotAxis);
		group.getChildren().add(sphere);
		
		if (atmosphere) {
			atmoSphere = new Sphere(radius + atmoSize);
			atmoSphere.setMaterial(getAtmoMaterial());
			atmoSphere.setRotationAxis(rotAxis);
			group.getChildren().add(atmoSphere);
		}
	}
	
	public String getName() { return name; }
	
	public Color getColor() { return color;	}
	
	public double getRadius() { return this.radius;	}
	
	@Override
	public double getWellWidth() { return getRadius()*1.3; }
	
	@Override
	public double getWellDepth() {
		return Math.min(1.0, (this.radius)/15000 + 0.5);
	}
}