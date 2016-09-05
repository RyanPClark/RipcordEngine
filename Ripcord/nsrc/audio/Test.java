package audio;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		
		int buffer = AudioMaster.loadSound("audio/bounce.wav");
		Source source = new Source();
		source.setLooping(true);
		source.play(buffer);
		
		float xPos = 8;
		source.setPosition(xPos, 0, 0);
		
		char c = ' ';
		while(c != 'q'){
			c = (char) System.in.read();
			xPos -= 0.03f;
			source.setPosition(xPos, 0, 0);
			Thread.sleep(10);
		}
		source.delete();
		AudioMaster.cleanUp();
	}

}
