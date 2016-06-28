package game;

import javafx.event.ActionEvent;

public class GateButton extends ActorButton {	
	GateButton(WarpGate host) {
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