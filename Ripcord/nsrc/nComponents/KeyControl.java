package nComponents;

/**
 * @author Ryan Clark
 * 
 * Keyboard input for camera movement.
 */

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class KeyControl extends Component {

	private float speed = 125.0f;
	
	/**
	 * @param dt - delta time
	 * 
	 * Updates the Position of the parent entity based on the rotation and keyboard inputs
	 */
	public void update(float dt) {
		
		Entity ent = (Entity)getParent();
		Position pos = (Position)ent.getComponentByType(CompType.POSITION);
		Rotation rot = (Rotation)ent.getComponentByType(CompType.ROTATION);
		
		
		Vector3f camDirection = rot.getRotation();
		Vector3f direction = new Vector3f(0,0,0);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			direction.x -= 1 * Math.cos(Math.toRadians(camDirection.y));
			direction.z -= 1 * Math.sin(Math.toRadians(camDirection.y));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			direction.x += 1 * Math.cos(Math.toRadians(camDirection.y));
			direction.z += 1 * Math.sin(Math.toRadians(camDirection.y));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			direction.x -= 1 * Math.cos(Math.toRadians(camDirection.y+90));
			direction.z -= 1 * Math.sin(Math.toRadians(camDirection.y+90));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			direction.x += 1 * Math.cos(Math.toRadians(camDirection.y+90));
			direction.z += 1 * Math.sin(Math.toRadians(camDirection.y+90));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			direction.y += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			direction.y -= 1;
		}
		
		float nx = pos.getPosition().getX()+direction.x * dt * speed;
		float ny = pos.getPosition().getY()+direction.y * dt * speed;
		float nz = pos.getPosition().getZ()+direction.z * dt * speed;
		
		if(nx < -360 || nx > 360)
			nx = pos.getPosition().getX();
		
		if(nz < -360 || nz > 360)
			nz = pos.getPosition().getZ();
		
		if(ny < 50 || ny > 175)
			ny = pos.getPosition().getY();
		
		
		pos.getPosition().setX(nx);
		pos.getPosition().setY(ny);
		pos.getPosition().setZ(nz);
	}

	public KeyControl(Entity parent, float speed){
		
		this.setType(CompType.KEY_CONTROL);
		this.setParent(parent);
		this.speed = speed;
	}
}
