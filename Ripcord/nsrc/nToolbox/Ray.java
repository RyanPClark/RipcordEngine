package nToolbox;

import org.lwjgl.util.vector.Vector3f;

public class Ray {

	private Vector3f origin;
	private Vector3f direction;
	
	Vector3f inv_direction;
	int sign[] = new int[3];

	/**
	 * @param o - origin
	 * @param d - direction
	 */
	
	public Ray(Vector3f o, Vector3f d)
	{ 
		setOrigin(o);
		setDirection(d);
		inv_direction = new Vector3f(1/d.x, 1/d.y, 1/d.z);
		sign[0] = (inv_direction.x < 0) ? 1 : 0;
		sign[1] = (inv_direction.y < 0) ? 1 : 0;
		sign[2] = (inv_direction.z < 0) ? 1 : 0;
	}

	
	
	/**
	 * Getters and setters
	 */
	
	public Vector3f getInv_direction() {
		return inv_direction;
	}

	public void setInv_direction(Vector3f inv_direction) {
		this.inv_direction = inv_direction;
	}

	public int[] getSign() {
		return sign;
	}

	public void setSign(int[] sign) {
		this.sign = sign;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	} 
}
