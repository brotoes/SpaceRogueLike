package game;

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