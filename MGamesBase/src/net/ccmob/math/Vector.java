package net.ccmob.math;

public class Vector {

	private int x;
	private int y;
	private int z;
	
	public Vector(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public Vector() {
		this(0,0,0);
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	public static Vector min(Vector a, Vector b){
		return new Vector((a.getX() < b.getX() ? a.getX() : b.getX()), (a.getY() < b.getY() ? a.getY() : b.getY()), (a.getZ() < b.getZ() ? a.getZ() : b.getZ()));
	}
	
	public static Vector max(Vector a, Vector b){
		return new Vector((a.getX() > b.getX() ? a.getX() : b.getX()), (a.getY() > b.getY() ? a.getY() : b.getY()), (a.getZ() > b.getZ() ? a.getZ() : b.getZ()));
	}
	
}
