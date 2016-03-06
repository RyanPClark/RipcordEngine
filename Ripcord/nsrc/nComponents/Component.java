package nComponents;

public abstract class Component {

	public abstract void update(float dt);
	
	protected Component parent;
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
