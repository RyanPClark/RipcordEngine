package loaders;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.MyPaths;

public final class AnimationLoader {
	
	
	public static List<Vector4f[]> readRotation(String name, int id){
		
		name = MyPaths.makeSavePath("Animations/"+name+id);
		
		List<Vector4f[]> list = new ArrayList<Vector4f[]>();
			
			try{
				
				  FileInputStream fstream = new FileInputStream(name);

				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  Vector4f[] subList = new Vector4f[2];
				  int i = 0;
				  
				  while ((strLine = br.readLine()) != null)   {
					  
					  if (strLine.startsWith("NewRotation:")){
						  
						  list.add(subList);
						  
						  subList = new Vector4f[2];
					  }
					  else if (strLine.startsWith("Vector4f:")){

						  String[] currentLine = strLine.split(" ");
						  
		                  Vector4f vector = new Vector4f(Float.valueOf(currentLine[1]),
		                            Float.valueOf(currentLine[2]), Float.valueOf(currentLine[3]),
		                            Float.valueOf(currentLine[4]));
		                  
						  subList[i] = vector;
						  i = (i++)%2;
					  }
				  
				  }
				  
				  in.close();
				    }catch (Exception e){
				  System.err.println("Error: " + e.getMessage());
				   }
			
			return list;
	} 
	
	public static List<Vector3f> readTranslation(String name, int id){
		
		name = MyPaths.makeSavePath("Animations/"+name+id);
		
		List<Vector3f> list = new ArrayList<Vector3f>();
			
			try{
				
				  FileInputStream fstream = new FileInputStream(name);

				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  
				  while ((strLine = br.readLine()) != null)   {
					  
					  if (strLine.startsWith("Translation:")){

						  String[] currentLine = strLine.split(" ");
						  
		                  Vector3f vector = new Vector3f(Float.valueOf(currentLine[1]),
		                            Float.valueOf(currentLine[2]), Float.valueOf(currentLine[3]));
		                  
						  list.add(vector);
					  }
				  
				  }
				  
				  in.close();
				    }catch (Exception e){
				  System.err.println("Error: " + e.getMessage());
				   }
			
			
			return list;
	} 
	
	public static int readLength(String name, int id){
		
		name = MyPaths.makeSavePath("Animations/"+name+id);
		
		int length = 1;
			
			try{
				
				  FileInputStream fstream = new FileInputStream(name);

				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  
				  while ((strLine = br.readLine()) != null)   {
					  
					  if (strLine.startsWith("Length:")){

						  String[] currentLine = strLine.split(" ");
						  
						  length = Integer.parseInt(currentLine[1]);
						  break;
					  }
				  }
				  in.close();
			}
			catch(Exception e){
				
				 System.err.println("Error: " + e.getMessage());
			}
			return length;
	} 
	
	
	
	
}
