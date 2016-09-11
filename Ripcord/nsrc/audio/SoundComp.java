package audio;

import org.lwjgl.util.vector.Vector3f;

import nComponents.CompType;
import nComponents.Component;
import nComponents.Entity;
import nComponents.Position;

public class SoundComp extends Component {

	private Source source;
	private int buffer;
	
	public void update(float dt) {
		Position pos = (Position)parent.getComponentByType(CompType.POSITION);
		if(pos != null){
			Vector3f additivePos = pos.getAdditivePosition();
			source.setPosition(additivePos.x, additivePos.y, additivePos.z);
		}
	}
	
	public SoundComp(Entity parent, Source source, int buffer){
		this.parent = parent;
		this.setType(CompType.SOUND);
		this.source = source;
		this.buffer = buffer;
		source.getBuffers().add(buffer);
	}
	
	public void stopAll(){
		source.stop();
	}

	public void playWithBuffer(int index){
		source.play(buffer);
	}
	
	public Source getSource() {
		return source;
	}

	public void setSources(Source source) {
		this.source = source;
	}

	public int getBuffer() {
		return buffer;
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}
}
