package html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiTexture;
import nRenderEngine.Loader;
import toolbox.MyPaths;

public class LoadPage {

	public static void load(FontType font, Page page, Loader loader, String pageName) throws IOException{
		
		File input = new File(MyPaths.getBasePath() + pageName);
		
		Document doc = Jsoup.parse(input, "UTF-8", "");
			
		Element body = doc.body();
		Elements children = body.children();
			
		float startY = 0.0f;
			
		for (Element elem : children) {
				
			String tag = elem.tagName();
				
			if(tag.equals("p") || tag.equals("h1") || tag.equals("h2")
					|| tag.equals("h3") || tag.equals("h4")
					|| tag.equals("h5") || tag.equals("h6")){
				
				List<Attribute> attributes = elem.attributes().asList();
				
				float fontSize = 1.0f;
				float padding = 0.03f;
				boolean centered = false;
				float x = 0.05f;
				float y = startY;
				boolean isAbs = false;
				float right_padding = 0.9f;
					
				if(tag.equals("h1")){
					fontSize = 3.0f;
					padding = 0.1f;
				}else if(tag.equals("h2")){
					fontSize = 2.0f;
					padding = 0.08f;
				}else if(tag.equals("h3")){
					fontSize = 1.667f;
					padding = 0.04f;
				}else if(tag.equals("h4")){
					fontSize = 1.5f;
					padding = 0.03f;
				}else if(tag.equals("h5")){
					fontSize = 1.25f;
					padding = 0.02f;
				}else if(tag.equals("h6")){
					fontSize = 1.15f;
					padding = 0.01f;
				}
				
				if(!isAbs)
					startY += padding;
				
				y = startY;
				
				float r=1,g=1,b=1;
				
				for(Attribute attrib : attributes){
					String key = attrib.getKey();
					if(key.equals("font-size")){
						fontSize = Float.parseFloat(attrib.getValue());
					}
					else if (key.equals("text-align")){
						centered = attrib.getValue().equals("center");
					}
					else if (key.equals("color")){
						String[] division = attrib.getValue().split(",");
						r = Float.parseFloat(division[0]);
						g = Float.parseFloat(division[1]);
						b = Float.parseFloat(division[2]);
					}
					else if(key.equals("x")){
						x = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("y")){
						isAbs = true;
						y = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("padding-right")){
						right_padding = Float.parseFloat(attrib.getValue());
					}
				}
				GUIText newText = new GUIText(elem.text(), fontSize, font,
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
				List<Attribute> attributes = elem.attributes().asList();
				
				String source = "misc/no_src";
				float width = 0.1f;
				float height = 0.1f;
				float x = -0.8f;
				float y = startY;
				float padding = 0.1f;
				boolean isAbs = false;
				int actionID=0;
				String actionData = "";
				boolean frame=false;
				
				for(Attribute attrib : attributes){
					String key = attrib.getKey();
					if(key.equals("src")){
						source = attrib.getValue();
					}
					else if(key.equals("width")){
						width = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("height")){
						height = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("x")){
						x = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("y")){
						isAbs = true;
						y = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("padding")){
						padding = Float.parseFloat(attrib.getValue());
					}
					else if(key.equals("href")){
						actionID = 1;
						actionData = attrib.getValue();
						if(actionData.equals("GAME")){
							actionID = 2;
						}
						frame = true;
					}
				}
				if(isAbs)
					page.setBottom(Math.max(-y/2+height*2, page.getBottom()));
				
				if(!isAbs){
					startY += padding+height;
					y = -startY;
					page.setBottom(Math.max(-y-1+2*height+2*padding, page.getBottom()));
				}
				GuiTexture gui = new GuiTexture(loader.loadTexture(MyPaths.makeTexturePath
						(source)),
						new Vector2f(x+width,y-height), new Vector2f(width,height));
				
				gui.setFrame(frame);
				gui.setActionID(actionID);
				gui.setActionData(actionData);
				
				if(!isAbs){
					startY += padding;
				}
				page.getGuis().add(gui);
			}
		}
	}
	
}
