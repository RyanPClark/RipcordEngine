package nRenderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import nComponents.CompType;
import nComponents.Entity;
import nComponents.Position;
import nComponents.Scale;
import nComponents.Terrain;
import nShaders.TerrainShader;
import textures.TerrainTexturePack;
import toolbox.GameMath;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Entity> terrains, Matrix4f matrix){
		shader.loadToShadowMapSpace(matrix);
		for(Entity terrain : terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			Terrain pComp = (Terrain)terrain.getComponentByType(CompType.TERRAIN);
			RawModel rModel = pComp.getRawModel();
			GL11.glDrawElements(GL11.GL_TRIANGLES, rModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Entity terrain){
		
		Terrain tComp = (Terrain)terrain.getComponentByType(CompType.TERRAIN);
		
		
		GL30.glBindVertexArray(tComp.getRawModel().getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(tComp);
	}
	
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendmap().getTextureID());
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Entity ent){
		Position pComp = (Position)ent.getComponentByType(CompType.POSITION);
		Vector3f position = pComp.getPosition();
		Scale sComp = (Scale)ent.getComponentByType(CompType.SCALE);
		float scale = sComp.getScale();
		
		Matrix4f matrix = GameMath.createTransformationMatrix(position, 0,
				0, 0, new Vector3f(scale, scale, scale));
		
		shader.loadTransformationMatrix(matrix);
	}
}
