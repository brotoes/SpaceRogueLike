package solarSystem;

import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import game.Game;
import game.World;
import javafx.scene.PointLight;

public class Star extends Planet {
	Group lightGroup;
	
	public Star(SolarSystem host) {
		super(host);
		addLight();
	}
	
	public Star(SolarSystem host, String name, double x, double y, 
			double radius, Color color, Color color2) {
		super(host, name, x, y, radius, color, color2);
		addLight();
	}
	
	private void addLight() {
		lightGroup = new Group();
		for (int i = 0; i < 1;i ++) {
			 PointLight light = new PointLight(Color.WHITE);
			 light.setTranslateZ(-100*i);
			 lightGroup.getChildren().add(light);
		}
		Game.screen.getLightGroup().getChildren().add(lightGroup);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		double dist = getPos().distance(World.player.getPos());
		double z = -(getRadius() + dist*2);
		
		lightGroup.setTranslateZ(z);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		//TODO: remove light
	}
	
	@Override
	protected PhongMaterial getAtmoMaterial() {
		Color atmoColor = getColor().brighter();
		atmoColor = new Color(atmoColor.getRed(), 
				atmoColor.getGreen(), atmoColor.getBlue(), 0.3);
		PhongMaterial mat = new PhongMaterial(atmoColor);
		
		Canvas illumMapCanvas = new Canvas(10, 10);
		GraphicsContext g = illumMapCanvas.getGraphicsContext2D();
		g.setFill(getColor());
		g.fillRect(0, 0, 10, 10);
		WritableImage selfIllumMap = illumMapCanvas.snapshot(null, null);
		mat.setSelfIlluminationMap(selfIllumMap);
		
		return mat;
	}
	
	@Override
	protected PhongMaterial getMaterial() {
		PhongMaterial mat = new PhongMaterial(getColor());
		
		Canvas illumMapCanvas = new Canvas(10, 10);
		GraphicsContext g = illumMapCanvas.getGraphicsContext2D();
		g.setFill(new Color(0.5, 0.5, 0.5, 1.0));
		g.fillRect(0, 0, 10, 10);
		WritableImage selfIllumMap = illumMapCanvas.snapshot(null, null);
		mat.setSelfIlluminationMap(selfIllumMap);
		
		//mat.setSpecularColor(Color.WHITE);
		
		return mat;
	}
	
	@Override
	protected void generatePlanet() {
		super.generatePlanet();
		//TODO: Generate a pretty planet
	}
}