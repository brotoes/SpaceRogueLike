package game;

import javafx.event.ActionEvent;

public class FuelButton extends ActorButton {
	String[] texts = {"Fuel Up", "Stop"};
	
	FuelButton(FuelPlatform host) {
		super(host);
		button.setText(texts[0]);
	}
	
	/**
	 * Handle the button click
	 */
	@Override
	public void handle(ActionEvent event) {
		((FuelPlatform)host).fuelling = !((FuelPlatform)host).fuelling;
		
		if (((FuelPlatform)host).fuelling) {
			button.setText(texts[1]);
		} else {
			button.setText(texts[0]);
		}
		
		super.handle(event);
	}
}