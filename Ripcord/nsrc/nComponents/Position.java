package nComponents;

/**
 * @author Ryan Clark
 * 
 * A component storing a 3D vector representing an entity's position
 */

import org.lwjgl.util.vector.Vector3f;

public class Position extends Component {

	private Vector3f pos;
	
	public void update(float dt) {}

	public Position(Entity parent, Vector3f pos){
		
		this.setType(CompType.POSITION);
		this.setParent(parent);
		this.pos = pos;
	}
	
	/**
	 * Getter and setter for position
	 */
	
	public void setPosition(Vector3f position){
		this.pos = position;
	}
	
	public Vector3f getPosition(){	
		return pos;
	}
}
