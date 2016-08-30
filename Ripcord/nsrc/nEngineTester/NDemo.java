package nEngineTester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fbos.MinimapFrameBuffer;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMaster;
import guis.ActionType;
import guis.GuiTexture;
import html.LoadPage;
import html.Page;
import mapBuilding.Box;
import models.RawModel;
import models.TexturedModel;
import nComponents.Color;
import nComponents.Entity;
import nComponents.Hitbox;
import nComponents.KeyControl;
import nComponents.ModelComp;
import nComponents.PickerControl;
import nComponents.Position;
import nComponents.Rotation;
import nComponents.Scale;
import nComponents.Terrain;
import nComponents.TextureIndex;
import nRenderEngine.DisplayManager;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;
import nToolbox.TerrainColor;
import objConverter.ModelData;
import objConverter.OBJLoader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MyPaths;

public class NDemo {

	public static void mainDemo(String[] args){
			
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		ModelData data = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/police_car1"));
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
			data.getNormals(), data.getIndices());
		
		Entity cam = new Entity();
		cam.addComponent(new Position(cam, new Vector3f(350,75.0f,350)));
		cam.addComponent(new Rotation(cam, new Vector3f(45,-45,0)));
		cam.addComponent(new KeyControl(cam, 125.0f));
		
		Entity minimapCam = new Entity();
		minimapCam.addComponent(new Position(minimapCam, new Vector3f(0,650,0)));
		minimapCam.addComponent(new Rotation(minimapCam, new Vector3f(90,180,0)));
		
