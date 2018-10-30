package mygame.blocks;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author bogdanpandia
 */
public class BlockLodControl extends AbstractControl implements Cloneable {
	
    private float maxDistance;

    public BlockLodControl() {
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        BlockLodControl control = new BlockLodControl();
        control.setSpatial(spatial);
        control.setCullDistance(maxDistance);
        return control;
    }
    

    public void setCullDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }
}
