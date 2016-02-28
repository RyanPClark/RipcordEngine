package maps;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import security.Cryption;
import toolbox.MyPaths;

public final class Reading {
	
	
	public static List<List<Vector2f>> readCollisions(String name){
		
		name = MyPaths.makeSavePath(name);
		
		List<List<Vector2f>> list = new ArrayList<List<Vector2f>>();
			
			try{
				
				Cryption.decrypt(new File(name));
				
				  FileInputStream fstream = new FileInputStream(name);

				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  List<Vector2f> subList = new ArrayList<Vector2f>();
				  
				  while ((strLine = br.readLine()) != null)   {
					  
					  if (strLine.startsWith("New:")){
						  
						  list.add(subList);
						  
						  subList = new ArrayList<Vector2f>();
					  }
					  else if (strLine.startsWith("Vector:")){

						  String[] currentLine = strLine.split(" ");
						  
		                  Vector2f vector = new Vector2f(Float.valueOf(currentLine[1]),
		                            Float.valueOf(currentLine[2]));
		                  
						  subList.add(vector);
					  }
				  
				  }
				  
				  in.close();
				  
				  Cryption.encrypt(new File(name));
				  
				    }catch (Exception e){
				  System.err.println("Error: " + e.getMessage());
				   }
			
			return list;
	} 
	
	
	
	
	
}
