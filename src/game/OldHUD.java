package game;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class OldHUD {
	
	int respawnCount = 0;
	int asteroidsKilled = 0;
	String deathText = "You Are Dead. Press 'R' to Respawn.";
	
	double[] scannerLocation = new double[2];
	double[] scannerCenter = new double[2];
	double[] scannerSize = {200, 200};
	long[] scannerRadiusRange = {10000, 100000};
	long scannerRadius = 100000;
	long zoomSpeed = 5000;
	boolean zoom = true;
	
	BufferedImage speedoBack; 
	double speedoRad = 120.0;
	int speedoDivs = 10;
	int speedoStartAngle = -15;
	int speedoArcAngle = 120;
	//double[] speedoPos = {-10, World.canvas.height - 120.0 - speedoRad};
	double[] speedoPos = {-10, 0.0};
	
	BufferedImage scannerImg;
	Graphics2D scannerg;
	
	double[] gaugeLocation = new double[2];
	double[] gaugeSize = {200, 10};
	
	public OldHUD() {
		//this.scannerLocation[0] = World.camera.cameraSize[0] - (this.scannerSize[0] + 50);
		//this.scannerLocation[1] = World.camera.cameraSize[1] - (this.scannerSize[1] + 50);
		this.scannerCenter[0] = this.scannerSize[0]/2;
		this.scannerCenter[1] = this.scannerSize[1]/2;
		this.gaugeLocation[0] = this.scannerLocation[0];
		this.gaugeLocation[1] = this.scannerLocation[1] - this.gaugeSize[1] - 10;
		
		scannerImg = new BufferedImage((int)this.scannerSize[0], 
				(int)this.scannerSize[1], BufferedImage.TYPE_INT_ARGB);
		scannerg = scannerImg.createGraphics();
		
		/* Generate Speedometer Background Image */
		speedoBack = new BufferedImage((int)speedoRad*2 + 50, (int)speedoRad*2 + 50,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D sg = speedoBack.createGraphics();
		sg.setColor(Color.CYAN);
		sg.drawArc(0, 50, (int)speedoRad*2 - 1, (int)speedoRad*2 - 1,
				speedoStartAngle, speedoArcAngle);
		sg.fillOval((int)speedoRad - 3, (int)speedoRad - 3 + 50, 6, 6 );
		double arcAngle = Math.toRadians(speedoArcAngle);
		double startAngle = Math.toRadians(speedoStartAngle);
		double divAngle = arcAngle/speedoDivs;
		for(int i = 0; i <= speedoDivs; i ++) {
			double outerDivX = Math.sin(startAngle + divAngle*(speedoDivs - i));
			double outerDivY = -Math.cos(startAngle + divAngle*(speedoDivs - i));
			double innerDivX = outerDivX*((int)speedoRad - 10) + (int)speedoRad;
			double innerDivY = outerDivY*((int)speedoRad - 10) + (int)speedoRad + 50;
			outerDivX = outerDivX*(int)speedoRad + (int)speedoRad;
			outerDivY = outerDivY*(int)speedoRad + (int)speedoRad + 50;
			
			sg.drawLine((int)outerDivX, (int)outerDivY, 
					(int)innerDivX, (int)innerDivY);
			String divString = "" + i;
			sg.drawString(divString, (int)outerDivX + 5, (int)outerDivY - 5);
			sg.drawString("x0.1c", (int)speedoRad + 20, (int)speedoRad + 40);
		}
	}
	
	public void drawGauge(Graphics g, double fill, double max, int x, int y, int width, int height) {
		double percent = fill/max;
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		if (percent < 0.2) {
			g.setColor(Color.RED);
		} else if (percent < 0.5) {
			g.setColor(Color.ORANGE);
		} else if (percent >= 0.95) {
			g.setColor(Color.GREEN);
		}
		g.fillRect(x, y, (int)(width*percent), height);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x, y, width, height);
		g.setColor(Color.WHITE);
		/*g.drawString("" + (int)Controller.player.getFuelAvailable() + "/" +
				(int)Controller.player.getFuelCapacity(),
				(int)this.gaugeLocation[0], (int)this.gaugeLocation[1] - 13);*/
	}
	
	public void draw() {
		/* Draw Text */
		//String text = String.format("Asteroids Destroyed: %d  Respawns: %d", asteroidsKilled, respawnCount);
		//Graphics g = World.canvas.graphics;
		
		//g.setColor(Color.WHITE);
		//g.drawChars(text.toCharArray(), 0, text.length(), 50, Controller.canvas.height - 50);
		/*
		if (!World.player.alive) {
			g.drawChars(this.deathText.toCharArray(), 0, this.deathText.length(), 100, 290);
		}
		
		/* Draw Gauges
		this.drawGauge(g, World.player.getFuelAvailable(), 
				World.player.getFuelCapacity(), (int)this.gaugeLocation[0], 
				(int)this.gaugeLocation[1], (int)this.gaugeSize[0], 
				(int)this.gaugeSize[1]);
		this.drawGauge(g, World.player.getChargeAvailable(), 
				World.player.getChargeCapacity(), (int)this.gaugeLocation[0], 
				(int)this.gaugeLocation[1] - 15, (int)this.gaugeSize[0], 
				(int)this.gaugeSize[1]);
		this.drawGauge(g, World.player.getShieldCharge(), 1.0, 
				(int)this.gaugeLocation[0], 
				(int)this.gaugeLocation[1] - 30, (int)this.gaugeSize[0], 
				(int)this.gaugeSize[1]);
		this.drawGauge(g, World.player.hull.HP, World.player.hull.maxHP, 
				(int)this.gaugeLocation[0], 
				(int)this.gaugeLocation[1] - 45, (int)this.gaugeSize[0], 
				(int)this.gaugeSize[1]);
		
		/* Draw Velocity 
		/*
		g.drawString(String.format("Velocity: %fc\n",
				Controller.player.getVelocity()/Controller.c),
				50, Controller.canvas.height - 50);
		g.setColor(Color.CYAN);
		g.drawImage(speedoBack, (int)speedoPos[0], (int)speedoPos[1], null);
		
		double velocity = World.player.getVelocity();
		double arcAngle = Math.toRadians(speedoArcAngle);
		double startAngle = Math.toRadians(speedoStartAngle);
		double divAngle = arcAngle/speedoDivs;
		double pinX = Math.sin(startAngle + divAngle*(speedoDivs - velocity*10));
		double pinY = -Math.cos(startAngle + divAngle*(speedoDivs - velocity*10));
		pinX = pinX*speedoRad;
		pinY = pinY*speedoRad;
		
		g.drawLine((int)(speedoPos[0] + speedoRad), 
				(int)(speedoPos[1] + speedoRad + 50),
				(int)(pinX + speedoPos[0] + speedoRad), 
				(int)(pinY + speedoPos[1] + speedoRad + 50));
		
		/* Draw Scanner
		if (this.zoom && this.scannerRadius < this.scannerRadiusRange[1]) {
			this.scannerRadius += this.zoomSpeed;
		} else if (!this.zoom && this.scannerRadius > this.scannerRadiusRange[0]) {
			this.scannerRadius -= this.zoomSpeed;
		}
		double scannerScale = (int)(this.scannerRadius/
				Math.max(this.scannerSize[0], this.scannerSize[1]));
		
		scannerg.setColor(Color.GRAY);
		scannerg.fillRect(0, 0,	scannerImg.getWidth() - 1, scannerImg.getHeight() - 1);
		scannerg.setColor(Color.DARK_GRAY);
		scannerg.drawRect(0, 0, scannerImg.getWidth() - 1, scannerImg.getHeight() - 1);
		/* Draw Scanner Contents
		for (int i = 0; i < World.system.planets.length; i ++) {
			Planet planet = World.system.planets[i];
			scannerg.setColor(planet.color);
			if (planet.color == Color.GRAY) {
				scannerg.setColor(Color.DARK_GRAY);
			}
			int x = (int)((planet.position[0] - World.player.getX())/scannerScale);
			int y = (int)((planet.position[1] - World.player.getY())/scannerScale);
			int size = Math.max((int)(planet.radius/scannerScale), 3);
			//if (Math.abs(x) < this.scannerSize[0]/2 && Math.abs(y) < this.scannerSize[1]/2) {
			scannerg.fillOval((int)this.scannerCenter[0] + x - size, 
					(int)this.scannerCenter[1] + y - size, 
					size*2, size*2);
			//}
		}
		scannerg.setColor(Color.DARK_GRAY);
		scannerg.drawLine((int)this.scannerCenter[0] - 3, (int)this.scannerCenter[1],
				(int)this.scannerCenter[0] + 3, (int)this.scannerCenter[1]);
		scannerg.drawLine((int)this.scannerCenter[0], (int)this.scannerCenter[1] - 3,
				(int)this.scannerCenter[0], (int)this.scannerCenter[1] + 3);
		
		g.drawImage(scannerImg, (int)scannerLocation[0], (int)scannerLocation[1], null);
		*/
	}
}
