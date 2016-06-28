package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
   @FXML protected void newGame(ActionEvent event) {
	   Game.screen.hideGui();
	   Game.startGame();
   }
}