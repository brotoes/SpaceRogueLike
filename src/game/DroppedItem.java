package game;

public class DroppedItem extends Actor {
	private InventoryItem item;
	
	public DroppedItem(double x, double y) {
		super(x, y, "crate");
	}
	
	public void setItem(InventoryItem item) {
		this.item = item;
	}
	
	@Override
	public void tick() {
		if (this.isColliding(World.player)/*	&& this.alive*/) {
			World.player.addToInventory(this.item);
			this.destroy();
		}
	}
}
