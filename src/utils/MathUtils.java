package utils;

import java.util.Random;

import game.*;

public class MathUtils {
	/**
	 * Maps x in range a-b to range c-d
	 * @return
	 */
	public static double map(double x, double a, double b, double c, double d) {
		return (x-a)/(b-a) * (d-c) + c;
	}
	
	public static double[] linearCombination(double[] desiredVector, double[][] vectors, int vectorLength, int numVectors, boolean allowNegative) {
		boolean[] usable = new boolean[numVectors];
		double[][] usableMatrix = new double[numVectors][vectorLength];
		int curVector;

		if (allowNegative) {
			for (int i = 0; i < numVectors; i ++) {
				usable[i] = true;
			}
		} else {
			for (int i = 0; i < numVectors; i ++) {
				System.out.println("Signs");
				System.out.println(Math.signum(vectors[i][0]));
				System.out.println(Math.signum(vectors[i][1]));
				System.out.println("");
				if ((Math.signum(vectors[i][0]) == Math.signum(desiredVector[0])
						&& Math.signum(vectors[i][0]) != 0) ||
						Math.signum(vectors[i][1]) == Math.signum(desiredVector[1])
						&& Math.signum(vectors[i][1]) != 0) {
					usable[i] = true;
				} else {
					usable[i] = false;
				}
			}
		}
		
		for (int i = 0; i < numVectors; i ++) {
			System.out.print(usable[i]);
		}
		System.out.println("");
		
		curVector = 0;
		for (int i = 0; i < numVectors; i ++) {
			if (usable[i]) {
				usableMatrix[curVector] = vectors[i];
				curVector ++;
			}
		}
		
		return dotProduct(usableMatrix, desiredVector);
	}
	
	public static double[] dotProduct(double[][] matrix, double[] vector) {
		double[] result = new double[vector.length];
		
		for (int i = 0; i < vector.length; i ++) {
			result[i] = 0;
			for (int j = 0; j < matrix[i].length; j ++) {
				result[i] += vector[i]*matrix[i][j];
				System.out.println(matrix[i][j]);
			}
			System.out.println(" ");
		}
		
		return result;
	}
	
	public static double distance(Actor a, Actor b) {
		return a.getPos().distance(b.getPos());
	}
	
	public static double distance(double[] a, double[] b) {
		return Math.sqrt((a[0] - b[0])*(a[0] - b[0]) + (a[1] - b[1])*(a[1] - b[1]));
	}
	
	public static double length(double x, double y) {
		double[] a = {0.0, 0.0};
		double[] b = {x, y};
		
		return distance(a, b);
	}
	
	public static double length(double[] u) {
		return length(u[0], u[1]);
	}
	
	public static double[] normalize(double[] u) {
		double len = length(u);
		u[0] /= len;
		u[1] /= len;
		
		return u;
	}
	
	public static double randomDouble(double min, double max) {
		Random rand = new Random();
		return min + (max - min) * rand.nextDouble();
	}
	
	public static int posMod(int a, int b) {
		return (a % b + b) % b;
	}
}
