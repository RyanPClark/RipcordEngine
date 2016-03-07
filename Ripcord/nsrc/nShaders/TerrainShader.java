package nShaders;

import org.lwjgl.util.vector.Matrix4f;

import nComponents.Color;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import toolbox.GameMath;

public class TerrainShader extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_intensity;
	private int location_backgroundSampler;
	private int location_rSampler;
	private int location_gSampler;
	private int location_bSampler;
	private int location_blendMap;
	private int location_shadowMapSpace;
	private int location_shadowMap;
	
	
	public TerrainShader(String vertexFile, String fragmentFile) {
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
		location_intensity = super.getUniformLocation("intensity");
		location_backgroundSampler = super.getUniformLocation("backgroundTexture");
		location_rSampler = super.getUniformLocation("rTexture");
		location_gSampler = super.getUniformLocation("gTexture");
		location_bSampler = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_shadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_shadowMap = super.getUniformLocation("shadowMap");
	}

	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadToShadowMapSpace(Matrix4f matrix){
		super.loadMatrix(location_shadowMapSpace, matrix);
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundSampler, 0);
		super.loadInt(location_rSampler, 1);
		super.loadInt(location_gSampler, 2);
		super.loadInt(location_bSampler, 3);
		super.loadInt(location_blendMap, 4);
		super.loadInt(location_shadowMap, 5);
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
