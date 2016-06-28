package gui;

import game.Game;
import game.World;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WarpMenuController {
	@FXML protected void warpTo(ActionEvent event) {
		World.generate();
		Game.screen.hideGui();
	}
	
	@FXML protected void cancel(ActionEvent event) {
		Game.screen.hideGui();
	}
}