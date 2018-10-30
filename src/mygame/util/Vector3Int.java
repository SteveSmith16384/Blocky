package mygame.util;

import com.jme3.math.Vector3f;

/**
 *
 * @author bogdan
 */
public class Vector3Int {

	private int x,y,z;

	public Vector3Int() {
	}

	public Vector3Int(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}


	public Vector3Int add(int x, int y, int z){
		return new Vector3Int(this.x+x, this.y+y, this.z+z);
	}


	public Vector3Int add(Vector3Int other){
		return this.add(other.x,other.y,other.z);
	}

	public Vector3Int subtract(int x,int y,int z){
		return new Vector3Int(this.x-x, this.y-y, this.z-z);
	}

	public Vector3Int subtract(Vector3Int other){
		return this.subtract(other.x,other.y,other.z);
	}

	public Vector3Int addLocal(int x,int y,int z){
		this.x+=x;
		this.y+=y;
		this.z+=z;
		return this;
	}

	public Vector3Int addLocal(Vector3Int other){
		return this.addLocal(other.x,other.y,other.z);
	}

	public Vector3Int subtractLocal(int x, int y, int z){
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vector3Int subtractLocal(Vector3Int other){
		return this.subtractLocal(other.x,other.y,other.z);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 41 * hash + this.x;
		hash = 41 * hash + this.y;
		hash = 41 * hash + this.z;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Vector3Int other = (Vector3Int) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.z != other.z) {
			return false;
		}
		return true;
	}

	public Vector3Int mult(int i) {
		return new Vector3Int(this.x*i, this.y * i, this.z*i);
	}

	public Vector3Int multLocal(int i){
		this.x *= i;
		this.y *= i;
		this.z *= i;
		return this;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void set(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f toVector3f(){
		return new Vector3f(x, y, z);
	}

	@Override
	public String toString() {
		return "Vector3Int{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}


}
