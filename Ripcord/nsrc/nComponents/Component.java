package nComponents;

/**
 * 
 * @author Ryan Clark
 *
 * Base class for component based architecture. Each component has a parent, type, 
 * and update method, as well as associated getters and setters.
 * 
 */

public abstract class Component {

	public abstract void update(float dt);
	
	protected Component parent = null;
	private CompType type;
	
	public Component getParent() {
		return parent;
	}
	public void setParent(Component parent) {
		this.parent = parent;
	}
	public CompType getType() {
		return type;
	}
	public void setType(CompType type) {
		this.type = type;
	}
}
