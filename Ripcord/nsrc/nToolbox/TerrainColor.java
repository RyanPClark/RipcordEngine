package nToolbox;

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
		image = null;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BLEND_MAP_SIZE = image.getHeight();
		MAP_SIZE = mapSize;
		
		int x = 0, y = 1023;
		
		int cl = image.getRGB(x,y);
		int  red = (cl & 0x00ff0000) >> 16;
		int  green = (cl & 0x0000ff00) >> 8;
		int  blue = cl & 0x000000ff;
		// and the Java Color is ...
		Color c = new Color(red,green,blue);
		
		System.out.println("Color1: " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
	}
	
	public static Vector3f getColor(Vector3f vec){
		
		float x = BLEND_MAP_SIZE * (    (0.5f + vec.x / MAP_SIZE));
		float z = BLEND_MAP_SIZE * (0 + (0.5f + vec.z / MAP_SIZE));
		
		//System.out.println(x + ", " + z);
		
		if(x < 0 || x >= image.getHeight()/Statics.TERRAIN_RES ||z < 0 || z >= image.getHeight()/Statics.TERRAIN_RES ){
			
			return new Vector3f(999,999,999);
		}
		Color color = new Color(image.getRGB((int)x, (int)z));
		
		return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
	}
}
