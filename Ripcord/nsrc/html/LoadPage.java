package html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import toolbox.MyPaths;

public class LoadPage {

	public static List<GUIText> texts = new ArrayList<GUIText>();
	
	public static void load(FontType font){
		
		
		File input = new File(MyPaths.getBasePath() + "/testHTML.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8", "");
			
			Element body = doc.body();
			Elements children = body.children();
			
			float startY = 0.0f;
			String currentLink = "no_link";
			
			for (Element elem : children) {
				
				String tag = elem.tagName();
				System.out.println(tag);
				if(tag.equals("p") || tag.equals("h1") || tag.equals("h2")
						|| tag.equals("h3") || tag.equals("h4")
						|| tag.equals("h5") || tag.equals("h6")){
					
					List<Attribute> attributes = elem.attributes().asList();
					
					float fontSize = 1.0f;
					float padding = 0.01f;
					boolean centered = false;
					
					if(tag.equals("h1")){
						fontSize = 3.0f;
						padding = 0.1f;
					}else if(tag.equals("h2")){
						fontSize = 2.0f;
						padding = 0.06f;
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
					
					startY += padding;
					
					try{
						Element link = elem.select("a").first();
						currentLink = link.attr("href");
					}
					catch(NullPointerException e){}
					
					
					for(Attribute attrib : attributes){
						String key = attrib.getKey();
						if(key.equals("font-size")){
							fontSize = Float.parseFloat(attrib.getValue());
						}
						else if (key.equals("text-align")){
							centered = attrib.getValue().equals("center");
						}
					}
					GUIText newText = new GUIText(elem.text(), fontSize, font,
							new Vector2f(centered ? 0.0f : 0.05f,startY),
							1.0f, centered);
					newText.setColour(1, 1, 1);
					
					texts.add(newText);
					
					if(!currentLink.equals("no_link")){
						newText.setLinkText(currentLink);
					}
					
					startY += padding;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
