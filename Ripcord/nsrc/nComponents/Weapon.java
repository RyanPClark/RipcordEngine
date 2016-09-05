package nComponents;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import nEngineTester.Input;

public class Weapon extends Component {

	private String projectileString = "POS:0:0:0 ROT:0:0:0 SCL:5 MDL:bullet:bullet:true PRJ:";
	
	public void update(float dt) {
		
		if(Input.wasClicked()){
			
			Entity projectile = new Entity();
			LoadEntity.newEntity(projectileString.split(" "), projectile);
			Position pos = (Position)projectile.getComponentByType(CompType.POSITION);
			Position parentPos = (Position)parent.getComponentByType(CompType.POSITION);
			pos.getPosition().x = parentPos.getAdditivePosition().x - 4;
			pos.getPosition().y = parentPos.getAdditivePosition().y;
			pos.getPosition().z = parentPos.getAdditivePosition().z;
			
			Rotation parentRot = (Rotation)parent.getComponentByType(CompType.ROTATION);
			Rotation rot = (Rotation)projectile.getComponentByType(CompType.ROTATION);
			rot.getRotation().x = parentRot.getAdditiveRotation().x;
			rot.getRotation().y = parentRot.getAdditiveRotation().y;
			rot.getRotation().z = parentRot.getAdditiveRotation().z;
			
			Projectile proj = (Projectile)projectile.getComponentByType(CompType.PROJECTILE);
			Vector3f addRot = rot.getAdditiveRotation();
			float x = (float) Math.sin(Math.toRadians(addRot.y));
			float z = (float) Math.cos(Math.toRadians(addRot.y));
			Random r = new Random();
			float variationX = (r.nextFloat()*2-1) / 100.0f;
			float variationY = (r.nextFloat()*2-1) / 100.0f;
			float variationZ = (r.nextFloat()*2-1) / 100.0f;
			proj.setRay(new Vector3f(x + variationX, variationY, z+variationZ));
			
			SoundComp sComp = (SoundComp)parent.getComponentByType(CompType.SOUND);
			sComp.playWithBuffer(0);
		}
	}
	
	public Weapon(Entity parent, String projString){
		if(projString != null)
			this.projectileString = projString;
		this.parent = parent;
		this.setType(CompType.WEAPON);
	}
	
}
