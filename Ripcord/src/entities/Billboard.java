package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Billboard {

	private Vector3f position;
	private Vector2f scale;
	private Vector3f rotation;
	private TexturedModel model;
	
	public Billboard(Vector3f position, Vector2f scale, Vector3f rotation, TexturedModel model) {
		
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.model = model;
		
	}
	
	public void update(float yaw){
		rotation.y = -yaw;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
}
