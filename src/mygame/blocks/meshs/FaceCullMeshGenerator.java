package mygame.blocks.meshs;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import mygame.blocks.IBlock;
import mygame.blocks.IBlockTexture;
import mygame.blocks.IBlockTextureLocator;
import mygame.util.Vector3Int;

/**
 *
 * @author bogdan
 */
public class FaceCullMeshGenerator implements IMeshGenerator {

	public static int texturesPerSheet = 16;
	private final List<Vector3f> verts = new ArrayList<Vector3f>();
	private final List<Integer> indices = new ArrayList<Integer>();
	private final List<Float> normals = new ArrayList<Float>();
	private final List<Vector2f> textureCoords = new ArrayList<Vector2f>();
	private final Vector3Int size;
	private final float blockSize;
	private final IBlock[][][] blocks;
	private final Vector3Int location;

	public FaceCullMeshGenerator(Vector3Int size, float blockSize, Vector3Int location, IBlock[][][] blocks) {
		this.size = size;
		this.blocks = blocks;
		this.blockSize = blockSize;
		this.location = location;
	}


	public Mesh generateMesh() {
		for (int i = 0; i < size.getX(); i++) {
			for (int j = 0; j < size.getY(); j++) {
				for (int k = 0; k < size.getZ(); k++) {
					IBlock bt = blocks[i][j][k];
					if (bt != null) {
						Vector3f loc = new Vector3f(i * this.blockSize, j * this.blockSize, k * this.blockSize);
						Vector3Int virtualLocation = new Vector3Int(i, j, k);
						if (shouldAddFace(virtualLocation, Face.FRONT)) {
							updateIndices(verts.size(), Face.FRONT);
							updateVerts(loc, Face.FRONT);
							updateTextures(bt.getTexture(), Face.FRONT);
						}
						if (shouldAddFace(virtualLocation, Face.BACK)) {
							updateIndices(verts.size(), Face.BACK);
							updateVerts(loc, Face.BACK);
							updateTextures(bt.getTexture(), Face.BACK);
						}
						if (shouldAddFace(virtualLocation, Face.LEFT)) {
							updateIndices(verts.size(), Face.LEFT);
							updateVerts(loc, Face.LEFT);
							updateTextures(bt.getTexture(), Face.LEFT);
						}
						if (shouldAddFace(virtualLocation, Face.RIGHT)) {
							updateIndices(verts.size(), Face.RIGHT);
							updateVerts(loc, Face.RIGHT);
							updateTextures(bt.getTexture(), Face.RIGHT);
						}
						if (shouldAddFace(virtualLocation, Face.TOP)) {
							updateIndices(verts.size(), Face.TOP);
							updateVerts(loc, Face.TOP);
							updateTextures(bt.getTexture(), Face.TOP);
						}
						if (shouldAddFace(virtualLocation, Face.BOTTOM)) {
							updateIndices(verts.size(), Face.BOTTOM);
							updateVerts(loc, Face.BOTTOM);
							updateTextures(bt.getTexture(), Face.BOTTOM);
						}

					}
				}
			}
		}
		Vector3f[] vertsArray = new Vector3f[this.verts.size()];
		int[] indicesArray = makeArray(this.indices);
		Vector2f[] textureCoordsArray = new Vector2f[this.textureCoords.size()];
		float[] normalsArray = makeFloatArray(this.normals);

		Mesh m = new Mesh();
		m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verts.toArray(vertsArray)));
		m.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indicesArray));
		m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoords.toArray(textureCoordsArray)));
		m.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normalsArray));
		m.setStatic();
		verts.clear();
		indices.clear();
		textureCoords.clear();
		normals.clear();

		m.updateBound();
		m.setStatic();
		return m;
	}


	private boolean shouldAddFace(Vector3Int position, Face face) {
		Vector3Int offset = position.add(face.getOffset());
		int x = offset.getX();
		int y = offset.getY();
		int z = offset.getZ();
		if (x < 0) {
			return true;
		} else if (x > size.getX() - 1) {
			return true;
		}
		if (y < 0) {
			return true;
		} else if (y > size.getY() - 1) {
			return true;
		}
		if (z < 0) {
			return true;
		} else if (z > size.getZ() - 1) {
			return true;
		}

		return blocks[x][y][z] == null;
	}

	private void updateVerts(Vector3f location, Face face) {
		float sizeX = this.blockSize * this.size.getX() * this.location.getX();
		float sizeY = this.blockSize * this.size.getY() * this.location.getY();
		float sizeZ = this.blockSize * this.size.getZ() * this.location.getZ();
		for (Vector3f vert : face.getBaseVerts()) {
			this.verts.add(vert.mult(this.blockSize).addLocal(location).addLocal(sizeX, sizeY, sizeZ));
		}
		Vector3Int normal = face.getOffset();
		for (int i = 0; i < 4; i++) {
			normals.add((float)normal.getX());
			normals.add((float)normal.getY());
			normals.add((float)normal.getZ());
		}
	}

	private void updateIndices(int count, Face face) {
		for (int i : face.getBaseIndices()) {
			indices.add(count + i);
		}
	}


	private void updateTextures(IBlockTextureLocator textureLocator, Face face) {
		IBlockTexture texture = textureLocator.getFaceTexture(face);
		int x = texture.getColumn();
		int y = texture.getRow();
		for (int[] texOffset : face.getTexturePositions()) {
			textureCoords.add(getTextureCoords(x, y, texOffset[0], texOffset[1]));
		}
	}


	private static Vector2f getTextureCoords(int row, int column, int addX, int addY) {
		float textureUnit = 1f / texturesPerSheet;
		float x = (((column + addX) * textureUnit));
		float y = ((((-1 * row) + (addY - 1)) * textureUnit) + 1);

		// Inset textures, otherwise get smudging
		float inset = textureUnit/8f;// 0.008f; // 0.015f; // 0.02f; todo - add as param
		if (addX == 0) {
			x = x + inset;
		} else {
			x = x - inset;
		}

		if (addY == 0) {
			y = y + inset;
		} else {
			y = y - inset;
		}

		Vector2f v = new Vector2f(x, y);

		//System.out.println("row:" + row + "  column:" + column + "  addX:" + addX + "  addY:" + addY + "  Coords: " + v);
		
		return v;
	}


	private static int[] makeArray(List<Integer> indices) {
		int[] array = new int[indices.size()];
		for (int i=0 ; i<array.length ; i++) {
			array[i] = indices.get(i);
		}
		return array;
	}

	private static float[] makeFloatArray(List<Float> normals) {
		float[] array = new float[normals.size()];
		for (int i=0 ; i<array.length ; i++) {
			array[i] = normals.get(i);
		}
		return array;
	}
}
