package nComponents;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Projectile extends Component {

	private Vector3f ray;
	private float speed = 50;
	private int timeOut;
	private List<Entity> entities;
	
	public void update(float dt) {
		Position pos = (Position)parent.getComponentByType(CompType.POSITION);
		pos.getPosition().x += ray.x * speed;
		pos.getPosition().y += ray.y * speed;
		pos.getPosition().z += ray.z * speed;
		timeOut--;
		if(timeOut == 0)
			this.entities.remove(parent);
	}

	public Projectile(Entity parent, Vector3f ray, float speed, int timeOut, List<Entity> entities){
		this.parent = parent;
		this.speed = speed;
		this.ray = ray;
		this.timeOut = timeOut;
		this.setType(CompType.PROJECTILE);
		this.entities = entities;
	}

	public Vector3f getRay() {
		return ray;
	}

	public void setRay(Vector3f ray) {
		this.ray = ray;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	
}
