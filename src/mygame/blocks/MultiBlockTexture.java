package mygame.blocks;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import mygame.blocks.meshs.Face;

/**
 * Currently allows for different textures on top and bottom.
 * @author StephenCS
 *
 */
public class MultiBlockTexture implements IBlockTexture, IBlockTextureLocator {

	private Map<Face, Point> textures;
	private Point currentPoint = new Point(0, 0);

	public MultiBlockTexture(int topX, int topY, int bottomX, int bottomY, int remainingX, int remainingY) {
		textures = new HashMap<Face, Point>();

		textures.put(Face.TOP, new Point(topX, topY));
		textures.put(Face.BOTTOM, new Point(bottomX, bottomY));

		textures.put(Face.LEFT, new Point(remainingX, remainingY));
		textures.put(Face.RIGHT, new Point(remainingX, remainingY));
		textures.put(Face.FRONT, new Point(remainingX, remainingY));
		textures.put(Face.BACK, new Point(remainingX, remainingY));
	}

	public int getColumn() {
		return currentPoint.y;
	}

	public int getRow() {
		return currentPoint.x;
	}

	public IBlockTexture getFaceTexture(Face face) {
		currentPoint = textures.get(face);
		return this;
	}

}
