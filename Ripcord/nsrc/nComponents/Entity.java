package nComponents;

import java.util.ArrayList;
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
	
	public void update(float dt) {
		
		for(Component comp : components){
			comp.update(dt);
		}
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
}
