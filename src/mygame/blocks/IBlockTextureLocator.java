package mygame.blocks;

import mygame.blocks.meshs.Face;

/**
 *
 * @author bogdanpandia
 */
public interface IBlockTextureLocator {
	
    public IBlockTexture getFaceTexture(Face face);
    
}
