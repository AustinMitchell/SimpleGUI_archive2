package testers.random_map;
import simple.gui.*;

public class Map {
	float[][] heightMap;
	ImageBox mapImage;
	
	public float get(int i, int j) {
		return heightMap[i][j];
	}
	
	public Map(int depth, float min, float max, float roughness) {
		heightMap = generateMap(depth, min, max, roughness);
	}
	
	public void reset(int depth, float min, float max, float roughness) {
		heightMap = generateMap(depth, min, max, roughness);
	}
	
	private static float random(float min, float max) {
		return (float)Math.random()*(max-min) + min;
	}
	
	private static float square(int i, int j, int step, float min, float max, float r, float[][]map) {
		float topLeft		= map[i-step][j-step];
		float topRight		= map[i-step][j+step];
		float bottomLeft	= map[i+step][j-step];
		float bottomRight	= map[i+step][j+step];
		return (float)Math.max(min, Math.min(max, (topLeft + topRight + bottomLeft + bottomRight)/4 + random(-r, r)));
	}
	private static float diamond(int i, int j, int step, float min, float max, float r, float[][]map) {
		int length = map.length; 
		
		float top 		= (j!=0 ? map[i][j-step] : map[i][length-1-step]);
		float left		= (i!=0 ? map[i-step][j] : map[length-1-step][j]);
		float bottom	= (j!=length-1 ? map[i][j+step] : map[i][step]);
		float right		= (i!=length-1 ? map[i+step][j] : map[step][j]);
		return (float)Math.max(min, Math.min(max, (top + left + bottom + right)/4 + random(-r, r)));
	}
	
	private static float[][] generateMap(int depth, float min, float max, float h) {
		
		int mapSize = (int)Math.pow(2, depth) + 1;
		float randRange = (max-min)/2;
		float[][] newMap = new float[mapSize][mapSize];
		
		newMap[0][0] = random(min, max);
		newMap[0][mapSize-1] = random(min, max);
		newMap[mapSize-1][0] = random(min, max);
		newMap[mapSize-1][mapSize-1] = random(min, max);
		
		for (int n=depth; n>0; n--) {
			int step = (int)Math.pow(2,n-1);
			for (int i=step; i<mapSize-1; i+=step*2) {
				for (int j=step; j<mapSize-1; j+=step*2) {
					newMap[i][j] = square(i, j, step, min, max, randRange, newMap);
				}
			}
			int loopCount = 0;
			for (int i=0; i<mapSize; i+=step) {
				for (int j=((loopCount&1)==0?step:0); j<mapSize; j+=step*2) {
					newMap[i][j] = diamond(i, j, step, min, max, randRange, newMap);
				}
				loopCount += 1;
			}
			randRange *= Math.pow(2, -h);
		}
		
		return newMap;
	}
	
	public static void shiftMap(int depth, float min, float max, float h, float[][] map) {
		int mapSize = (int)Math.pow(2, depth) + 1;
		float randRange = (max-min)/2;
		
		map[0][0] = (map[0][0] + random(min, max))/2;
		map[0][mapSize-1] = (map[0][mapSize-1]+random(min, max))/2;
		map[mapSize-1][0] = (map[mapSize-1][0] + random(min, max))/2;
		map[mapSize-1][mapSize-1] = (map[mapSize-1][mapSize-1]+random(min, max))/2;
		
		for (int n=depth; n>0; n--) {
			int step = (int)Math.pow(2,n-1);
			for (int i=step; i<mapSize-1; i+=step*2) {
				for (int j=step; j<mapSize-1; j+=step*2) {
					map[i][j] = (square(i, j, step, min, max, randRange, map) + map[i][j]*3)/4;
				}
			}
			int loopCount = 0;
			for (int i=0; i<mapSize; i+=step) {
				for (int j=((loopCount&1)==0?step:0); j<mapSize; j+=step*2) {
					map[i][j] = (diamond(i, j, step, min, max, randRange, map) + map[i][j]*3)/4;
				}
				loopCount += 1;
			}
			randRange *= Math.pow(2, -h);
		}
		
	}
}
