package gui;

import game.Game;
import game.WarpGate;
import javafx.event.ActionEvent;

public class GateButton extends ActorButton {	
	public GateButton(WarpGate host) {
		super(host);
		button.setText("Warp");
	}
	
	/**
	 * Handle the button click
	 */
	@Override
	public void handle(ActionEvent event) {
		super.handle(event);

		Game.screen.loadGui("warpMenu");
		Game.screen.showGui();
	}
}