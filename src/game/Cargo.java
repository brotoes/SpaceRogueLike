package game;

public class Cargo {
	
	private String name;
	private int units;
	private double massPerUnit;
	
	public Cargo() {
		/* TODO Choose cargo from db of precious materials (gold, dilithium etc) */
		this.name = "Gold";
		this.units = 1;
		this.massPerUnit = 0.5;
	}
	
	public double getMass() {
		return this.units*this.massPerUnit;
	}
	
	public int getUnits() {
		return this.units;
	}
	
	public String getName() {
		return this.name;
	}
}
