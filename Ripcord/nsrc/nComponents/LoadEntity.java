package nComponents;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import mapBuilding.Box;
import models.RawModel;
import models.TexturedModel;
import nAnimation.Animation;
import nAnimation.LoadAnimation;
import nParticles.ParticleEmitter;
import nRenderEngine.Loader;
import nRenderEngine.MasterRenderer;
import nToolbox.MousePicker;
import objConverter.ModelData;
import objConverter.OBJLoader;
import textures.ModelTexture;
import toolbox.MyPaths;

public class LoadEntity {
	
	private static Map<String, TexturedModel> models = new HashMap<String, TexturedModel>();
	private static Map<String, ModelData> modelData = new HashMap<String, ModelData>();
	
	private static Loader loader;
	private static MasterRenderer mRenderer;
	private static MousePicker picker;
	private static List<Entity> entities;
	private static Entity cam;
	
	public static void newEntity(String[] components, Entity ent){
		newEntity(components, 0, loader, mRenderer, picker, entities, ent, cam);
		entities.add(ent);
	}
	
	public static int newEntity(String[] components, int index, Loader loader, MasterRenderer mRenderer, MousePicker picker,
			List<Entity> entities, Entity ent, Entity cam){
		
		for(int i = index; i < components.length; i++){
			
			String comp = components[i];
			
			String rootString = comp.substring(0, 4);
			String argString = comp.substring(4);
			String[] args = argString.split(":");
			
			
			if(rootString.equals("POS:"))
			{
				ent.addComponent(new Position(ent, new Vector3f(Float.parseFloat(args[0]),Float.parseFloat(args[1]), Float.parseFloat(args[2]))));
			}
			else if (rootString.equals("ROT:"))
			{
				ent.addComponent(new Rotation(ent, new Vector3f(Float.parseFloat(args[0]),Float.parseFloat(args[1]), Float.parseFloat(args[2]))));
			}
			else if (rootString.equals("SCL:"))
			{
				ent.addComponent(new Scale(ent, Float.parseFloat(argString)));
			}
			else if(rootString.equals("MDL:"))
			{
				TexturedModel tModel2;
				
				if(models.containsKey(args[0] + ":" + args[1]))
				{
					tModel2 = models.get(args[0] + ":" + args[1]);
				}
				else 
				{
					ModelData data2 = OBJLoader.loadOBJ(MyPaths.makeOBJPath("static/" + args[0]));
					RawModel model2 = loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(),
							data2.getNormals(), data2.getIndices());
					
					ModelTexture texture2 = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("static/" + args[1])));
					tModel2 = new TexturedModel(model2, texture2);
					models.put(args[0] + ":" + args[1], tModel2);
					modelData.put(args[0] + ":" + args[1],data2);
				}
				
				
				ent.addComponent(new ModelComp(ent, tModel2, mRenderer));
				
				if(args[2].equals("true"))
				{
					Box hitbox2 = modelData.get(args[0]+":"+args[1]).generateHitbox();
					hitbox2.generateModel(loader);
					ent.addComponent(new Hitbox(ent, hitbox2, picker));
				}
			}
			else if(rootString.equals("INX:"))
			{
				ent.addComponent(new TextureIndex(ent, Integer.parseInt(args[0])));
			}
			else if(rootString.equals("PCT:"))
			{
				ent.addComponent(new PickerControl(ent, picker, entities));
			}
			else if(rootString.equals("IDL:"))
			{
				ent.addComponent(new Idle(ent, Integer.parseInt(args[0]), Float.parseFloat(args[1]),
						Integer.parseInt(args[2]), Boolean.parseBoolean(args[3])));
			}
			else if(rootString.equals("ANM:"))
			{
				Animation anim = null;
				try {
					anim = LoadAnimation.loadAnimation(args[0]);
				} catch (IOException e) {e.printStackTrace();}
				
				// Hardcode for human
				ent.addComponent(new AnimationComp(ent, anim, 0));
			}
			else if (rootString.equals("SND:")){
				int buffer = AudioMaster.loadSound(args[0]);
				Source source = new Source();
				source.setLooping(Boolean.parseBoolean(args[2]));
				source.setVolume(Integer.parseInt(args[1]));
				ent.addComponent(new SoundComp(ent, source, buffer));
				if(args.length >= 4){
					if(Boolean.parseBoolean(args[3])){
						source.play(buffer);
					}
				}
			}
			else if (rootString.equals("EMT:")){
				Rotation rot = (Rotation)cam.getComponentByType(CompType.ROTATION);
				ent.addComponent(new ParticleEmitter(ent, new Vector3f(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])),
						args[3], mRenderer, rot));
			}
			else if (rootString.equals("PRJ:")){
				ent.addComponent(new Projectile(ent, new Vector3f(0,0,1), 20, 100, entities));
			}
			else if (rootString.equals("WPN:")){
				ent.addComponent(new Weapon(ent, null));
			}
			else if(rootString.equals("END:"))
			{
				return i;
			}
			else if(rootString.equals("SUB:"))
			{
				Entity entity = new Entity();
				entity.setParent(ent);
				ent.getChildren().add(entity);
				entities.add(entity);
				i = LoadEntity.newEntity(components, i+1, loader, mRenderer, picker, entities, entity, cam);
				if(components.length == 0)
					return i;
			}
			
		}
		
		LoadEntity.cam = cam;
		LoadEntity.entities = entities;
		LoadEntity.loader = loader;
		LoadEntity.mRenderer = mRenderer;
		LoadEntity.picker = picker;
		
		return -1;
	}
	
}
