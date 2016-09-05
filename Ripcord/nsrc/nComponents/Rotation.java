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
	
	public Vector3f getAdditiveRotation(){
		
		Entity origEnt = parent;
		
		Vector3f rotation = new Vector3f(0,0,0);
		
		while(true){
			
			// get position
			Rotation pComp = (Rotation)parent.getComponentByType(CompType.ROTATION);
			Vector3f nrotation = (pComp != null ? pComp.getRotation() : new Vector3f(0,0,0));
			Vector3f.add(rotation, nrotation, rotation);
			
			if(parent.getParent() != null){
				parent = parent.getParent();
			}
			else {
				break;
			}
		}
		
		parent = origEnt;

		return rotation;
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
