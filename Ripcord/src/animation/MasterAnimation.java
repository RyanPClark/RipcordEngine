package animation;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import loaders.AnimationLoader;
import maps.Saving;

public class MasterAnimation {

	public static Animation[] animations = new Animation[1];
	
	public static void init(){
		
		for(int i = 0; i < 1; i++){
			
			addAnimation(i);
		}
	}
	
	public static void addAnimation(int ID){
		
		List<Vector4f[]> rotation = AnimationLoader.readRotation("animation", ID);
		List<Vector3f> translation = AnimationLoader.readTranslation("animation", ID);
		int length = AnimationLoader.readLength("animation", ID);
		
		animations[ID] = new Animation(rotation, translation, length);
	}
	
	public static void cleanUp(){
		
		for(int i = 0; i < animations.length; i++){
			
			Saving.saveAnimation(animations[i], "Animations/animation"+i);
		}
	}
}
