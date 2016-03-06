package nComponents;

public class TextureIndex extends Component {

	int index;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		// pass
	}

	public TextureIndex(Entity parent, int index){
		
		this.setType(CompType.TEXTURE_INDEX);
		this.setParent(parent);
		this.index = index;
	}
	
	public float getTextureXOffset(){
		Entity eParent = (Entity)parent;
		ModelComp mComp = (ModelComp)eParent.getComponentByType(CompType.MODEL);
		int column = index % mComp.getModel().getTexture().getNumberOfRows();
		return (float)column/(float)mComp.getModel().getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		Entity eParent = (Entity)parent;
		ModelComp mComp = (ModelComp)eParent.getComponentByType(CompType.MODEL);
		int row = index / mComp.getModel().getTexture().getNumberOfRows();
		return (float)row/(float)mComp.getModel().getTexture().getNumberOfRows();
	}
	
	public float getIndex(){
		
		return index;
	}
}
