package animation;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import engineTester.Initialize;
import entities.Camera;
import entities.Light;
import entities.MultiModeledEntity;
import nRenderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import toolbox.MyInput;

public final class TestLoop {
	
	private Camera camera; private MasterRenderer masterRenderer;
	private Loader loader; private float fps;
	private MultiModeledEntity entity;
	private Light light; Quaternion q;
	private List<Light> lights;
	private AnimationEditor animationEditor;
	
	public void run(){
		
		init();
		
		while(!Display.isCloseRequested()){
			
			render();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				camera.increaseRotation(-0.5f, 0, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				camera.increaseRotation(0.5f, 0, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				camera.increaseRotation(0, -0.5f, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				camera.increaseRotation(0, 0.5f, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				camera.increasePosition(0, 0.5f, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				camera.increasePosition(0, -0.2f, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				camera.increasePosition(0.2f * (float)Math.sin(Math.toRadians(camera.getYaw())), 0f, -0.2f * (float) Math.cos(Math.toRadians(camera.getYaw())));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				camera.increasePosition(-0.2f * (float)Math.sin(Math.toRadians(camera.getYaw())), 0f, 0.2f * (float) Math.cos(Math.toRadians(camera.getYaw())));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				camera.increasePosition(-0.2f * (float)Math.sin(Math.toRadians(camera.getYaw()+90)), 0f, 0.2f * (float) Math.cos(Math.toRadians(camera.getYaw()+90)));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				camera.increasePosition(0.2f * (float)Math.sin(Math.toRadians(camera.getYaw()+90)), 0f, -0.2f * (float) Math.cos(Math.toRadians(camera.getYaw()+90)));
			}
			if(MyInput.keyboardClicked(Keyboard.KEY_TAB)){
				loadMapEditors();
			}
			if(animationEditor != null){
				animationEditor.ModifyAnimation(entity);
			}
			
			camera.increasePosition(0, 0, Mouse.getDWheel()/60);
			
			fps = 1000/DisplayManager.getDelta();

			displayFPS();

		}
		cleanUp();
	}
	
	private void displayFPS(){
		if (System.currentTimeMillis() % 10 == 0){
			DisplayManager.setTitle((int)fps + "");
		}
	}
	
	private void init(){
		DisplayManager.createDisplay();
		loader = new Loader();
		masterRenderer = new MasterRenderer(loader);
		camera = new Camera();
		Initialize.init(loader);
		light = new Light(new Vector3f(300,200,-1000), new Vector3f(1,1,1), 2);
		lights = new ArrayList<Light>();
		lights.add(light);
		entity = Initialize.loadMultiModeledEntity("zombie", new Vector3f(0,0,-50), 0, 180, 0, new Vector3f(1, 1, 1), 1, false, 1, 0, false, 0, 2, false);
		entity.setStatic(false);
		entity.setAnimID(0);
		MasterAnimation.init();
		
	}
	
	private void render(){
		
		masterRenderer.processMultiModeledEntity(entity, 0);
		masterRenderer.renderTestScene(camera, lights);
			
		if (Display.wasResized()){
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}
		DisplayManager.updateDisplay();
		
	}
	
	private void cleanUp(){
		
		MasterAnimation.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
		System.exit(0);
	}
	private void loadMapEditors(){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				animationEditor = new AnimationEditor();
				animationEditor.setVisible(true);
			}
		});
	}
	
}