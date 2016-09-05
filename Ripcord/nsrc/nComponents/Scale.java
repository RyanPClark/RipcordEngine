package nComponents;


/**
 * 
 * @author Ryan Clark
 * 
 * Component that stores a float representing an entity's scale.
 */

public class Scale extends Component {

	private float scale;
	
	public void update(float dt) {}

	public Scale(Entity parent, float scale){
		
		this.setType(CompType.SCALE);
		this.setParent(parent);
		this.scale = scale;
	}
	
	public float getMultiplicativeScale(){
		
		Entity origEnt = parent;
		
		float scale = 1;
		
		while(true){
			
			// get position
			Scale sComp = (Scale)parent.getComponentByType(CompType.SCALE);
			float nscale = (sComp != null ? sComp.getScale() : 1);
			scale *= nscale;
			
			if(parent.getParent() != null){
				parent = parent.getParent();
			}
			else {
				break;
			}
		}
		
		parent = origEnt;
		
		return scale;
	}
	
	public float getScale(){
		return scale;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
}
