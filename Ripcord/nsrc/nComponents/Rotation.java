package nComponents;

import org.lwjgl.util.vector.Vector3f;

public class Rotation extends Component {

	Vector3f rot;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
	}

	public Rotation(Entity parent, Vector3f rot){
		
		this.setType(CompType.ROTATION);
		this.setParent(parent);
		this.rot = rot;
	}
	
	public Vector3f getRotation(){
		
		return rot;
	}
}
