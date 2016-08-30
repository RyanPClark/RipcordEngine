package nToolbox;

/**
 * @author Ryan Clark
 * 
 * Basic class that uses the mouse picker and blendmap to calculate what
 * the blendmap color of the terrain the mouse is over.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import components.Statics;

public class TerrainColor {

	private static int BLEND_MAP_SIZE;
	private static int MAP_SIZE;
	
	private static BufferedImage image;
	
	public static void load(String filename, int mapSize){
		
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {}
		
		BLEND_MAP_SIZE = image.getHeight();
		MAP_SIZE = mapSize;
	}
	
	public static Vector3f getColor(Vector3f vec){
		
		float x = 0, z = 0;
		
		try{
			x = BLEND_MAP_SIZE * (    (0.5f + vec.x / MAP_SIZE));
			z = BLEND_MAP_SIZE * (0 + (0.5f + vec.z / MAP_SIZE));
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		//System.out.println(x + ", " + z);
		
		if(x < 0 || x >= image.getHeight()/Statics.TERRAIN_RES ||z < 0 || z >= image.getHeight()/Statics.TERRAIN_RES ){
			
			return new Vector3f(999,999,999);
		}
		Color color = new Color(image.getRGB((int)x, (int)z));
		
		return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
	}
}
