package mygame.generators;

public class Startup {

	public static void main(String[] args) {
		IWorldGenerator worldgen = new SimplexNoiseGenerator(3, 0.4f, 0.005f); // RandomPoint();
		MapImage mi = new MapImage();

		for (int i = 0; i < 5; i++) {
			double[][] array = worldgen.createWorld(100, 100);
			mi.visualize(array, "generatedMap" + i);
		}

	}
	
}
