package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Animation;
import animation.MasterAnimation;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.GameMath;
import enums.Mode;
import entities.Billboard;
import entities.Entity;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(Map<TexturedModel, List<Entity>> entities, boolean wireframe, Mode mode){
		
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				
				prepareInstance(entity, mode, model.getPartID());
				
				int renderType = GL11.GL_TRIANGLES;
				
				if (wireframe){
					renderType = GL11.GL_LINE_LOOP;
				}
				
				GL11.glDrawElements(renderType, Math.round(model.getRawModel().getVertexCount()), GL11.GL_UNSIGNED_INT,0);
				
			}
			unBindTexturedModel();
		}
		
	}
	
	protected void render(Map<TexturedModel, List<Billboard>> entities, boolean wireframe, Vector3f position){
		
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Billboard> batch = entities.get(model);
			for(Billboard entity:batch){

				prepareInstance(entity, position);
				
				int renderType = GL11.GL_TRIANGLES;
				
				if (wireframe){
					renderType = GL11.GL_LINE_LOOP;
				}
				
				GL11.glDrawElements(renderType, Math.round(model.getRawModel().getVertexCount()), GL11.GL_UNSIGNED_INT,0);
				
			}
			unBindTexturedModel();
		}
		
	}
	
	protected void makeNewProjectionMatrix(Matrix4f projectionMatrix){
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	private void prepareTexturedModel(TexturedModel model){
		
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		if (texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		if (texture.getReflectivity() > 0){
			shader.loadUseSpecular(true);
		}else{
			shader.loadUseSpecular(false);
		}
		shader.loadUseFakeLighting(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShine_damper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unBindTexturedModel(){
		
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	
	private void prepareInstance(Entity entity, Mode mode, int i){
		
		Matrix4f transformationMatrix = entity.getMatrix();
		
		if (!entity.isStatic() || mode == Mode.DEV){
			
			transformationMatrix = GameMath.createTransformationMatrix(entity.getPosition(),
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		}
		
		if (entity.getAnimID() > -1){
			animate(i-1, entity.getAnimID(), transformationMatrix, entity);
		}
		else {
			shader.loadTransformationMatrix(transformationMatrix);
		}
	}
	
	private void animate(int i, int ID, Matrix4f transformationMatrix, Entity entity){
		
		Animation anim = MasterAnimation.animations[ID];
		
		int length = anim.getLength();
		
		entity.setAnimCounter((entity.getAnimCounter()+1)%length);
		
		float amountThrough = 2 * Math.abs((float) (entity.getAnimCounter()-length/2)/length);
		float first = (float) Math.toRadians(anim.rotationData.get(i)[0].w) * amountThrough;
		float second = (float) Math.toRadians(anim.rotationData.get(i)[1].w) * (1-amountThrough);
		
		Vector3f axis1 = new Vector3f(anim.rotationData.get(i)[0].x, anim.rotationData.get(i)[0].y,
				anim.rotationData.get(i)[0].z);
		
		Vector3f axis2 = new Vector3f(anim.rotationData.get(i)[1].x, anim.rotationData.get(i)[1].y,
				anim.rotationData.get(i)[1].z);
		
		Vector3f axis = new Vector3f(axis1.x * amountThrough + axis2.x * (1-amountThrough), axis1.y * amountThrough + axis2.y * (1-amountThrough),
				axis1.z * amountThrough + axis2.z * (1-amountThrough));
		
		Vector3f translation = new Vector3f(anim.getTranslationData().get(i).x, anim.getTranslationData().get(i).y,
				anim.getTranslationData().get(i).z);
		
		Matrix4f.translate(translation, transformationMatrix, transformationMatrix);
		Matrix4f.rotate((second+first), axis, transformationMatrix, transformationMatrix);
		Matrix4f.translate(translation.negate(translation), transformationMatrix, transformationMatrix);
		
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void prepareInstance(Billboard entity, Vector3f position){
		
		float entry = (float) (position.z - entity.getPosition().z)/(position.x - entity.getPosition().x);
		float pythagorean = (float) Math.atan(entry);
		
		entity.update((float) (Math.toDegrees(pythagorean)));
		
		Matrix4f transformationMatrix = GameMath.createTransformationMatrix(entity.getPosition(),
				entity.getRotation().x, entity.getRotation().y, entity.getRotation().z,
				new Vector3f(entity.getScale().x, entity.getScale().y, entity.getScale().x));
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
}
