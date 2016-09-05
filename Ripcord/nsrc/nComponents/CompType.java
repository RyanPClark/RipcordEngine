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
	TERRAIN(6), TEXTURE_INDEX(7), HIT_BOX(8), PICKER_CONTROL(9), IDLE(10),
	MINIMAP_CONTROL(11), ANIMATION(12), SOUND(13), EMITTER(14), PROJECTILE(15),
	WEAPON(16);
	
	public final int type;
	
	CompType(int type){
		this.type = type;
	}
}
