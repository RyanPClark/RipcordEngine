package nComponents;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import mapBuilding.Box;
import nToolbox.MousePicker;
import toolbox.GameMath;

/**
 * Stores a hitbox for use in the cba.
 * 
 * @author Ryan Clark
 */

public class Hitbox extends Component{

	private List<Box> boxes = new ArrayList<Box>();
	private MousePicker picker;
	private boolean isRender = false;
	private boolean selected = true;
	
	public void addParentAndDestory(){
		Entity eParent = (Entity)parent;
		while(true){
			if(eParent.getParent() == null){
				return;
			}
			else {
				Entity grandParent = (Entity)eParent.getParent();
				Hitbox parentBox = (Hitbox)grandParent.getComponentByType(CompType.HIT_BOX);
				if(parentBox == null)return;
				parentBox.boxes.addAll(boxes);
				System.out.println("WHAT");
			}
			eParent = (Entity)eParent.getParent();
		}
	}
	
	public void update(float dt) {
		
		for(Box box : boxes){
			if(Mouse.isButtonDown(0)){
				//box.setSelected(!inBox);
				//box.setSelected(true);
				//boolean inBox = GameMath.inBox(getBox(), picker.getCameraRay(), 0, -1000);
				
				boolean intersected = GameMath.intersection(box, picker.getCameraRay(), (Entity)parent);
				setSelected(intersected);
			}
			MousePicker.setUnit_selected(isSelected());
		}
	}
	
	public Hitbox(Entity parent, Box box, MousePicker picker){
		this.parent = parent;
		this.getBoxes().add(box);
		this.setType(CompType.HIT_BOX);
		this.picker = picker;
	}

	/**
	 * @return - whether the hitbox is set to be rendered
	 */
	
	public boolean isRender(){
		return isRender;
	}
	
	/**
	 * @return - whether two hitboxes intersect
	 */
	
	public boolean isIntersect(Hitbox others, Vector3f posThem, Vector3f posMe, float scaleMe, float scaleThem)
	{
		for(Box box : boxes){
			for(Box other : others.boxes) {
				if(box.getBounds()[1].x*scaleMe + posMe.x < other.getBounds()[0].x*scaleThem + posThem.x){return false;}
				if(box.getBounds()[0].x*scaleMe + posMe.x > other.getBounds()[1].x*scaleThem + posThem.x){return false;}
				if(box.getBounds()[1].z*scaleMe + posMe.z < other.getBounds()[0].z*scaleThem + posThem.z){return false;}
				if(box.getBounds()[0].z*scaleMe + posMe.z > other.getBounds()[1].z*scaleThem + posThem.z){return false;}
				if(box.getBounds()[1].y*scaleMe + posMe.y < other.getBounds()[0].y*scaleThem + posThem.y){return false;}
				if(box.getBounds()[0].y*scaleMe + posMe.y > other.getBounds()[1].y*scaleThem + posThem.y){return false;}
			}
		}
		
		
		
		return true;
	}
	
	/**
	 * Getter and setter for hitbox
	 */
	
	public List<Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
