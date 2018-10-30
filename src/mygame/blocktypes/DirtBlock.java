package mygame.blocktypes;

import mygame.blocks.IBlock;
import mygame.blocks.IBlockTextureLocator;
import mygame.blocks.SimpleBlockTexture;

public class DirtBlock implements IBlock {

	private final IBlockTextureLocator blockTextureLocator;

	public DirtBlock() {
		blockTextureLocator = new SimpleBlockTexture(15, 1);
	}

	public IBlockTextureLocator getTexture() {
		return blockTextureLocator;
	}

}
