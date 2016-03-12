package nRenderEngine;

/**
 * @author Ryan Clark
 * 
 * Manages display creation, updating, and destruction, as well as
 * display attributes such as changing the title.
 */

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

import toolbox.MyPaths;

public class DisplayManager {

	private static final int WIDTH = 1080;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;
	
	private static long lastFrameTime;
	private static float delta;
	
	private static final String DISPLAY_TITLE = "Alpha 0.8.7";
	
	private static final String ICON_LOCATION = "misc/gameIcon";
	
	/**
	 * Method used once at the beginning of the program to create the display
	 * and initialize the context attribs
	 */
	
	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
			Display.create(new PixelFormat(), attribs); 
			
			Display.setResizable(true);
			lastFrameTime = getCurrentTime();
			
			Display.setIcon(new ByteBuffer[] {
				new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(MyPaths.makeTexturePath(ICON_LOCATION))), false, false, null),
				new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(MyPaths.makeTexturePath(ICON_LOCATION))), false, false, null)
			});
		
		} catch (LWJGLException | IOException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	/**
	 * synchronizes the frame rate, swaps buffers, updates the time delta,
	 * and checks for viewport changes.
	 */
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime);
		lastFrameTime = currentFrameTime;
		
		if (Display.wasResized())
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
	public static void setTitle(String title){
		Display.setTitle(title);
	}
	public static void resetTitle(){
		Display.setTitle(DISPLAY_TITLE);
	}
	private static long getCurrentTime(){
		return 1000*Sys.getTime()/Sys.getTimerResolution();
	}
	public static float getDelta(){
		return delta;
	}
	public static void setTitleDelta(){
		Display.setTitle(""+Math.round(1000f/delta));
	}
}
