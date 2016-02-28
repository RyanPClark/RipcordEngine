package toolbox;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class MyPaths {

	private static final String RESOURCE_FOLDER = "res";
	private static String basePath;
	
	static {
		
		Path realPath = null;
		Path path = Paths.get(RESOURCE_FOLDER);
		try {
			realPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
				e.printStackTrace();
		}
		basePath = realPath.toString();
	}
		
	public static String makeTexturePath(String input){
		return basePath + "\\Images\\"+input+".png";
	}
	public static String makeOBJPath(String input){
		
		return basePath + "\\OBJs\\"+input+".obj";

	}public static String makeShaderPath(String input){
		
		return basePath + "\\Shaders\\"+input+".txt";
	}
	public static String makeSoundPath(String input){
		
		return basePath + "\\Sounds\\"+input;
	}
	public static String makeSavePath(String input){
		
		return basePath + "\\Save Files\\"+input+".data";
	}
}
