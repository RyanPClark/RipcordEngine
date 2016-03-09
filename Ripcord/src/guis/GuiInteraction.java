package guis;


import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import nRenderEngine.Loader;
import toolbox.MyPaths;

	public final class GuiInteraction {
		
		public static GuiTexture highLightTexture;
		private static boolean wasDown = false;
		private static boolean isClick = false;
		
		public static void init(Loader loader, GUIRenderer _guiRenderer){
			highLightTexture  = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath("guis/highlight")), new Vector2f(0f,0f), new Vector2f(1f,1f));
			highLightTexture.setFrame(false);
		}
		
		
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
		
		public static boolean isInArea(GuiTexture gui){
				
			float width = Display.getWidth();
			float height = Display.getHeight();
				
			int mx = Mouse.getX(); mx -= width/2;
			int my = Mouse.getY(); my -= height/2;
				
			Vector2f position = gui.getPosition();
			Vector2f scale = gui.getScale();
			
			float left = (0.5f*position.x*width + 0.5f*scale.x*width);
			float top = (0.5f*position.y*height + 0.5f*scale.y*height);
			float right = (0.5f*position.x*width - 0.5f*scale.x*width);
			float down = (0.5f*position.y*height - 0.5f*scale.y*height);
			
			if(mx < left && mx > right && my > down && my < top){

				gui.setInArea(true);
				return true;
			}

			gui.setInArea(false);
			return false;
		}
		
		public static void update(){

			boolean isDown = Mouse.isButtonDown(0);
			if(!isDown && wasDown)
				isClick = true;
			else
				isClick = false;
			wasDown = isDown;
			
		}
		
		public static boolean isClicked(GuiTexture gui){
			
			if(isClick && gui.isInArea())
				return true;
			
			return false;
		}
		
		
}
