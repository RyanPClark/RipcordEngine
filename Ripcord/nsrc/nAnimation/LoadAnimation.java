package nAnimation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.MyPaths;

public class LoadAnimation {

	public static Animation loadAnimation(String fileName) throws IOException{
		
		fileName = MyPaths.makeSavePath("Animations/"+fileName);
		
		List<Vector4f[]> rotations = new ArrayList<Vector4f[]>();
		List<Vector3f[]> translations = new ArrayList<Vector3f[]>();
		Animation anim = new Animation(rotations, translations, 100);
		
		FileInputStream fstream = new FileInputStream(fileName);

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		while((strLine = br.readLine()) != null){
			String[] args = strLine.split(" ");
			if(args[0].equals("length"))
			{
				anim.setLength(Integer.parseInt(args[1]));
			}
			else if(args[0].equals("loop"))
			{
				anim.setLoop(Boolean.parseBoolean(args[1]));
			}
			else if(args[0].equals("TaB"))
			{
				anim.setThroughAndBack(Boolean.parseBoolean(args[1]));
			}
			else if (args[0].equals("RotFrame")){
				Vector4f zeroes = new Vector4f(0,0,0,0);
				Vector4f[] array = new Vector4f[Integer.parseInt(args[1])];
				for(int i = 0; i < array.length; i++){
					array[i] = zeroes;
				}
				anim.getRotationData().add(array);
			}
			else if (args[0].equals("Rotation:")){
				
				Vector4f newVec = new Vector4f(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
						Float.parseFloat(args[3]), 0);
					
				anim.getRotationData().get(anim.getRotationData().size() - 1)
				[Integer.parseInt(args[4])] = newVec;
				
			}
			else if (args[0].equals("TransFrame")){
				Vector3f zeroes = new Vector3f(0,0,0);
				Vector3f[] array = new Vector3f[Integer.parseInt(args[1])];
				for(int i = 0; i < array.length; i++){
					array[i] = zeroes;
				}
				anim.getTranslationData().add(array);
			}
			else if (args[0].equals("Translation:")){
				Vector3f newVec = new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
						Float.parseFloat(args[3]));
				anim.getTranslationData().get(anim.getTranslationData().size() - 1)
				[Integer.parseInt(args[4])] = newVec;
			}
		}
		
		br.close();
		
		
		return anim;
	}
}
