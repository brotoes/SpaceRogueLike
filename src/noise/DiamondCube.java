package noise;

import java.util.Random;

import utils.MathUtils;

/**
 * Generates Noise in Three Dimensions using the diamond square algorithm
 * adapted to 3D
 * @author brock
 *
 */
public class DiamondCube {
	/**
	 * generates noise with values between 0 and 1
	 * 
	 * Size must be 2^n + 1
	 * @param size
	 * @return
	 */
	public double[][][] generate(int size) {
		double[][][] result = new double[size][size][size];
		double seed = 0.0;
		
		/* Seed the eight corners */
		result[0][0][0] = seed;
		result[0][0][size - 1] = seed;
		result[0][size - 1][0] = seed;
		result[0][size - 1][size - 1] = seed;
		result[size - 1][0][0] = seed;
		result[size - 1][0][size - 1] = seed;
		result[size - 1][size - 1][0] = seed;
		result[size - 1][size - 1][size - 1] = seed;
		
		step(result, 0, 0, 0, size, 1);
		
		double curMin = 0.0;
		double curMax = 0.0;
		
		for (int z = 0; z < size; z ++) {
			for (int y = 0; y < size; y ++) {
				for (int x = 0; x < size; x ++) {
					if (result[z][y][x] > curMax) {
						curMax = result[z][y][x];
					} else if (result[z][y][x] < curMin) {
						curMin = result[z][y][x];
					}
				}
			}
		}
		for (int z = 0; z < size; z ++) {
			for (int y = 0; y < size; y ++) {
				for (int x = 0; x < size; x ++) {
					result[z][y][x] = MathUtils.map(
							result[z][y][x], curMin, curMax, 0.0, 1.0);
				}
			}
		}
		
		return result;
	} 

	private double sumCorners(double[][][] cube, int sx, int sy, int sz, int size) {
		int ex = sx + size - 1;
		int ey = sy + size - 1;
		int ez = sz + size - 1;
		double sum = 0.0;
		
		sum += cube[sz][sy][sx];
		sum += cube[sz][sy][ex];
		sum += cube[sz][ey][sx];
		sum += cube[sz][ey][ex];
		sum += cube[ez][sy][sx];
		sum += cube[ez][sy][ex];
		sum += cube[ez][ey][sx];
		sum += cube[ez][ey][ex];
		
		return sum;
	}
	
	private void step(double[][][] result, int sx, int sy, int sz, int size, int step) {
		Random rand = new Random();
		/* Perform Diamond Step */
		int mx = sx + size/2;
		int my = sy + size/2;
		int mz = sz + size/2;
		double sum = sumCorners(result, sx, sy, sz, size);
		
		result[mz][my][mx] = sum/8 + rand.nextDouble()/step;
		/* TEST DELETE */
		result[mz][my][mx] = step;
		
		/* Perform Square/Cube Step */
		int ex = sx + size - 1;
		int ey = sy + size - 1;
		int ez = sz + size - 1;
		sum += result[mz][my][mx];
		sum /= 9;
		
		result[sz][my][mx] = sum + rand.nextDouble()/step;
		result[ez][my][mx] = sum + rand.nextDouble()/step;
		result[mz][sy][mx] = sum + rand.nextDouble()/step;
		result[mz][ey][mx] = sum + rand.nextDouble()/step;
		result[mz][my][sx] = sum + rand.nextDouble()/step;
		result[mz][my][ex] = sum + rand.nextDouble()/step;
		
		result[sz][sy][mx] = sum + rand.nextDouble()/step;
		result[sz][ey][mx] = sum + rand.nextDouble()/step;
		result[ez][sy][mx] = sum + rand.nextDouble()/step;
		result[ez][ey][mx] = sum + rand.nextDouble()/step;
		result[sz][my][sx] = sum + rand.nextDouble()/step;
		result[sz][my][ex] = sum + rand.nextDouble()/step;
		result[ez][my][sx] = sum + rand.nextDouble()/step;
		result[ez][my][ex] = sum + rand.nextDouble()/step;
		result[mz][sy][sx] = sum + rand.nextDouble()/step;
		result[mz][sy][ex] = sum + rand.nextDouble()/step;
		result[mz][ey][sx] = sum + rand.nextDouble()/step;
		result[mz][ey][ex] = sum + rand.nextDouble()/step;
		
		if (size > 3) {
			step(result, sx, sy, sz, size/2 + 1, step + 1);
			step(result, sx, sy, mz, size/2 + 1, step + 1);
			step(result, sx, my, sz, size/2 + 1, step + 1);
			step(result, sx, my, mz, size/2 + 1, step + 1);
			step(result, mx, sy, sz, size/2 + 1, step + 1);
			step(result, mx, sy, mz, size/2 + 1, step + 1);
			step(result, mx, my, sz, size/2 + 1, step + 1);
			step(result, mx, my, mz, size/2 + 1, step + 1);
		}
	}
	
}
