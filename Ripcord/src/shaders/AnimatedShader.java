package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.GameMath;
import toolbox.MyPaths;
import entities.Camera;
import entities.Light;

public class AnimatedShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = MyPaths.makeShaderPath("animatedVertexShader");
	private static final String FRAGMENT_FILE = MyPaths.makeShaderPath("animatedFragmentShader");
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightColor;
	private int location_lightPosition;
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_intensity;
	private int location_useFakeLighting;
	private int location_numberOfRows;
	private int location_offset;
	private int location_tween;
	private int location_plane;

	public AnimatedShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadClipPlane(Vector4f plane){
		super.loadVector4f(location_plane, plane);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getColor());
		super.loadFloat(location_intensity, light.getIntensity());
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "position1");
		super.bindAttribute(4, "normal1");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightColor = super.getUniformLocation("lightColor");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_shine_damper = super.getUniformLocation("shine_damper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_intensity = super.getUniformLocation("intensity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_tween = super.getUniformLocation("tween");
		location_plane = super.getUniformLocation("plane");
	}
	
	public void loadTween(float tween){
		super.loadFloat(location_tween, tween);
	}
	
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y){
		super.loadVector2f(location_offset, new Vector2f(x, y));
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting){
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shine_damper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
		
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = GameMath.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
