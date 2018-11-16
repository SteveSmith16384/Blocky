package mygame.blocktypes;

import mygame.blocks.IBlock;
import mygame.blocks.IBlockTextureLocator;
import mygame.blocks.SimpleBlockTexture;

public class AbstractBlock implements IBlock {

	private final IBlockTextureLocator blockTextureLocator;

	public AbstractBlock(int texX, int textY) { // origin is top-left
		blockTextureLocator = new SimpleBlockTexture(texX, textY);
	}

	public IBlockTextureLocator getTexture() {
		return blockTextureLocator;
	}

}
