package models;

import textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	private int partID;
	
	public TexturedModel (RawModel model, ModelTexture texture, int partID){
		this.rawModel = model;
		this.texture = texture;
		this.partID = partID;
		
	}
	
	public TexturedModel (RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}
	
	

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public int getPartID() {
		return partID;
	}
	
}
