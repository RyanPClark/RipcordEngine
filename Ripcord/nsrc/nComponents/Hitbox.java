package nComponents;

import org.lwjgl.input.Mouse;

import mapBuilding.Box;
import nToolbox.MousePicker;
import toolbox.GameMath;

/**
 * Stores a hitbox for use in the cba.
 * 
 * @author Ryan Clark
 */

public class Hitbox extends Component{

	private Box box;
	private MousePicker picker;
	
	public void update(float dt) {
		
		if(Mouse.isButtonDown(0)){
			//box.setSelected(!inBox);
			//box.setSelected(true);
			//boolean inBox = GameMath.inBox(getBox(), picker.getCameraRay(), 0, -1000);
			
			boolean intersected = GameMath.intersection(box, picker.getCameraRay(), (Entity)parent);
			
			System.out.println(intersected);
		}
	}
	
	public Hitbox(Entity parent, Box box, MousePicker picker){
		this.parent = parent;
		this.setBox(box);
		this.setType(CompType.HIT_BOX);
		this.picker = picker;
	}

	/**
	 * @return - whether the hitbox is set to be rendered or not
	 */
	
	public boolean isRender(){
		return box.isSelected();
	}
	
	/**
	 * Getter and setter for hitbox
	 */
	
	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

}
