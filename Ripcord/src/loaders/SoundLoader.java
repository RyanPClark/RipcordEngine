package loaders;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import security.Cryption;
import toolbox.MyPaths;

public final class SoundLoader {
	
	private static String soundData = "sound";
	private static String soundStrings[];
	private static float soundVolumes[];
	
	public static String[] load(){
		
		soundData = MyPaths.makeSavePath(soundData);
		soundStrings = new String[15];
		soundVolumes = new float[15];
		
		try{
			
			Cryption.decrypt(new File(soundData));
			
			  FileInputStream fstream = new FileInputStream(soundData);

			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  Integer i = 0;
			  
			  while ((strLine = br.readLine()) != null)   {
				  
				  String[] currentLine = strLine.split(" ");
				  
				  soundStrings[i] = currentLine[0];
				  soundVolumes[i] = Float.valueOf(currentLine[1]);
				  i++;
			  }
			  
			  in.close();
			  
			  Cryption.encrypt(new File(soundData));
			  
			    }catch (Exception e){
			    	System.out.println("bogger");
			  System.err.println("Error: " + e.getMessage());
			   }
		
		return soundStrings;
		
	}
	
	public static float[] getVolumes(){
		return soundVolumes;
	}
}
