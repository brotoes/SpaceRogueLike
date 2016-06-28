package game;

import javafx.scene.paint.Color;
import javafx.scene.PointLight;

public class Star extends Planet {
	PointLight light;
	
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
		light = new PointLight();
		light.setColor(Color.WHITE);
		light.setTranslateZ(-10000);
		Game.screen.getLightGroup().getChildren().add(light);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		light.setTranslateZ(-10000);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		//TODO: remove light
	}
	
	@Override
	protected void generatePlanet() {
		//TODO: Generate a pretty planet
	}
}