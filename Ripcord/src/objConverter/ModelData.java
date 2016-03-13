package objConverter;

import mapBuilding.Box;

/**
 * Stores vertices, textureCoords, normals, indices, and furthestPoint arrays.
 * 
 * @author Ryan Clark
 */

public class ModelData {
 
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;
 
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
            float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }
 
    public Box generateHitbox(){
    	
    	float min_x =  20000000;
    	float max_x = -20000000;
    	
    	float min_y =  20000000;
    	float max_y = -20000000;
    	
    	float min_z =  20000000;
    	float max_z = -20000000;
    	
    	for(int i = 0; i < vertices.length; i++){
    		if(i % 3 == 0){
    			min_x = Math.min(min_x, vertices[i]);
    			max_x = Math.max(max_x, vertices[i]);
    		}
    		else if(i % 3 == 1){
    			min_y = Math.min(min_y, vertices[i]);
    			max_y = Math.max(max_y, vertices[i]);
    		}
    		else {
    			min_z = Math.min(min_z, vertices[i]);
    			max_z = Math.max(max_z, vertices[i]);
    		}
    	}
    	
    	Box b = new Box(max_y, min_y, min_x, max_x, min_z, max_z);
    	
    	return b;
    }
    
    
    
    /**
     * Getters and setters for the data
     */
    
    public float[] getVertices() {
        return vertices;
    }
 
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    public float[] getNormals() {
        return normals;
    }
 
    public int[] getIndices() {
        return indices;
    }
 
    public float getFurthestPoint() {
        return furthestPoint;
    }

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public void setTextureCoords(float[] textureCoords) {
		this.textureCoords = textureCoords;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public void setFurthestPoint(float furthestPoint) {
		this.furthestPoint = furthestPoint;
	}
}
