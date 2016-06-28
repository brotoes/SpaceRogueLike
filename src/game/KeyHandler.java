package game;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import static javafx.scene.input.KeyEvent.*;

public class KeyHandler implements EventHandler<KeyEvent> {
	@Override
	public void handle(KeyEvent e) {
		if (e.getEventType() == KEY_PRESSED) {
			press(e);
		} else if (e.getEventType() == KEY_RELEASED) {
			release(e);
		}
	}
	
	private void press(KeyEvent e) {
		String key = e.getText();
		Ship playerShip = World.player;
		
		if (playerShip == null) {
			return;
		}
		
		Player player = (Player)playerShip.getRemote();
		switch (key) {
		case "w":
			player.setFore(true);
			break;
		case "a":
			player.setPort(true);
			break;
		case "s":
			player.setAft(true);
			break;
		case "d":
			player.setStarboard(true);
			break;
		case " ":
			player.setBraking(true);
			break;
		case "r":
			playerShip.setAlive(true);
			playerShip.setVX(0.0);
			playerShip.setVY(0.0);
			playerShip.setPos(playerShip.getOrigin());
			playerShip.addFuel(playerShip.getFuelCapacity());
			playerShip.hull.HP = playerShip.hull.maxHP;
			break;
		case "z":
			Game.screen.getHud().toggleZoom();
			break;
		case "i":
			Game.screen.loadGui("inventoryMenu");
			Game.screen.showGui();
		}
	}
	
	public void release(KeyEvent e) {
		String key = e.getText();
		Ship playerShip = World.player;
		
		if (playerShip == null) {
			return;
		}
		
		Player player = (Player)playerShip.getRemote();
		switch (key) {
		case "w":
			player.setFore(false);
			break;
		case "a":
			player.setPort(false);
			break;
		case "s":
			player.setAft(false);
			break;
		case "d":
			player.setStarboard(false);
			break;
		case " ":
			player.setBraking(false);
			break;
		}
	}
}
