package nParticles;


import org.lwjgl.util.vector.Vector3f;

public class MultiParticle{

	private Vector3f[] position, scale, velocity;
	private float gravity;
	private Vector3f color;
	private int lifeTime, size;
	
	public MultiParticle(Vector3f[] position, Vector3f[] scale,
			
			Vector3f velocity[], int life_length, float gravity, Vector3f color, int size) {
		
		this.position = position; 
		this.scale = scale;
		this.velocity = velocity;
		this.gravity = gravity;
		this.color = color;
		this.lifeTime = 0;
		this.size = size;
	}
	
	public void update(float yaw){
		
		for(int i = 0; i < size; i++){
			
			velocity[i].y += gravity;
			position[i].y += velocity[i].y;
			position[i].x += velocity[i].x;
			position[i].z += velocity[i].z;
		}
		
		lifeTime++;
	}
	
	public int getLifeTime(){
		return lifeTime;
	}

	public Vector3f[] getPosition() {
		return position;
	}

	public Vector3f[] getScale() {
		return scale;
	}

	public Vector3f[] getVelocity() {
		return velocity;
	}

	public float getGravity() {
		return gravity;
	}

	public Vector3f getColor() {
		return color;
	}
	
	public int getSize(){
		return size;
	}

	public void setPosition(Vector3f[] position) {
		this.position = position;
	}

	public void setScale(Vector3f[] scale) {
		this.scale = scale;
	}

	public void setVelocity(Vector3f[] velocity) {
		this.velocity = velocity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
	
	
}
