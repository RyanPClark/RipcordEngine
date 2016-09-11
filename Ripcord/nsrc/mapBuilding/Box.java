package mapBuilding;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import nComponents.Position;
import nRenderEngine.Loader;
import textures.ModelTexture;

/**
 * Stores hitbox data for rendering, collisions, and entity selection
 * 
 * @author Ryan Clark
 */

public class Box {
	
	/**
	 * bounds[0] is the x,y, and z coords of the bottom, bounds[1] is that of top
	 */
	
	private Vector3f bounds[] = new Vector3f[2];
	private TexturedModel model;
	private Position parentPos;
	private Vector3f middle = new Vector3f();
	
	public Box(){}
	
	public Box(Vector3f min, Vector3f max){
		bounds[0] = min;
		bounds[1] = max;
	}
	
	public Box(float max_y, float min_y, float min_x, float max_x, float min_z, float max_z) {
		
		bounds[0] = new Vector3f(min_x, min_y, min_z);
		bounds[1] = new Vector3f(max_x, max_y, max_z);
	}
	
	/**
	 * @param loader  - OpenGL loader for RawModel generation
	 * @param texName - URL of texture file relative to res/img/ subfolder
	 */
	
	public void generateModel(Loader loader){
		float[] vertices = {
			bounds[0].x, bounds[1].y, bounds[0].z,
			bounds[0].x, bounds[0].y, bounds[0].z,
			bounds[1].x, bounds[0].y, bounds[0].z,
			bounds[1].x, bounds[1].y, bounds[0].z,
				
			bounds[0].x, bounds[1].y, bounds[1].z,
			bounds[0].x, bounds[0].y, bounds[1].z,
			bounds[1].x, bounds[0].y, bounds[1].z,
			bounds[1].x, bounds[1].y, bounds[1].z
		};
		float[] textureCoords = {
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0
		};
		float[] normals = {
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f
		};
		int[] indices = {
			0,1,3,
			3,1,2,
			0,3,4,
			3,4,7
		};
		model = new TexturedModel(
				loader.loadToVAO(vertices, textureCoords, normals, indices),
				new ModelTexture(-1)
			);
		calculateMiddle();
		
		//System.out.println("MIN: " + bounds[0].x + ", " + bounds[0].y + ", " + bounds[0].z);
		//System.out.println("MAX: " + bounds[1].x + ", " + bounds[1].y + ", " + bounds[1].z);
	}

	public void calculateMiddle(){
		middle.x = (bounds[0].x + bounds[1].x) / 2.0f;
		middle.y = (bounds[0].y + bounds[1].y) / 2.0f;
		middle.z = (bounds[0].z + bounds[1].z) / 2.0f;
	}
	
	/**
	 * Getters and setters for data
	 */
	
	public TexturedModel getModel() {
		return model;
	}

	public Vector3f[] getBounds() {
		return bounds;
	}

	public void setBounds(Vector3f[] bounds) {
		this.bounds = bounds;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Position getParentPos() {
		return parentPos;
	}

	public void setParentPos(Position parentPos) {
		this.parentPos = parentPos;
	}

	public Vector3f getMiddle() {
		return middle;
	}

	public void setMiddle(Vector3f middle) {
		this.middle = middle;
	}
	
	
}
