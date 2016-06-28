package game;

public class Trail {
	int x1, y1, x2, y2;
	boolean inAtmo = false;
	
	public Trail(double x1, double y1, double x2, double y2) {
		this.x1 = (int)x1;
		this.y1 = (int)y1;
		this.x2 = (int)x2;
		this.y2 = (int)y2;
		
		//double[] start = {x1, y1};
		//double[] end = {x2, y2};
		/*for (int i = 0; i < World.solarSystem.planets.length; i ++) {
			Planet planet = World.solarSystem.planets[i];
			double[] planetPos = planet.position;
			if (MathUtils.distance(start, planetPos) < planet.radius ||
					MathUtils.distance(end, planetPos) < planet.radius) {
				this.inAtmo = true;
				break;
			}
		}*/
	}
	
	/*public void draw(int age) {
		if (!this.inAtmo) {
			return;
		}
		
		double[] offset = World.camera.cameraPos;
		
		Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f - (age/10.0f));
		World.canvas.graphics.setColor(color);
		World.canvas.graphics.drawLine(x1 - (int)offset[0], y1 - (int)offset[1], 
				x2 - (int)offset[0], y2 - (int)offset[1]);
	}*/
}
