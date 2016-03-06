package nComponents;

public class Scale extends Component {

	float scale;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
	}

	public Scale(Entity parent, float scale){
		
		this.setType(CompType.SCALE);
		this.setParent(parent);
		this.scale = scale;
	}
	
	
	public float getScale(){
		
		return scale;
	}
}
