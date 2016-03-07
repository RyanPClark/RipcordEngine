package nComponents;

import org.lwjgl.util.vector.Vector3f;

public class Position extends Component {

	Vector3f pos;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
		
		///Entity ent = (Entity)getParent();
		///Rotation rot = (Rotation)ent.getComponentByType(CompType.ROTATION);
		///rot.getRotation().setX(rot.getRotation().getX()+0.1f);
		///this.pos.z -= 0.1f;
	}

	public Position(Entity parent, Vector3f pos){
		
		this.setType(CompType.POSITION);
		this.setParent(parent);
		this.pos = pos;
	}
	
	public void setPosition(Vector3f position){
		this.pos = position;
	}
	
	public Vector3f getPosition(){	
		return pos;
	}
}
