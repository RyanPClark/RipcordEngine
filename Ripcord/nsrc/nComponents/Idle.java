package nComponents;

/**
 * 
 * @author Ryan Clark
 * 
 * Stores a texture index and ability to calculate its offset for an entity.
 *
 */

public class Idle extends Component {

	private static final boolean RIGHT = true;
	
	private int frequency;
	private float turnSpeed = 10.1f;
	private int turnSize = 30;
	private boolean direction = RIGHT;
	private boolean turning = false;
	private float amountTurned = 0;
	private boolean changeDirection = false;
	
	public void update(float dt) {
		
		if(!turning)
		{
			turning = (amountTurned == frequency);
			amountTurned++;
			if(turning){
				if(changeDirection)
					direction = !direction;
				amountTurned = 0;
			}
		}
		else {
			Rotation rComp = (Rotation)parent.getComponentByType(CompType.ROTATION);
			amountTurned += turnSpeed;
			if (direction == RIGHT) rComp.getRotation().y += turnSpeed;
			else rComp.getRotation().y -= turnSpeed;
			if(amountTurned >= turnSize)
			{
				turning = false;
				amountTurned = 0;
			}
		}
		
	}

	public Idle(Entity parent, int frequency, float speed, int size, boolean changeDirection){
		
		this.setType(CompType.IDLE);
		this.setParent(parent);
		this.setFrequency(frequency);
		this.turnSize = size;
		this.turnSpeed = speed;
		this.changeDirection = changeDirection;
	}
	
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
