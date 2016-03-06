package nComponents;

import models.RawModel;
import nRenderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Terrain extends Component {

	private static final int VERTEX_COUNT = 128;
	private static final float SIZE = 1.0f;
	
	private TerrainTexturePack texturePack;
	private TerrainTexture blendmap;
	private RawModel rModel;
	
	public void update(float dt) {
	}

	public Terrain(Entity parent, Loader loader,
			TerrainTexturePack texturePack, TerrainTexture blendmap){
		
		this.setType(CompType.TERRAIN);
		this.setParent(parent);
		
		rModel = generateTerrain(loader);
		this.texturePack = texturePack;
		this.blendmap = blendmap;
	}
	
	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	public RawModel getRawModel(){
		return rModel;
	}
	
	public TerrainTexture getBlendmap(){
		return blendmap;
	}
	
	public TerrainTexturePack getTexturePack(){
		return texturePack;
	}

	public void setTexturePack(TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
	}

	public void setBlendmap(TerrainTexture blendmap) {
		this.blendmap = blendmap;
	}

	public void setrModel(RawModel rModel) {
		this.rModel = rModel;
	}
	
}
