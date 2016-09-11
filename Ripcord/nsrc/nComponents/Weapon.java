package nComponents;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import audio.SoundComp;

public class Weapon extends Component {

	private String projectileString = "NME:projectile POS:0:0:0 ROT:0:0:0 SCL:5 MDL:bullet:bullet:true PRJ:10:1:100";
	private Vector3f fireOffset = new Vector3f(0,0,0);
	private Vector3f offset = new Vector3f(0,0,0);
	private float maxAngleDelta = 100;
	private int ROF = 10;
	private boolean isFiring;
	private int counter = ROF;
	private float power = 1;
	private float speed = 10;
	private int bulletLifeLength = 100;
	private float scale = 5;
	
	public void update(float dt) {}
	
	public void pressTrigger(){
		
		counter--;
		if(counter == 0){
			fire();
			counter = ROF;
		}
	}
	
	private String generateProjString(){
		String ret = "NME:projectile POS:0:0:0 ROT:0:0:0 SCL:" + scale + " MDL:bullet:bullet:true PRJ:"
	+ speed + ":" + power + ":" + bulletLifeLength;
		return ret;
	}
	
	private void fire(){
		
		Entity projectile = new Entity();
		LoadEntity.newEntity(projectileString.split(" "), projectile);
		Position pos = (Position)projectile.getComponentByType(CompType.POSITION);
		Position parentPos = (Position)parent.getComponentByType(CompType.POSITION);
		pos.getPosition().x = parentPos.getAdditivePosition().x + offset.x;
		pos.getPosition().y = parentPos.getAdditivePosition().y + offset.y;
		pos.getPosition().z = parentPos.getAdditivePosition().z + offset.z;
		
		Rotation parentRot = (Rotation)parent.getComponentByType(CompType.ROTATION);
		Rotation rot = (Rotation)projectile.getComponentByType(CompType.ROTATION);
		rot.getRotation().x = parentRot.getAdditiveRotation().x;
		rot.getRotation().y = parentRot.getAdditiveRotation().y;
		rot.getRotation().z = parentRot.getAdditiveRotation().z;
		
		Projectile proj = (Projectile)projectile.getComponentByType(CompType.PROJECTILE);
		Vector3f addRot = rot.getAdditiveRotation();
		float x = (float) Math.sin(Math.toRadians(addRot.y));
		float y = -addRot.x / 90;
		float z = (float) Math.cos(Math.toRadians(addRot.y));
		Random r = new Random();
		float variationX = (r.nextFloat()*2-1) / 100.0f;
		float variationY = (r.nextFloat()*2-1) / 100.0f;
		float variationZ = (r.nextFloat()*2-1) / 100.0f;
		proj.setRay(new Vector3f(x + variationX, y + variationY, z+variationZ));
		proj.setShooter(parent);
		
		SoundComp sComp = (SoundComp)parent.getComponentByType(CompType.SOUND);
		sComp.playWithBuffer(0);
	}
	
	public Weapon(Entity parent, Vector3f offset, Vector3f fireOffset, int ROF, float power, float speed,
			int lifeLength, float bulletScale){
		this.parent = parent;
		this.offset = offset;
		this.fireOffset = fireOffset;
		this.ROF = ROF;
		this.power = power;
		this.speed = speed;
		this.bulletLifeLength = lifeLength;
		this.scale = bulletScale;
		this.setType(CompType.WEAPON);
		projectileString = generateProjString();
	}
	
	public boolean isFiring() {
		return isFiring;
	}

	public void setFiring(boolean isFiring) {
		this.isFiring = isFiring;
	}

	public int getROF() {
		return ROF;
	}

	public void setROF(int rOF) {
		ROF = rOF;
	}

	public float getMaxAngleDelta() {
		return maxAngleDelta;
	}

	public void setMaxAngleDelta(float maxAngleDelta) {
		this.maxAngleDelta = maxAngleDelta;
	}

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}

	public Vector3f getFireOffset() {
		return fireOffset;
	}

	public void setFireOffset(Vector3f fireOffset) {
		this.fireOffset = fireOffset;
	}
	
	
	
	
	
	
}
