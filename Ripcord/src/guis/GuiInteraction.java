package guis;

/**
 * @author Ryan Clark
 * 
 * this class contains methods to highlight guis when the mouse is over them
 * and responds to GUI button clicks
 */


import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import nRenderEngine.Loader;
import toolbox.MyPaths;

	public final class GuiInteraction {
		
		protected static GuiTexture highLightTexture;
		private static boolean wasDown = false;
		private static boolean isClick = false;
		
		public static void init(Loader loader){
			highLightTexture  = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("guis/highlight")), new Vector2f(0f,0f), new Vector2f(1f,1f));
			highLightTexture.setFrame(false);
			// safety because if true, recursion will cause game to crash
		}
		
		/**
		 * @deprecated
		 * @param guis
		 * @param highLightTexture
		 * @param guiRenderer
		 * @param skips
		 */
		protected static void frameGuis(List<GuiTexture> guis, GuiTexture highLightTexture, GUIRenderer guiRenderer, boolean[] skips){
			
			int i = -1;
			
			for(GuiTexture gui : guis){
				
				i++;
				if(skips != null){
					
					if(!skips[i]){
						
						if(isInArea(gui)){
							highLightTexture.setPosition(gui.getPosition());
							highLightTexture.setscale(gui.getScale());
							guiRenderer.Render(highLightTexture,true);
						}
					}
				}
			}
		}
		/**
		 * determines whether the mouse is over a gui. Called once per gui per frame
		 * 
		 * @param gui
		 * @return
		 */
		protected static boolean isInArea(GuiTexture gui){
				
			float dWidth = Display.getWidth();
			float dHeight = Display.getHeight();
				
			int mx = Mouse.getX(); mx -= dWidth/2;
			int my = Mouse.getY(); my -= dHeight/2;
				
			Vector2f position = gui.getPosition();
			Vector2f scale = gui.getScale();
			
			float left = (0.5f*position.x*dWidth + 0.5f*scale.x*dWidth);
			float top = (0.5f*position.y*dHeight + 0.5f*scale.y*dHeight);
			float right = (0.5f*position.x*dWidth - 0.5f*scale.x*dWidth);
			float down = (0.5f*position.y*dHeight - 0.5f*scale.y*dHeight);
			
			if(mx < left && mx > right && my > down && my < top){

				gui.setInArea(true);
				return true;
			}

			gui.setInArea(false);
			return false;
		}
		
		/**
		 * Called once each frame and checks for clicks
		 */
		
		public static void update(){

			boolean isDown = Mouse.isButtonDown(0);
			if(!isDown && wasDown)
				isClick = true;
			else
				isClick = false;
			wasDown = isDown;
			
		}
		
		/**
		 * 
		 * checks if a gui is clicked - used by page class
		 * 
		 * @param gui
		 * @return
		 */
		
		public static boolean isClicked(GuiTexture gui){
			
			if(isClick && gui.isInArea())
				return true;
			
			return false;
		}
		
		
}
