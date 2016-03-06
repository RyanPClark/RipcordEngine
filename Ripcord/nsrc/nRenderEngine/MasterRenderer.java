package nRenderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import fontMeshCreator.TextMaster;
import guis.GUIRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.ModelComp;
import nShaders.StaticShader;
import nShaders.TerrainShader;
import toolbox.MyPaths;

public class MasterRenderer {

	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 1000.0f;
	private static final float ZOOM_AMOUNT = 0.0f;
	
	private StaticShader shader = new StaticShader(MyPaths.makeShaderPath("nVertex"),
			MyPaths.makeShaderPath("nFragment"));
	
	private TerrainShader terrainShader = new TerrainShader(MyPaths.makeShaderPath("nTerrainVertex"),
			MyPaths.makeShaderPath("nTerrainFragment"));
	
	private Renderer renderer;
	private TerrainRenderer terrainRenderer;
	private GUIRenderer guiRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Entity> terrains = new ArrayList<Entity>();
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	private Matrix4f projectionMatrix;
	
	public MasterRenderer(Loader loader){
		
		createProjectionMatrix();
		renderer = new Renderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		guiRenderer = new GUIRenderer(loader);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public Matrix4f getProjectionMatrix(){
		return this.projectionMatrix;
	}
	
	public void prepare(){
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		if (Display.wasResized()){
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			createProjectionMatrix();
			shader.start();
			shader.loadProjectionMatrix(projectionMatrix);
			shader.stop();
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	public void Render(Entity light, Entity camera){
		
		prepare();

		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadLight(light);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadLight(light);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		guiRenderer.Render(guis);
		
		TextMaster.render();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processGui(GuiTexture gui){
		guis.add(gui);
	}
	
	public void processTerrain(Entity terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity ent){
		
		ModelComp mComp = (ModelComp)ent.getComponentByType(CompType.MODEL);
		TexturedModel model = mComp.getModel();
		List<Entity> batch = entities.get(model);
		if(batch != null){
			batch.add(ent);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(ent);
			entities.put(model, newBatch);
		}
	}
	
	private void createProjectionMatrix(){
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f/Math.tan(Math.toRadians((FOV-ZOOM_AMOUNT)/2)))*aspectRatio;
		float x_scale = y_scale/aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE+NEAR_PLANE)/frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2*NEAR_PLANE*FAR_PLANE)/frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void cleanUp(){
		
		terrainShader.cleanUp();
		shader.cleanUp();
	}
}
