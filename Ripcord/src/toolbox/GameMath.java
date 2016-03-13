package toolbox;

/**
 * TODO Add scaling, positioning, and rotating to hitbox intersection algorithm
 * 
 * Random math used in the game can be found here
 * 
 * @author Ryan Clark
 */

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import mapBuilding.Box;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import nComponents.Rotation;
import nComponents.Scale;
import nToolbox.Ray;

public final class GameMath {
	
	private static final Vector3f rotation1 = new Vector3f(1, 0, 0);
	private static final Vector3f rotation2 = new Vector3f(0, 1, 0);
	private static final Vector3f rotation3 = new Vector3f(0, 0, 1);
	
	private static final float PI = 3.14159265f;
	
	public static boolean intersection(Box b, Ray r, Entity camera){
		
		Vector3f min3 = new Vector3f(b.getBounds()[0]);
		Vector3f max3 = new Vector3f(b.getBounds()[1]);
		
		/* Load transformation variables */
		
		Scale cScale = (Scale)camera.getComponentByType(CompType.SCALE);
		float scale = cScale.getScale();
		
		Rotation cRot = (Rotation)camera.getComponentByType(CompType.ROTATION);
		Vector3f rotation = cRot.getRotation();
		
		Position cPos = (Position)camera.getComponentByType(CompType.POSITION);
		Vector3f position = cPos.getPosition();
		
		Matrix4f translationMatrix = GameMath.createTransformationMatrix(position, rotation.x, rotation.y, rotation.z, new Vector3f(scale,scale,scale));
		Vector4f min = new Vector4f(min3.x,min3.y,min3.z,1);
		Vector4f max = new Vector4f(max3.x,max3.y,max3.z,1);
		
		Matrix4f.transform(translationMatrix, min, min);
		Matrix4f.transform(translationMatrix, max, max);
		
		/* Calculate intersection */
		
		float tx1 = (min.x-r.getOrigin().x)*r.getInv_direction().x;
		float tx2 = (max.x-r.getOrigin().x)*r.getInv_direction().x;
		
		float tmin = Math.min(tx1, tx2);
		float tmax = Math.max(tx1, tx2);
		
		float ty1 = (min.y-r.getOrigin().y)*r.getInv_direction().y;
		float ty2 = (max.y-r.getOrigin().y)*r.getInv_direction().y;
		
		tmin = Math.max(tmin, Math.min(ty1, ty2));
		tmax = Math.min(tmax, Math.max(ty1, ty2));
		
		float tz1 = (min.z-r.getOrigin().z)*r.getInv_direction().z;
		float tz2 = (max.z-r.getOrigin().z)*r.getInv_direction().z;
		
		tmin = Math.max(tmin, Math.min(tz1, tz2));
		tmax = Math.min(tmax, Math.max(tz1, tz2));
		
		return tmax >= tmin && tmax >= 0;
	}
	
	public static boolean inBox(Box box, Ray r, float t0, float t1){
		
		float tmin, tmax, tymin, tymax, tzmin, tzmax;
		
		tmin = (box.getBounds()[r.getSign()[0]].x - r.getOrigin().x) * r.getInv_direction().x;
		tmax = (box.getBounds()[1-r.getSign()[0]].x - r.getOrigin().x) * r.getInv_direction().x;
		tymin = (box.getBounds()[r.getSign()[1]].y - r.getOrigin().y) * r.getInv_direction().y;
		tymax = (box.getBounds()[1-r.getSign()[1]].y - r.getOrigin().y) * r.getInv_direction().y;
		
		if ( (tmin > tymax) || (tymin > tmax) )
			return false;
		if (tymin > tmin)
			tmin = tymin;
		if (tymax < tmax)
			tmax = tymax;

		tzmin = (box.getBounds()[r.getSign()[2]].z - r.getOrigin().z) * r.getInv_direction().z;
		tzmax = (box.getBounds()[1-r.getSign()[2]].z - r.getOrigin().z) * r.getInv_direction().z;
		
		System.out.println((tmin > tzmax) + ", " + (tzmin > tmax));
		
		if ( (tmin > tzmax) || (tzmin > tmax) )
			return false;
		if (tzmin > tmin)
			tmin = tzmin;
		if (tzmax < tmax)
			tmax = tzmax;
		
		return ( (tmin < t1) && (tmax > t0) );
	}
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(toRadians(rx), rotation1, matrix, matrix);
		Matrix4f.rotate(toRadians(ry), rotation2, matrix, matrix);
		Matrix4f.rotate(toRadians(rz), rotation3, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, float ry, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(toRadians(ry), rotation2, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate(toRadians(camera.getPitch()), rotation1, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(camera.getYaw()), rotation2, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(camera.getRoll()), rotation3, viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	public static Matrix4f createViewMatrix(Entity camera){
		
		Rotation rComp = (Rotation)camera.getComponentByType(CompType.ROTATION);
		Vector3f rotation = rComp.getRotation();
		
		Position pComp = (Position)camera.getComponentByType(CompType.POSITION);
		Vector3f position = pComp.getPosition();
		
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate(toRadians(rotation.getX()), rotation1, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(rotation.getY()), rotation2, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(rotation.getZ()), rotation3, viewMatrix, viewMatrix);
		Vector3f cameraPos = position;
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	private static float sign (Vector2f p1, Vector2f p2, Vector2f p3){
		return (p1.x-p3.x)*(p2.y-p3.y)-(p2.x-p3.x)*(p1.y-p3.y);
	}
	public static boolean barryCentric(Vector2f pt, Vector2f v1, Vector2f v2, Vector2f v3, int i){
		
		boolean b1, b2, b3;
		
		b1 = sign(pt, v1, v2) < i;
		b2 = sign(pt, v2, v3) < i;
		b3 = sign(pt, v3, v1) < i;
		
		return ((b1 == b2) && (b2 == b3));
	}
	public static float dotProd(Vector3f a, Vector3f b){
		
		return a.x * b.x + a.y *b.y + a.z * b.z;
	}
	public static float dotProd(Vector4f a, Vector4f b){
		
		return a.x * b.x + a.y *b.y + a.z * b.z + a.w * b.w;
	}
	public static float dotProd(Vector2f a, Vector2f b){
		
		return a.x * b.x + a.y *b.y;
	}
	public static float crossProd(Vector2f a, Vector2f b){
		
		return a.x * b.y - a.y * b.x;
	}
	private static float toRadians(float input){
		
		return PI * input / 180;
	}
}
