package nRenderEngine;

/**
 * @author Ryan Clark
 * 
 * This class manages almost all rendering and shading.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import fontMeshCreator.TextMaster;
import guis.GUIRenderer;
import guis.GuiInteraction;
import guis.GuiTexture;
import models.TexturedModel;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.ModelComp;
import nComponents.Rotation;
import nParticles.MultiParticle;
import nShaders.ParticleShader;
import nShaders.StaticShader;
import nShaders.TerrainShader;
import particles.ParticleMaster;
import shadows.ShadowMapMasterRenderer;
import toolbox.MyPaths;

public class MasterRenderer {

	public  static final float FOV = 70.0f;
	public  static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 1000.0f;
	private static final float ZOOM_AMOUNT = 0.0f;
	
	private StaticShader shader = new StaticShader(MyPaths.makeShaderPath("nVertex"),
			MyPaths.makeShaderPath("nFragment"));
	
	private TerrainShader terrainShader = new TerrainShader(MyPaths.makeShaderPath("nTerrainVertex"),
			MyPaths.makeShaderPath("nTerrainFragment"));
	
	private ParticleShader particleShader = new ParticleShader();
	
	private Renderer renderer;
	private TerrainRenderer terrainRenderer;
	private GUIRenderer guiRenderer;
	private ParticleRenderer particleRenderer;
	
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Entity> terrains = new ArrayList<Entity>();
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private List<MultiParticle> particles = new ArrayList<MultiParticle>();
	
	private Matrix4f projectionMatrix;
	
	private boolean renderText = true;
	
	public MasterRenderer(Loader loader, Entity camera){
		
		createProjectionMatrix();
		renderer = new Renderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		guiRenderer = new GUIRenderer(loader);
		ParticleMaster.init(loader, projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		GuiInteraction.init(loader);
		particleRenderer = new ParticleRenderer(loader, particleShader, projectionMatrix);
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
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.23f, 0.46f, 0, 0.047f);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	public void quickPrepare(){
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	public void Render(){
		quickPrepare();
		guiRenderer.Render(guis);
		TextMaster.render();
		guis.clear();
	}
	
	public void Render(Entity light, Entity camera, boolean clearLists){
		
		prepare();
		
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadLight(light);
		renderer.render(entities);
		shader.stop();
		
		particleShader.start();
		particleShader.loadViewMatrix(camera);
		{
			Rotation rot = (Rotation)camera.getComponentByType(CompType.ROTATION);
			particleRenderer.Render(particles, false, -rot.getRotation().y);
		}
		
		particleShader.stop();
		
		terrainShader.start();
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadLight(light);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		guiRenderer.Render(guis);
		
		ParticleMaster.update();
		ParticleMaster.render(camera);
		
		if(renderText)
			TextMaster.render();
		
		if(clearLists){
			terrains.clear();
			entities.clear();
			guis.clear();
			particles.clear();
		}
	}
	
	public void processParticle(MultiParticle particle){
		particles.add(particle);
	}
	
	public void processGui(GuiTexture gui){
		guis.add(gui);
	}
	
	public void processTerrain(Entity terrain){
		terrains.add(terrain);
	}
	
	/**
	 * Adds an entity and its textured model to the entity map. If the entity has a
	 * hitbox with rendering enabled, then the hitbox texturedModel is used instead.
	 * 
	 * @param ent - Entity to be added to the map
	 */
	
	public void processEntity(Entity ent){
		
		TexturedModel model;
		
		ModelComp mComp = (ModelComp)ent.getComponentByType(CompType.MODEL);
		model = mComp.getModel();
		
		List<Entity> batch = entities.get(model);
		if(batch != null){
			batch.add(ent);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(ent);
			entities.put(model, newBatch);
		}
	}
	
	public void renderShadowMap(List<Entity> entitiesList, Entity sun, boolean clear){
		for(Entity ent : entitiesList){
			if(ent.getComponentByType(CompType.MODEL) != null)
				processEntity(ent);
		}
		shadowMapRenderer.render(entities, sun);
		if(clear)
			entities.clear();
	}
	
	public int getShadowMapTexture(){
		return shadowMapRenderer.getShadowMap();
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
		
		guiRenderer.cleanUp();
		shadowMapRenderer.cleanUp();
		ParticleMaster.cleanUp();
		terrainShader.cleanUp();
		shader.cleanUp();
	}

	public boolean isRenderText() {
		return renderText;
	}

	public void setRenderText(boolean renderText) {
		this.renderText = renderText;
	}
}
