package nComponents;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import nParticles.ParticleEmitter;

public class Projectile extends Component {

	private Vector3f ray;
	private float speed = 10;
	private int timeOut;
	private List<Entity> entities;
	private Entity shooter;
	private Hitbox myHitbox;
	private float power;
	
	public void update(float dt) {
		Position pos = (Position)parent.getComponentByType(CompType.POSITION);
		pos.getPosition().x += ray.x * speed;
		pos.getPosition().y += ray.y * speed;
		pos.getPosition().z += ray.z * speed;
		timeOut--;
		if(timeOut == 0)
			this.entities.remove(parent);

		Position myPos = (Position)parent.getComponentByType(CompType.POSITION);
		Scale myScale = (Scale)parent.getComponentByType(CompType.SCALE);
		Vector3f myAddPos = myPos.getAdditivePosition();
		float myMultScale = myScale.getMultiplicativeScale();
		
		
		for(int i = 0; i < entities.size(); i++){
			Entity rootEntity = entities.get(i);
			Entity entity = rootEntity;
			
			if(rootEntity == parent ||rootEntity == shooter.parent || rootEntity == shooter)
				continue;
			
			for(int j = -1; j < rootEntity.getChildren().size(); j++){
				
				
				if(j != -1)
					entity = rootEntity.getChildren().get(j);
				
				if(entity.getName() != null)
					if(entity.getName().equals("projectile"))
						continue;
				
				Hitbox hbox = (Hitbox)entity.getComponentByType(CompType.HIT_BOX);
				if(hbox != null){
					
					if(myHitbox.isIntersect(hbox, myAddPos, myMultScale, false)){
						
						ParticleEmitter emitter = (ParticleEmitter)rootEntity.getComponentByType(CompType.EMITTER);
						if(emitter != null){
							emitter.setEmitting(true);
						}
						HealthComp healthComp = (HealthComp)rootEntity.getComponentByType(CompType.HEALTH);
						if(healthComp != null){
							healthComp.decreaseHealth(power);
							if(healthComp.getHealth() <= 0){
								Death death = (Death)rootEntity.getComponentByType(CompType.DEATH);
								death.kill();
							}
						}
						this.entities.remove(parent);
					}
				}
			}
		}
	}

	public Projectile(Entity parent, Vector3f ray, float speed, float power, int timeOut, List<Entity> entities){
		this.parent = parent;
		this.speed = speed;
		this.power = power;
		this.ray = ray;
		this.timeOut = timeOut;
		this.setType(CompType.PROJECTILE);
		this.entities = entities;
		this.myHitbox = (Hitbox)parent.getComponentByType(CompType.HIT_BOX);
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

	public Entity getShooter() {
		return shooter;
	}

	public void setShooter(Entity shooter) {
		this.shooter = shooter;
	}

	
}
