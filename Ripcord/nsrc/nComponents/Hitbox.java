package nComponents;


import org.lwjgl.util.vector.Vector3f;

import mapBuilding.Box;
import nEngineTester.Input;
import nToolbox.MousePicker;
import toolbox.GameMath;

/**
 * Stores a hitbox for use in the cba.
 * 
 * @author Ryan Clark
 */

public class Hitbox extends Component{

	private Box box;
	private Box translatedBox;
	private MousePicker picker;
	private boolean isRender = false;
	private boolean selected = true;
	private boolean isHover = false;
	
	public void update(float dt) {
		
		/*Position posMe = (Position)parent.getComponentByType(CompType.POSITION);
		Scale scaleMe = (Scale)parent.getComponentByType(CompType.SCALE);
		
		if(posMe != null && scaleMe != null){
			Vector3f addPos = posMe.getAdditivePosition();
			float multScale = scaleMe.getMultiplicativeScale();
			
			translatedBox.getBounds()[1].x = box.getBounds()[1].x*multScale + addPos.x;
			translatedBox.getBounds()[1].y = box.getBounds()[1].y*multScale + addPos.y;
			translatedBox.getBounds()[1].z = box.getBounds()[1].z*multScale + addPos.z;
			translatedBox.getBounds()[0].x = box.getBounds()[0].x*multScale + addPos.x;
			translatedBox.getBounds()[0].y = box.getBounds()[0].y*multScale + addPos.y;
			translatedBox.getBounds()[0].z = box.getBounds()[0].z*multScale + addPos.z;
			
			translatedBox.calculateMiddle();
		}*/
		
		GameMath.transformBox(box, parent, translatedBox);
		translatedBox.calculateMiddle();
		
		boolean intersected = GameMath.intersection(translatedBox, picker.getCameraRay(), parent);
		isHover = intersected;
		
		if((Input.wasClicked())){
			if(intersected){
				setSelected(!isSelected());
			}
		}
		if(isSelected()){
			if(parent.getComponentByType(CompType.PLAYER_CONTROL) != null){
				MousePicker.getUnits_selected().add(parent);
			}
		}
	}
	
	public Hitbox(Entity parent, Box box, Box tBox, MousePicker picker){
		this.parent = parent;
		this.box = box;
		this.translatedBox = tBox;
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
	
	public boolean isIntersect(Hitbox other)
	{
		if(translatedBox.getBounds()[1].x < other.getTranslatedBox().getBounds()[0].x){return false;}
		if(translatedBox.getBounds()[0].x > other.getTranslatedBox().getBounds()[1].x){return false;}
		if(translatedBox.getBounds()[1].z < other.getTranslatedBox().getBounds()[0].z){return false;}
		if(translatedBox.getBounds()[0].z > other.getTranslatedBox().getBounds()[1].z){return false;}
		if(translatedBox.getBounds()[1].y < other.getTranslatedBox().getBounds()[0].y){return false;}
		if(translatedBox.getBounds()[0].y > other.getTranslatedBox().getBounds()[1].y){return false;}
		
		return true;
	}
	
	public boolean isIntersect(Hitbox other, Vector3f myPos, float myScale, boolean doY)
	{
		if(box.getBounds()[1].x * myScale + myPos.x < other.getTranslatedBox().getBounds()[0].x){return false;}
		if(box.getBounds()[0].x * myScale + myPos.x > other.getTranslatedBox().getBounds()[1].x){return false;}
		if(box.getBounds()[1].z * myScale + myPos.z < other.getTranslatedBox().getBounds()[0].z){return false;}
		if(box.getBounds()[0].z * myScale + myPos.z > other.getTranslatedBox().getBounds()[1].z){return false;}
		if(doY){
			if(box.getBounds()[1].y * myScale + myPos.y < other.getTranslatedBox().getBounds()[0].y){return false;}
			if(box.getBounds()[0].y * myScale + myPos.y > other.getTranslatedBox().getBounds()[1].y){return false;}
		}
		
		return true;
	}
	
	/**
	 * Getter and setter for hitbox
	 */
	
	

	public boolean isSelected() {
		return selected;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Box getTranslatedBox() {
		return translatedBox;
	}

	public void setTranslatedBox(Box translatedBox) {
		this.translatedBox = translatedBox;
	}

	public void setRender(boolean isRender) {
		this.isRender = isRender;
	}

	public boolean isHover() {
		return isHover;
	}

	public void setHover(boolean isHover) {
		this.isHover = isHover;
	}
	
	
}
