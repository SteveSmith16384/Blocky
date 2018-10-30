package mygame;

import com.jme3.app.Application;
import com.jme3.material.Material;

import mygame.util.Vector3Int;

/**
 *
 * @author bogdanpandia
 */
public class BlockSettings {

	private Vector3Int worldSize;
	private Vector3Int chunkSize;
	private Material material;
	private final Application application;
	private float blockSize = 1f;
	private float viewDistance = 200f;

	public BlockSettings(Application application) {
		this.application = application;
	}



	public BlockSettings(Application app, Vector3Int chunkSize, Material material) {
		this(app);
		this.chunkSize = chunkSize;
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Vector3Int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Vector3Int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public Application getApplication() {
		return application;
	}

	public void setWorldSize(Vector3Int worldSize) {
		this.worldSize = worldSize;
	}

	public Vector3Int getWorldSize() {
		return worldSize;
	}

	public void setBlockSize(float blockSize) {
		this.blockSize = blockSize;
	}

	public float getBlockSize() {
		return blockSize;
	}

	public void setViewDistance(float viewDistance) {
		this.viewDistance = viewDistance;
	}

	public float getViewDistance() {
		return this.viewDistance;
	}

}
