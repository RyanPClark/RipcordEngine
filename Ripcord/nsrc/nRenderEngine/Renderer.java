package nRenderEngine;

/**
 * Basic instanced renderer that handles non-normal mapped entity rendering.
 * 
 * @author Ryan Clark
 */

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
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
	
	/**
	 * Loads a vao, uniform variables, and a texture from a TexturedModel
	 * onto the graphics card. Called once each frame for each individual
	 * textured model.
	 * 
	 * @param tModel - The model to be loaded
	 */
	
	private void prepareTexturedModel(TexturedModel tModel){
		
		GL30.glBindVertexArray(tModel.getRawModel().getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		if(tModel.getTexture().getID() < 0)
			return;
		if(tModel.getTexture().isHasTransparency())
			MasterRenderer.disableCulling();
		shader.loadShineVariables(tModel.getTexture().getReflectivity(), tModel.getTexture().getShine_damper());
		shader.loadNumberOfRows(tModel.getTexture().getNumberOfRows());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tModel.getTexture().getID());
	}
	
	/**
	 * Unbinds the VAO, disables the VBOs, and enables backface culling
	 */
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * @param ent - The entity to be prepared. Must have a position, rotation, and scale.
	 */
	
	private void prepareInstance(Entity ent){
		// get position
		Position pComp = (Position)ent.getComponentByType(CompType.POSITION);
		Vector3f position = (pComp != null ? pComp.getPosition() : new Vector3f(0,0,0));
		
		// get rotation
		Rotation rComp = (Rotation)ent.getComponentByType(CompType.ROTATION);
		Vector3f rotation = (rComp != null ? rComp.getRotation() : new Vector3f(0,0,0));
		
		// get scale
		Scale sComp = (Scale)ent.getComponentByType(CompType.SCALE);
		float scale = (sComp != null ? sComp.getScale() : 0.0f);
		
		// load transformation matrix
		Matrix4f matrix = GameMath.createTransformationMatrix(position, rotation.x,
				rotation.y, rotation.z, new Vector3f(scale, scale, scale));
		shader.loadTransformationMatrix(matrix);
		
		// get texture index
		TextureIndex indexComp = (TextureIndex)ent.getComponentByType(CompType.TEXTURE_INDEX);
		Vector2f index = (indexComp != null ?
				new Vector2f(indexComp.getTextureXOffset(), indexComp.getTextureYOffset()) :
				new Vector2f(0,0));
		
		// load offset to shader
		shader.loadOffset(index.x, index.y);
	}
}
