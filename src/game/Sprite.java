package game;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import utils.FileUtils;

public class Sprite {
	
	public String name = "";
	
	private Actor host;
	private ImageView view = new ImageView();
	private ArrayList<Image> frames = new ArrayList<Image>();
	private int curFrame = 0;
	
	private boolean playing = true;
	
	public static Sprite createFromMeta(Actor host, String fname) {
		Sprite sprite = new Sprite(host);
		sprite.name = fname;
		Document spriteXML;
		NodeList frames;
		String spriteFName = "/resources/sprites/meta/" + fname.trim() + ".xml";
		
		spriteXML = FileUtils.openXML(spriteFName);
		
		/* Get list of frames */
		frames = spriteXML.getElementsByTagName("frame");
		if (frames == null) {
			return sprite;
		}
		for (int i = 0; i < frames.getLength(); i ++) {
			sprite.addFrame(frames.item(i).getTextContent().trim());
		}
		
		sprite.setFrame(0);
		
		return sprite;
	}
	
	public Sprite(Actor host, String fname) {
		name = fname;
		this.host = host;
		addFrame(fname);
		setFrame(0);
		place();
	}
	
	public Sprite(Actor host, Image img) {
		this.host = host;
		addFrame(img);
		setFrame(0);
		place();
	}
	
	private Sprite(Actor host) {
		this.host = host;
		view.setLayoutX(host.getX());
		view.setLayoutY(host.getY());
		place();
	}
	
	/**
	 * Place the sprite on Screen's spritePane
	 */
	public void place() {
		Game.screen.addSprite(this);
	}
	
	/**
	 * Remove the sprite from World's list of ships and from 
	 * Screen's spritePane
	 */
	public void destroy() {
		Game.screen.removeSprite(this);
		view.setVisible(false);
	}
	
	/**
	 * return this host's depth
	 * @return
	 */
	public int getDepth() {
		return host.getDepth();
	}
	
	/**
	 * set the ImageView to display the frame at the specified index
	 * @param frame
	 */
	public void setFrame(int frame) {
		curFrame = frame;
		view.setImage(frames.get(curFrame));
	}
	
	public int addFrame(String fname) {
		fname = "/resources/sprites/" + fname + ".png";
		
		Image frame = new Image(fname);
		
		return addFrame(frame);
	}
	
	public int addFrame(Image frame) {
		int index = this.frames.size();
		frames.add(frame);
		
		return index;
	}
	
	public ImageView getView() {
		return view;
	}
	
	public double getWidth() {
		Image frame = frames.get(this.curFrame);
		return frame.getWidth();
	}
	
	public double getHeight() {
		Image frame = frames.get(this.curFrame);
		return frame.getHeight();
	}
	
	public void pause() {
		this.playing = false;
	}
	
	public void unpause() {
		this.playing = true;
	}
	
	public void nextFrame() {
		curFrame = (curFrame + 1) % frames.size();
		setFrame(curFrame);
	}

	public void tick() {
		if (playing) {
			nextFrame();
		}
		/* Change position to host's */
		view.relocate(host.getX() - getWidth()/2, host.getY() - getHeight()/2);
		view.setRotate(Math.toDegrees(host.getTheta()));
	}
}
