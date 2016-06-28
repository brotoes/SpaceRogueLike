package game;

public class DroppedCargo extends Actor {
	private Cargo cargo;
	
	public DroppedCargo(double x, double y) {
		super(x, y, "crate");
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	@Override
	public void tick() {
		if (this.isColliding(World.player)/*	&& this.alive*/) {
			World.player.addToCargo(this.cargo);
			this.destroy();
		}
	}
}
