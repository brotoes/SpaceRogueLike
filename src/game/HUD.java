package game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import solarSystem.Planet;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;

public class HUD {
	private Pane sensorPane;
	private Canvas sensorCanvas;
	private Pane gaugesPane;
	private Canvas gaugesCanvas;
	private double sensorSize = 200;
	private double[] sensorRadiusRange = {10000, 50000};
	private double zoomSpeed = 0.05;
	private double zoom = 1.0;
	private boolean isZoomed = false;
	
	/** gauge variables */
	private double barInset = 50;
	private double barWidth = sensorSize - barInset;
	private double barHeight = 15;
	private double curBarY = 0;
	
	
	private Color hudFillColor = new Color(0.1, 0.4, 0.9, 0.4);
	private Color hudActiveColor = new Color(0.1, 0.4, 1.0, 1.0);
	private Color hudBorderColor = Color.WHITE;
	
	public HUD(AnchorPane pane) {
		pane.setPadding(new Insets(50, 50, 50, 50));
		/** Create Panes */
		sensorPane = new Pane();
		sensorPane.setMinSize(sensorSize, sensorSize);
		sensorPane.setMaxSize(sensorSize, sensorSize);
		
		gaugesPane = new Pane();
		gaugesPane.setMinSize(sensorSize, sensorSize);
		gaugesPane.setMaxSize(sensorSize, sensorSize);
		
		AnchorPane.setBottomAnchor(sensorPane, 0.0);
		AnchorPane.setRightAnchor(sensorPane, 0.0);
		pane.getChildren().add(sensorPane);
		
		AnchorPane.setBottomAnchor(gaugesPane, sensorSize);
		AnchorPane.setRightAnchor(gaugesPane, 0.0);
		pane.getChildren().add(gaugesPane);
		
		sensorCanvas = new Canvas(sensorSize, sensorSize);
		gaugesCanvas = new Canvas(sensorSize, sensorSize);
		
		sensorCanvas.setClip(new Circle(sensorSize/2, sensorSize/2, sensorSize/2));
		sensorPane.getChildren().add(sensorCanvas);
		
		gaugesPane.getChildren().add(gaugesCanvas);
	}
	
	public void tick() {
		if (isZoomed && zoom > 0.0) {
			zoom -= zoomSpeed;
		} else if (!isZoomed && zoom < 1.0) {
			zoom += zoomSpeed;
		}
		drawSensor(sensorCanvas);
		drawGauges(gaugesCanvas);
	}
	
	public void toggleZoom() {
		isZoomed = !isZoomed;
	}
	
	private double getSensorRadius() {
		return (sensorRadiusRange[1] - sensorRadiusRange[0]) * zoom + sensorRadiusRange[0];
	}
	
	private void drawFillBar(Canvas canvas, double x, double y, 
			double width, double height, double fill) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setStroke(hudBorderColor);
		g.setFill(hudFillColor);
		g.fillRect(x, y, width, height);
		g.setFill(hudActiveColor);
		g.fillRect(x, y, width * fill, height);
		g.strokeRect(x, y, width, height);
		g.strokeRect(x, y, width * fill, height);
	}
	
	private void nextGauge(Canvas canvas, String label, double fill) {	
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(hudBorderColor);
		g.fillText(label, 0, curBarY + barHeight);
		drawFillBar(canvas, barInset, curBarY, barWidth, barHeight, fill);
		
		curBarY += barHeight + 5;
	}
	
	private void drawGauges(Canvas canvas) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		double fuelFill = World.player.getFuelAvailable()/World.player.getFuelCapacity();
		double powerFill = World.player.getChargeAvailable()/World.player.getChargeCapacity();
		double speedFill = World.player.getVelocity();
		double shieldFill = World.player.getShieldCharge();
		
		g.clearRect(0, 0, sensorSize, sensorSize);
		
		nextGauge(canvas, "FUEL", fuelFill);
		nextGauge(canvas, "POWER", powerFill);
		nextGauge(canvas, "SHIELD", shieldFill);
		nextGauge(canvas, "SPEED", speedFill);

		gaugesPane.setMinHeight(curBarY + barHeight);
		gaugesPane.setMaxHeight(curBarY + barHeight);
		
		curBarY = 0;
		g.applyEffect(new Glow(0.5));
	}
	
	private void drawSensor(Canvas canvas) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		double scaleFactor = sensorSize/getSensorRadius();
		Point2D sensorPlayerPos = World.player.getPos().multiply(scaleFactor);

		g.setFill(hudFillColor);
		g.setStroke(hudBorderColor);
		g.clearRect(0, 0, sensorSize, sensorSize);
		g.fillRect(0, 0, sensorSize, sensorSize);
		
		for (int i = 0; i < World.solarSystem.numPlanets(); i ++) {
			Planet planet = World.solarSystem.getPlanet(i);
			double drawRadius = planet.getRadius() * scaleFactor;
			Point2D drawPos = planet.getPos().multiply(scaleFactor);
			drawPos = drawPos.subtract(sensorPlayerPos);
			
			g.setFill(hudActiveColor);
			g.fillOval(drawPos.getX() - drawRadius + sensorSize/2, 
					drawPos.getY() - drawRadius + sensorSize/2, 
					drawRadius*2, drawRadius*2);
			g.strokeOval(drawPos.getX() - drawRadius + sensorSize/2, 
					drawPos.getY() - drawRadius + sensorSize/2, 
					drawRadius*2, drawRadius*2);
		}
		
		g.strokeOval(0.5, 0.5, sensorSize - 1, sensorSize - 1);
		double mid = sensorSize/2;
		g.strokeLine(mid, mid - 5, mid, mid + 5);
		g.strokeLine(mid - 5, mid, mid + 5, mid);
		
		g.applyEffect(new Glow(0.5));
	}
	
	public Color getFillColor() {
		return hudFillColor;
	}
	
	public Color getActiveColor() {
		return hudActiveColor;
	}
	
	public Color getBorderColor() {
		return hudBorderColor;
	}
}
