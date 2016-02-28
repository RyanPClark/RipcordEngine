package loaders;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import security.Cryption;
import toolbox.MyPaths;
import entities.Light;

public final class LightLoader {
	
	private final static String lightData = MyPaths.makeSavePath("light");
	
	
	public static List<Light> Load(){
		
		List<Light> lights = new ArrayList<Light>();
		
		try{
			
			Cryption.decrypt(new File(lightData));
			
			  FileInputStream fstream = new FileInputStream(lightData);

			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  
			 Vector3f position = new Vector3f(0,0,0);
			 Vector3f color = new Vector3f(1,1,1);
			 Vector3f attenuation = new Vector3f(1,0,0);
			 Float intensity = 1f;
			  
			  while ((strLine = br.readLine()) != null)   {
				  
				 String[] currentLine = strLine.split(" ");
				  
				 if       (strLine.startsWith("lightPosX:")){
					 position.x = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightPosY:")){
					 position.y = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightPosZ:")){
					 position.z = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightColorRed:")){
					 color.x = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightColorGreen:")){
					 color.y = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightColorBlue:")){
					 color.z = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightAttenuationX:")){
					 attenuation.x = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightAttenuationY:")){
					 attenuation.y = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightAttenuationZ:")){
					 attenuation.z = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("lightIntensity:")){
					 intensity = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("light")){
					 Light light = new Light(position, color, intensity, attenuation);
					 lights.add(light);
					 
					 position = new Vector3f(0,0,0);
					 color = new Vector3f(1,1,1);
					 attenuation = new Vector3f(1, 0, 0);
					 intensity = new Float(1);
				 }
			  }
			  
			  in.close();
			  
			  Cryption.encrypt(new File(lightData));
			  
			    }catch (Exception e){
			    	System.out.println("bogger in light");
			  System.err.println("Error: " + e.getMessage());
			   }
		
		return lights;
		
	}
}