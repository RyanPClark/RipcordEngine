package nShaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import nComponents.Color;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import toolbox.GameMath;

public class StaticShader extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_reflectivity;
	private int location_shine_damper;
	private int location_intensity;
	private int location_numberOfRows;
	private int location_offset;
	
	
	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		
		location_transformationMatrix = super.getUniformLocation("transformation_matrix");
		location_projectionMatrix = super.getUniformLocation("projection_matrix");
		location_viewMatrix = super.getUniformLocation("view_matrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_shine_damper = super.getUniformLocation("shine_damper");
		location_intensity = super.getUniformLocation("intensity");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}

	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadNumberOfRows(int numberOfRows){
		super.loadInt(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y){
		super.loadVector2f(location_offset, new Vector2f(x,y));
	}
	
	public void loadShineVariables(float reflectivity, float shine_damper){
		
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_shine_damper, shine_damper);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Entity camera){
		super.loadMatrix(location_viewMatrix, GameMath.createViewMatrix(camera));
	}
	
	public void loadLight(Entity light){
		
		Position pComp = (Position)(light.getComponentByType(CompType.POSITION));
		Color cComp = (Color)light.getComponentByType(CompType.COLOR);
		
		super.loadVector(location_lightPosition, pComp.getPosition());
		super.loadVector(location_lightColor, cComp.getColor());
		super.loadFloat(location_intensity, cComp.getAlpha());
	}
}
