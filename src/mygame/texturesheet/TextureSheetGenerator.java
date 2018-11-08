package mygame.texturesheet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureSheetGenerator {

	public static void main(String[] args) {
		try {
			String[][] tiles = {{"mud.png", "lavarock.jpg", "yellowsun.jpg"}};
			new TextureSheetGenerator().generateTextureSheet("assets/Textures", tiles, 1024, 16, "newtilemap");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void generateTextureSheet(String path, String[][] tiles, int imageSize, int tileSize, String outputFilename) throws IOException {
		if (!path.endsWith("\\") && !path.endsWith("/")) {
			path = path + "/";
		}
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// fill all the image with white
		g2d.setColor(new Color(1f, 1f, 1f, 0f)); // Transparent background!
		g2d.fillRect(0, 0, imageSize, imageSize);

		for (int i=0 ; i<tiles.length ; i++) {
			for (int j=0 ; j<tiles[0].length ; j++) {
				try {
					if (tiles[i][j] != null) {
						this.addImage(g2d, path + tiles[i][j], j, i, tileSize);
					}
				} catch (ArrayIndexOutOfBoundsException ex) {

				}
			}
		}
		g2d.dispose();

		// Save as PNG
		File file = new File(path + outputFilename + ".png");
		ImageIO.write(bufferedImage, "png", file);

		System.out.println(outputFilename + ".png created.");
	}


	private void addImage(Graphics2D g2d, String filename, int x, int y, int tileSize) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		g2d.drawImage(image, tileSize*x, tileSize*y, tileSize, tileSize, null);
	}

}