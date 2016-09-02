package nComponents;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import nEngineTester.Input;
import nToolbox.MousePicker;

public class PickerControl extends Component {

	private MousePicker picker;
	private List<Entity> entities;
	private boolean enabled = true;
	
	public void update(float dt) {
		
		Entity eParent = (Entity)parent;
		Position pos = (Position)eParent.getComponentByType(CompType.POSITION);
		Hitbox mine  = (Hitbox)eParent.getComponentByType(CompType.HIT_BOX);
		Scale myScale = (Scale)eParent.getComponentByType(CompType.SCALE);
		
		
		if(mine != null){
			
			if(mine.isSelected()){
				if(Input.wasClicked()){
					if(mine != null && enabled)mine.setSelected(false);
					enabled = !enabled;
				}
			}
			else {
				if(enabled && Input.wasClicked())enabled = false;
			}
			if(!enabled)return;
			
			for(Entity e : entities)
			{
				Position ePos = (Position)e.getComponentByType(CompType.POSITION);
				Hitbox theirs = (Hitbox)e.getComponentByType(CompType.HIT_BOX);
				Scale theirScale = (Scale)e.getComponentByType(CompType.SCALE);
				if(e == eParent.parent){continue;}
				if(mine == theirs || theirs == null){continue;}
				if(mine.isIntersect(theirs, ePos.getAdditivePosition(), picker.getCurrentTerrainPoint(), myScale.getMultiplicativeScale(),
						theirScale.getMultiplicativeScale())){
					return;
				}
			}
		}
		
		pos.setPosition(new Vector3f(picker.getCurrentTerrainPoint().x, pos.getPosition().y, picker.getCurrentTerrainPoint().z));
	}
	
	public PickerControl(Entity parent, MousePicker picker, List<Entity> entities){
		this.parent = parent;
		this.picker = picker;
		this.entities = entities;
		this.setType(CompType.PICKER_CONTROL);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
