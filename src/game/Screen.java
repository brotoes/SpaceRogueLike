package game;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

public class Screen {
	private Canvas bg;
	private ParallelCamera camera = new ParallelCamera();
	private Image bgImg = null;

	private StackPane pane;
	
	private SubScene camSubScene;
	private StackPane camStackPane;
	private Group lightGroup;
	private Pane planetPane;
	private Pane spritePane;
	
	private Pane overlayPane;
	private AnchorPane hudPane;
	private Pane gui = null;
	
	private HUD hud;
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	
	public Screen(int width, int height, String bgname) {
		/* Init Container Pane */
		pane = new StackPane();
		pane.setMaxSize(width, height);
		pane.setMinSize(width, height);
		pane.setBackground(null);
		
		/* Init Background Canvas */
		if (bgname != null) {
			bgname = "/resources/sprites/" + bgname + ".png";
			bgImg = new Image(bgname);
		}
		bg = new Canvas(width + bgImg.getWidth() * 2, 
				height + bgImg.getHeight() * 2 + 100);
		drawbg();
		
		/* Init camera Panes */
		camStackPane = new StackPane();
		camStackPane.setBackground(null);
		
		planetPane = new Pane();
		planetPane.setPrefSize(width, height);
		planetPane.setBackground(null);
		camStackPane.getChildren().add(planetPane);
		
		spritePane = new Pane();
		spritePane.setPrefSize(width, height);
		spritePane.setBackground(null);
		camStackPane.getChildren().add(spritePane);
		
		lightGroup = new Group();
		
		camStackPane.getChildren().add(lightGroup);
		
		camSubScene = new SubScene(camStackPane, width, height);
		camSubScene.setCamera(camera);
		
		camera.setTranslateZ(-100);
		camera.setFarClip(100000);
		
		/* Init HUD Pane */
		hudPane = new AnchorPane();
		hudPane.setMaxSize(width, height);
		hudPane.setMinSize(width, height);
		hudPane.setMouseTransparent(true);
		
		hud = new HUD(hudPane);
		
		/* init overlay pane */
		overlayPane = new Pane();
		overlayPane.setMaxSize(width, height);
		overlayPane.setMinSize(width, height);
		overlayPane.setBackground(null);
		
		/* Init Menu Pane */
		loadGui("mainMenu");
		
		/* Add All Children */
		pane.getChildren().add(bg);
		pane.getChildren().add(camSubScene);
		pane.getChildren().add(overlayPane);
		pane.getChildren().add(hudPane);
		pane.getChildren().add(gui);
	}
	
	public void loadGui(String fname) {
		Pane oldGui = gui;
		
		fname = "/resources/fxml/" + fname + ".fxml";
		try {
			System.out.println(fname);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fname));
			gui = loader.load();
			gui.setBackground(null);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		/* Check if gui is already placed in Pane. if yes, replace it */
		int index = pane.getChildren().indexOf(oldGui);
		if (index > -1) {
			pane.getChildren().remove(index);
			pane.getChildren().add(index, gui);
		}
		
	}
	
	public void showGui() {
		World.pause();
		gui.setVisible(true);
		gui.setMouseTransparent(false);
	}
	
	public void hideGui() {
		World.unpause();
		gui.setVisible(false);
		gui.setMouseTransparent(true);
	}
	
	public void tick() {
		hud.tick();
	}
	
	public void drawbg() {
		/* Draw Background Image */
		GraphicsContext gc = bg.getGraphicsContext2D();
		if (bgImg != null) {
			for (int y = 0; y < bg.getHeight(); y += bgImg.getHeight()) {
				for (int x = 0; x < bg.getWidth(); x += bgImg.getWidth()) {
					gc.drawImage(bgImg, x, y);
				}
			}
		}
	}
	
	public HUD getHud() {
		return hud;
	}
	
	public Pane getOverlayPane() {
		return overlayPane;
	}
	
	public Pane getSpritePane() {
		return spritePane;
	}
	
	public StackPane getPane() {
		return pane;
	}
	
	public ParallelCamera getCamera() {
		return camera;
	}
	
	public Point2D getCamMousePos() {
		Point2D mousePos = Game.mhandler.getMousePos();
		
		return mousePos.add(getCamPos());
	}
	
	public Point2D getCamPos() {
		return new Point2D(camera.getTranslateX(), camera.getTranslateY());
	}
	
	public double getCamX() {
		return camera.getTranslateX();
	}
	
	public double getCamY() {
		return camera.getTranslateY();
	}
	
	public void setCamPos(Point2D pos) {
		camera.setTranslateX(pos.getX() - pane.getWidth()/2);
		camera.setTranslateY(pos.getY() - pane.getHeight()/2);
		
		/** Set BG Position with parallax */
		bg.setTranslateX(-(pos.getX()/10 % bgImg.getWidth()));
		bg.setTranslateY(-(pos.getY()/10 % bgImg.getHeight()));
	}
	
	/**
	 * Add sprite to list of sprites and to spritePane
	 * at index according to sprite depth. Remove sprite from
	 * sprite list and pane first to ensure no duplicates
	 * @param sprite
	 */
	public void addSprite(Sprite sprite) {
		int index = 0;
	
		removeSprite(sprite);
		
		/* Find index for new sprite */
		for (int i = sprites.size(); i > 0; i --) {
			if (sprites.get(i - 1).getDepth() < sprite.getDepth()) {
				index = i;
				break;
			}
		}
		
		sprites.add(index, sprite);
		spritePane.getChildren().add(index, sprite.getView());
	}
	
	public Group getLightGroup() {
		return lightGroup;
	}
	
	/**
	 * Get the pane on which background stuff, such as planets,
	 * are drawn
	 * @return
	 */
	public Pane getPlanetPane() {
		return planetPane;
	}
	
	/**
	 * Remove sprite from list of sprites, and from spritePane
	 * @param sprite
	 */
	public void removeSprite(Sprite sprite) {
		if (sprites.indexOf(sprite) > -1) {
			sprites.remove(sprite);
			spritePane.getChildren().remove(sprite.getView());
		}
	}
}
