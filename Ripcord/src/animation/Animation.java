package animation;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Animation {

	public List<Vector4f[]> rotationData;
	public List<Vector3f> translationData;
	private int length = 1000;
	private int amountThrough = 0;
	private boolean loop;
	
	public Animation(List<Vector4f[]> rotationData, List<Vector3f> translationData, int length){
		
		this.translationData = translationData;
		this.rotationData = rotationData;
		this.length = length;
	}

	public List<Vector3f> getTranslationData() {
		return translationData;
	}

	public void setTranslationData(List<Vector3f> translationData) {
		this.translationData = translationData;
	}

	public List<Vector4f[]> getRotationData() {
		return rotationData;
	}

	public void setRotationData(List<Vector4f[]> rotationData) {
		this.rotationData = rotationData;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getAmountThrough() {
		return amountThrough;
	}

	public void setAmountThrough(int amountThrough) {
		this.amountThrough = amountThrough;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	
	
}
