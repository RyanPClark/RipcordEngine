package nComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import components.Statics;

/**
 * @author Ryan Clark
 * 
 * This class generates a terrain given a blendmap and its textures. In the future a
 * heightmap may be implemented but that is unncessary for now.
 */

import models.RawModel;
import nRenderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.GameMath;
import toolbox.MyPaths;

public class Terrain extends Component {

	private static final int VERTEX_COUNT = 128;
	private static final float SIZE = 800.0f * 8.0f / 7.0f;

	private float x, z, gridSquareSize;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendmap;
	private RawModel rModel;

	private float heights[][];
	
	public void update(float dt) {}

	public Terrain(Entity parent, float GridX, float GridZ, Loader loader,
			TerrainTexturePack texturePack, TerrainTexture blendmap, String heightmap){
		
		this.setType(CompType.TERRAIN);
		this.setParent(parent);
		
		this.rModel = generateTerrain(loader, heightmap);
		this.texturePack = texturePack;
		this.blendmap = blendmap;

		gridSquareSize = SIZE / ((float) heights.length - 1);
		
		this.x = GridX * SIZE - 0.5f*SIZE;
		this.z = GridZ * SIZE + 0.5f*SIZE;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ){
		
		float terrainX = worldX - x;
		float terrainZ = worldZ - z;
		
		int gridX = (int) Math.floor(terrainX/gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
		
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1
				|| gridX <0 || gridZ<0){
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = GameMath
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = GameMath
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
	}
	
	/**
	 * @param loader
	 * @return - flat RawModel with VAO ID and Vertex count
	 */
	
	private RawModel generateTerrain(Loader loader, String heightMap){
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(MyPaths.makeTexturePath(heightMap)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//VERTEX_COUNT = image.getHeight()/Statics.TERRAIN_RES;
		
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		int pointer = 0;
		
		for(int i = 0; i < VERTEX_COUNT; i++){
			for(int j = 0; j < VERTEX_COUNT; j++){
						
				vertices[vertexPointer*3] = (float) j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = getHeight(j, i, image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				
				heights [j][i] = vertices[vertexPointer*3+1];
						
				Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
				
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
	
	

	private float getHeight(int x, int z, BufferedImage image){
		if(x < 0 || x >= image.getHeight()/Statics.TERRAIN_RES ||z < 0 || z >= image.getHeight()/Statics.TERRAIN_RES ){
			return 0;
		}
		float height = image.getRGB(Statics.TERRAIN_RES * x, Statics.TERRAIN_RES * z);
		height += Statics.TERRAIN_MAX_PIXEL_COLOR/2f;
		height /= Statics.TERRAIN_MAX_PIXEL_COLOR/2f;
		height*= Statics.TERRAIN_MAX_HEIGHT;
		return height;
		
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight((x-1), z, image);
		float heightR = getHeight((x+1), z, image);
		float heightD = getHeight(x, (z-1), image);
		float heightU = getHeight(x, (z+1), image);
		
		Vector3f normalVector = new Vector3f(heightL-heightR, 2f, heightD-heightU);
		normalVector.normalise();
		
		return normalVector;
	}
	
	/**
	 * Getters and setters for the model and textures.
	 */
	
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

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	
}
