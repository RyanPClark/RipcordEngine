package nComponents;

import org.lwjgl.util.vector.Vector3f;

public class Color extends Component {

	float alpha;
	Vector3f col;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
		
		///Entity ent = (Entity)getParent();
		///Rotation rot = (Rotation)ent.getComponentByType(CompType.ROTATION);
		///rot.getRotation().setX(rot.getRotation().getX()+0.1f);
		///this.pos.z -= 0.1f;
	}

	public Color(Entity parent, Vector3f col, float alpha){
		
		this.setType(CompType.COLOR);
		this.setParent(parent);
		this.col = col;
		this.alpha = alpha;
	}
	
	public float getAlpha(){
		return alpha;
	}
	
	public Vector3f getColor(){
		
		return col;
	}
}
