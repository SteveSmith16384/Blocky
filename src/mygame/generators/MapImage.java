package mygame.generators;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapImage {

	private final int PIXEL_SCALE = 1;

	/**
	 * Creates a 2D PNG Image from a two dimensional array.
	 *
	 * @param array
	 * @throws IOException 
	 */
	public void visualize(float[][] array) throws IOException {
		createImage(array, "generatedMap");
	}

	/**
	 * Creates an amount of 2D PNG Images from a two dimensional array.
	 *
	 * @param array
	 * @throws IOException 
	 */
	public void visualize(float[][] array, int amount) throws IOException {
		for (int i = 0; i < amount; i++) {
			createImage(array, "generatedMap" + i);
		}
	}

	/**
	 * Creates an amount of 2D PNG Images from a two dimensional array.
	 *
	 * @param array
	 * @throws IOException 
	 */
	public void visualize(float[][] array, String filename) throws IOException {
		createImage(array, filename);
	}

	/**
	 * Private Method to create a Buffered Image and save the result in a file.
	 *
	 * @param array
	 * @param filename
	 * @throws IOException 
	 */
	private void createImage(float[][] array, String filename) throws IOException {
		System.out.println("Creating MapImage, please wait...");

		int IMAGE_HEIGHT = array.length * PIXEL_SCALE;
		int IMAGE_WIDTH = array[0].length * PIXEL_SCALE;

		System.out.println("Image Width: " + IMAGE_WIDTH + "px");
		System.out.println("Image Height: " + IMAGE_HEIGHT + "px");

		BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// fill all the image with white
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array[x].length; y++) {

				//Defining coloring rules for each value
				//You may also use enums with switch case here

				if (array[x][y] < 0) {
					array[x][y] = 0;
				} else if (array[x][y] > 1) {
					array[x][y] = 1;
				}
				/*if (array[x][y] == 0) { // if value equals 0, fill with water
					g2d.setColor(Color.BLUE);
					g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);

				} else {*/
					g2d.setColor(new Color((float)array[x][y], (float)array[x][y], (float)array[x][y]));
					g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
				//}
			}
		}
		g2d.dispose();

		System.out.printf("Saving MapImage to Disk as %s.png ... \n", filename);
		// Save as PNG
		File file = new File(filename + ".png");
		ImageIO.write(bufferedImage, "png", file);

		System.out.println("Done! \n");
	}
}

