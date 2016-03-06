package nRenderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import nComponents.Rotation;
import nComponents.Scale;
import nComponents.TextureIndex;
import nShaders.StaticShader;
import toolbox.GameMath;

public class Renderer {

	private StaticShader shader;
	
	public Renderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities){
		
		for(TexturedModel tModel : entities.keySet()){
			
			prepareTexturedModel(tModel);
			List<Entity> batch = entities.get(tModel);
			for(Entity ent : batch){
				prepareInstance(ent);
				GL11.glDrawElements(GL11.GL_TRIANGLES, tModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel tModel){
		
		GL30.glBindVertexArray(tModel.getRawModel().getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		if(tModel.getTexture().isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadShineVariables(tModel.getTexture().getReflectivity(), tModel.getTexture().getShine_damper());
		shader.loadNumberOfRows(tModel.getTexture().getNumberOfRows());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tModel.getTexture().getID());
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity ent){
		Position pComp = (Position)ent.getComponentByType(CompType.POSITION);
		Vector3f position = pComp.getPosition();
		Rotation rComp = (Rotation)ent.getComponentByType(CompType.ROTATION);
		Vector3f rotation;
		if(rComp != null){
			rotation = rComp.getRotation();
		}
		else{
			rotation = new Vector3f(0,0,0);
		}
		Scale sComp = (Scale)ent.getComponentByType(CompType.SCALE);
		float scale = sComp.getScale();
		
		Matrix4f matrix = GameMath.createTransformationMatrix(position, rotation.x,
				rotation.y, rotation.z, new Vector3f(scale, scale, scale));
		
		TextureIndex indexComp = (TextureIndex)ent.getComponentByType(CompType.TEXTURE_INDEX);
		
		shader.loadOffset(indexComp.getTextureXOffset(), indexComp.getTextureYOffset());
		shader.loadTransformationMatrix(matrix);
	}
}
