package gui;

import game.FuelPlatform;
import javafx.event.ActionEvent;

public class FuelButton extends ActorButton {
	String[] texts = {"Fuel Up", "Stop"};
	
	public FuelButton(FuelPlatform host) {
		super(host);
		button.setText(texts[0]);
	}
	
	/**
	 * Handle the button click
	 */
	@Override
	public void handle(ActionEvent event) {
		((FuelPlatform)host).setFuelling(!((FuelPlatform)host).isFuelling());
		
		if (((FuelPlatform)host).isFuelling()) {
			button.setText(texts[1]);
		} else {
			button.setText(texts[0]);
		}
		
		super.handle(event);
	}
}