		MasterRenderer mRenderer = new MasterRenderer(loader, cam);
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTextureWithBias(MyPaths.makeTexturePath("font/candara"), 0.0f),
				new File(MyPaths.makeFontPath("candara")));
		
		ModelTexture texture = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/police_car1")));
		texture.setReflectivity(0.0f);
		texture.setShine_damper(10.0f);
		texture.setNumberOfRows(1);
		TexturedModel tModel = new TexturedModel(model, texture);
		//TexturedModel tHModel = new TexturedModel(hitModel, texture);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> terrains = new ArrayList<Entity>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		Page homepage = new Page();
		
		try {
			LoadPage.load(homepage, loader, "/html/homepage.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* Load test text */
		
		
		GUIText textNo = new GUIText("You cannot put this entity here!", 2, font,
				new Vector2f(0,0), 1.0f, true);
		GUIText textYes = new GUIText("You can put this entity here!", 2, font,
				new Vector2f(0,0), 1.0f, true);
		textNo.setColour(1, 0, 0);
		textNo.setRender(false);
		textYes.setColour(0, 1, 0);
		textYes.setRender(false);
		
		/* Finished with test fonts */
		
		MousePicker picker = new MousePicker(cam, mRenderer.getProjectionMatrix());
		
		Box hitbox = data.generateHitbox();
		hitbox.generateModel(loader);
		
		Entity ent = new Entity();
		ent.addComponent(new Position(ent, new Vector3f(0,0,0)));
		ent.addComponent(new Rotation(ent, new Vector3f(0,0,0)));
		ent.addComponent(new Scale(ent, 2.0f));
		ent.addComponent(new ModelComp(ent, tModel));
		ent.addComponent(new Hitbox(ent, hitbox, picker));
		ent.addComponent(new TextureIndex(ent, 0));

		entities.add(ent);
		
		/* load light */
		
		Entity light = new Entity();
		light.addComponent(new Position(light, new Vector3f(-10000,20000,-10000)));
		light.addComponent(new Color(light, new Vector3f(1,1,1), 2.0f));
		
		/* done with light */
		
		/* TERRAIN GENERATION HERE */
		
		final float SIZE = 800.0f * 8.0f / 7.0f;
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/grass_old")));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/yellow_old")));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/asphalt_old")));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/concrete_old")));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendmapTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/nBlendmap")));
		
		TerrainColor.load(MyPaths.makeTexturePath("Terrain/nBlendmap"), (int)SIZE);
		
		Entity terrain = new Entity();
		terrain.addComponent(new Position(terrain, new Vector3f(-0.5f*SIZE,0,-0.5f*SIZE)));
		terrain.addComponent(new Scale(terrain, SIZE));
		terrain.addComponent(new Terrain(terrain, loader, texturePack, blendmapTexture));
		
		terrains.add(terrain);
		
		/* END TERRAIN GENERATION */
		
		/* Load fbos */
		
		MinimapFrameBuffer fbos = new MinimapFrameBuffer();
		
		/* Load other guis */
		
		GuiTexture backarea = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("guis/lightblack")),
				new Vector2f(0.82f,-0.14f), new Vector2f(0.17f,0.7f));
		backarea.setActionID(ActionType.NONE);
		
		guis.add(backarea);
		
		GuiTexture entityOne = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("guis/gunGui1")),
				new Vector2f(0.82f,0.45f), new Vector2f(0.1f,0.06f));
		entityOne.setActionID(ActionType.NEW_ENTITY);
		entityOne.setFrame(true);
		guis.add(entityOne);
		
		/* Load test minimap */
		
		GuiTexture minimap = new GuiTexture(fbos.getReflectionTexture(),
				new Vector2f(0.83f, 0.78f), new Vector2f(0.14f, 0.18f));
		guis.add(minimap);
		
		/* Gameloop below */
		
		boolean gameloop = false;
		Page currentPage = homepage;
		
		while(!Display.isCloseRequested()){
			
			if(!gameloop){
				render(mRenderer, currentPage);
				currentPage = currentPage.update(loader);
				gameloop = currentPage == null;
			}
			else {
				float dt = DisplayManager.getDelta();
				mandatoryUpdates(picker, entities, cam, dt);
				testColorFunction(picker, textYes, textNo);
				interactWithGuis(guis, loader, picker, entities);
				render(cam, mRenderer, entities, terrains, guis, light, fbos, minimapCam);
			}
		}
		
		TextMaster.cleanUp();
		mRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
	
	private static void newEntity(Loader loader, MousePicker picker, List<Entity> entities){
		
		ModelData data2 = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/totalcrap"));
		RawModel model2 = loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(),
				data2.getNormals(), data2.getIndices());
		 
		Box hitbox2 = data2.generateHitbox();
		hitbox2.generateModel(loader);
		
		ModelTexture texture2 = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/old_house1")));
		TexturedModel tModel2 = new TexturedModel(model2, texture2);
		
		Entity ent2 = new Entity();
		ent2.addComponent(new Position(ent2, new Vector3f(0,0,0)));
		ent2.addComponent(new Rotation(ent2, new Vector3f(0,0,0)));
		ent2.addComponent(new Scale(ent2, 5.0f));
		ent2.addComponent(new ModelComp(ent2, tModel2));
		ent2.addComponent(new Hitbox(ent2, hitbox2, picker));
		ent2.addComponent(new TextureIndex(ent2, 0));
		ent2.addComponent(new PickerControl(ent2, picker));

		entities.add(ent2);
	}
	
	private static void interactWithGuis(List<GuiTexture> guis, Loader loader, MousePicker picker,
			List<Entity> entities){
		
		//Position pos = (Position)entities.get(1).getComponentByType(CompType.POSITION);
		//pos.setPosition(picker.getCurrentTerrainPoint());
		
		for(GuiTexture gui : guis){
			
			if(gui.isInArea() && Input.wasClicked() && gui.getActionID() == ActionType.NEW_ENTITY){
				newEntity(loader, picker, entities);
			}
		}
	}
	
	/**
	 * Updates the camera, picker, and entities.
	 */
	private static void mandatoryUpdates(MousePicker picker, List<Entity> entities,
			Entity cam, float dt){
		Input.update();
		picker.update();
		for(Entity ent : entities){
			ent.update(dt/1000.0f);
		}
		cam.update(dt/1000.0f);
	}
	
	/**
	 * Temporary function that displays one text if the mouse is over the road and another if not
	 * 
	 * @param picker  - The mouse picker
	 * @param textYes - The premade text that displays "You can put the entity here"
	 * @param textNo  - The premade text that displays "You cannot put the entity here"
	 */
	private static void testColorFunction(MousePicker picker, GUIText textYes, GUIText textNo){
		Vector3f color = TerrainColor.getColor(picker.getCurrentTerrainPoint());
		
		if(color.y > 100 || color.x > 100){
			textYes.setRender(true);
			textNo.setRender(false);
		}
		else {
			textYes.setRender(false);
			textNo.setRender(true);
		}
	}
	
	private static void render(MasterRenderer mRenderer, Page page){
		
		for(GuiTexture gui : page.getGuis()){
			mRenderer.processGui(gui);
		}
		mRenderer.Render();
		DisplayManager.updateDisplay();
	}
	
	private static void render(Entity camera, MasterRenderer mRenderer, List<Entity> ents, List<Entity> terrains,
			List<GuiTexture> guis, Entity light, MinimapFrameBuffer fbos, Entity minimapCam){

		mRenderer.renderShadowMap(ents, light);
		
		for(Entity ent : ents){
			mRenderer.processEntity(ent);
		}
		for(Entity terrain : terrains){
			mRenderer.processTerrain(terrain);
		}
		
		mRenderer.setRenderText(false);
		fbos.bindReflectionFrameBuffer();
		mRenderer.Render(light, minimapCam);
		fbos.unbindCurrentFrameBuffer();
		mRenderer.setRenderText(true);
		
		for(Entity ent : ents){
			mRenderer.processEntity(ent);
		}
		for(Entity terrain : terrains){
			mRenderer.processTerrain(terrain);
		}
		for(GuiTexture gui : guis){
			mRenderer.processGui(gui);
		}
		
		mRenderer.Render(light, camera);
		DisplayManager.updateDisplay();
	}
}
