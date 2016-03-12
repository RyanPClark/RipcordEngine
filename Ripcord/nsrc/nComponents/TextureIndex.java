package nComponents;

/**
 * 
 * @author Ryan Clark
 * 
 * Stores a texture index and ability to calculate its offset for an entity.
 *
 */

public class TextureIndex extends Component {

	private int index;
	
	public void update(float dt) {}

	public TextureIndex(Entity parent, int index){
		
		this.setType(CompType.TEXTURE_INDEX);
		this.setParent(parent);
		this.index = index;
	}
	
	/**
	 * @return - the x value of a texture in a texture atlas
	 */
	public float getTextureXOffset(){
		Entity eParent = (Entity)parent;
		ModelComp mComp = (ModelComp)eParent.getComponentByType(CompType.MODEL);
		int column = index % mComp.getModel().getTexture().getNumberOfRows();
		return (float)column/(float)mComp.getModel().getTexture().getNumberOfRows();
	}
	
	/**
	 * @return - the y value of a texture in a texture atlas
	 */
	public float getTextureYOffset(){
		Entity eParent = (Entity)parent;
		ModelComp mComp = (ModelComp)eParent.getComponentByType(CompType.MODEL);
		int row = index / mComp.getModel().getTexture().getNumberOfRows();
		return (float)row/(float)mComp.getModel().getTexture().getNumberOfRows();
	}
	
	/**
	 * Getters and setters for the texture index
	 */
	
	public float getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
}
