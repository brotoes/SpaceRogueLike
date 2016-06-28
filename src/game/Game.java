package game;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import static javafx.scene.input.MouseEvent.*;
import static javafx.scene.input.KeyEvent.*;

public class Game extends Application {
	
	private final static int WIDTH = 1280;
	private final static int HEIGHT = 800;
	
	public static Screen screen;
	private static GameLoop gloop = new GameLoop();
	private static Scene scene;
	public static MouseHandler mhandler = new MouseHandler();
	private static KeyHandler khandler = new KeyHandler();
	
	private static double startTime;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		/** Configure JavaFX */
		screen = new Screen(WIDTH, HEIGHT,"starfield");
		scene = new Scene(screen.getPane());
		
		primaryStage.setTitle("Game");
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		startTime = System.nanoTime();
	}
	
	public static void startGame() {
		/* Configure Events */
		scene.addEventHandler(MOUSE_MOVED, mhandler);
		scene.addEventHandler(MOUSE_PRESSED, mhandler);
		scene.addEventHandler(MOUSE_RELEASED, mhandler);
		scene.addEventHandler(MOUSE_DRAGGED, mhandler);
		
		scene.addEventHandler(KEY_PRESSED, khandler);
		scene.addEventHandler(KEY_RELEASED, khandler);
		
		/* Set up Game */
		World.generate("sol");
		
		/* Start Everything */
		gloop.start();
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	public static double getGameTime() {
		return System.nanoTime() - startTime;
	}
	
	/** Returns the length of the last frame in seconds */
	public static double getFrameLen() {
		return gloop.getFrameLen()/1000000000.0;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
}
