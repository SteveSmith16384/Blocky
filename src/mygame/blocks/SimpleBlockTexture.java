package mygame.blocks;

import mygame.blocks.meshs.Face;

/**
 *
 * @author bogdanpandia
 */
public class SimpleBlockTexture implements IBlockTexture, IBlockTextureLocator {
	
    private final int row;
    private final int column;

    public SimpleBlockTexture(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return row;//column;
    }

    public int getRow() {
        return column;//row;
    }

    public IBlockTexture getFaceTexture(Face face) {
        return this;
    }
    
}
