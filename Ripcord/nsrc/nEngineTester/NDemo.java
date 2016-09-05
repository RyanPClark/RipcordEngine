package nEngineTester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import fbos.MinimapFrameBuffer;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMaster;
import guis.ActionType;
import guis.GuiTexture;
import html.LoadPage;
import html.Page;
import nComponents.Color;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Hitbox;
import nComponents.KeyControl;
import nComponents.LoadEntity;
import nComponents.MinimapControl;
import nComponents.Position;
import nComponents.Rotation;
import nComponents.Scale;
import nComponents.Terrain;
import nRenderEngine.DisplayManager;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;
import nToolbox.TerrainColor;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MyPaths;

public class NDemo {

	/**
	 * 
	 * TODO: 
	 * Close source at termination + high volume or change attenuation model
	 * Add AI component(s)
	 * Add frustum culling
	 * Add game-flow component?
	 */
	
	
	public static void mainDemo(String[] args){
			
		DisplayManager.createDisplay();
		AudioMaster.init();
		Loader loader = new Loader();
		
		Entity cam = new Entity();
		cam.addComponent(new Position(cam, new Vector3f(375,125.0f,375)));
		cam.addComponent(new Rotation(cam, new Vector3f(45,-45,0)));
		cam.addComponent(new KeyControl(cam, 125.0f, new int[]{-240,375,-240,375,50,175}));
		
		Entity minimapCam = new Entity();
		minimapCam.addComponent(new Position(minimapCam, new Vector3f(0,400,0)));
		minimapCam.addComponent(new Rotation(minimapCam, new Vector3f(90,180,0)));
		Position camPos = (Position)cam.getComponentByType(CompType.POSITION);
		minimapCam.addComponent(new MinimapControl(minimapCam, new int[]{-300,300,-300,300,50,175}, camPos));
		
		MasterRenderer mRenderer = new MasterRenderer(loader, cam);
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTextureWithBias(MyPaths.makeTexturePath("font/candara"), 0.0f),
				new File(MyPaths.makeFontPath("candara")));
		
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
		
		
		GUIText textNo = new GUIText("You can put this entity here", 2, font,
				new Vector2f(0,0), 1.0f, true);
		GUIText textYes = new GUIText("You cannot put this entity here", 2, font,
				new Vector2f(0,0), 1.0f, true);
		textNo.setColour(0, 1, 0);
		textNo.setRender(false);
		textYes.setColour(1, 0, 0);
		textYes.setRender(false);
		
		/* Finished with test fonts */
		
		MousePicker picker = new MousePicker(cam, mRenderer.getProjectionMatrix());
		Entity HQ = new Entity();
		LoadEntity.newEntity("POS:300:0:300 ROT:0:0:0 SCL:0.3 MDL:tower:tower:true".split(" "), 0, loader, mRenderer, picker, entities, HQ, cam);
		Hitbox HQbox = (Hitbox)HQ.getComponentByType(CompType.HIT_BOX);
		HQbox.setSelected(false);
		entities.add(HQ);
		
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
		
		String[] icons_sub1 = {"guis/128-house", "guis/128-factory", "guis/128-mine", "guis/128-lab", "guis/128-airdefense", "guis/128-ground-defense"};
		String[] icons_sub2 = {"guis/128-infantry", "guis/128-car", "guis/128-tank", "guis/128-helicopter", "guis/128-mech", "guis/128-air-strike"};
		String[][] icons = {icons_sub1, icons_sub2};
		
