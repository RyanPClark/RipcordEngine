package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class AnimatedEntity extends Entity{
	
	private int timeBetweenAnimations;
	private int counter;
	private boolean forward;
	private float tween;

	public AnimatedEntity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, Vector3f scale, int textureIndex, String tag, boolean Static, int timeBetweenAnimations) {
		
		super(model, position, rotX, rotY, rotZ, scale, tag, Static);
		
		this.timeBetweenAnimations = timeBetweenAnimations;
		this.counter = 0;
		this.forward = true;
		this.tween = 0;
	}

	
	public void update(){
		
		if (forward){
			counter++;
			if (counter == timeBetweenAnimations){
				forward = false;
			}
		} else {
			counter--;
			if (counter == 0){
				forward = true;
			}
		}
		
		tween = (float)counter/timeBetweenAnimations;
		
	}
	
	public float getTween(){
		return tween;
	}
	
	
}
