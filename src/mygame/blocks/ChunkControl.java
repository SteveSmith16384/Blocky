package mygame.blocks;

import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;

import mygame.blocks.meshs.FaceCullMeshGenerator;
import mygame.blocks.meshs.IMeshGenerator;
import mygame.util.Vector3Int;

/**
 *
 * @author bogdan
 */
public class ChunkControl extends AbstractControl implements Savable {

	private final IBlock[][][] blocks;
	private final float blockSize;

	private final Vector3Int size;

	private boolean needsUpdate = false;
	private Geometry geom;
	//private final BlockTerrainControl terrain;
	private final Vector3Int location;
	private final IMeshGenerator generator;
	public boolean generatedProperMesh = false;

	public ChunkControl(BlockTerrainControl terrain, Vector3Int location) {
		this.location = location;
		this.size = terrain.getSettings().getChunkSize();
		this.blockSize = terrain.getSettings().getBlockSize();
		this.blocks = new IBlock[this.size.getX()][this.size.getY()][this.size.getZ()];

		Material material = terrain.getSettings().getMaterial();

		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		material.setTransparent(true);
		this.geom = new Geometry("terrain " + location);
		geom.setMaterial(material);
		geom.setQueueBucket(Bucket.Transparent);

		geom.setMesh(new Box(1, 1, 1)); // dummy mesh for now.
		geom.setShadowMode(ShadowMode.CastAndReceive);

		generator = new FaceCullMeshGenerator(size, blockSize, location, blocks);


	}


	@Override
	public void setSpatial(Spatial spatial) {
		Node old = (Node) spatial;
		super.setSpatial(spatial); 
		if(old != null){
			old.detachChild(this.geom);
		}
		((Node) spatial).attachChild(this.geom);
	}



	public boolean putBlock(Vector3Int at, IBlock blockType){
		return this.updateBlockAt(at, blockType);
	}
/*
	public void removeBlock_(Vector3Int from){
		this.updateBlockAt(from, null);
	}
*/
	public IBlock getBlock(Vector3Int from){
		if(isValidPosition(from)){
			return blocks[from.getX()][from.getY()][from.getZ()];
		}
		return null;
	}

	private boolean updateBlockAt(final Vector3Int at, IBlock type){
		if (this.isValidPosition(at)) {
			if (blocks[at.getX()][at.getY()][at.getZ()] != type) {
				blocks[at.getX()][at.getY()][at.getZ()] = type;
				needsUpdate = true;
				return true;
			}
		}
		return false;
	}


	@Override
	protected void controlUpdate(float tpf) {
		if (needsUpdate) {
			needsUpdate = false;
			geom.setMesh(regenerateMesh());
			geom.updateModelBound();
		}
	}


	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	private Mesh regenerateMesh(){
		return generator.generateMesh();
	}



	private boolean isValidPosition(Vector3Int position){
		int x = position.getX();
		int y = position.getY();
		int z = position.getZ();
		return x >= 0 && x < size.getX() && y >= 0 && y<size.getY() && z>=0 && z<size.getZ();

	}

	public Vector3Int getLocation() {
		return location;
	}

	public Geometry getGeometry() {
		return geom;
	}

}
