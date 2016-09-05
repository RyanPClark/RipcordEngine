package audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceId;
	private List<Integer> buffers = new ArrayList<Integer>();
	
	public Source(){
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0.3f);
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 15);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
	}
	
	public void playFromIndex(int index){
		play(buffers.get(index));
	}
	
	public void play(int buffer){
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void delete(){
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	public void setVolume(float volume){
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
	}
	
	public void setPitch(float pitch){
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z){
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z){
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	public void setLooping(boolean val){
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, val ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying(){
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void pause(){
		AL10.alSourcePause(sourceId);
	}
	
	public void play(){
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop(){
		AL10.alSourceStop(sourceId);
	}

	public List<Integer> getBuffers() {
		return buffers;
	}

	public void setBuffers(List<Integer> buffers) {
		this.buffers = buffers;
	}
	
	
}
