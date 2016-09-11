package nComponents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.SoundComp;
import audio.Source;
import mapBuilding.Box;
import mapBuilding.Level;
import mapBuilding.PlayerControl;
import models.RawModel;
import models.TexturedModel;
import nAnimation.Animation;
import nAnimation.AnimationComp;
import nAnimation.Idle;
import nAnimation.LoadAnimation;
import nParticles.ParticleEmitter;
import objConverter.ModelData;
import objConverter.OBJLoader;
import textures.ModelTexture;
import toolbox.MyPaths;

public class LoadEntity {
	
	private static Map<String, TexturedModel> models = new HashMap<String, TexturedModel>();
	private static Map<String, ModelData> modelData = new HashMap<String, ModelData>();
	private static Map<String, Integer> soundBuffers = new HashMap<String, Integer>();
	
	private static Level level;
	
	public static void newEntity(String[] components, Entity ent){
		newEntity(components, 0, ent, level);
		level.getEntities().add(ent);
	}
	
	public static int newEntity(String[] components, int index, Entity ent, Level level){
	
		for(int i = index; i < components.length; i++){
			
			String comp = components[i];
			String rootString = comp.substring(0, 4);
			String argString = comp.substring(4);
			String[] args = argString.split(":");
			
			
			if(rootString.equals("TEM:")){
				ent.addComponent(new Team(ent, Integer.parseInt(args[0])));
			}
			else if(rootString.equals("POS:"))
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
					RawModel model2 = level.getLoader().loadToVAO(data2.getVertices(), data2.getTextureCoords(),
							data2.getNormals(), data2.getIndices());
					
					ModelTexture texture2 = new ModelTexture(level.getLoader().loadTexture(MyPaths.makeTexturePath("static/" + args[1])));
					tModel2 = new TexturedModel(model2, texture2);
					models.put(args[0] + ":" + args[1], tModel2);
					modelData.put(args[0] + ":" + args[1],data2);
				}
				if(args.length > 3){
					tModel2.getTexture().setNumberOfRows(Integer.parseInt(args[3]));
				}
				
				ent.addComponent(new ModelComp(ent, tModel2, level.getmRenderer()));
				
				if(args[2].equals("true"))
				{
					Box hitbox2 = modelData.get(args[0]+":"+args[1]).generateHitbox();
					Box hitbox3 = modelData.get(args[0]+":"+args[1]).generateHitbox();
					//hitbox3.setBounds(hitbox2.getBounds());
					hitbox2.generateModel(level.getLoader());
					ent.addComponent(new Hitbox(ent, hitbox2, hitbox3, level.getPicker()));
				}
			}
			else if(rootString.equals("INX:"))
			{
				ent.addComponent(new TextureIndex(ent, Integer.parseInt(args[0])));
			}
			else if(rootString.equals("PCT:"))
			{
				ent.addComponent(new PickerControl(ent, level.getPicker(), level.getEntities(), level.getTerrain(), Boolean.parseBoolean(args[0]),
						Float.parseFloat(args[1]), Boolean.parseBoolean(args[2])));
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
					anim.setName(args[1]);
				} catch (IOException e) {e.printStackTrace();}
				
				// Hardcode for human
				ent.addComponent(new AnimationComp(ent, anim, args[1].equals("spinning") ? 0 : -1));
			}
			else if (rootString.equals("SND:")){
				int buffer = 0;
				if(soundBuffers.containsKey(args[0])){
					buffer = soundBuffers.get(args[0]);
				}
				else{
					buffer = AudioMaster.loadSound(args[0]);
				}
				
				Source source = new Source();
				source.setLooping(Boolean.parseBoolean(args[2]));
				source.setVolume(Float.parseFloat(args[1]));
				ent.addComponent(new SoundComp(ent, source, buffer));
				if(args.length >= 4){
					source.setPitch(Float.parseFloat(args[4]));
					if(Boolean.parseBoolean(args[3])){
						source.play(buffer);
					}
					
				}
			}
			else if (rootString.equals("EMT:")){
				Rotation rot = (Rotation)level.getCamera().getComponentByType(CompType.ROTATION);
				ent.addComponent(new ParticleEmitter(ent, new Vector3f(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])),
						args[3], level.getmRenderer(), rot));
			}
			else if (rootString.equals("PRJ:")){
				ent.addComponent(new Projectile(ent, new Vector3f(0,0,1), Float.parseFloat(args[0]), Float.parseFloat(args[1]),
						Integer.parseInt(args[2]), level.getEntities()));
			}
			else if (rootString.equals("WPN:")){
				ent.addComponent(
						new Weapon(ent,
								new Vector3f(Float.parseFloat(args[0]), Float.parseFloat(args[1]),Float.parseFloat(args[2])),
								new Vector3f(Float.parseFloat(args[3]), Float.parseFloat(args[4]),Float.parseFloat(args[5])),
								Integer.parseInt(args[6]),
								Float.parseFloat(args[7]),
								Float.parseFloat(args[8]),
								Integer.parseInt(args[9]),
								Float.parseFloat(args[10])
								)
						);
				
				if(args.length >= 5){
					Weapon w = (Weapon)ent.getComponentByType(CompType.WEAPON);
					w.setMaxAngleDelta(Float.parseFloat(args[4]));
				}
			}
			else if (rootString.equals("PLA:")){
				Terrain terrain = (Terrain)level.getTerrain().getComponentByType(CompType.TERRAIN);
				ent.addComponent(new PlayerControl(ent, terrain, Boolean.parseBoolean(args[0])));
			}
			else if (rootString.equals("CBT:")){
				ent.addComponent(new Combat(ent, Float.parseFloat(args[0]), level.getEntities()));
			}
			else if (rootString.equals("HLT:")){
				Rotation camRot = (Rotation)level.getCamera().getComponentByType(CompType.ROTATION);
				ent.addComponent(new HealthComp(ent, Integer.parseInt(args[0]), level.getmRenderer(), new Vector3f(0,Float.parseFloat(args[1]),0),
						new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(5,2,5),camRot));
			}
			else if (rootString.equals("KBC:")){
				ent.addComponent(new KeyControl(ent, Float.parseFloat(args[0]), new int[]{Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
						Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6])}));
			}
			else if (rootString.equals("MMC:")){
				Position camPos = (Position)level.getCamera().getComponentByType(CompType.POSITION);
				ent.addComponent(new MinimapControl(ent, new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), 
						Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])}, camPos));
			}
			else if (rootString.equals("GRA:")){
				Terrain terrain = (Terrain)level.getTerrain().getComponentByType(CompType.TERRAIN);
				ent.addComponent(new Gravity(ent, terrain));
			}
			else if (rootString.equals("DTH:")){
				ent.addComponent(new Death(ent, Integer.parseInt(args[0]), Integer.parseInt(args[1]), level.getEntities()));
			}
			else if (rootString.equals("NME:")){
				ent.setName(args[0]);
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
				i = LoadEntity.newEntity(components, i+1, entity, level);
				if(components.length == 0)
					return i;
			}
			
		}
		
		LoadEntity.level = level;
		
		return -1;
	}

	public static Level getLevel() {
		return level;
	}

	public static void setLevel(Level level) {
		LoadEntity.level = level;
	}
	
	
	
	
	
}
