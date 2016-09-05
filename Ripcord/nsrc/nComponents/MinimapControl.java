package nComponents;

/**
 * @author Ryan Clark
 * 
 * Keyboard input for camera movement.
 */

public class MinimapControl extends Component {

	private int[] bounds;
	private Position posToFollow;
	
	/**
	 * @param dt - delta time
	 * 
	 * Updates the Position of the parent entity based on the rotation and keyboard inputs
	 */
	public void update(float dt) {
		
		Position pos = (Position)parent.getComponentByType(CompType.POSITION);
		
		float nx = posToFollow.getPosition().getX();
		float ny = posToFollow.getPosition().getY() + pos.getPosition().getY();
		float nz = posToFollow.getPosition().getZ();
		
		// -360, 360, -360, 360, 50, 175
		
		if(nx < bounds[0] || nx > bounds[1])
			nx = pos.getPosition().getX();
		
		if(nz < bounds[2] || nz > bounds[3])
			nz = pos.getPosition().getZ();
		
		if(ny < bounds[4] || ny > bounds[5])
			ny = pos.getPosition().getY();
		
		
		pos.getPosition().setX(nx);
		pos.getPosition().setY(ny);
		pos.getPosition().setZ(nz);
	}

	public MinimapControl(Entity parent, int[] bounds, Position posToFollow){
		
		this.setType(CompType.MINIMAP_CONTROL);
		this.setParent(parent);
		this.bounds = bounds;
		this.posToFollow = posToFollow;
	}
}
