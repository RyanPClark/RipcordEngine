package nEngineTester;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Random;

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
import mapBuilding.Level;
import mapBuilding.LoadMap;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.LoadEntity;
import nComponents.Navigate;
import nComponents.Position;
import nComponents.Terrain;
import nRenderEngine.DisplayManager;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import toolbox.MyPaths;

public class NDemo {

	/**
	 * TODO: 
	 * Add AI component(s)
	 * Add frustum culling
	 * Add game-flow component?
	 * Make navigation real navigation
	 * Add bombing to plane
	 * Add gui sound effects
	 */
	
	
	public static void mainDemo(String[] args){
			
		DisplayManager.createDisplay();
		AudioMaster.init();
		Loader loader = new Loader();
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTextureWithBias(MyPaths.makeTexturePath("font/candara"), 0.0f),
				new File(MyPaths.makeFontPath("candara")));
		
		Page homepage = new Page();
		
		try {
			LoadPage.load(homepage, loader, "/html/homepage.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* Finished with test fonts */
		
		/* Load fbos */
		
		MinimapFrameBuffer fbos = new MinimapFrameBuffer();

		Level level = new Level(loader);

		try {
			LoadMap.load("default-map", level);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/* Load test minimap */

		GuiTexture minimap = new GuiTexture(fbos.getReflectionTexture(),
				new Vector2f(0.83f, 0.78f), new Vector2f(0.14f, 0.18f));
		level.getGuis().add(minimap);
		
		Fbo main_fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		PostProcessing.init(loader);
		
		/* Gameloop below */
		
		boolean gameloop = false;
		Page currentPage = homepage;
		
		while(!Display.isCloseRequested()){
			
			if(!gameloop){
				render(level.getmRenderer(), currentPage);
				currentPage = currentPage.update(loader);
				gameloop = currentPage == null;
			}
			else {
				float dt = DisplayManager.getDelta();
				mandatoryUpdates(level, dt);
				testColorFunction(level.getPicker(), font);
				interactWithGuis(level);
				render(level, fbos, main_fbo, false);
			}
		}
		
		PostProcessing.cleanUp();
		main_fbo.cleanUp();
		TextMaster.cleanUp();
		level.getmRenderer().cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
		AudioMaster.cleanUp();
	}
	
	private static void newEntity(Level level, String actionData){
		
		Entity ent2 = new Entity();
		LoadEntity.newEntity(actionData.split(" "), 0, ent2, level);
		level.getEntities().add(ent2);
	}
	
	private static void interactWithGuis(Level level){
		
		for(GuiTexture gui : level.getGuis()){
			
			if(gui.isInArea() && Input.wasClicked() && gui.getActionID() == ActionType.NEW_ENTITY){
				newEntity(level, gui.getActionData());
			}
			
			/* test for bad intersections */
		}
	}
	
	private static void spawnTestEnemies(Level level){
		String loadString = " NME:Soldier POS:-75:0:75 ROT:0:45:0 DTH:0:0 SCL:3.5 INX:X MDL:soldier_body:soldier_tex:true:2 ANM:walking-small:walking HLT:5:20"
				+ " EMT:0:8:2:BLOOD WPN:0:4:0:0:4:0:10:1:10:100:5 SND:audio/gun.wav:1:false:false:1 CBT:163 SUB: NME:SoldierRightLeg POS:0:0:0 ROT:0:0:0 SCL:1"
				+ " INX:X MDL:soldier_right:soldier_tex:true:2 END: SUB: NME:SolderLeftLeg POS:0:0:0 ROT:0:0:0 SCL:1 INX:X MDL:soldier_left:soldier_tex:true:2 END:";
		
		String loadString2 = " NME:Humvee POS:75:0:-75 ROT:0:0:0 SCL:5.5 DTH:0:0 MDL:humvee:humvee:true:2 INX:X HLT:10:20 SND:audio/EngineIdle.wav:0.1:true:true:1 SUB:"
				+ " NME:hvturret WPN:0:8:0:0:8:0:5:1:10:100:5 SND:audio/gun.wav:1:false:false:1 POS:0:0:0 ROT:0:0:0 SCL:1 MDL:air-turret-top:mcor_d_sentry_gun_d:false"
				+ " CBT:163 END:";
		
		Random r = new Random();
		
		if(r.nextInt(100) == 0){
			
			String actionString = (r.nextInt(4) == 1) ? loadString2 : loadString;
			int teamNum = r.nextInt(4);
			actionString = "TEM:" + teamNum + actionString;
			actionString = actionString.replaceAll("INX:X", "INX:" + teamNum);
			Entity ent2 = new Entity();
			LoadEntity.newEntity(
					actionString.split(" "),
					0, ent2, level);
			Position pos = (Position)ent2.getComponentByType(CompType.POSITION);
			
			pos.setPosition(new Vector3f(r.nextInt(100), 0, r.nextInt(100)));
			Terrain terrain = (Terrain)level.getTerrain().getComponentByType(CompType.TERRAIN);
			ent2.addComponent(new Navigate(ent2, new Vector3f(300, 0, 300), terrain, level.getPicker(), 0.1f, false));
			level.getEntities().add(ent2);
		}
	}
	
	/**
	 * Updates the camera, picker, and entities.
	 */
	private static void mandatoryUpdates(Level level, float dt){
		Input.update();
		level.getPicker().update();
		try{
			for(int i = 0; i < level.getEntities().size(); i++){
				level.getEntities().get(i).update(dt/1000.0f);
			}
		}
		catch(ConcurrentModificationException e){
		}
		spawnTestEnemies(level);
		level.getCamera().update(dt/1000.0f);
		level.getMinimapCam().update(dt/1000.0f);
		TextMaster.clear();
	}
	
	/**
	 * Temporary function that displays one text if the mouse is over the road and another if not
	 * 
	 * @param picker  - The mouse picker
	 * @param textYes - The premade text that displays "You can put the entity here"
	 * @param textNo  - The premade text that displays "You cannot put the entity here"
	 */
	private static void testColorFunction(MousePicker picker, FontType font){
		
		String strText = "Entities enabled: ";
		
		for(Entity str : MousePicker.getUnits_selected()){
			strText += (str.getName() + ", ");
		}
		GUIText text = new GUIText(strText, 2, font,
				new Vector2f(0,0), 1.0f, true);
		text.setRender(true);
	}
	
	private static void render(MasterRenderer mRenderer, Page page){
		
		for(GuiTexture gui : page.getGuis()){
			mRenderer.processGui(gui);
		}
		mRenderer.Render();
		DisplayManager.updateDisplay();
	}
	
	private static void render(Level level, MinimapFrameBuffer fbos, Fbo fbo, boolean doPost){

		MasterRenderer mRenderer = level.getmRenderer();
		
		mRenderer.renderShadowMap(level.getEntities(), level.getSun(), false);
		mRenderer.processTerrain(level.getTerrain());
		mRenderer.setRenderText(false);
		fbos.bindReflectionFrameBuffer();
		mRenderer.Render(level.getSun(), level.getMinimapCam(), false);
		fbos.unbindCurrentFrameBuffer();
		mRenderer.setRenderText(true);
		
		for(GuiTexture gui : level.getGuis()){
			mRenderer.processGui(gui);
		}
		
		if(doPost)
			fbo.bindFrameBuffer();
		mRenderer.Render(level.getSun(), level.getCamera(), true);
		if(doPost){
			fbo.unbindFrameBuffer();
			PostProcessing.doPostProcessing(fbo.getColourTexture());
		}
		DisplayManager.updateDisplay();
	}
}
