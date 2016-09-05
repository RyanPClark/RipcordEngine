package nComponents;

/**
 * @author Ryan Clark
 * 
 * Stores a texture model. That's it.
 */

import models.TexturedModel;
import nRenderEngine.MasterRenderer;

public class ModelComp extends Component {

	private TexturedModel txm;
	private MasterRenderer mRenderer;
	
	public void update(float dt) {
		mRenderer.processEntity(parent);
	}

	public ModelComp(Entity parent, TexturedModel model, MasterRenderer mRenderer){
		
		this.setType(CompType.MODEL);
		this.setParent(parent);
		this.txm = model;
		this.mRenderer = mRenderer;
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
