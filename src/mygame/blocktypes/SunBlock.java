package mygame.blocktypes;

import mygame.blocks.IBlock;
import mygame.blocks.IBlockTextureLocator;
import mygame.blocks.SimpleBlockTexture;

public class SunBlock implements IBlock {

	private final IBlockTextureLocator blockTextureLocator;

	public SunBlock() {
		blockTextureLocator = new SimpleBlockTexture(0, 0);
	}

	public IBlockTextureLocator getTexture() {
		return blockTextureLocator;
	}

}
