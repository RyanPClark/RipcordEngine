package maps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import animation.Animation;
import entities.Light;
import entities.MultiModeledEntity;
import security.Cryption;
import toolbox.MyPaths;

public final class Saving {

	protected static void saveEntityData(String name, List<MultiModeledEntity> entities){
		
		name = MyPaths.makeSavePath(name);
        try {
        	
          File file = new File(name);
          
          Cryption.decrypt(file);
          
          Float flt = new Float(0);
          Integer intt;
          Boolean bool = new Boolean(false);
          BufferedWriter output = new BufferedWriter(new FileWriter(file));
          
        	for(MultiModeledEntity entity : entities){
        		  
        		flt = entity.getPosition().x;
                output.write("x: "+flt.toString()+"\n");
                  
                flt = entity.getPosition().y;
                output.write("y: "+flt.toString()+"\n");
                  
                flt = entity.getPosition().z;
                output.write("z: "+flt.toString()+"\n");
                  
                flt = entity.getRotX();
                output.write("rotX: "+flt.toString()+"\n");
                
                flt = entity.getRotY();
                output.write("rotY: "+flt.toString()+"\n");
                  
                flt = entity.getRotZ();
                output.write("rotZ: "+flt.toString()+"\n");
                  
                flt = entity.getScale().x;
                output.write("scaleX: "+flt.toString()+"\n");
                  
                flt = entity.getScale().y;
                output.write("scaleY: "+flt.toString()+"\n");
                  
                flt = entity.getScale().z;
                output.write("scaleZ: "+flt.toString()+"\n");
                  
                flt = (float) entity.getID();
                output.write("TexID: "+flt.toString() + "\n");
                
                flt = (float) entity.getModel().getTexture().getReflectivity();
                output.write("Reflectivity: " + flt.toString() + "\n");
                  
                flt = (float) entity.getModel().getTexture().getShine_damper();
                output.write("Shine_Damper: " + flt.toString() + "\n");
                
                bool = entity.getModel().getTexture().isHasTransparency();
                output.write("Transparency: " + bool.toString() + "\n");
                  
                bool = entity.getModel().getTexture().isUseFakeLighting();
                output.write("Fake_Lighting: " + bool.toString() + "\n");
                  
                intt = entity.getNumber_of_parts();
                output.write("Parts: "+intt.toString() + "\n");
                  
                bool = entity.isB_model();
                output.write("B_Model: " + bool.toString() + "\n");
                  
                output.write("Tag: " + entity.getTag()+"\n");

        	}
          
          output.close();
          
          Cryption.encrypt(file);
          
        } catch ( IOException e ) {
           e.printStackTrace();
        }
		
	}
	
	protected static void saveLightData(String name, List<Light> lights){
		
		name = MyPaths.makeSavePath(name);
        try {
        	
          File file = new File(name);
          
          Cryption.decrypt(file);
          
          Float flt = new Float(0);
          
          BufferedWriter output = new BufferedWriter(new FileWriter(file));
          
        	for(Light light : lights){
        		  
        		flt = light.getPosition().x;
                output.write("lightPosX: "+flt.toString()+"\n");
                  
                flt = light.getPosition().y;
                output.write("lightPosY: "+flt.toString()+"\n");
                  
                flt = light.getPosition().z;
                output.write("lightPosZ: "+flt.toString()+"\n");
                
                flt = light.getColor().x;
                output.write("lightColorRed: "+flt.toString()+"\n");
                
                flt = light.getColor().y;
                output.write("lightColorGreen: "+flt.toString()+"\n");
                
                flt = light.getColor().z;
                output.write("lightColorBlue: "+flt.toString()+"\n");
                
                flt = light.getAttenuation().x;
                output.write("lightAttenuationX: "+flt.toString()+"\n");
                
                flt = light.getAttenuation().y;
                output.write("lightAttenuationY: "+flt.toString()+"\n");
                
                flt = light.getAttenuation().z;
                output.write("lightAttenuationZ: "+flt.toString()+"\n");
                
                flt = light.getIntensity();
                output.write("lightIntensity: "+flt.toString()+"\n");
               
                  
                output.write("light" + "\n");

        	}
          
          output.close();
          
          Cryption.encrypt(file);
          
        } catch ( IOException e ) {
           e.printStackTrace();
        }
		
	}
	
	protected static void saveCollisions(List<List<Vector2f>> collisions, String name){
		
		try {
	        	
			name = MyPaths.makeSavePath(name);
		    File file = new File(name);
		    
		    Cryption.decrypt(file);
		    
		    BufferedWriter output = new BufferedWriter(new FileWriter(file));
		          
		   for (int i = 0; i < collisions.size(); i++){
			   for (int j = 0; j < collisions.get(i).size(); j++){
		  				
				   output.write("Vector:" + " " + collisions.get(i).get(j).x + " " + collisions.get(i).get(j).y+"\n");
		  				
		  				}
		  			
		  			output.write("New:\n");
		  			
		  			}
		          
		   output.close();
		   
		   Cryption.encrypt(file);
		          
		} catch ( IOException e ) {
		    e.printStackTrace();
		}
	}
	
	public static void saveAnimation(Animation animation, String name){
		
		List<Vector4f[]> rotations = animation.getRotationData();
		List<Vector3f> translations = animation.getTranslationData();
		
		try {
	        	
			name = MyPaths.makeSavePath(name);
		    File file = new File(name);
		    BufferedWriter output = new BufferedWriter(new FileWriter(file));
		    
		    output.write("Length: "+ animation.getLength()+"\n");
		    
		   for (int i = 0; i < rotations.size(); i++){
			   for (int j = 0; j < rotations.get(i).length; j++){
		  				
				   output.write("Vector4f:" + " " + rotations.get(i)[j].x + " " + rotations.get(i)[j].y +
						   " " + rotations.get(i)[j].z +  " " + rotations.get(i)[j].w  + "\n");
		  				
		  		}
		  			
		  		output.write("NewRotation:\n");	
		  	}
		   
		   for(int i = 0; i < translations.size(); i++){
			   
			   output.write("Translation: "+translations.get(i).x + " " + translations.get(i).y
					   + " " + translations.get(i).z + "\n");
		   }
		          
		   output.close();
		          
		} catch ( IOException e ) {
		    e.printStackTrace();
		}
	}
}