		String[] data_sub1 = {
				"POS:0:0:0 ROT:0:0:0 SCL:7" + " MDL:totalcrap:totalcrap:true PCT: ",
				"POS:0:0:0 ROT:0:0:0 SCL:0.13" + " MDL:bower:bower:true PCT: ",
				"POS:0:0:0 ROT:0:0:0 SCL:0.45" + " MDL:rocker:rocker:true PCT: ",
				"POS:0:0:0 ROT:0:0:0 SCL:5" + " MDL:build10:build10:true PCT: ",
				"POS:0:0:0 ROT:0:0:0 SCL:6" + " MDL:air-turret-base:mcor_d_sentry_gun_d:true PCT: SUB: " + 
						"POS:0:0:5 ROT:-30:0:0 SCL:1" + " MDL:air-turret-top:mcor_d_sentry_gun_d:false IDL:200:0.1:30:true END:",
				"POS:0:0:0 ROT:0:0:0 SCL:3" + " MDL:turret-base:sci_fi_turret_4_diffuse:true PCT: SUB: " + 
				"POS:0:0:0 ROT:0:0:0 SCL:1" + " MDL:turret-top:sci_fi_turret_4_diffuse:false IDL:200:0.1:30:true END:"
				};
		String[] data_sub2 = {
				"POS:0:0:0 ROT:0:45:0 SCL:3.5" + " MDL:soldier_body:soldier_tex:true PCT: ANM:walking-small EMT:0:8:2:BLOOD WPN: SND:audio/gun.wav:1:false:false SUB: " + 
						"POS:0:0:0 ROT:0:0:0 SCL:1 MDL:soldier_right:soldier_tex:false END: SUB: " + 
						"POS:0:0:0 ROT:0:0:0 SCL:1 MDL:soldier_left:soldier_tex:false END:",
				"POS:0:0:0 ROT:0:0:0 SCL:4.5" + " MDL:humvee:humvee:true PCT: SND:audio/EngineIdle.wav:1:true:true ",
				"POS:0:0:0 ROT:0:180:0 SCL:4" + " MDL:lowpoly-tank:lowpoly-tank:true PCT: ",
				"POS:0:30:0 ROT:0:0:0 SCL:3.5" + " MDL:mi24_base:diffuse_m24_alpha:true PCT: ANM:spinning SND:audio/Helicopter_Hovering_short.wav:1:true:true SUB: " + 
						"POS:0:0:-2 ROT:0:0:0 SCL:1" + " MDL:mi24_top:diffuse_m24_alpha:false END:",
						"POS:0:0:0 ROT:0:0:0 SCL:7" + " MDL:mech_top:MechBody:true PCT: ANM:walking SND:audio/mech_walking.wav:1:true:true SUB: " + 
								"POS:0:0:0 ROT:0:0:0 SCL:1 MDL:mech_right:MechCage:false END: SUB: " + 
								"POS:0:0:0 ROT:0:0:0 SCL:1 MDL:mech_left:MechCage:false END:",
				"POS:0:40:0 ROT:0:0:0 SCL:11 MDL:fighter:fighter:true PCT: "
				};
		String[][] data = {data_sub1, data_sub2};
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 6; j++){
				
				GuiTexture entityOne = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath(icons[i][j])),
						new Vector2f(0.75f + 0.14f * i, 0.45f - 0.15f * j), new Vector2f(0.05f,0.06f));
				entityOne.setActionID(ActionType.NEW_ENTITY);
				entityOne.setActionData(data[i][j]);
				entityOne.setFrame(true);
				
				guis.add(entityOne);
			}
		}
		
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
				mandatoryUpdates(picker, entities, cam, minimapCam, dt);
				testColorFunction(picker, textYes, textNo);
				interactWithGuis(guis, loader, picker, entities, cam, mRenderer);
				render(cam, mRenderer, entities, terrains, guis, light, fbos, minimapCam);
			}
		}
		
		AudioMaster.cleanUp();
		TextMaster.cleanUp();
		mRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
	
	private static void newEntity(Entity camera, Loader loader, MousePicker picker,
			List<Entity> entities, String actionData, MasterRenderer mRenderer){
		
		Entity ent2 = new Entity();
		LoadEntity.newEntity(actionData.split(" "), 0, loader, mRenderer, picker, entities, ent2, camera);
		entities.add(ent2);
	}
	
	private static void interactWithGuis(List<GuiTexture> guis, Loader loader, MousePicker picker,
			List<Entity> entities, Entity cam, MasterRenderer mRenderer){
		
		for(GuiTexture gui : guis){
			
			if(gui.isInArea() && Input.wasClicked() && gui.getActionID() == ActionType.NEW_ENTITY){
				newEntity(cam, loader, picker, entities, gui.getActionData(), mRenderer);
			}
		}
	}
	
	/**
	 * Updates the camera, picker, and entities.
	 */
	private static void mandatoryUpdates(MousePicker picker, List<Entity> entities,
			Entity cam, Entity minimapCam, float dt){
		Input.update();
		picker.update();
		try{
			for(int i = 0; i < entities.size(); i++){
				entities.get(i).update(dt/1000.0f);
			}
		}
		catch(ConcurrentModificationException e){
		}
		
		cam.update(dt/1000.0f);
		minimapCam.update(dt/1000.0f);
	}
	
	/**
	 * Temporary function that displays one text if the mouse is over the road and another if not
	 * 
	 * @param picker  - The mouse picker
	 * @param textYes - The premade text that displays "You can put the entity here"
	 * @param textNo  - The premade text that displays "You cannot put the entity here"
	 */
	private static void testColorFunction(MousePicker picker, GUIText textYes, GUIText textNo){
		
		if(!MousePicker.isUnit_selected()){
			textYes.setRender(false);
			textNo.setRender(false);
			return;
		}
		
		Vector3f color = TerrainColor.getColor(picker.getCurrentTerrainPoint());
		
		if(color.y > 100){
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
		
		for(Entity terrain : terrains){
			mRenderer.processTerrain(terrain);
		}
		for(Entity ent : ents){
			mRenderer.processEntity(ent);
		}
		
		mRenderer.setRenderText(false);
		fbos.bindReflectionFrameBuffer();
		mRenderer.Render(light, minimapCam, false);
		fbos.unbindCurrentFrameBuffer();
		mRenderer.setRenderText(true);
		
		for(GuiTexture gui : guis){
			mRenderer.processGui(gui);
		}
		
		mRenderer.Render(light, camera, true);
		DisplayManager.updateDisplay();
	}
}
