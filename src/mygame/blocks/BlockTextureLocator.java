/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.blocks;

import mygame.blocks.meshs.Face;

/**
 *
 * @author bogdanpandia
 */
public interface BlockTextureLocator {
    public BlockTexture getFaceTexture(Face face);
}
