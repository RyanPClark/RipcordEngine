package nComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import mapBuilding.Box;
import models.RawModel;
import models.TexturedModel;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;
import objConverter.ModelData;
import objConverter.OBJLoader;
import textures.ModelTexture;
import toolbox.MyPaths;

/**
 * 
 * @author Ryan Clark
 * 
 * Each entity has a list of components, which are updated each frame. The class also
 * provides a method for adding components and a method for getting a component given
 * its type.
 *
 */

public class Entity extends Component{

	private List<Component> components = new ArrayList<Component>();
	private String name;
	
	public String[] newEntity(String[] components, Loader loader, MasterRenderer mRenderer, MousePicker picker,
			List<Entity> entities){
		
		int index = 0;
		
		for(String comp : components){
			
			String rootString = comp.substring(0, 4);
			String argString = comp.substring(4);
			String[] args = argString.split(":");

			index++;
			
			if(rootString.equals("POS:"))
			{
				addComponent(new Position(this, new Vector3f(Float.parseFloat(args[0]),Float.parseFloat(args[1]), Float.parseFloat(args[2]))));
			}
			else if (rootString.equals("ROT:"))
			{
				addComponent(new Rotation(this, new Vector3f(Float.parseFloat(args[0]),Float.parseFloat(args[1]), Float.parseFloat(args[2]))));
			}
			else if (rootString.equals("SCL:"))
			{
				addComponent(new Scale(this, Float.parseFloat(argString)));
			}
			else if(rootString.equals("MDL:"))
			{
				ModelData data2 = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/" + args[0]));
				RawModel model2 = loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(),
						data2.getNormals(), data2.getIndices());
				
				ModelTexture texture2 = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/" + args[1])));
				TexturedModel tModel2 = new TexturedModel(model2, texture2);
				
				addComponent(new ModelComp(this, tModel2, mRenderer));
				
				if(args[2].equals("true"))
				{
					Box hitbox2 = data2.generateHitbox();
					hitbox2.generateModel(loader);
					addComponent(new Hitbox(this, hitbox2, picker));
				}
			}
			else if(rootString.equals("INX:"))
			{
				addComponent(new TextureIndex(this, Integer.parseInt(args[0])));
			}
			else if(rootString.equals("PCT:"))
			{
				addComponent(new PickerControl(this, picker, entities));
			}
			else if(rootString.equals("IDL:"))
			{
				addComponent(new Idle(this, Integer.parseInt(args[0])));
			}
			else if(rootString.equals("SUB:"))
			{
				Entity ent = new Entity();
				ent.setParent(this);
				entities.add(ent);
				components = ent.newEntity(Arrays.copyOfRange(components, index, components.length), loader, mRenderer, picker, entities);
				if(components.length == 0)
					return components;
			}
			else if(rootString.equals("END:"))
			{
				return Arrays.copyOfRange(components, index, components.length);
			}
		}
		return components;
	}
	
	public void update(float dt) {
		
		try{
		for(Component comp : components){
			comp.update(dt);
		}
		}
		catch(ConcurrentModificationException e){e.printStackTrace();}
	}

	public void setName(String newName)
	{
		name = newName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Component getComponentByType(CompType cType){
		
		for(Component c : components){
			
			if(cType == c.getType())
				return c;
		}
		return null;
	}
	
	public void addComponent(Component comp){
		
		components.add(comp);
	}
	
	public void removeComponent(Component comp){
		
		components.remove(comp);
	}
}
