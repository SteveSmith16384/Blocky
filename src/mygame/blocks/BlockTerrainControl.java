package mygame.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.terrain.heightmap.HeightMap;

import mygame.BlockSettings;
import mygame.blocks.meshs.FaceCullMeshGenerator;
import mygame.util.Vector3Int;

/**
 * You get a null mesh error if the only block is at 0, 0, 0
 *  
 * @author bogdan
 */
public class BlockTerrainControl extends AbstractControl implements Savable {

	private final ChunkControl[][][] chunks;
	private final Map<Class<? extends IBlock>, IBlock> blockTypes;
	private final Vector3Int worldSizeInChunks;
	private final Vector3Int chunkSize;
	private final BlockSettings settings;
	private final Set<ChunkControl> updateables = new HashSet<ChunkControl>();
	private final List<IBlockTerrainListener> listeners = new ArrayList<IBlockTerrainListener>();

	//private boolean needsUpdate = false;

	public BlockTerrainControl(BlockSettings settings) {
		this.worldSizeInChunks = settings.getWorldSizeInChunks();
		this.chunkSize = settings.getChunkSize();
		this.settings = settings;
		this.chunks = new ChunkControl[worldSizeInChunks.getX()][worldSizeInChunks.getY()][worldSizeInChunks.getZ()];
		blockTypes = new HashMap<Class<? extends IBlock>, IBlock>();
		FaceCullMeshGenerator.texturesPerSheet = settings.texturesPerSheet;
	}


