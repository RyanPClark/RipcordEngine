package nEngineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMaster;
import guis.GuiTexture;
import html.LoadPage;
import models.RawModel;
import models.TexturedModel;
import nComponents.Color;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.KeyControl;
import nComponents.ModelComp;
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

public class NMain {

	public static void main(String[] args){
			
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		ModelData data = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/police_car1"));
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
			data.getNormals(), data.getIndices());
		
		Entity cam = new Entity();
		cam.addComponent(new Position(cam, new Vector3f(350,75.0f,350)));
		cam.addComponent(new Rotation(cam, new Vector3f(45,-45,0)));
		cam.addComponent(new KeyControl(cam));
		
		MasterRenderer mRenderer = new MasterRenderer(loader, cam);
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTextureWithBias(MyPaths.makeTexturePath("font/candara"), 0.0f),
				new File(MyPaths.makeFontPath("candara")));
		
		ModelTexture texture = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/police_car1")));
		texture.setReflectivity(0.0f);
		texture.setShine_damper(10.0f);
		texture.setNumberOfRows(1);
		TexturedModel tModel = new TexturedModel(model, texture);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> terrains = new ArrayList<Entity>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		LoadPage.load(font);
		
		/* Load test text */
		
		
		GUIText textNo = new GUIText("You cannot put this entity here!", 3, font,
				new Vector2f(0,0), 1.0f, true);
		GUIText textYes = new GUIText("You can put this entity here!", 3, font,
				new Vector2f(0,0), 1.0f, true);
		textNo.setColour(1, 0, 0);
		textNo.setRender(false);
		textYes.setColour(0, 1, 0);
		textYes.setRender(false);
		
		/* Finished with test fonts */
		
		for(int i = 0; i < 1; i++){
			Entity ent = new Entity();
			ent.addComponent(new Position(ent, new Vector3f(0,0,0)));
			ent.addComponent(new Rotation(ent, new  Vector3f(0,0,0)));
			ent.addComponent(new Scale(ent, 2.0f));
			ent.addComponent(new ModelComp(ent, tModel));
			ent.addComponent(new TextureIndex(ent, 0));

			entities.add(ent);
		}
		
		/* load light */
		
		Entity light = new Entity();
		light.addComponent(new Position(light, new Vector3f(-10000,20000,-10000)));
		light.addComponent(new Color(light, new Vector3f(1,1,1), 2.0f));
		
		/* done with light */
		
		MousePicker picker = new MousePicker(cam, mRenderer.getProjectionMatrix());
		
		//GuiTexture gui = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("Guis/resumeGame")),
		//		new Vector2f(-0.8f,0.8f), new Vector2f(0.2f,0.2f));
		
		//guis.add(gui);
		
		//GuiTexture shadowMap = new GuiTexture(mRenderer.getShadowMapTexture(), new Vector2f(0.5f,0.5f), new Vector2f(0.5f, 0.5f));
		//guis.add(shadowMap);
		
		/* TERRAIN GENERATION HERE */
		
		final float SIZE = 800.0f * 8.0f / 7.0f;
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/grass")));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/yellow")));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/asphalt")));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/concrete")));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendmapTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/nBlendmap")));
		
		TerrainColor.load(MyPaths.makeTexturePath("Terrain/nBlendmap"), (int)SIZE);
		
		Entity terrain = new Entity();
		terrain.addComponent(new Position(terrain, new Vector3f(-0.5f*SIZE,0,-0.5f*SIZE)));
		terrain.addComponent(new Scale(terrain, SIZE));
		terrain.addComponent(new Terrain(terrain, loader, texturePack, blendmapTexture));
		
		terrains.add(terrain);
		
		/* END TERRAIN GENERATION */
		
		boolean gameloop = false;
		
		while(!Display.isCloseRequested()){
			
			if(!gameloop){
				render(mRenderer, guis);
				if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
					for(GUIText text : LoadPage.texts){
						TextMaster.removeText(text);
					}
					gameloop = true;
				}
			}
			else {
				
				float dt = DisplayManager.getDelta();
				
				picker.update();
				
				for(Entity ent : entities){
					ent.update(dt/1000.0f);
					Position pos = (Position)ent.getComponentByType(CompType.POSITION);
					pos.setPosition(picker.getCurrentTerrainPoint());
				}
				
				
				cam.update(dt/1000.0f);
				
				Vector3f color = TerrainColor.getColor(picker.getCurrentTerrainPoint());
				
				if(color.y > 100 || color.x > 100){
					textYes.setRender(true);
					textNo.setRender(false);
				}
				else {
					textYes.setRender(false);
					textNo.setRender(true);
				}
				
				render(cam, mRenderer, entities, terrains, guis, light);
			}
			
		}
		
		TextMaster.cleanUp();
		mRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
	
	private static void render(MasterRenderer mRenderer, List<GuiTexture> guis){
		
		for(GuiTexture gui : guis){
			mRenderer.processGui(gui);
		}
		mRenderer.Render();
		DisplayManager.updateDisplay();
	}
	
	private static void render(Entity camera, MasterRenderer mRenderer, List<Entity> ents, List<Entity> terrains,
			List<GuiTexture> guis, Entity light){
		
		mRenderer.renderShadowMap(ents, light);
		
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
