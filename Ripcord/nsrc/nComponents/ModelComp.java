package nComponents;

/**
 * @author Ryan Clark
 * 
 * Stores a texture model. That's it.
 */

import models.TexturedModel;

public class ModelComp extends Component {

	private TexturedModel txm;
	
	public void update(float dt) {}

	public ModelComp(Entity parent, TexturedModel model){
		
		this.setType(CompType.MODEL);
		this.setParent(parent);
		this.txm = model;
	}
	
	/**
	 * Getter and setter.
	 */
	
	public TexturedModel getModel(){
		return txm;
	}
	
	public void setModel(TexturedModel txm){
		this.txm = txm;
	}
}
