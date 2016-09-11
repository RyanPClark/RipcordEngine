package nComponents;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import nToolbox.MousePicker;

public class PickerControl extends Component {

	private MousePicker picker;
	private List<Entity> entities;
	private boolean enabled = true;
	private boolean addComponent;
	private float speedToBe;
	private boolean flying;
	private Terrain terrain;
	
	public void update(float dt) {
		
		Hitbox mine  = (Hitbox)parent.getComponentByType(CompType.HIT_BOX);
		Position posComp = (Position)parent.getComponentByType(CompType.POSITION);
		Vector3f pos = posComp.getPosition();
		
		if(!enabled)return;

			
		Scale myScale = (Scale)parent.getComponentByType(CompType.SCALE);
		
		for(Entity e : entities)
		{
			Hitbox theirs = (Hitbox)e.getComponentByType(CompType.HIT_BOX);
			if(e == parent.parent){continue;}
			if(mine == theirs || theirs == null){continue;}
			if(mine.isIntersect(theirs, picker.getCurrentTerrainPoint(), myScale.getMultiplicativeScale(),true)){
				return;
			}
		}
		float y = pos.y;
		if(!flying)
			y = terrain.getHeightOfTerrain(pos.x, pos.z);
		posComp.setPosition(new Vector3f(picker.getCurrentTerrainPoint().x, y, picker.getCurrentTerrainPoint().z));
	}
	
	public PickerControl(Entity parent, MousePicker picker, List<Entity> entities, Entity terrain, boolean addComponent, float speedToBe, boolean flying){
		this.parent = parent;
		this.picker = picker;
		this.entities = entities;
		this.addComponent = addComponent;
		this.speedToBe = speedToBe;
		this.flying = flying;
		this.terrain = (Terrain)terrain.getComponentByType(CompType.TERRAIN);
		this.setType(CompType.PICKER_CONTROL);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAddComponent() {
		return addComponent;
	}

	public void setAddComponent(boolean addComponent) {
		this.addComponent = addComponent;
	}

	public MousePicker getPicker() {
		return picker;
	}

	public void setPicker(MousePicker picker) {
		this.picker = picker;
	}

	public float getSpeedToBe() {
		return speedToBe;
	}

	public void setSpeedToBe(float speedToBe) {
		this.speedToBe = speedToBe;
	}
	
	

}
