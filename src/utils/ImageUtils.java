package utils;

import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;

public class ImageUtils {
	/**
	 * Returns an image modified to be applied to a sphere
	 */
	public static WritableImage mapToSphere(WritableImage img) {
		WritableImage result = new WritableImage((int)img.getWidth(), (int)img.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader pixels = img.getPixelReader();
		
		for (int y = 0; y < result.getHeight(); y ++) {
			for (int x = 0; x < result.getWidth(); x++) {
				double xT = MathUtils.map(x, 0.0, img.getWidth() - 1, 0.0, 1.0);
				double yT = MathUtils.map(y, 0.0, img.getHeight() - 1, 0.0, 1.0);
				double u = (int)(xT/(xT*xT + yT*yT + 1));
				double v = (int)(yT/(xT*xT + yT*yT + 1));
				
				System.out.println("" + u + ", " + v);
				
				int fromX = (int)(u*(img.getWidth() - 1));
				int fromY = (int)(v*(img.getHeight() - 1));
				writer.setColor(x, y, pixels.getColor(fromX, fromY));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a overlayed on b using the mean function
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static WritableImage getAverage(WritableImage a, WritableImage b) {
		WritableImage result = new WritableImage((int)a.getWidth(), 
				(int)a.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader aPixels = a.getPixelReader();
		PixelReader bPixels = b.getPixelReader();
		
		for (int y = 0; y < a.getHeight(); y ++) {
			for (int x = 0; x < a.getWidth(); x ++) {
				Color color = getAverageColor(
						aPixels.getColor(x, y), 
						bPixels.getColor(x, y));
				writer.setColor(x, y, color);
			}
		}
		
		return result;
	}
	
	/**
	 * returns the average color of two colors
	 */
	public static Color getAverageColor(Color a, Color b) {
		Color result = new Color(
				(a.getRed() + b.getRed())/2,
				(a.getGreen() + b.getGreen())/2,
				(a.getBlue() + b.getBlue())/2,
				(a.getOpacity() + b.getOpacity())/2);
		
		return result;
	}
	
	/**
	 * Returns an image based on img, where each pixel is 
	 * made to be either color a or b
	 * 
	 * @param img
	 * @param a
	 * @param b
	 * @return
	 */
	public static WritableImage getHighContrastColoring(WritableImage img, Color a, Color b, double pivot) {
		WritableImage result = new WritableImage((int)img.getWidth(), (int)img.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader reader = img.getPixelReader();
		
		for (int y = 0; y < img.getHeight(); y ++) {
			for (int x = 0; x < img.getWidth(); x ++) {
				Color color = reader.getColor(x, y);
				if (color.getBrightness() > pivot) {
					writer.setColor(x, y, a);
				} else {
					writer.setColor(x, y, b);
				}
			}
		}
		
		return result;
	}
	
	public static WritableImage getHighContrastColoring(WritableImage img, Color a, Color b) {
		return getHighContrastColoring(img, a, b, 0.5);
	}
	
	/**
	 * Assigns the smaller of mask's and image's alpha values to image
	 * @param image
	 * @param mask
	 */
	public static WritableImage maskImage(WritableImage image, WritableImage mask) {
		WritableImage result = new WritableImage((int)image.getWidth(), (int)image.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader imagePixels = image.getPixelReader();
		PixelReader maskPixels = mask.getPixelReader();
		
		for (int y = 0; y < image.getHeight(); y ++) {
			for (int x = 0; x < image.getWidth(); x ++) {
				Color imageColor = imagePixels.getColor(x, y);
				double value = Math.min(imageColor.getOpacity(),
						maskPixels.getColor(x, y).getOpacity());
				Color color = new Color(imageColor.getRed(), imageColor.getGreen(),
						imageColor.getBlue(), value);
				writer.setColor(x, y, color);
			}
		}
		
		return result;
	}
	
	/**
	 * Interpolate between colors
	 */
	public static Color lerpColor(Color a, Color b, double t) {
		return new Color(
				(1-t)*a.getRed() + t*b.getRed(),
				(1-t)*a.getGreen() + t*b.getGreen(),
				(1-t)*a.getBlue() + t*b.getBlue(),
				(1-t)*a.getOpacity() + t*b.getOpacity()
			);
	}
	
	/**
	 * Add colors together. Channels clipped at 1
	 */
	public static Color addColors(Color a, Color b) {
		return new Color(
				Math.min(a.getRed() + b.getRed(), 1.0),
				Math.min(a.getGreen() + b.getGreen(), 1.0),
				Math.min(a.getBlue() + b.getBlue(), 1.0),
				Math.min(a.getOpacity() + b.getOpacity(), 1.0)
			);
	}
	
	/**
	 * multiply all channels by a factor. 
	 */
	public static Color multiplyColor(Color a, double factor) {
		return new Color(
				a.getRed() * factor,
				a.getGreen() * factor,
				a.getBlue() * factor,
				a.getOpacity() * factor
			);
	}
	
	/**
	 * Applys a filter to each pixel according to function provided
	 */
	public static interface ColorMap {
		public Color map(Color a, double x, double y);
	}
	
	public static WritableImage filterPixels(WritableImage img, ColorMap f ) {
		WritableImage result = new WritableImage((int)img.getWidth(), (int)img.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader pixels = img.getPixelReader();
		
		for (int y = 0; y < img.getHeight(); y ++) {
			for (int x = 0; x < img.getWidth(); x ++) {
				Color color = pixels.getColor(x, y);
				writer.setColor(x, y, f.map(color, x, y));
			}
		}
		
		return result;
	}
	
	public static WritableImage getSphereTexture(WritableImage noise) {
		WritableImage result = new WritableImage((int)noise.getWidth(), (int)noise.getHeight());
		PixelWriter writer = result.getPixelWriter();
		PixelReader pixels = noise.getPixelReader();
		
		for (int y = 0; y < noise.getHeight(); y++) {
			double theta = Math.PI * (y - (noise.getHeight()-1)/2.0) / noise.getHeight()-1;
			for (int x = 0; x < noise.getWidth(); x ++) {
		        double phi  = Math.PI*2 * (x - noise.getWidth()/2.0) / noise.getWidth();
		        double phi2 = phi * Math.cos(theta);
		        int fromX  = (int)(phi2 * noise.getWidth() / (Math.PI*2) + noise.getWidth()/2);
		        Color col;
		        if (fromX < 0 || fromX > noise.getWidth()) {
		            col = Color.RED;                         /* Should not happen */
		        } else {
		            col = pixels.getColor(fromX, y);
		        }
				
				writer.setColor(x, y, col);
			}	
		}
		return result;
	}
	
	/**
	 * Generates a sphere texture from 3D random noise
	 */
	public static WritableImage getSphereTexture(double[][][] noise, int width, int height) {
		WritableImage result = new WritableImage(width, height);
		PixelWriter writer = result.getPixelWriter();
		
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x ++) {
				double u = (double)x/width;
				double v = (double)y/height;
				
				double lat = u * 2 * Math.PI;
				double lng = v * 2 * Math.PI;
				
				double xt = Math.sin(lng)*Math.cos(lat);
				double yt = Math.sin(lng)*Math.sin(lat);
				double zt = Math.cos(lng);
				
				xt = MathUtils.map(xt, -1.0, 1.0, 0.0, noise.length - 1);
				yt = MathUtils.map(yt, -1.0, 1.0, 0.0, noise.length - 1);
			    zt = MathUtils.map(zt, -1.0, 1.0, 0.0, noise.length - 1);
				
				//System.out.printf("%f %f\n", lat, lng);
				//System.out.printf("%d %d => %d %d %d\n", x, y, dX, dY, dZ);
				
			    /* get remainders to weight values */
			    double xrem = xt - Math.floor(xt);
			    double yrem = yt - Math.floor(yt);
			    double zrem = zt - Math.floor(zt);
			    
			    /* Weights of all adjacent cells */
			    
			    /* Values of all adjacent cells */
			    double brf = noise[(int)Math.ceil(zt)][(int)Math.ceil(yt)][(int)Math.ceil(xt)];
			    double blf = noise[(int)Math.ceil(zt)][(int)Math.ceil(yt)][(int)Math.floor(xt)];
			    double tlf = noise[(int)Math.ceil(zt)][(int)Math.floor(yt)][(int)Math.floor(xt)];
			    double trf = noise[(int)Math.ceil(zt)][(int)Math.floor(yt)][(int)Math.ceil(xt)];
			    double brb = noise[(int)Math.floor(zt)][(int)Math.ceil(yt)][(int)Math.ceil(xt)];
			    double blb = noise[(int)Math.floor(zt)][(int)Math.ceil(yt)][(int)Math.floor(xt)];
			    double tlb = noise[(int)Math.floor(zt)][(int)Math.floor(yt)][(int)Math.floor(xt)];
			    double trb = noise[(int)Math.floor(zt)][(int)Math.floor(yt)][(int)Math.ceil(xt)];
			    
			    /* weighted average of all adjacent cells */
				double val = 0.0;
				val += brf*xrem*yrem*zrem;
				val += blf*(1-xrem)*yrem*zrem;
				val += tlf*(1-xrem)*(1-yrem)*zrem;
				val += trf*xrem*(1-yrem)*zrem;
				val += brb*xrem*yrem*(1-zrem);
				val += blb*(1-xrem)*yrem*(1-zrem);
				val += tlb*(1-xrem)*(1-yrem)*(1-zrem);
				val += trb*xrem*(1-yrem)*(1-zrem);
						
				//val /= 8;
				
				writer.setColor(x, y, new Color(val,val,val,1.0));
			}
		}
		
		return result;
	}
}