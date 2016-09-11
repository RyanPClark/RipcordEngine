package particles;



import org.lwjgl.util.vector.Vector3f;



import nRenderEngine.DisplayManager;



public class Particle {



	private Vector3f position;

	private Vector3f velocity;

	public float gravity;

	private float lifeLength;

	private float scale;

	private float rotation;

	private float elapsedTime;

	

	public Particle(Vector3f vector3f, Vector3f velocity, float gravityComplient, float lifeLength, int i, int j) {

		// TODO Auto-generated constructor stub

		position = vector3f;

		this.velocity = velocity;

		this.gravity = gravityComplient;

		this.lifeLength = lifeLength;

		this.scale = i;

		this.rotation = j;

		ParticleMaster.addParticle(this);

	}



	protected boolean update(){

		velocity.y += gravity * DisplayManager.getDelta();

		Vector3f change = new Vector3f(velocity);

		change.scale(DisplayManager.getDelta());

		Vector3f.add(change, position, position);

		elapsedTime += DisplayManager.getDelta();

		return elapsedTime < lifeLength;

	}

	

	protected Vector3f getPosition() {

		return position;

	}



	protected Vector3f getVelocity() {

		return velocity;

	}



	protected float getLifeLength() {

		return lifeLength;

	}



	protected float getGravity() {

		return gravity;

	}



	protected float getScale() {

		return scale;

	}



	protected float getRotation() {

		return rotation;

	}



	protected float getElapsedTime() {

		return elapsedTime;

	}



	

}