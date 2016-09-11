package nParticles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import nComponents.CompType;
import nComponents.Component;
import nComponents.Entity;
import nComponents.Position;
import nComponents.Rotation;
import nRenderEngine.MasterRenderer;

public class ParticleEmitter extends Component {

	private Vector3f position, velocity;
	private int hz, lifeLength;
	private float gravity;
	private Vector3f color;
	private List<MultiParticle> particles = new ArrayList<MultiParticle>();
	private Vector3f scale;
	private Random r;
	private boolean emitting;
	private MasterRenderer mRenderer;
	private Rotation cameraRot;
	
	public ParticleEmitter (Entity parent, Vector3f position,
			
			Vector3f velocity, int hz, int life_length, float gravity, Vector3f color, Vector3f scale) {
		
		this.position = position; 
		this.velocity = velocity;
		this.hz = hz;
		this.lifeLength = life_length;
		this.gravity = gravity;
		this.color = color;
		this.scale = scale;
		this.setType(CompType.EMITTER);
		
		r = new Random();
		this.parent = parent;
	}
	
	public ParticleEmitter (Entity parent, Vector3f relPosition, String type, MasterRenderer mRenderer, Rotation camRot) {
		
		if(type.equals("BLOOD")){
			this.velocity = new Vector3f(8,3,8);
			this.hz = 12;
			this.lifeLength = 50;
			this.gravity = -0.01f;
			this.color = new Vector3f(1,0,0);
			this.scale = new Vector3f(0.09f, 0.09f, 0.09f);
		}else if(type.equals("FIRE")){
			this.velocity = new Vector3f(10,6,10);
			this.hz = 3;
			this.lifeLength = 60;
			this.gravity = 0.001f;
			this.color = new Vector3f(1,0.6f,0);
			this.scale = new Vector3f(0.08f, 0.08f, 0.08f);
		}else if(type.equals("SMOKE")){
			this.velocity = new Vector3f(8,12,8);
			this.hz = 6;
			this.lifeLength = 120;
			this.gravity = 0.0f;
			this.color = new Vector3f(0.5f,0.5f,0.5f);
			this.scale = new Vector3f(0.08f, 0.08f, 0.08f);
		}
		
		this.position = relPosition; 
		this.parent = parent;
		this.mRenderer = mRenderer;
		this.cameraRot = camRot;
		this.setType(CompType.EMITTER);
		
		r = new Random();
	}
	
	public void update(float dt){
		
		if (emitting){
			addParticles();
			emitting = !emitting;
		}
		
		for (MultiParticle particle : particles){
			particle.update(cameraRot.getRotation().getY());
			mRenderer.processParticle(particle);
		}
		
		removeParticles();
	}
	
	private void removeParticles(){
		
		for(int i = 0; i < particles.size(); i++){
			
			if(particles.get(i).getLifeTime() >= lifeLength){
				particles.remove(i);
			}
		}
	}
	
	private void addParticles(){
		
		Vector3f[] velocities = new Vector3f[hz];
		Vector3f[] positions = new Vector3f[hz];
		Vector3f[] scales = new Vector3f[hz];
		
		Position parentPos = (Position)parent.getComponentByType(CompType.POSITION);
		Vector3f parentVecPos = parentPos.getAdditivePosition();
		
		for(int i = 0; i < hz; i++){
			positions[i] = new Vector3f(parentVecPos.x + position.x, parentVecPos.y + position.y, parentVecPos.z + position.z);
			velocities[i] = new Vector3f(r.nextFloat()/velocity.x - 0.5f/velocity.x,r.nextFloat()/velocity.y,r.nextFloat()/velocity.z - 0.5f/velocity.z);
			scales[i] = scale;
		}
		
		particles.add(new MultiParticle(positions, scales, velocities, lifeLength, gravity, color, hz));
		
	}

	
	
	public boolean isEmitting() {
		return emitting;
	}

	public void setEmitting(boolean emitting) {
		this.emitting = emitting;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public float getHz() {
		return hz;
	}

	public void setHz(int hz) {
		this.hz = hz;
	}

	public int getLifeLength() {
		return lifeLength;
	}

	public void setLifeLength(int lifeLength) {
		this.lifeLength = lifeLength;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
}
