package game;

import gui.GateButton;

public class WarpGate extends Actor {
	boolean active = false;
	GateButton button;
	
	public WarpGate(double x, double y) {
		super(x, y, "gateInactive");
		sprite.addFrame("gateActive");
		sprite.pause();
		//TODO: Remove when the art is good
		sprite.getView().setScaleX(4.0);
		sprite.getView().setScaleY(4.0);
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
		
		/* Check if the player is close */
		boolean near = this.distanceFrom(World.player) < 
				this.getSize() + World.player.getSize();
		
		/* activate when the player approaches, deactivate
		 * when the player leaves		
		 */
		if (active && !near) {
			sprite.nextFrame();
			active = false;
			button.destroy();
		} else if (!active && near) {
			sprite.nextFrame();
			active = true;
			button = new GateButton(this);
		}
		
		if (button != null) {
			button.tick();
		}
	}
}