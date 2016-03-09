package guis;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import toolbox.GameMath;

public final class GuiTexture {
	
	private int texture;
	private Vector2f position, scale;
	private Matrix4f matrix;
	private int actionID;
	private String actionData;
	private boolean frame=false;
	private boolean inArea=false;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	
		matrix = GameMath.createTransformationMatrix(position, scale);
	}

	public void changeY(float dy){
		this.position.y += dy;
		matrix = GameMath.createTransformationMatrix(position, scale);
	}
	
	public int getTexture() {
		return texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	
	public void setTexture(int texture){
		this.texture = texture;
	}
	public void setPosition(Vector2f position){
		this.position = position;
		matrix = GameMath.createTransformationMatrix(position, scale);
	}
	public void setscale(Vector2f scale){
		this.scale = scale;
		matrix = GameMath.createTransformationMatrix(position, scale);
	}

	public Matrix4f getMatrix() {
		return matrix;
	}

	public boolean isFrame() {
		return frame;
	}

	public void setFrame(boolean frame) {
		this.frame = frame;
	}

	public int getActionID() {
		return actionID;
	}

	public void setActionID(int actionID) {
		this.actionID = actionID;
	}

	public String getActionData() {
		return actionData;
	}

	public void setActionData(String actionData) {
		this.actionData = actionData;
	}

	public boolean isInArea() {
		return inArea;
	}

	public void setInArea(boolean inArea) {
		this.inArea = inArea;
	}
	
}
