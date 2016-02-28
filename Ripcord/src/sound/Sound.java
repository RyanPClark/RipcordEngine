package sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import loaders.SoundLoader;
import toolbox.MyPaths;


public final class Sound {
	
	private static Clip[] clip;
	private static AudioInputStream[] sounds;
	private static float volumes[];
	private static String[] soundStrings;
	private static boolean musicOn = true;

	public static void init(){
		sounds = 		new AudioInputStream[15];
		soundStrings = 	new String[15];
		clip = 			new Clip[15];
		volumes = 		new float[15];
		
		soundStrings = SoundLoader.load();
		volumes = SoundLoader.getVolumes();
		
		for (int i = 0; i < soundStrings.length; i++){
			try {
				File file = new File(MyPaths.makeSoundPath(soundStrings[i]));
			    sounds[i]=AudioSystem.getAudioInputStream(file);
			    clip[i] = AudioSystem.getClip();
			
				if(i == 1){
					clip[i].open(sounds[i]);
					FloatControl gainControl = 
						    (FloatControl) clip[i].getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(volumes[i]);
				}
				
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			
		}
		
		clip[SoundNames.BG_MUSIC.ID].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void playSound(SoundNames name){
		
		int i = name.ID;
		
		if(!clip[i].isOpen()){
			try {
				clip[i].open(sounds[i]);
				FloatControl gainControl = 
					    (FloatControl) clip[i].getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(volumes[i]);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (musicOn){
			clip[i].setFramePosition(0);
			clip[i].stop();
		    clip[i].start();
		}
		
	}public static void pauseMusic(){
		
		for(int i = 0; i < clip.length; i++){
			clip[i].stop();
		}
		
	}public static void resumeMusic(){
		if (musicOn){
			for(int i = 0; i < clip.length; i++){
				if(clip[i].isOpen()){
					clip[i].start();
				}
			}
		}
		
	}public static void cleanUp(){
		for(int i = 0; i < clip.length; i++){
			if (clip[i].isOpen()){
				clip[i].close();
			}
		}
	}public static void scream(){
		
		if (musicOn){
			clip[SoundNames.SCREAM1.ID].start();
		}
	}public static void setMusicOn(){
		
		if (clip[SoundNames.BG_MUSIC.ID].isOpen()){
			clip[SoundNames.BG_MUSIC.ID].start();
		}
		musicOn = true;
		
	}public static void setMusicOff(){
		pauseMusic();
		musicOn = false;
	}
	
	public static boolean getMusicOn(){
		return musicOn;
	}

	public static void setMusicOn(boolean _musicOn) {
		musicOn = _musicOn;
	}
	
	
	
	
	
}
