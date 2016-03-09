package html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import fontMeshCreator.FontType;
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
	private float yShift=0.0f;
	
	public Page update(Loader loader, FontType font){
		
		GuiInteraction.update();

		float dWheel = Mouse.getDWheel() / 1000.0f;
		
		if(dWheel != 0){
			
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
		
		for(GuiTexture gui : guis){
			
			if(GuiInteraction.isClicked(gui)){
				
				if(gui.getActionID() == 1){
					unload();
					Page nPage = new Page();
					try {
						LoadPage.load(font, nPage, loader, gui.getActionData());
					} catch (IOException e) {
						e.printStackTrace();
						return this;
					}
					return nPage;
				}
				else if(gui.getActionID() == 2){
					unload();
					return null;
				}
			}
		}
		
		return this;
	}
	
	public void unload(){
		for(GUIText text : texts){
			TextMaster.removeText(text);
		}
		guis.clear();
	}
	
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
