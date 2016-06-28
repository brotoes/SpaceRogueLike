package gui;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import game.Actor;
import game.Game;
import javafx.event.ActionEvent;
import javafx.scene.effect.Glow;

public class ActorButton implements EventHandler<ActionEvent> {
	private Pane pane;
	protected NoKeyButton button = new NoKeyButton();
	protected Point2D offset = new Point2D(-75.0, -75.0);
	protected Actor host;
	private double startTime;
	private double radius;
	private double animTime = 500;
	private Point2D centre;
	private Canvas canvas;
	
	ActorButton(Actor host) {
		/* set up interface element */
		this.host = host;
		pane = Game.screen.getOverlayPane();
		pane.getChildren().add(button);
		button.addEventFilter(ActionEvent.ACTION, this);
		button.relocate(
				host.getX() + offset.getX() - Game.screen.getCamX(), 
				host.getY() + offset.getY() - Game.screen.getCamY());
		
		/* Set up flourish */
		radius = offset.magnitude();
		canvas = new Canvas(radius*2 + 100, radius*2 + 100);
		startTime = System.currentTimeMillis();
		pane.getChildren().add(0, canvas);
		centre = new Point2D(canvas.getWidth()/2, canvas.getHeight()/2);
		canvas.relocate(
				host.getX() - canvas.getWidth()/2 - Game.screen.getCamX(), 
				host.getY() - canvas.getHeight()/2 - Game.screen.getCamY());
	}
	
	/**
	 * Every tick, update the button position according to camera and 
	 * host location
	 */
	public void tick() {
		/* Move Interface elements */
		button.relocate(
			host.getX() + offset.getX() - Game.screen.getCamX(), 
			host.getY() + offset.getY() - Game.screen.getCamY());
		canvas.relocate(
				host.getX() - canvas.getWidth()/2 - Game.screen.getCamX(), 
				host.getY() - canvas.getHeight()/2 - Game.screen.getCamY());
		
		/* Draw Flourish */
		double age = System.currentTimeMillis() - startTime;
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		g.setStroke(Game.screen.getHud().getActiveColor());
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		/* Draw expanding arc around the object */
		double arcRad = radius - 50;
		g.strokeArc(
				centre.getX() - arcRad, 
				centre.getY() - arcRad, 
				arcRad*2, arcRad*2,
				offset.angle(new Point2D(1, 0)),
				-300.0*(Math.min(animTime, age)/animTime),
				ArcType.OPEN);
		
		g.applyEffect(new Glow(0.5));
	}
	
	/**
	 * removes all associated interface elements from Screen
	 */
	public void destroy() {
		pane.getChildren().remove(button);
		pane.getChildren().remove(canvas);
	}
	
	/**
	 * Handle the button click
	 */
	@Override
	public void handle(ActionEvent event) {
		event.consume();
	}
}