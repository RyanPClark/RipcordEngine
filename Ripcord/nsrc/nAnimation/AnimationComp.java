package nAnimation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import nComponents.CompType;
import nComponents.Component;
import nComponents.Entity;
import nComponents.Position;
import nComponents.Rotation;

public class AnimationComp extends Component {

	private List<Animation> animations = new ArrayList<Animation>();
	private int currentAnimation = -1;
	
	public void update(float dt) {
		if(currentAnimation != -1)
		{
			playAnimation(currentAnimation);
		}
	}
	
	public void setAnimationByName(String name){
		for(int i = 0; i < animations.size(); i++){
			if(animations.get(i).getName().equals(name)){
				currentAnimation = i;
				return;
			}
		}
	}
	
	public void stopAll(){
		currentAnimation = -1;
	}
	
	public void stopAnimationByName(String name){
		for(int i = 0; i < animations.size(); i++){
			if(animations.get(i).getName().equals(name)){
				currentAnimation = -1;
				return;
			}
		}
	}
	
	// List of animations
	  // List of Keyframes
	    // Rotation and position offset
	  // Length of animation
	
	public AnimationComp(Entity parent, Animation anim, int currentAnim){
		this.parent = parent;
		this.animations.add(anim);
		this.setType(CompType.ANIMATION);
		this.currentAnimation = currentAnim;
	}
	
	public void setCurrentAnimation(int animNum){
		this.currentAnimation = animNum;
	}
	
	private void playAnimation(int animNum){
		// List of Keyframes & length
		// Rotation frame = 4 component vector
		// Position frame = 3 component vector
		// Start with two frames
		
		Rotation parentRot = (Rotation)parent.getComponentByType(CompType.ROTATION);
		float y = parentRot.getRotation().y;
		float sinY = (float)Math.sin(Math.toRadians(y));
		float cosY = (float)Math.cos(Math.toRadians(y));
		
		Animation currentAnim = animations.get(animNum);
		int amountThrough = currentAnim.getAmountThrough();
		int length = currentAnim.getLength();
		currentAnim.setAmountThrough(currentAnim.getAmountThrough()+currentAnim.getDirection());
		
		if(amountThrough >= length || amountThrough < 0){
			
			boolean loop = currentAnim.isLoop();
			
			if(!loop)
				currentAnimation = -1;
			if(!currentAnim.isThroughAndBack())
				currentAnim.setAmountThrough(0);
			else{
				currentAnim.setDirection(currentAnim.getDirection() * -1);
				currentAnim.setAmountThrough(currentAnim.getAmountThrough()+currentAnim.getDirection()*2);
			}
			
			return;
		}
		
		Vector4f[] rotationKeyFrame0 = currentAnim.getRotationData().get(0);
		Vector4f[] rotationKeyFrame1 = currentAnim.getRotationData().get(1);
		// interpolate each part of vector
		Vector4f[] interpolatedRotation = new Vector4f[rotationKeyFrame0.length];
		for(int i = 0; i < interpolatedRotation.length; i++){
			
			Entity ent = parent.getChildren().get(i);
			interpolatedRotation[i] = new Vector4f(0,0,0,0);
			
			interpolatedRotation[i].x = ((float)(1-amountThrough)/length)*rotationKeyFrame0[i].x + ((float)amountThrough/length)*rotationKeyFrame1[i].x;
			interpolatedRotation[i].y = ((float)(1-amountThrough)/length)*rotationKeyFrame0[i].y + ((float)amountThrough/length)*rotationKeyFrame1[i].y;
			interpolatedRotation[i].z = ((float)(1-amountThrough)/length)*rotationKeyFrame0[i].z + ((float)amountThrough/length)*rotationKeyFrame1[i].z;
			
			Rotation rot = (Rotation)ent.getComponentByType(CompType.ROTATION);
			rot.getRotation().setX(interpolatedRotation[i].x);
			rot.getRotation().setY(interpolatedRotation[i].y);
			rot.getRotation().setZ(interpolatedRotation[i].z);
		}
		
		Vector3f[] translationKeyFrame0 = currentAnim.getTranslationData().get(0);
		Vector3f[] translationKeyFrame1 = currentAnim.getTranslationData().get(1);
		// interpolate each part of vector
		Vector3f[] interpolatedtranslation = new Vector3f[translationKeyFrame0.length];
		for(int i = 0; i < interpolatedtranslation.length; i++){
			
			Entity ent = parent.getChildren().get(i);
			interpolatedtranslation[i] = new Vector3f(0,0,0);
			
			interpolatedtranslation[i].x = ((float)(1-amountThrough)/length)*translationKeyFrame0[i].x + ((float)amountThrough/length)*translationKeyFrame1[i].x;
			interpolatedtranslation[i].y = ((float)(1-amountThrough)/length)*translationKeyFrame0[i].y + ((float)amountThrough/length)*translationKeyFrame1[i].y;
			interpolatedtranslation[i].z = ((float)(1-amountThrough)/length)*translationKeyFrame0[i].z + ((float)amountThrough/length)*translationKeyFrame1[i].z;
			
			Position pos = (Position)ent.getComponentByType(CompType.POSITION);
			pos.getPosition().setX(interpolatedtranslation[i].x * cosY + interpolatedtranslation[i].z * sinY);
			pos.getPosition().setY(interpolatedtranslation[i].y);
			pos.getPosition().setZ(interpolatedtranslation[i].z * cosY + interpolatedtranslation[i].x * sinY);
		}
	}

	public List<Animation> getAnimations() {
		return animations;
	}

	public void setAnimations(List<Animation> animations) {
		this.animations = animations;
	}

	public int getCurrentAnimation() {
		return currentAnimation;
	}
	
	
}
