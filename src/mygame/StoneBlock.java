package mygame;

import mygame.blocks.IBlock;
import mygame.blocks.IBlockTextureLocator;
import mygame.blocks.SimpleBlockTexture;

/**
 *
 * @author bogdanpandia
 */
public class StoneBlock implements IBlock {

	private final IBlockTextureLocator blockTextureLocator;

	public StoneBlock() {
		blockTextureLocator = new SimpleBlockTexture(0, 0);
	}

	public IBlockTextureLocator getTexture() {
		return blockTextureLocator;
	}

}
