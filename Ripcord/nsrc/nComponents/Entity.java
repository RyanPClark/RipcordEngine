package nComponents;

import java.util.ArrayList;
import java.util.List;

public class Entity extends Component{

	public Entity(){
		
		components = new ArrayList<Component>();
	}
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
		for(Component comp : components){
			comp.update(dt);
		}
	}

	public Component getComponentByType(CompType cType){
		
		for(Component c : components){
			
			if(cType == c.getType()){
				
				return c;
			}
		}
		return null;
	}
	
	public void addComponent(Component comp){
		
		components.add(comp);
	}
	
	private List<Component> components;
}
