package noise;

import javafx.scene.image.WritableImage;
import utils.ImageUtils;

public class FractalNoise {
	public WritableImage generate(int width, int height, int freq) {
		WritableImage image = null;
		WritableImage newImage = null;
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
