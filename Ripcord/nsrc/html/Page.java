package html;

/**
 * @author Ryan Clark
 * 
 * this class stores the guis, text, and scroll variables for an rhtml page.
 * It is updated each frame to scroll and respond to button inputs
 * 
 * TODO: Add sliders / scroll bar
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import fontMeshCreator.GUIText;
import fontMeshCreator.TextMaster;
import guis.GuiInteraction;
import guis.GuiTexture;
import nRenderEngine.Loader;

public class Page {
	
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private List<GUIText> texts = new ArrayList<GUIText>();
	
	private static final float TOP = 0.0f;
	private float bottom = 0.0f;
	private float yShift = 0.0f;
	
	public Page update(Loader loader){
		
		GuiInteraction.update();
		updateScroll();

		for(GuiTexture gui : guis){
			
			if(!GuiInteraction.isClicked(gui))
				continue;
				
			if(gui.getActionID() == 1){
				
				try {
					LoadPage.load(this, loader, gui.getActionData());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return this;
			}
			else if(gui.getActionID() == 2){
				unload();
				return null;
			}
		}
		
		return this;
	}
	
	/**
	 * update the position of the guis and texts in response to the mouse wheel
	 * scrolling
	 */
	
	private void updateScroll(){
		
		// speed and direction of mouse wheel spin
		float dWheel = Mouse.getDWheel() / 1000.0f;
		
		// do not continue if the mouse wheel is not spinning
		if(dWheel == 0)
			return;
		
		if(yShift + dWheel <= TOP && yShift + dWheel >= -bottom){
			yShift += dWheel;
			for(GuiTexture gui : guis){
				gui.changeY(-dWheel);
			}
			for(GUIText text : texts){
				text.changeY(dWheel/2.0f);
			}
		}
	}
	
	/**
	 * Removes texts from TextMaster and clears the guis. Called when page is closed.
	 */
	
	public void unload(){
		for(GUIText text : texts){
			TextMaster.removeText(text);
		}
		guis.clear();
	}
	
	
	/**
	 * Getters and setters for guis, texts, and bottom
	 */
	
	public List<GuiTexture> getGuis() {
		return guis;
	}

	public void setGuis(List<GuiTexture> guis) {
		this.guis = guis;
	}

	public List<GUIText> getTexts() {
		return texts;
	}

	public void setTexts(List<GUIText> texts) {
		this.texts = texts;
	}

	public float getBottom() {
		return bottom;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}
}
