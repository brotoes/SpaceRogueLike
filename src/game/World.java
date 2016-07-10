package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import remotes.Player;
import solarSystem.SolarSystem;

import java.util.ArrayList;

public class World {

	private static boolean paused = false;
	
	public static Ship player = null;
	public static WarpGate gate;
	
	private static ObservableList<Actor> actors = FXCollections.observableArrayList();
	public static SolarSystem solarSystem;
	
	public static void generate(String fname) {
		destroy();
		/* Load solar system from XML */
		solarSystem = new SolarSystem(fname);
		spawn();
	}
	
	public static void generate() {
		destroy();
		/* Create Solar System */
		solarSystem = new SolarSystem();
		spawn();
	}
	
	public static void pause() {
		paused = true;
	}
	
	public static void unpause() {
		paused = false;
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
	/**
	 * Destroy all instances
	 */
	private static void destroy() {
		for (int i = 0; i < actors.size(); i ++) {
			actors.get(i).destroy();
		}
	}
	
	/**
	 * Spawn all instances into a new solar system
	 */
	private static void spawn() {
		int startPlanet = 1;
		/* Spawn all instances */
		World.player = new Ship("WyvernShip", 
				solarSystem.getPlanet(startPlanet).getX(),
				solarSystem.getPlanet(startPlanet).getY());
		World.player.setRemote(new Player(World.player));
		for (int i = 1; i < solarSystem.numPlanets(); i ++) {
			new FuelPlatform(
					solarSystem.getPlanet(i).getX() + 
					solarSystem.getPlanet(i).getRadius() + 100,
					solarSystem.getPlanet(i).getY());
		}
		new WarpGate(
			solarSystem.getPlanet(startPlanet).getX() - 
				solarSystem.getPlanet(startPlanet).getRadius() - 150,
			solarSystem.getPlanet(startPlanet).getY());
	}
	
	public static void tick() {
		
		/* Tick All Objects */
		for (int i = 0; i < actors.size(); i ++) {
			actors.get(i).tick();
		}
		solarSystem.tick();
	}
	
	/**
	 * insert a new actor at the correct location
	 * @param obj
	 * @return
	 */
	public static int addActor(Actor obj) {
		int index = 0;
		
		/* ensure no duplicates */
		removeActor(obj);
		
		/* Find the index to insert the new actor */
		for (int i = actors.size(); i > 0; i --) {
			if (actors.get(i - 1).getDepth() < obj.getDepth()) {
				index = i;
				break;
			}
		}
		
		actors.add(index, obj);
		
		return index;
	}

	public static void removeActor(Actor obj) {
		int index = actors.indexOf(obj);
		if (index >= 0) {
			actors.remove(index);
		}
	}
	
	/**
	 * look through list of actors, and return those actors that are ships
	 * @return
	 */
	public static ArrayList<Ship> getActiveShips() {
		ArrayList<Ship> ships = new ArrayList<Ship>();
		
		for (int i = 0; i < World.actors.size(); i ++) {
			Actor actor = World.actors.get(i);
			if (actor instanceof Ship) {
				Ship ship = (Ship)actor;
				if (ship.alive) {
					ships.add(ship);
				}
			}
		}
		
		return ships;
	}
	
	/*public static void startGame() {
		/* Set Up Objects
		solarSystem = new SolarSystem("sol");
		World.hud = new HUD();
		
		for (int i = 1; i < solarSystem.planets.length; i ++) {
			FuelPlatform fuelPlatform = new FuelPlatform(
					solarSystem.planets[i].position[0] + 
					solarSystem.planets[i].radius + 100,
					solarSystem.planets[i].position[1]);
			/*DroppedItem crate = new DroppedItem(
					Controller.system.planets[i].position[0],
					Controller.system.planets[i].position[1]);
			crate.setItem(new Reactor("BasicReactor"));
		}
		
		World.gate = new WarpGate(
				solarSystem.planets[3].position[0] - 1000,
				solarSystem.planets[3].position[1]);
		World.player = new Ship("WyvernShip", 
				solarSystem.planets[3].position[0], 
				solarSystem.planets[3].position[1]);
		Player playerRemote = new Player(World.player);
		World.player.setRemote(playerRemote);
	}*/
}