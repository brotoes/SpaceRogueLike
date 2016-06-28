package game;

public class FuelPlatform extends Actor {
	double fuelRate = 50;
	double fuelDistance = 150.0;
	boolean fuelling = false;
	boolean fuelPossible = false;
	FuelButton button;
	
	public FuelPlatform(double x, double y) {
		super(x, y, "fuelPlatform");
		
		setDepth(75);
		
		sprite.addFrame("fuelPlatformActive");
		sprite.pause();
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
