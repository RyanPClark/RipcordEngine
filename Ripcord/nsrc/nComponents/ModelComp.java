package nComponents;

import models.TexturedModel;

public class ModelComp extends Component {

	TexturedModel txm;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
	}

	public ModelComp(Entity parent, TexturedModel model){
		
		this.setType(CompType.MODEL);
		this.setParent(parent);
		this.txm = model;
	}
	
	public TexturedModel getModel(){
		
		return txm;
	}
	
	public void setModel(TexturedModel txm){
		this.txm = txm;
	}
}
