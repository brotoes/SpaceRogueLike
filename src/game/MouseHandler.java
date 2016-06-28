package game;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;

public class MouseHandler implements EventHandler<MouseEvent> {
	
	Point2D mousePos = new Point2D(0, 0);
	
	@Override
	public void handle(MouseEvent e) {
		mousePos = new Point2D(e.getSceneX(), e.getSceneY());
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			World.player.getRemote().setShooting(true);
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			World.player.getRemote().setShooting(false);
		}
	}
	
	public Point2D getMousePos() {
		return mousePos;
	}
	
	public double getMouseX() {
		return mousePos.getX();
	}
	
	public double getMouseY() {
		return mousePos.getY();
	}
}
