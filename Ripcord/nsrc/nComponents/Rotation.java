package nComponents;

/**
 * @author Ryan Clark
 * 
 * A component storing a Vector3f representing an Entity's rotation.
 */

import org.lwjgl.util.vector.Vector3f;

public class Rotation extends Component {

	private Vector3f rot;
	
	public void update(float dt) {}

	public Rotation(Entity parent, Vector3f rot){
		
		this.setType(CompType.ROTATION);
		this.setParent(parent);
		this.rot = rot;
	}
	
	/**
	 * Getter and setter for Rotation
	 */
	
	public Vector3f getRotation(){
		return rot;
	}
	
	public void setRotation(Vector3f rotation){
		this.rot = rotation;
	}
}
