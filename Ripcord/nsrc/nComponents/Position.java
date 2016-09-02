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
	
	public Vector3f getAdditivePosition(){
		
		Entity ent = (Entity)parent;
		Entity origEnt = ent;
		
		Vector3f position = new Vector3f(0,0,0);
		
		while(true){
			
			// get position
			Position pComp = (Position)ent.getComponentByType(CompType.POSITION);
			Vector3f nposition = (pComp != null ? pComp.getPosition() : new Vector3f(0,0,0));
			Vector3f.add(position, nposition, position);
			
			if(ent.getParent() != null){
				ent = (Entity)ent.getParent();
			}
			else {
				break;
			}
		}
		
		parent = origEnt;
		
		return position;
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
