package nEngineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fbos.MinimapFrameBuffer;
import fontMeshCreator.TextMaster;
import guis.GuiTexture;
import html.Page;
import nComponents.Color;
import nComponents.Entity;
import nComponents.KeyControl;
import nComponents.Position;
import nComponents.Rotation;
import nRenderEngine.DisplayManager;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;

public class NMain {

	public static void nmain(String[] args){
			
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		Entity cam = new Entity();
		cam.addComponent(new Position(cam, new Vector3f(350,75.0f,350)));
		cam.addComponent(new Rotation(cam, new Vector3f(45,-45,0)));
		cam.addComponent(new KeyControl(cam, 125.0f, new int[]{-360,360,-360,360,50,175}));
		
		Entity minimapCam = new Entity();
		minimapCam.addComponent(new Position(minimapCam, new Vector3f(0,650,0)));
		minimapCam.addComponent(new Rotation(minimapCam, new Vector3f(90,180,0)));
		
		MasterRenderer mRenderer = new MasterRenderer(loader, cam);
		
		TextMaster.init(loader);
		
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> terrains = new ArrayList<Entity>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		Page homepage = new Page();
		
		//try {
			//LoadPage.load(homepage, loader, "/html/homepage.html");
		//} catch (IOException e) {
			//e.printStackTrace();
		//}
		
		
		/* Finished with test fonts */
		
		MousePicker picker = new MousePicker(cam, mRenderer.getProjectionMatrix());
		
		Entity light = new Entity();
		light.addComponent(new Position(light, new Vector3f(-10000,20000,-10000)));
		light.addComponent(new Color(light, new Vector3f(1,1,1), 2.0f));
		
		/* Load fbos */
		
		MinimapFrameBuffer fbos = new MinimapFrameBuffer();
		
		
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
				render(cam, mRenderer, entities, terrains, guis, light, fbos, minimapCam);
			}
		}
		
		TextMaster.cleanUp();
		mRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
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
		mRenderer.Render(light, minimapCam, true);
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
		
		mRenderer.Render(light, camera, true);
		DisplayManager.updateDisplay();
	}
}
