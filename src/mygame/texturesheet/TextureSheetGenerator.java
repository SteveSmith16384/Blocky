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
			new TextureSheetGenerator().generateTextureSheet("assets/Textures", tiles, 8, 16, "newtilemap", 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void generateTextureSheet(String path, String[][] tiles, int tilesAcrossDown, int tileSize, String outputFilename, int inset) throws IOException {
		if (!path.endsWith("\\") && !path.endsWith("/")) {
			path = path + "/";
		}
		int imageSize = tilesAcrossDown * tileSize;
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// fill all the image with white
		g2d.setColor(new Color(1f, 1f, 1f, 0f)); // Transparent background!
		g2d.fillRect(0, 0, imageSize, imageSize);

		for (int i=0 ; i<tiles.length ; i++) {
			for (int j=0 ; j<tiles[0].length ; j++) {
				try {
					if (tiles[i][j] != null) {
						this.addImage(g2d, path + tiles[i][j], j, i, tileSize, inset);
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


	private void addImage(Graphics2D g2d, String filename, int tileX, int tileY, int tileSize, int inset) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		g2d.drawImage(image, tileSize*tileX, tileSize*tileY, tileSize, tileSize, null);
		if (inset > 0) {
			g2d.drawImage(image, (tileSize*tileX)+inset, (tileSize*tileY)+inset, tileSize-(inset*2), tileSize-(inset*2), null);
		}
	}

}
