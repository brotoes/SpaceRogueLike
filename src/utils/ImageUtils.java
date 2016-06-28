package utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.Color;

public class ImageUtils {
	public static BufferedImage getAverage(BufferedImage a, BufferedImage b) {
		BufferedImage result = new BufferedImage(a.getWidth(), b.getHeight(), a.getType());
		WritableRaster aRaster = a.getRaster();
		WritableRaster bRaster = b.getRaster();
		
		for (int y = 0; y < a.getHeight(); y ++) {
			for (int x = 0; x < a.getWidth(); x ++) {
				for (int channel = 0; channel < 3; channel ++) {
					float sum = aRaster.getSample(x, y, channel) + bRaster.getSample(x, y, channel);
					result.getRaster().setSample(x, y, channel, sum/2);
				}
			}
		}
		
		return result;
	}
	
	public static BufferedImage getHighContrastColoring(BufferedImage img, Color a, Color b) {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		
		for (int y = 0; y < img.getHeight(); y ++) {
			for (int x = 0; x < img.getWidth(); x ++) {
				int[] pixel = new int[4];
				img.getRaster().getPixel(x, y, pixel);
				if ((pixel[0] + pixel[1] + pixel[2])/3 > 128) {
					pixel[0] = a.getRed();
					pixel[1] = a.getGreen();
					pixel[2] = a.getBlue();
				} else {
					pixel[0] = b.getRed();
					pixel[1] = b.getGreen();
					pixel[2] = b.getBlue();
				}
				result.getRaster().setPixel(x, y, pixel);
			}
		}
		
		return result;
	}
	
	public static void maskImage(BufferedImage image, BufferedImage mask) {
		for (int y = 0; y < image.getHeight(); y ++) {
			for (int x = 0; x < image.getWidth(); x ++) {
				double value = mask.getAlphaRaster().getSampleDouble(x, y, 0);
				image.getAlphaRaster().setSample(x, y, 0, value);
			}
		}
	}
}
