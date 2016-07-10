package noise;

import utils.MathUtils;
import java.util.Random;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class SimplexNoise {

    SimplexNoiseOctave[] octaves;
    double[] frequencys;
    double[] amplitudes;

    int largestFeature;
    double persistence;
    int seed;

    public SimplexNoise(int largestFeature, double persistence) {    	
    	this(largestFeature, persistence, new Random().nextInt());
    }
    
    public SimplexNoise(int largestFeature, double persistence, int seed) {
        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));

        octaves=new SimplexNoiseOctave[numberOfOctaves];
        frequencys=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];

        Random rnd=new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoiseOctave(rnd.nextInt());

            frequencys[i] = Math.pow(2,i);
            amplitudes[i] = Math.pow(persistence,octaves.length-i);
        }
    }
    
    public double getNoise(int x, int y){
        double result=0;

        for(int i=0;i<octaves.length;i++){
          //double frequency = Math.pow(2,i);
          //double amplitude = Math.pow(persistence,octaves.length-i);

          result=result+octaves[i].noise(x/frequencys[i], y/frequencys[i])* amplitudes[i];
        }

        return result;
    }

    public double getNoise(int x,int y, int z){
        double result = 0;

        for(int i = 0; i < octaves.length; i++){
          double frequency = Math.pow(2,i);
          double amplitude = Math.pow(persistence, octaves.length - i);

          result += octaves[i].noise(x/frequency, y/frequency, z/frequency) * amplitude;
        }


        return result;
    }
    
    public WritableImage getSphereMap(int width, int height, double scale) {
    	WritableImage result = new WritableImage(width, height);
    	PixelWriter writer = result.getPixelWriter();
    	double[][] values = new double[height][width];
    	double curMax = 0.0;
    	double curMin = 0.0;
    	
    	scale = largestFeature/scale;
    	
    	for (int y = 0; y < height; y ++) {
			double v = (double)y/height;
    		for (int x = 0; x < width; x ++) {
	    		/*double fNX = (x + 0.5);// divide isize
	    		double fNY = (y + 0.5);
	    		double fRdx = fNX*2*Math.PI;
	    		double fRdy = fNY*Math.PI;
	    		double fYSin = Math.sin(fRdy+Math.PI);
	    		double a = fRdsSin*Math.sin(fRdx)*fYSin;
	    		double b = fRdsSin*Math.cos(fRdx)*fYSin
	    		double c = fRdsSin*Math.cos(fRdy)
	    		double v = Simplex.noise(
	    				 123+a*fNoiseScale
	    				,132+b*fNoiseScale
	    				,312+c*fNoiseScale
	    			)*/
    			double u = (double)x/width;
    			
    			double theta = 2*Math.PI*u;
    			double phi = Math.PI*v;
    			
    			double tx = Math.cos(theta) * Math.sin(phi);
    			double ty = Math.sin(theta) * Math.sin(phi);
    			double tz = -Math.cos(phi);
    			
    			int ix = (int)(tx * scale);
    			int iy = (int)(ty * scale);
    			int iz = (int)(tz * scale);
    			
    			values[y][x] = getNoise(ix, iy, iz);
    			if (values[y][x] < curMin) {
    				curMin = values[y][x];
    			} else if (values[y][x] > curMax) {
    				curMax = values[y][x];
    			}
    		}
    	}
    	
    	for (int y = 0; y < height; y ++) {
    		for (int x = 0; x < width; x ++) {
    			double val = MathUtils.map(values[y][x], curMin, curMax, 0.0, 1.0);
    			writer.setColor(x, y, new Color(val, val, val, 1.0));
    		}
    	}
    	
    	return result;
    }
} 