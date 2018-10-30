package mygame.blocks;

import mygame.blocks.meshs.Face;

/**
 *
 * @author bogdanpandia
 */
public class SimpleBlockTexture implements IBlockTexture, IBlockTextureLocator {
	
    private final int row;
    private final int column;

    public SimpleBlockTexture(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public IBlockTexture getFaceTexture(Face face) {
        return this;
    }
    
}