	@Override
	protected void controlUpdate(float tpf) {
		if (updateables.size() > 0) {
			for (ChunkControl c : this.updateables) {
				c.update(tpf);
				for (IBlockTerrainListener l : listeners) {
					l.onChunkUpdated(c);
				}
			}

			this.updateables.clear();
			//needsUpdate = false;
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	public void registerBlock(IBlock block){
		this.blockTypes.put(block.getClass(), block);
	}

	public void setBlock(Vector3Int location, Class<? extends IBlock> blockType) {
		IBlock block = this.blockTypes.get(blockType);
		if (block == null) {
			//throw new RuntimeException("Block " + blockType + " not registered");
			//this.blockTypes.put(arg0, arg1)
			try {
				block = (IBlock)blockType.newInstance();
				this.blockTypes.put(blockType, block);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("Unable to create " + blockType.getName(), e);
			}
		}
		this.updateBlock(location, block);
	}
	

	public void removeBlock(Vector3Int location){
		this.updateBlock(location, null);
	}

	
	public IBlock getBlock(Vector3Int location){
		ChunkPosition chunkPosition = getValidChunk(location);
		return chunkPosition.chunk.getBlock(chunkPosition.positionInChunk);
	}

	
	public BlockSettings getSettings() {
		return settings;
	}

	
	public ChunkControl[][][] getChunks() {
		return chunks;
	}

	/*
	void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}
	 */

	private void updateBlock(Vector3Int location, IBlock block){
		ChunkPosition chunkPosition = getValidChunk(location);
		ChunkControl chunk = chunkPosition.chunk;
		if (chunk.putBlock(chunkPosition.positionInChunk, block)) {
			this.updateables.add(chunk);
			//this.needsUpdate = true;
		}
	}


	private ChunkPosition getValidChunk(Vector3Int location){
		int x = location.getX();
		int y = location.getY();
		int z = location.getZ();
		int cX = 0;
		int cY = 0;
		int cZ = 0;
		while(x >= chunkSize.getX()){
			x -= chunkSize.getX();
			cX++;
		}
		while(y >= chunkSize.getY()){
			y -= chunkSize.getY();
			cY++;
		}
		while(z >= chunkSize.getZ()){
			z -= chunkSize.getZ();
			cZ++;
		}
		ChunkControl chunk = this.chunks[cX][cY][cZ];
		if(chunk == null){
			chunk = new ChunkControl(this, new Vector3Int(cX,cY,cZ));

			chunk.setSpatial(this.spatial);
			this.chunks[cX][cY][cZ] = chunk;
		}

		return new ChunkPosition(chunk, new Vector3Int(x, y, z));
	}

	private static final class ChunkPosition {
		final ChunkControl chunk;
		final Vector3Int positionInChunk;

		public ChunkPosition(ChunkControl chunk, Vector3Int positionInChunk) {
			this.chunk = chunk;
			this.positionInChunk = positionInChunk;
		}

	}


	public Vector3Int getPointedBlockLocation(Vector3f collisionLocation) { //, boolean getNeighborLocation) {
		return getPointedBlockLocation(new Vector3Int(collisionLocation)); //, getNeighborLocation);
	}


	public Vector3Int getPointedBlockLocation(Vector3Int collisionLocation) {//, boolean getNeighborLocation) {
		Vector3Int blockLocation = new Vector3Int(
				(int) (collisionLocation.getX() / this.getSettings().getBlockSize()),
				(int) (collisionLocation.getY() / this.getSettings().getBlockSize()),
				(int) (collisionLocation.getZ() / this.getSettings().getBlockSize()));
		/*
		if((this.getBlock(blockLocation) != null) == getNeighborLocation){
			if((collisionLocation.getX() % this.getSettings().getBlockSize()) == 0) {
				blockLocation.subtractLocal(1, 0, 0);
			}
			else if((collisionLocation.getY() % this.getSettings().getBlockSize()) == 0){
				blockLocation.subtractLocal(0, 1, 0);
			}
			else if((collisionLocation.getZ() % this.getSettings().getBlockSize()) == 0){
				blockLocation.subtractLocal(0, 0, 1);
			}
		}
		 */
		return blockLocation;
	}


	public void loadFromHeightMap(Vector3Int location, HeightMap heightmap, Class<? extends IBlock> blockClass) {
		setBlocksFromHeightmap(location, getHeightmapBlockData(heightmap.getScaledHeightMap(), heightmap.getSize()), blockClass);
	}


	private static int[][] getHeightmapBlockData(float[] heightmapData, int length) {
		int[][] data = new int[heightmapData.length / length][length];
		int x = 0;
		int z = 0;
		for(int i=0 ; i<heightmapData.length ; i++){
			data[x][z] = (int) Math.round(heightmapData[i]);
			x++;
			if((x != 0) && ((x % length) == 0)) {
				x = 0;
				z++;
			}
		}
		return data;
	}


	public void setBlocksFromHeightmap(Vector3Int location, int[][] heightmap, Class<? extends IBlock> blockClass){
		Vector3Int tmpLocation = new Vector3Int();
		Vector3Int tmpSize = new Vector3Int();
		for(int x=0;x<heightmap.length;x++){
			for(int z=0;z<heightmap[0].length;z++){
				tmpLocation.set(location.getX() + x, location.getY(), location.getZ() + z);
				tmpSize.set(1, heightmap[x][z], 1);
				setBlockArea(tmpLocation, tmpSize, blockClass);
			}
		}
	}


	public void setBlockHeightsFromArray(Vector3Int location, int[][] heights, Class<? extends IBlock> blockClass){
		Vector3Int tmpLocation = new Vector3Int();
		for(int x=0 ; x<heights.length ; x++){
			for(int z=0 ; z<heights[0].length ; z++){
				for(int y=0;y<heights[x][z];y++){
					tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
					setBlock(tmpLocation, blockClass);
				}
			}
		}
	}


	public void setBlockArea(Vector3Int location, Vector3Int size, Class<? extends IBlock> blockClass){
		Vector3Int tmpLocation = new Vector3Int();
		for(int x=0;x<size.getX();x++){
			for(int y=0;y<size.getY();y++){
				for(int z=0;z<size.getZ();z++){
					tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
					setBlock(tmpLocation, blockClass);
				}
			}
		}
	}


	public void setBlockAreaBySphere(Vector3Int location, int diameter, Class<? extends IBlock> blockClass){
		int rad = diameter/2;
		Vector3Int tmpLocation = new Vector3Int();
		for (int z = 0; z < diameter; z++) {
			for (int y = 0; y < diameter; y++) {
				for (int x = 0; x < diameter; x++) {
					if (Math.sqrt((float) (x-diameter/2)*(x-diameter/2) + (y-diameter/2)*(y-diameter/2) + (z-diameter/2)*(z-diameter/2)) <= diameter/2) {
						tmpLocation.set(location.getX() - rad + x, location.getY() - rad + y, location.getZ() - rad + z);
						setBlock(tmpLocation, blockClass);
					}
				}
			}
		}
	}


	public void addListener(IBlockTerrainListener listener){
		this.listeners.add(listener);
	}

	public void removeListener(IBlockTerrainListener listener){
		this.listeners.remove(listener);
	}

}
