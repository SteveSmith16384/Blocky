package mygame.texturesheet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureSheetGenerator {

	// todo - ensure final image is square!
	public static void main(String[] args) {
		try {
			String[] tiles = {"mud.png", "lavarock.jpg", "yellowsun.jpg"};
			new TextureSheetGenerator("newtilemap", 16, tiles);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public TextureSheetGenerator(String filename, int tileSize, String[] tiles) throws IOException {
		int IMAGE_HEIGHT = tileSize;
		int IMAGE_WIDTH = tileSize * tiles.length;

		BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// fill all the image with white
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

		for (int i=0 ; i<tiles.length ; i++) {
			this.addImage(g2d, "assets/Textures/" + tiles[i], i, tileSize);
		}
		g2d.dispose();

		// Save as PNG
		File file = new File(filename + ".png");
		ImageIO.write(bufferedImage, "png", file);
	}


	private void addImage(Graphics2D g2d, String filename, int num, int tileSize) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		//if (image.getWidth() != tileSize || image.getHeight() != tileSize) {
		//Image i = image.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_DEFAULT);
		g2d.drawImage(image, tileSize*num, 0, tileSize, tileSize, null);
		//} else {

		//}

	}
}
