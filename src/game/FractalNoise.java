package game;

import java.awt.image.BufferedImage;

import utils.ImageUtils;

public class FractalNoise {
	public static BufferedImage generate(int width, int height, int freq) {
		BufferedImage image = null;
		BufferedImage newImage = null;
		PerlinNoise noise;
		
		while (freq > 1) {
			noise = new PerlinNoise(width, height);
			newImage = noise.generate(width, height, freq);
			if (image != null) {
				image = ImageUtils.getAverage(newImage, image);
			} else {
				image = newImage;
			}
			
			freq /= 2;
		}
		
		return image;
	}
}
