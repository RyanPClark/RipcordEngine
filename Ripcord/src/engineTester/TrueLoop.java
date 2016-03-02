package engineTester;

import loaders.GunLoader;
import maps.Map;
import nRenderEngine.DisplayManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import components.Frustum;
import animation.MasterAnimation;
import entities.Camera;
import entities.Gun;
import enums.Mode;
import enums.State;
import guis.GuiMaster;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import sound.Sound;
import sound.SoundNames;
import textures.CurseClass;
import toolbox.MousePicker;
import toolbox.MyInput;

public final class TrueLoop {
	
	private Camera camera; 				private MasterRenderer masterRenderer;
	private Loader loader; 				private Gun gun;
	private State state; 				private short weaponID;
	private boolean wireframe, flying; 	private float fps;
	private MousePicker picker;			private Map map;
	private Mode mode;
	
	public void run(){
		
		init();
		weaponID = (short) (System.currentTimeMillis()%24);
		
		while(!Display.isCloseRequested()){
			
			checkGuiInteractions();
			devMode();
			render();
			fps = 1000/DisplayManager.getDelta();
			
			if(state == State.CHOOSING)
				startLogic();
			
			else if(state == State.PLAYING)
				gameLogic();
			
		}
		cleanUp();
	}
	
	private void displayFPS(){
		if (System.currentTimeMillis() % 10 == 0)
			DisplayManager.setTitle((int)fps + "");
	}
	
	private void init(){
		DisplayManager.createDisplay();
		loader = new Loader();
		GuiMaster.init(loader);
		GuiMaster.RenderSplash();
		DisplayManager.updateDisplay();
		masterRenderer = new MasterRenderer(loader);
		Sound.init();
		camera = new Camera();
		Frustum.update(camera.getPosition(), camera.getYaw());
		Initialize.init(loader);
		GuiMaster.loadRest(loader);
		picker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), map);
		GunLoader.init();
		MasterAnimation.init();
		state = State.CHOOSING;
		mode = Mode.PLAYER;
		map = new Map(masterRenderer, camera, mode, picker, fps);
	}
	
	private void render(){
		map.render(camera.getPosition());
		masterRenderer.render(map.getLights(), camera, wireframe, mode);
		GuiMaster.update(state, gun, camera.getScore());
		if (gun != null)
			masterRenderer.processMultiModeledEntity(gun, 0);
		
		if (Display.wasResized())
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		DisplayManager.updateDisplay();
	}
	
	private void startLogic(){
		
		state = GuiMaster.startState();
		short oldWeaponID = weaponID;
		weaponID = GuiMaster.getWeaponID(weaponID);
		Frustum.update(camera.getPosition(), camera.getYaw());
		if(oldWeaponID != weaponID){
			gun = Initialize.loadGun(weaponID);
			map.setGun(gun);
		}
		
		if (gun != null){
			gun.update(camera, masterRenderer, true);
			gun.ammo = gun.maxAmmo;
		}
		
		if(state != State.CHOOSING){
			deltaCursor();
			map.loadZombies();
			camera.setScore(0);
			if (gun == null){
				gun = Initialize.loadGun(weaponID);
				map.setGun(gun);
			}
			
			picker.setOffset((int)gun.offsets[3]);
			masterRenderer.setZoomAmount(0);
		}
		camera.increaseRotation(-0.1f, 0, 0);
	}
	
	private void gameLogic(){

		if(Keyboard.isKeyDown(Keyboard.KEY_K))
			camera.setHealth(0);
		
		escapeChecker();
		map.update();
		camera.update(map.getTerrain(), gun.mobility, flying, mode, picker.getCurrentRay(), map.getCollisions(), fps);
		gun.update(camera, masterRenderer, false);
		
		if(camera.getHealth() <= 0){
			state = State.CHOOSING;
			camera.setHealth(64);
			Sound.playSound(SoundNames.SCREAM2);
			Sound.playSound(SoundNames.BITE);
			deltaCursor();
		}
		displayFPS();
	}
	
	private void escapeChecker(){
		if (state != State.PAUSED){
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				state = State.PAUSED;
				deltaCursor();
			}
		}
	}
	
	private void deltaCursor(){
		if(state != State.PAUSED)
			Sound.resumeMusic();
		else
			Sound.pauseMusic();
		
		DisplayManager.resetTitle();
		CurseClass.toggle();
	}
	
	private void cleanUp(){
		
		map.save(mode);
		GunLoader.save(gun, weaponID, mode);
		GuiMaster.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
		Sound.cleanUp();
		System.exit(0);
	}
	
	private void checkGuiInteractions(){
		if(state == State.PAUSED){
			if(GuiMaster.savedAndQuit()){
				cleanUp();
			}
			state = GuiMaster.pauseState();
			if(state != State.PAUSED)
				deltaCursor();
		}
	}
	private void devMode(){
		if (MyInput.keyboardClicked(Keyboard.KEY_TAB)){
			mode = Mode.values()[(mode.type+1)%2];
			CurseClass.toggle();
			if(mode == Mode.DEV)
				map.loadMapEditors();
			else 
				map.closeMapEditors();
			
		}
		if (MyInput.keyboardClicked(Keyboard.KEY_Q))
			flying = !flying;
		
		if (MyInput.keyboardClicked(Keyboard.KEY_0))
			wireframe = !wireframe;
		
	}
	
}