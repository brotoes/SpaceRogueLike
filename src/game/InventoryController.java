package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InventoryController {
   @FXML protected void close(ActionEvent event) {
	   Game.screen.hideGui();
   }
}