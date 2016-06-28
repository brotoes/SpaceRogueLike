package game;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
	
	private double lastFrameTime = 0;
	private double frameTime = 0;
	
	@Override
	public void handle(long time) {
		
		lastFrameTime = frameTime;
		frameTime = System.nanoTime();
		
		/* Tick Everything */
		World.tick();
		Game.screen.tick();
	}
	
	public double getFrameLen() {
		return frameTime - lastFrameTime;
	}
}
