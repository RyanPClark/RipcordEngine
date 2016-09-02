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
		
		Entity ent = (Entity)parent;
		Entity origEnt = ent;
		
		float scale = 1;
		
		while(true){
			
			// get position
			Scale sComp = (Scale)ent.getComponentByType(CompType.SCALE);
			float nscale = (sComp != null ? sComp.getScale() : 1);
			scale *= nscale;
			
			if(ent.getParent() != null){
				ent = (Entity)ent.getParent();
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
