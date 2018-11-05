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
			new TextureSheetGenerator().generateTextureSheet("assets/Textures", tiles, 1024, 16, "newtilemap");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void generateTextureSheet(String path, String[] tiles, int imageSize, int tileSize, String outputFilename) throws IOException {
		if (!path.endsWith("\\") && !path.endsWith("/")) {
			path = path + "/";
		}
		BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// fill all the image with white
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, imageSize, imageSize);

		for (int i=0 ; i<tiles.length ; i++) {
			this.addImage(g2d, path + tiles[i], i, tileSize);
		}
		g2d.dispose();

		// Save as PNG
		File file = new File(path + outputFilename + ".png");
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
