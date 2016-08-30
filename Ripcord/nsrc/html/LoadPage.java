package html;

/**
 * @author Ryan Clark
 * 
 * This method class has one method which loads an rhtml page using the jsoup HTML
 * parser and terrible custom methods
 */

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.ActionType;
import guis.GuiTexture;
import nRenderEngine.Loader;
import toolbox.MyPaths;

public class LoadPage {
	
	// this map stores all the fonts that have been loaded to avoid redundant loading
	private static Map<String, FontType> fonts = new HashMap<String, FontType>();
	
	private static float startY;
	
	/**
	 * 
	 * @param elem   - current element(<tag key="value">content</tag>)
	 * @param page   - page which is being built
	 * @param loader - OpenGL loader which is used to load textures and fonts
	 */
	
	private static void dealWithElement(Element elem, Page page, Loader loader){
		
		String tag 			= 	elem.tagName();
		String source 		= 	"misc/no_src";
		String actionData 	= 	"";
		String fontName 	= 	"candara";
		
		float width 		= 	0.1f;
		float height 		= 	0.1f;
		float x 			= 	0.05f;
		float y 			= 	startY;
		float padding 		= 	0.1f;
		float fontSize 		= 	1.0f;
		float right_padding = 	0.9f;
		float r				= 	1.0f;
		float g				= 	1.0f;
		float b				=	1.0f;
		
		boolean isAbs 		= 	false;
		boolean frame		=	false;
		boolean centered 	= 	false;
		
		int actionID		=	0;
		
		
		switch(tag){
			case "p":
				fontSize  	= 1.0f;
				padding 	= 0.03f;
				break;
			case "h1":
				fontSize  	= 3.0f;
				padding 	= 0.1f;
				break;
			case "h2":
				fontSize  	= 2.0f;
				padding 	= 0.08f;
				break;
			case "h3":
				fontSize  	= 1.667f;
				padding 	= 0.06f;
				break;
			case "h4":
				fontSize  	= 1.5f;
				padding 	= 0.05f;
				break;
			case "h5":
				fontSize  	= 1.25f;
				padding 	= 0.04f;
				break;
			case "h6":
				fontSize  	= 1.15f;
				padding 	= 0.03f;
				break;
			case "img":
				x = -0.8f;
				break;
		}

		List<Attribute> attributes = elem.attributes().asList();

		for(Attribute attrib : attributes){
			
			String key = attrib.getKey();
			
			switch(key){
				case "font-size":
					fontSize = Float.parseFloat(attrib.getValue());
					break;
				case "text-align":
					centered = attrib.getValue().equals("center");
					break;
				case "color":
					String[] division = attrib.getValue().split(",");
					r = Float.parseFloat(division[0]);
					g = Float.parseFloat(division[1]);
					b = Float.parseFloat(division[2]);
					break;
				case "x":
					x = Float.parseFloat(attrib.getValue());
					break;
				case "y":
					isAbs = true;
					y = Float.parseFloat(attrib.getValue());
					break;
				case "padding-right":
					right_padding = Float.parseFloat(attrib.getValue());
					break;
				case "font-family":
					fontName = attrib.getValue();
					break;
				case "src":
					source = attrib.getValue();
					break;
				case "width":
					width = Float.parseFloat(attrib.getValue());
					break;
				case "height":
					height = Float.parseFloat(attrib.getValue());
					break;
				case "padding":
					padding = Float.parseFloat(attrib.getValue());
					break;
				case "href":
					actionID = 1;
					actionData = attrib.getValue();
					frame = true;
					if(actionData.equals("GAME"))
						actionID = 2;
					break;
			}
		}
		
		if(			tag.equals("p")
				||  tag.equals("h1") || tag.equals("h2")
				||  tag.equals("h3") || tag.equals("h4")
				||  tag.equals("h5") || tag.equals("h6")){
			
			
			if(!isAbs){
				startY += padding;
				y = startY;
			}
			
			if(!fonts.containsKey(fontName)){
				fonts.put(fontName, new FontType(loader.loadTextureWithBias
						(MyPaths.makeTexturePath("font/"+fontName),
						0.0f), new File(MyPaths.makeFontPath(fontName))));
			}
			
			GUIText newText = new GUIText(elem.text(), fontSize, fonts.get(fontName),
					new Vector2f(x, y),
					right_padding, centered);
			
			newText.setColour(r, g, b);
			
			page.getTexts().add(newText);
			
			if(!isAbs){
				startY += (newText.getNumberOfLines())*padding;
				page.setBottom(Math.max(startY-1, page.getBottom()));
			}
		}
		else if (tag.equals("img")){
			
			if(isAbs)
				page.setBottom(Math.max(-y-1+2*height+2*padding, page.getBottom()));
				
			else {
				startY += padding+height;
				y = -startY;
				page.setBottom(Math.max(-y-1+2*height+2*padding, page.getBottom()));
			}
			GuiTexture gui = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath
					(source)),
					new Vector2f(x+width,y-height), new Vector2f(width,height));
			
			gui.setFrame(frame);
			gui.setActionID(ActionType.values()[actionID]);
			gui.setActionData(actionData);
			
			if(!isAbs){
				startY += padding;
			}
			page.getGuis().add(gui);
		}
	}
	
	/**
	 * loads an rhtml Page from the disc and stores it in page
	 * 
	 * @param page     - Page to be built
	 * @param loader   - OpenGL loader for textures and text
	 * @param pageName - location on disc of page to be loaded relative to basepath
	 */
	
	public static void load(Page page, Loader loader, String pageName) throws IOException{

		File input = new File(MyPaths.getBasePath() + pageName);
		Document doc = Jsoup.parse(input, "UTF-8", "");

		// clear all the guis and text on the input page
		page.unload();
		
		Element body = doc.body();
		Elements children = body.children();
			
		for (Element elem : children) {
		
			dealWithElement(elem, page, loader);
		}
		// reset y value for next load
		startY = 0.0f;
	}
}
