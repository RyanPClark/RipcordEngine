package guis;

/**
 * @author Ryan Clark
 * 
 * Renders GUIs to screen and highlights them each frame.
 */

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

import models.RawModel;

public final class GUIRenderer {

	private static final float distanceBetweenBullets = 70.0f;
	
	private final RawModel quad;
	private GuiShader shader;
	
	public GUIRenderer(nRenderEngine.Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	/**
	 * Called once each frame to render a list of GUIs to the screen.
	 * 
	 * @param guis - the GUIs to be rendered this frame.
	 */
	public void Render(List<GuiTexture> guis){
			
		initialize();
			
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		for(GuiTexture gui : guis){
			
			if(GuiInteraction.isInArea(gui) && gui.isFrame()){
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
				shader.loadTransformation(gui.getMatrix());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
				
				GuiInteraction.highLightTexture.setPosition(gui.getPosition());
				GuiInteraction.highLightTexture.setscale(gui.getScale());
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, GuiInteraction.highLightTexture.getTexture());
				shader.loadTransformation(GuiInteraction.highLightTexture.getMatrix());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
			else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
				shader.loadTransformation(gui.getMatrix());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		terminate();
	}
	
	/**
	 * Starts shader and binds VAO. Called once each frame.
	 */
	private void initialize(){
		
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
			
		shader.start();
	}
	/**
	 * Stops shader and unbinds VAO. Called once each frame.
	 */
	private void terminate(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
			
		shader.stop();
	}
	
	/**
	 * called by MasterRenderer at program termination
	 */
	public void cleanUp(){
		shader.cleanUp();
	}
	
	/**
	 * @deprecated
	 * @param gui
	 * @param do_initialize
	 */
	protected void Render(GuiTexture gui, boolean do_initialize){
		
		if(do_initialize){
			initialize();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
		shader.loadTransformation(gui.getMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			
		if(do_initialize)
			terminate();
	}
		
	/**
	 * @deprecated
	 * @param bullet
	 * @param ammo
	 */
	protected void renderBullets(GuiTexture bullet, int ammo){
			
		initialize();
			
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bullet.getTexture());
		
		for(short i = 0; i < ammo; i++){
			
			bullet.setPosition(new Vector2f(bullet.getPosition().x + 1/distanceBetweenBullets, bullet.getPosition().y));
			
			shader.loadTransformation(bullet.getMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		bullet.setPosition(new Vector2f(bullet.getPosition().x - ammo/distanceBetweenBullets, bullet.getPosition().y));
		
		terminate();
	}
}
