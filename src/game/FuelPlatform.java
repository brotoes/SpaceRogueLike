package game;

import gui.FuelButton;

public class FuelPlatform extends Actor {
	private double fuelRate = 50;
	private double fuelDistance = 150.0;
	private boolean fuelling = false;
	private boolean fuelPossible = false;
	private FuelButton button;
	
	public FuelPlatform(double x, double y) {
		super(x, y, "fuelPlatform");
		
		setDepth(75);
		
		sprite.addFrame("fuelPlatformActive");
		sprite.pause();
	}
	
	public void setFuelling(boolean state) {
		fuelling = state;
	}
	
	public boolean isFuelling() {
		return fuelling;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (button != null) {
			button.destroy();
		}
	}
	
	@Override
	public void tick() {
		super.tick();

		boolean prevState = fuelPossible;
		
		if (World.player.getPos().distance(getPos()) < fuelDistance) {
			fuelPossible = true;
		} else {
			fuelPossible = false;
		}
		if (fuelPossible != prevState) {
			sprite.nextFrame();
			if (fuelPossible) {
				button = new FuelButton(this);
			} else {
				fuelling = false;
				button.destroy();
				button = null;
			}
		}
		
		if (fuelling) {
			World.player.addFuel(this.fuelRate);
		}
		
		if (button != null) {
			button.tick();
		}
	}
}
