package nToolbox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Clark
 * 
 * Reverses the shader matrix math to determine what the mouse is pointing at
 * implements this to calculate point on the terrain.
 * TODO add ray-box intersection
 */

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import toolbox.GameMath;

public class MousePicker {
	
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private static List<Entity> units_selected = new ArrayList<Entity>();
	
	private Ray cameraRay;
	
	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix, viewMatrix;
	private Entity camera;
	
	private Vector3f currentTerrainPoint;
	
	public MousePicker(Entity cam, Matrix4f projection){
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = GameMath.createViewMatrix(camera);
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public Vector3f getCurrentTerrainPoint(){
		return currentTerrainPoint;
	}
	
	public void update() {
		
		viewMatrix = GameMath.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		setCameraRay(new Ray(getCameraPosition(camera), currentRay));
		
		currentTerrainPoint = intersectionInRange(0, RAY_RANGE, currentRay) ?
				binarySearch(0,0,RAY_RANGE,currentRay) : null;
				
		units_selected.clear();
	}
	
	private Vector3f getCameraPosition(Entity camera){
		Position pos = (Position)camera.getComponentByType(CompType.POSITION);
		return pos.getPosition();
	}
	
	private Vector3f calculateMouseRay() {
		
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	/* ********************************************************************************* */
	
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Position pos = (Position)camera.getComponentByType(CompType.POSITION);
		Vector3f camPos = pos.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			return endPoint;
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnderGround(Vector3f testPoint) {
		float height = 0;
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	public Ray getCameraRay() {
		return cameraRay;
	}

	public void setCameraRay(Ray cameraRay) {
		this.cameraRay = cameraRay;
	}

	public static List<Entity> getUnits_selected() {
		return units_selected;
	}

	public static void setUnits_selected(List<Entity> units_selected) {
		MousePicker.units_selected = units_selected;
	}
}
