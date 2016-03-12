package nComponents;

/**
 * 
 * @author Ryan Clark
 *
 * Enum of components implemented so far
 *
 */

public enum CompType {

	POSITION(0), ROTATION(1), SCALE(2), MODEL(3), KEY_CONTROL(4), COLOR(5),
	TERRAIN(6), TEXTURE_INDEX(7);
	
	public final int type;
	
	CompType(int type){
		this.type = type;
	}
}
