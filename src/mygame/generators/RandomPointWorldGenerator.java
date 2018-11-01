package mygame.generators;

import java.util.Random;

public class RandomPointWorldGenerator implements IWorldGenerator {

	@Override
	public float[][] createWorld(int width, int height) {
		float[][] map = new float[width][height];

		Random r = new Random();

		for(int x = 0; x<map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				map[x][y] = r.nextFloat(); //Random value between 0 and 1;
			}
		}
		return map;
	}
}