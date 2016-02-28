package loaders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.util.vector.Vector3f;

import security.Cryption;
import toolbox.MyPaths;
import enums.Mode;
import entities.Gun;

public final class GunLoader {
	
	private static String gunData = "Guns/";
	
	public static void init(){
		
		gunData = MyPaths.makeSavePath(gunData);
		gunData = gunData.substring(0, gunData.length() - 5);
	}
	
	
	public static Gun Load(Gun gun, int ID){
		
		try{
			
			Cryption.decrypt(new File(gunData + ID + ".data"));
			
			  FileInputStream fstream = new FileInputStream(gunData + ID + ".data");

			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  
			  Vector3f scale = new Vector3f(1,1,1);
			  boolean transparency = false;
			  int ROF = 8;
			  boolean automatic = true;
			  int maxAmmo = 20;
			  float recoilAmount = 0.1f;
			  float power = 1.5f;
			  float[] offsets = new float[4];
			  float mobility = 0;
			  int gunSoundID = 0;
			  float[] movingValues = new float[3];
			  boolean semiauto = false;
			  
			  while ((strLine = br.readLine()) != null)   {
				  
				 String[] currentLine = strLine.split(" ");
				  
				 if       (strLine.startsWith("scaleX:")){
					 scale.x = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("scaleY:")){
					 scale.y = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("scaleZ:")){
					 scale.z = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Transparency:")){
					 transparency = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("ROF:")){
					 ROF = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Automatic:")){
					 automatic = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MaxAmmo:")){
					 maxAmmo = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("RecoilAmount:")){
					 recoilAmount = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Power:")){
					 power = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets0:")){
					 offsets[0] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets1:")){
					 offsets[1] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets2:")){
					 offsets[2] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets3:")){
					 offsets[3] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Mobility:")){
					 mobility = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("GunSoundID:")){
					 gunSoundID = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Semiauto:")){
					 semiauto = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues0:")){
					 movingValues[0] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues1:")){
					 movingValues[1] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues2:")){
					 movingValues[2] = Float.valueOf(currentLine[1]);
				 }
				 
			  }
			  
			  gun.setSpecificData(scale, transparency, (short)ROF, automatic, (short)maxAmmo, recoilAmount, power, offsets, mobility, gunSoundID, movingValues, semiauto);
			  
			  in.close();
			  
			  Cryption.encrypt(new File(gunData + ID + ".data"));
			  
			    }catch (Exception e){
			    	System.out.println("bogger");
			  System.err.println("Error: " + e.getMessage());
			   }
		
		return gun;
		
	}
	
	public static void save(Gun gun, short ID, Mode mode){
		
		if (mode == Mode.DEV){
			String name = gunData + ID + ".data";
			
	        try {
	        	
	          File file = new File(name);
	          Float flt = new Float(0);
	          Boolean bool = new Boolean(false);
	          Integer inr = new Integer(0);
	          BufferedWriter output = new BufferedWriter(new FileWriter(file));
	          
	          flt = gun.getScale().x;
	          output.write("scaleX: "+flt.toString()+"\n");
	                  
	          flt = gun.getScale().y;
	          output.write("scaleY: "+flt.toString()+"\n");
	                  
	          flt = gun.getScale().z;
	          output.write("scaleZ: "+flt.toString()+"\n");
	                  
	          bool = gun.getModel().getTexture().isHasTransparency();
	          output.write("Transparency: "+bool.toString()+"\n");
	                  
	          inr = (int) gun.ROF;
	          output.write("ROF: "+inr.toString()+"\n");
	                  
	          bool = (Boolean) gun.automatic;
	          output.write("Automatic: "+bool.toString()+"\n");
	                  
	          inr = (int) gun.maxAmmo;
	          output.write("MaxAmmo: "+inr.toString()+"\n");
	                  
	          flt = gun.recoilAmount;
	          output.write("RecoilAmount: "+flt.toString()+"\n");
	                  
	          flt = gun.power;
	          output.write("Power: "+flt.toString()+"\n");
	                  
	          flt = (float) gun.offsets[0];
	          output.write("Offsets0: "+flt.toString() + "\n");
	          
	          flt = (float) gun.offsets[1];
	          output.write("Offsets1: "+flt.toString() + "\n");
	          
	          flt = (float) gun.offsets[2];
	          output.write("Offsets2: "+flt.toString() + "\n");
	          
	          flt = (float) gun.offsets[3];
	          output.write("Offsets3: "+flt.toString() + "\n");
	                  
	          flt = (float) gun.mobility;
	          output.write("Mobility: " + flt.toString() + "\n");
	                  
	          inr = gun.gunSoundID;
	          output.write("GunSoundID: " + inr.toString() + "\n");
	                  
	          bool = (Boolean) gun.semiauto;
	          output.write("Semiauto: " + flt.toString() + "\n");
	                  
	          flt = (float) gun.movingValues[0];
	          output.write("MovingValues0: " + flt.toString() + "\n");
	                  
	          flt = (float) gun.movingValues[1];
	          output.write("MovingValues1: " + flt.toString() + "\n");
	                  
	          flt = (float) gun.movingValues[2];
	          output.write("MovingValues2: " + flt.toString() + "\n");
	        	
	          
	          output.close();
	          
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
		}
	}
}
