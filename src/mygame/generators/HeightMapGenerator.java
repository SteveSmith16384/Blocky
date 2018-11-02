package mygame.generators;

public class HeightMapGenerator {

	public static void main(String[] args) {
		try {
			//IWorldGenerator worldgen = new SimplexNoiseGenerator(3, 0.4f, 0.005f); // Good
			IWorldGenerator worldgen = new SimplexNoiseGenerator(3, 0.7f, 0.007f); // Good
			MapImage mi = new MapImage();

			for (int i = 0; i < 5; i++) {
				float[][] array = worldgen.createWorld(500, 500);
				mi.visualize(array, "generatedMap" + i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
