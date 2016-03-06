package nEngineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMaster;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import nComponents.Color;
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
		ModelData data = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/bus1"));
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
			data.getNormals(), data.getIndices());
		
		MasterRenderer mRenderer = new MasterRenderer(loader);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/bus1")));
		texture.setReflectivity(0.0f);
		texture.setShine_damper(10.0f);
		texture.setNumberOfRows(1);
		TexturedModel tModel = new TexturedModel(model, texture);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> terrains = new ArrayList<Entity>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTextureWithBias(MyPaths.makeTexturePath("font/arial"), 0.0f),
				new File(MyPaths.makeFontPath("arial")));
		GUIText text = new GUIText("My first text", 1, font, new Vector2f(0,0), 1.0f, true);
		text.setColour(1, 1, 1);
		
		
		Random r = new Random();
		
		/*for(int i = 0; i < 8; i++){
			Entity ent = new Entity();
			ent.addComponent(new Position(ent, new Vector3f(r.nextInt(500)-250,0,r.nextInt(500)-250)));
			ent.addComponent(new Rotation(ent, new  Vector3f(0,-30,0)));
			ent.addComponent(new Scale(ent, 0.5f));
			ent.addComponent(new ModelComp(ent, tModel));
			ent.addComponent(new TextureIndex(ent, 0));

			entities.add(ent);
		}*/
		
		Entity cam = new Entity();
		cam.addComponent(new Position(cam, new Vector3f(350,75.0f,350)));
		cam.addComponent(new Rotation(cam, new Vector3f(45,-45,0)));
		cam.addComponent(new KeyControl(cam));
		
		Entity light = new Entity();
		light.addComponent(new Position(light, new Vector3f(1000,2000,0)));
		light.addComponent(new Color(light, new Vector3f(1,1,1), 2.0f));
		
		MousePicker picker = new MousePicker(cam, mRenderer.getProjectionMatrix());
		
		GuiTexture gui = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("Guis/resumeGame")),
				new Vector2f(-0.8f,0.8f), new Vector2f(0.2f,0.2f));
		
		guis.add(gui);
		
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
		
		while(!Display.isCloseRequested()){
			
			float dt = DisplayManager.getDelta();
			
			for(Entity ent : entities){
				ent.update(dt/1000.0f);
			}
			
			picker.update();
			cam.update(dt/1000.0f);
			
			//Vector3f color = TerrainColor.getColor(picker.getCurrentTerrainPoint());
			
			render(cam, mRenderer, entities, terrains, guis, light);
		}
		
		TextMaster.cleanUp();
		mRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
	
	private static void render(Entity camera, MasterRenderer mRenderer, List<Entity> ents, List<Entity> terrains,
			List<GuiTexture> guis, Entity light){
		
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
