package noise;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.ImageUtils;

public class Gradient {
	private ArrayList<ColorPoint> points = new ArrayList<ColorPoint>();
	private boolean print = false;
	
	public Gradient() {
		/* Corners */
		add(new ColorPoint(0, 0, Color.DARKBLUE));
		add(new ColorPoint(0, 1, Color.DARKBLUE));
		add(new ColorPoint(1, 1, Color.WHITE));
		add(new ColorPoint(1, 0, Color.DARKGREEN));
		
		
		add(new ColorPoint(1, 0.8, Color.DARKGREEN));
		add(new ColorPoint(0.4, 0.5, Color.BLUE));
		add(new ColorPoint(0.7, 0.5, Color.DARKGREEN));
		add(new ColorPoint(0.5, 0.5, Color.LIGHTGREEN));
		
		add(new ColorPoint(0.4, 0.2, Color.BLUE));
		add(new ColorPoint(0.7, 0.2, Color.DARKGREEN));
		add(new ColorPoint(0.5, 0.2, Color.LIGHTGREEN));
	}
	
	public void add(ColorPoint point) {
		points.add(point);
	}
	
	public Color getColor(double x, double y) {
		return getColor(new Point2D(x, y));
	}
	
	/**
	 * Interpolates between all points using inverse distance weighting
	 * @param pos
	 * @return
	 */
	public Color getColor(Point2D pos) {
		double[] invDists = new double[points.size()];
		Color result = Color.TRANSPARENT;
		double total = 0.0;
		
		for (int i = 0; i < points.size(); i ++) {
			invDists[i] = 1/Math.pow(pos.distance(points.get(i).getPos()), 4);
			total += invDists[i];
			if (print) {
				System.out.println(""+ i + ", " +  invDists[i]);
			}
		}
		
		for (int i = 0; i < points.size(); i ++) {
			if (Double.isInfinite(invDists[i])) {
				result = points.get(i).getColor();
				break;
			}
			result = ImageUtils.addColors(result,
					ImageUtils.multiplyColor(points.get(i).getColor(),
							invDists[i]/total));
		}
		
		return result;
	}
}
