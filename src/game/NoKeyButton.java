package game;

import javafx.scene.control.Button;

public class NoKeyButton extends Button {
	@Override
	public void requestFocus() {}
	
	NoKeyButton(String text) {
		super(text);
	}
	
	NoKeyButton() {
		super();
	}
}
