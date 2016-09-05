package nComponents;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

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
	private List<Entity> children = new ArrayList<Entity>();
	private String name;
	
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

	public List<Entity> getChildren() {
		return children;
	}

	public void setChildren(List<Entity> children) {
		this.children = children;
	}
}
