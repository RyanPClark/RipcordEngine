package nComponents;

/**
 * @author Ryan Clark
 * 
 * Color component for light sources and potential future entity types
 */


import org.lwjgl.util.vector.Vector3f;

public class Color extends Component {

	private float alpha;
	private Vector3f col;
	
	public void update(float dt) {}

	public Color(Entity parent, Vector3f col, float alpha){
		
		this.setType(CompType.COLOR);
		this.setParent(parent);
		this.col = col;
		this.alpha = alpha;
	}
	
	/**
	 * Getter and setter for Alpha and Color
	 */
	
	public float getAlpha(){
		return alpha;
	}
	
	public Vector3f getColor(){	
		return col;
	}
}
